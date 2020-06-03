package com.sopristec.extractor;

import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class Application {

    private static final String INPUT_OPTION = "input";
    private static final String METHODS_OPTION = "methods";
    private static final String OUTPUT_OPTION = "output";
    private static final String METRIC_PREFIX_OPTION = "metricPrefix";

    private static long startTime;

    public static void main(String[] args) {

        // Beginning of extraction
        recordStartTime();
        ExtractorConfig config = new ExtractorConfig();

        // Step 1. Configure input JAR path
        InputCommand input = new InputCommand(
                System.getProperty(INPUT_OPTION));
        if(!input.isValid()){
            return;
        }
        input.setup(config);

        // Step 2. Configure "methods" attribute
        MethodsCommand methods = new MethodsCommand(
                System.getProperty(METHODS_OPTION),
                config.inputPath);
        methods.setup(config);
        if(!methods.isValid()){
            return;
        }
        methods.setup(config);

        // Step 3. Configure output filename. default is *.jar's file name
        OutputCommand output = new OutputCommand(
                System.getProperty(OUTPUT_OPTION, config.inputFilename.split("\\.")[0]));
        if(!output.isValid()){
            return;
        }
        output.setup(config);

        // Step 4. Configure Metric Prefix
        MetricPrefixCommand metricPrefix = new MetricPrefixCommand(
                System.getProperty(METRIC_PREFIX_OPTION, ""));
        if(!metricPrefix.isValid()){
            return;
        }
        metricPrefix.setup(config);

        // Step 5. Compose xml with the help of the JarClassListener
        ExtensionsXmlEncoder encoder = null;
        try {
            encoder = new ExtensionsXmlEncoder(config.metricPrefix);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }

        // Step 6. Extract method names
        Extractor extractor = new Extractor(config.inputPath, encoder);
        extractor.execute();

        // Step 7. Write file
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(config.outputFilename), StandardCharsets.UTF_8))) {
            assert encoder != null;
            writer.write(encoder.encode());
        } catch (IOException e) {
            e.printStackTrace();
        }


        // End-of-extraction
        logDuration();
    }

    // region Utils

    private static String composeDefaultOutputFilename(String inputFilename){
        String[] temp = inputFilename.split("\\.");
        return temp[temp.length-2] + ".xml";
    }

    //endregion

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
