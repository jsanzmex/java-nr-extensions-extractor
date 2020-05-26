package com.sopristec.extractor;

public class Extractor {

    private static final String INPUT_OPTION = "input";
    private static final String METHODS_OPTION = "methods";
    private static final String OUTPUT_OPTION = "output";

    private static long startTime;

    public static void main(String[] args) {

        recordStartTime();

        // Step 1. Fetch and validate input JAR path
        InputCommand input = new InputCommand(System.getProperty(INPUT_OPTION, ""));
        if(!input.isValid()){
            return;
        }

        // Step 2. Extract methods, default is "public only"
        final String defaultPublicMethodsOnly = "+";
        final StringBuilder extensionsBodyBuilder = new StringBuilder();
        MethodsCommand methods = new MethodsCommand(
                System.getProperty(METHODS_OPTION, defaultPublicMethodsOnly),
                extensionsBodyBuilder,
                input.inputPath);
        methods.execute();

        // Step 3. Fetch output filename and use to create output XML file, default is *.jar file name
        OutputCommand output = new OutputCommand(
                System.getProperty(OUTPUT_OPTION, composeDefaultOutputFilename(input.getCommandOptions())),
                extensionsBodyBuilder.toString());
        output.execute();

        logDuration();
    }

    private static String composeDefaultOutputFilename(String inputFilename){
        String[] temp = inputFilename.split("\\.");
        return temp[temp.length-2] + ".xml";
    }

    //region Record duration
    private static void recordStartTime(){
        startTime = System.nanoTime();
    }

    private static void logDuration(){
        System.out.println("Methods extraction and parsing took " +
                (System.nanoTime() - startTime)/1000000 + " milliseconds.");
    }
    //endregion
}
