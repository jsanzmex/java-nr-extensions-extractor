package com.sopristec.extractor;

import java.io.File;

/**
 * Processes the command line "input" argument: "-Dinput"
 * Possible values: Path to a valid JAR file, e.g. "MyLibrary.jar"
 * Compulsory argument, there is no optional value.
 */
public class InputCommand extends Command  {

    private String inputPath;
    private String inputFilename;
    private String outputPath;

    public InputCommand(String options){
        super(options);

        File file = new File(getCommandOptions());
        inputPath = file.getAbsolutePath();
        inputFilename = file.getName();
        outputPath = changePathExtension(file, "xml");
        SopristecLogManager.logger.info("Input path: " + inputPath);
        SopristecLogManager.logger.info("Output path: " + outputPath);
    }

    @Override
    public boolean isValid() {

        if (getCommandOptions() == null || getCommandOptions().isEmpty()){
            SopristecLogManager.logger.error("No input JAR filename was specified!");
            return false;
        }

        if(!inputFileExists(inputPath)){
            SopristecLogManager.logger.error("Input file \"" + inputPath + "\" not found!");
            return false;
        }
        if(!inputFileIsJar(getCommandOptions())){
            SopristecLogManager.logger.error("Input file \"" + getCommandOptions() + "\" is not a valid Java Archive!");
            return false;
        }
        return true;
    }

    @Override
    public void dumpTo(ExtractorConfig config) {
        config.inputPath = inputPath;
        config.inputFilename = inputFilename;
        config.outputPath = outputPath;
    }

    private boolean inputFileExists(String inputFilename){
        File f = new File(inputFilename);
        return f.exists();
    }

    private boolean inputFileIsJar(String inputFilename){
        String[] temp = inputFilename.split("\\.");
        return temp[temp.length-1].toLowerCase().equals("jar") || temp[temp.length-1].toLowerCase().equals("war");
    }

    private static String changePathExtension(File file, String extension) {
        String path = file.getAbsolutePath();

        if (path.contains(".")) {
            path = path.substring(0, path.lastIndexOf('.'));
        }
        path += "." + extension;

        return path;
    }
}
