package com.sopristec.extractor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Processes the command line "output" argument: "-Doutput"
 * Possible values: Path to a valid extensions output file, e.g. "MyExtensions.xml"
 * Default value: "<INPUT_JAR_FILE_NAME>.xml"
 */
public class OutputCommand extends Command {

    static final String XML_EXTENSION;

    static {
        XML_EXTENSION = ".xml";
    }

    private String filename;

    public OutputCommand(String options){
        super(options);
        filename = getCommandOptions();
        if(!filename.contains(XML_EXTENSION)){
            filename = filename + XML_EXTENSION;
        }
    }

    @Override
    public boolean isValid() {
        Pattern pattern = Pattern.compile("^[a-zA-Z0-9_.@()-]+\\.xml$");
        Matcher matcher = pattern.matcher(filename);
        return matcher.matches();
    }

    @Override
    public void dumpTo(ExtractorConfig config) {
        config.outputFilename = filename;
    }
}
