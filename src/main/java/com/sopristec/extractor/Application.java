package com.sopristec.extractor;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.config.Configurator;

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

        // Set log level
        setLogLevel();

        // Beginning of extraction
        recordStartTime();
        ExtractorConfig config = new ExtractorConfig();

        // Step 1. Configure input JAR path
        InputCommand input = new InputCommand(System.getProperty(INPUT_OPTION));
        if(!input.isValid()){
            return;
        }
        input.dumpTo(config);

        // Step 2. Configure "methods" attribute
        MethodsCommand methods = new MethodsCommand(
                System.getProperty(METHODS_OPTION)
        );
        methods.dumpTo(config);
        if(!methods.isValid()){
            return;
        }
        methods.dumpTo(config);

        // Step 3. Configure output filename. default is *.jar's file name
        OutputCommand output = new OutputCommand(
                System.getProperty(OUTPUT_OPTION, config.inputFilename.split("\\.")[0]));
        if(!output.isValid()){
            return;
        }
        output.dumpTo(config);

        // Step 4. Configure Metric Prefix
        MetricPrefixCommand metricPrefix = new MetricPrefixCommand(
                System.getProperty(METRIC_PREFIX_OPTION, ""));
        if(!metricPrefix.isValid()){
            return;
        }
        metricPrefix.dumpTo(config);

        // Step 5. Compose xml with the help of the JarClassListener
        ExtensionsXmlEncoder encoder = null;
        try {
            encoder = new ExtensionsXmlEncoder(config.metricPrefix, config.inputFilename.split("\\.")[0]);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }

        // Step 6. Extract method names
        Extractor extractor = new Extractor(config, encoder);
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

    //region Record duration
    private static void recordStartTime(){
        startTime = System.nanoTime();
    }

    private static void logDuration(){
        SopristecLogManager.logger.info("Methods extraction and parsing took " +
                (System.nanoTime() - startTime)/1000000 + " milliseconds.");
    }
    //endregion

    // region Logger Configuration
    private static void setLogLevel() {
        // Inspired on Neo's answer:
        // https://stackoverflow.com/questions/7126709/how-do-i-set-log4j-level-on-the-command-line
        if (Boolean.getBoolean("log4j.trace")) {
            Configurator.setLevel("com.sopristec.extractor", Level.TRACE);
            return;
        }else if(Boolean.getBoolean("log4j.debug")){
            Configurator.setLevel("com.sopristec.extractor", Level.DEBUG);
            return;
        }else if(Boolean.getBoolean("log4j.info")){
            Configurator.setLevel("com.sopristec.extractor", Level.INFO);
            return;
        }else if(Boolean.getBoolean("log4j.warn")){
            Configurator.setLevel("com.sopristec.extractor", Level.WARN);
            return;
        }else if(Boolean.getBoolean("log4j.error")){
            Configurator.setLevel("com.sopristec.extractor", Level.ERROR);
            return;
        }else if(Boolean.getBoolean("log4j.fatal")){
            Configurator.setLevel("com.sopristec.extractor", Level.FATAL);
            return;
        }else if(Boolean.getBoolean("log4j.off")){
            Configurator.setLevel("com.sopristec.extractor", Level.OFF);
            return;
        }

        Configurator.setLevel("com.sopristec.extractor", Level.INFO);
    }
    // endregion
}
