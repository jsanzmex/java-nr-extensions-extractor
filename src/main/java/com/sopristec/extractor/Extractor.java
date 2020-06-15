package com.sopristec.extractor;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.w3c.dom.Node;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

public class Extractor {

    //region Input Mocks
    final static String[] inputMocks = new String[]{
            "public class com.sopristec.newrelic.InputCommand extends com.sopristec.newrelic.Command {\n" +
                    "  public com.sopristec.newrelic.InputCommand(java.lang.StringAtConstructor);\n" +
                    "  public boolean isValid(java.lang.String, java.lang.StringBuilder);\n" +
                    "  public void execute(java.lang.String, java.lang.StringBuilder);\n" +
                    "}",
            "Compiled from \"ExtensionsExtractor.java\"\n" +
                    "public class com.sopristec.newrelic.ExtensionsExtractor {\n" +
                    "  public com.sopristec.newrelic.ExtensionsExtractor();\n" +
                    "  public static void main(java.lang.String[]);\n" +
                    "}\n" +
                    "Compiled from \"Command.java\"\n" +
                    "public abstract class com.sopristec.newrelic.Command {\n" +
                    "  public com.sopristec.newrelic.Command(java.lang.String);\n" +
                    "  public java.lang.String getCommandOptions();\n" +
                    "  public abstract boolean isValid();\n" +
                    "  public abstract void execute();\n" +
                    "  public static java.lang.String dasherizeName(java.lang.Class<? extends com.sopristec.newrelic.Command>);\n" +
                    "}\n" +
                    "Compiled from \"OutputCommand.java\"\n" +
                    "public class com.sopristec.newrelic.OutputCommand extends com.sopristec.newrelic.Command {\n" +
                    "  public com.sopristec.newrelic.OutputCommand(java.lang.String, java.lang.String);\n" +
                    "  public boolean isValid();\n" +
                    "  public void execute();\n" +
                    "}\n" +
                    "Compiled from \"TestNonAbstractClassWithMixedMethods.java\"\n" +
                    "public class com.bursatec.bmv.CustomHydraPropertyResourceConfigurer implements org.springframework.beans.factory.config.BeanFactoryPostProcessor,org.springframework.core.Ordered {\n" +
                    "  protected final org.apache.commons.logging.Log logger;\n" +
                    "  public com.bursatec.bmv.CustomHydraPropertyResourceConfigurer(int,int);\n" +
                    "  public abstract void execute(int);\n" +
                    "  public void setOrder(int);\n" +
                    "  public void resetOrder();\n" +
                    "}"
    };
    //endregion

    private final ExtensionsXmlEncoder encoder;
    private final ExtractorConfig config;
    private Integer lineCount = 0;

    // region API

    public Extractor(ExtractorConfig config, ExtensionsXmlEncoder encoder) {
        this.config = config;
        this.encoder = encoder;
    }

    public void execute(){

        // Extract Classes & Methods from Raw Command Line utils.
        List<String> classNames = getRawClassNames();
        Predicate<String> isNotClass = this::indexOfClassGTOne;
        removeNonClasses(classNames, isNotClass);
        List<String> formattedClassNames = new ArrayList<>();
        classNames.forEach(s -> {
            formattedClassNames.add(formatClassName(s));
            SopristecLogManager.logger.trace("cn: "+formatClassName(s));
        });
        List<String> methodNames = getRawMethodNames(
                formattedClassNames);
        methodNames.forEach(s -> {
            SopristecLogManager.logger.trace(String.format("mn %d: ", ++lineCount) + s);
            SopristecLogManager.logger.debug(s);
        });


        // ANTLR4 Extraction
        JarClassListener listener = new JarClassListener();

        // Load input string into AN Input Stream
        String input = String.join(
                String.format("%n"),
                methodNames);
        // UNCOMMENT TO DO HARD-CODED TESTING
        //input = inputMocks[1];

        String[] lines = input.split("\\n");
        int i = 0;
        StringBuilder lastClass = new StringBuilder();
        for(; i < lines.length; i++){
            if(lines[i].contains("Compiled from"))
            {
                if(lastClass.length() != 0){
                    processAntlr4Input(lastClass.toString(), listener);
                    lastClass.setLength(0);
                }
            }else{
                lastClass.append(lines[i]);
            }
        }

        if(lastClass.length() > 0){
            processAntlr4Input(lastClass.toString(), listener);
        }

        // Get klasses and compose XML with them
        encoder.appendAndGetPointcutElement(true);
        listener.getKlasses().forEach(klass -> {
            if(klass.shouldBeInstrumented(config)) {
                Node klassNameNode = encoder.getClassNode(klass.getName());
                encoder.appendToPointcutNode(klassNameNode);
                klass.getMethodList().forEach(method -> {
                    if(method.shouldBeInstrumented(config)){
                        Node methodNode = encoder.getMethodNode(method.getName(), method.getParameterList());
                        encoder.appendToPointcutNode(methodNode);
                    }
                });
            }
        });

    }

    // endregion

    private void processAntlr4Input(String input, JarClassListener listener) {
        InputStream stream =
                new ByteArrayInputStream(TextUtils.unsetDots(input)
                        .getBytes(StandardCharsets.UTF_8));

        // create a lexer that feeds off of input CharStream
        Java8Lexer lexer = null;
        try {
            lexer = new Java8Lexer(
                    CharStreams.fromStream(
                            stream,
                            StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
            SopristecLogManager.logger.error("An error ocurred while tokenizing!. Error message: " + e.getMessage());
        }

        // create a buffer of tokens pulled from the lexer
        if (lexer == null) throw new AssertionError();
        CommonTokenStream tokens = new CommonTokenStream(lexer);

        // create a parser that feeds off the tokens buffer
        Java8Parser parser = new Java8Parser(tokens);

        // NOTE: Although we could use classBody() instead of classDeclaration(), it throws
        //       even more errors because the whole text is missing many elements.
        //       Hence, split the array when declarations of style (Compiled from "InputCommand.java")
        //       are found and use several trees to walk through.
        ParseTree tree = parser.classDeclaration();

        // Print LISP-style tree
        SopristecLogManager.logger.trace(tree.toStringTree(parser));

        // Walk tree and listen for records
        ParseTreeWalker walker = new ParseTreeWalker();
        walker.walk(listener, tree);
    }

    private String formatClassName(String input){
        return input
                .replaceAll(".class","")
                .replaceAll("/",".");
    }

    private boolean indexOfClassGTOne(String s){
        return !s.contains(".class");
    }

    private <T> void removeNonClasses(List<T> l, Predicate<T> p)
    {
        l.removeIf(p);
    }

    private List<String> getRawClassNames(){
        try {
            return RuntimeTask.executeCommand(
                    new String[] {"jar", "tf", config.inputFilename});
        } catch (IOException e) {
            SopristecLogManager.logger.error("An error ocurred while trying to extract CLASS NAMES!");
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    private List<String> getRawMethodNames(List<String> classNames){
        if(classNames.size() == 0){
            SopristecLogManager.logger.info("There are no classes to extract methods to!");
            return new ArrayList<>();
        }
        List<String> initialCommands = Arrays.asList(
                "javap", "-classpath", config.inputFilename);
        List<String> commands = new ArrayList<>(
                initialCommands.size() + classNames.size());
        commands.addAll(initialCommands);
        commands.addAll(classNames);
        try {
            return RuntimeTask.executeCommand(commands.toArray(new String[0]));
        } catch (IOException e) {
            SopristecLogManager.logger.error("An error occurred while trying to extract METHOD NAMES!");
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
}
