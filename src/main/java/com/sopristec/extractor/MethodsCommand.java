package com.sopristec.extractor;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class MethodsCommand extends Command {

    public StringBuilder extensionsBody;
    private String inputFilename;
    private boolean shouldExtractPrivate = false;
    private boolean shouldExtractPublic = false;

    public MethodsCommand(String options, StringBuilder extensionsBody, String inputFilename){
        super(options);
        shouldExtractPrivate = options.indexOf("-") >= 0;
        shouldExtractPublic = options.indexOf("+") >= 0;
        this.extensionsBody = extensionsBody;
        this.inputFilename = inputFilename;
    }

    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public void execute() {
        List<String> classNames = getRawClassNames();
        Predicate<String> isNotClass = item -> indexOfClassGTOne(item);
        classNames = removeNonClasses(classNames, isNotClass);
        List<String> formattedClassNames = new ArrayList<String>();
        classNames.forEach(s -> {
            formattedClassNames.add(formatClassName(s));
            //System.out.println("cn: "+formatClassName(s));
        });
        List<String> methodNames = getRawMethodNames(formattedClassNames);
        methodNames.forEach(s -> {
            //System.out.println("mn: " + s);
        });

        // ANTLR4 Extraction
        // Load rawData from file into ANT Input Stream
        InputStream stream = new ByteArrayInputStream(fetchTestText().getBytes(StandardCharsets.UTF_8));

        /*ANTLRInputStream input = null;
        try {
            input = new ANTLRInputStream(fetchTestText());
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        // create a lexer that feeds off of input CharStream
        Java8Lexer lexer = null;
        try {
            lexer = new Java8Lexer(CharStreams.fromStream(stream, StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // create a buffer of tokens pulled from the lexer
        CommonTokenStream tokens = new CommonTokenStream(lexer);

        // create a parser that feeds off the tokens buffer
        Java8Parser parser = new Java8Parser(tokens);

        ParseTree tree = parser.classDeclaration();
        // Print LISP-style tree
        System.out.println(tree.toStringTree(parser));

        // Walk tree and listen for records
        ParseTreeWalker walker = new ParseTreeWalker();
        JarClassListener listener = new JarClassListener(parser);
        walker.walk(listener, tree);
    }

    private String formatClassName(String input){
        return input.replaceAll(".class","").replaceAll("/",".");
    }

    private boolean indexOfClassGTOne(String s){
        return s.indexOf(".class") < 0;
    }

    private <T> List<T> removeNonClasses(List<T> l, Predicate<T> p)
    {
        Iterator<T> itr = l.iterator();
        while (itr.hasNext()) {
            T t = itr.next();
            if (p.test(t)) {
                itr.remove();
            }
        }

        // Return the null
        return l;
    }

    private List<String> getRawClassNames(){
        try {
            return RuntimeTask.executeCommand(new String[] {"jar", "tf", inputFilename});
        } catch (IOException e) {
            System.out.println("An error ocurred while trying to extract CLASS NAMES!");
            e.printStackTrace();
        }
        return new ArrayList<String>();
    }

    private List<String> getRawMethodNames(List<String> classNames){
        if(classNames.size() == 0){
            System.out.println("There are no classes to extract methods to!");
            return new ArrayList<String>();
        }
        List<String> initialCommands = Arrays.asList(new String[] {"javap", "-classpath", inputFilename});
        List<String> commands = new ArrayList<>(initialCommands.size() + classNames.size());
        commands.addAll(initialCommands);
        commands.addAll(classNames);
        try {
            return RuntimeTask.executeCommand(commands.toArray(new String[commands.size()]));
        } catch (IOException e) {
            System.out.println("An error ocurred while trying to extract METHOD NAMES!");
            e.printStackTrace();
        }
        return new ArrayList<String>();
    }

    //region Test Methods
    private String fetchTestText()
    {
        /*return "public class MethodsCommand extends Command {\n" +
                "  public java.lang.StringBuilder extensionsBody;\n" +
                "  public MethodsCommand(String, StringBuilder);\n" +
                "  public boolean isValid(String, StringBuilder);\n" +
                "  public void execute(String, StringBuilder);\n" +
                "}\n";+*/
        /*return "public class MethodsCommand extends Command {\n" +
                "  public java.lang.StringBuilder extensionsBody;\n" +
                "  public MethodsCommand(java.lang.String, java.lang.StringBuilder);\n" +
                "  public boolean isValid(java.lang.String, java.lang.StringBuilder);\n" +
                "  public void execute(java.lang.String, java.lang.StringBuilder);\n" +
                "}\n";*/
        // The strategy to avoid invalid names: REPLACE BY VALID KNOWN STRINGS, e.g. __DOT__
        return "public class com.sopristec.newrelic.InputCommand extends com.sopristec.newrelic.Command {\n" +
                "  public com.sopristec.newrelic.InputCommand(java.lang.String);\n" +
                "  public boolean isValid(java.lang.String, java.lang.StringBuilder);\n" +
                "  public void execute(java.lang.String, java.lang.StringBuilder);\n" +
                "}";
    }
    //endregion

}
