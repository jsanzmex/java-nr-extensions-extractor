package com.sopristec.extractor;

import java.io.File;

/**
 * Processes the command line "input" argument: "-Dinput"
 * Possible values: Path to a valid JAR file, e.g. "MyLibrary.jar"
 * Compulsory argument, there is no optional value.
 */
public class InputCommand extends Command  {

    private String inputPath;

    public InputCommand(String options){
        super(options);

        inputPath = System.getProperty("user.dir") + '/' + getCommandOptions();
        SopristecLogManager.logger.debug("Input path: " + inputPath);
    }

    @Override
    public boolean isValid() {

        if (getCommandOptions() == null || getCommandOptions().isEmpty()){
            SopristecLogManager.logger.info("No input JAR filename was specified!");
            return false;
        }

        if(!inputFileExists(inputPath)){
            SopristecLogManager.logger.debug("Input file \"" + inputPath + "\" not found!");
            return false;
        }
        if(!inputFileIsJar(getCommandOptions())){
            SopristecLogManager.logger.debug("Input file \"" + getCommandOptions() + "\" is not a valid Java Archive!");
            return false;
        }
        return true;
    }

    @Override
    public void dumpTo(ExtractorConfig config) {
        config.inputPath = inputPath;
        config.inputFilename = getCommandOptions();
    }

    private boolean inputFileExists(String inputFilename){
        File f = new File(inputFilename);
        return f.exists();
    }

    private boolean inputFileIsJar(String inputFilename){
        String[] temp = inputFilename.split("\\.");
        return temp[temp.length-1].toLowerCase().equals("jar");
    }

}
