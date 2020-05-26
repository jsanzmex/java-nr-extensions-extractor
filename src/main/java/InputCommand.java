import java.io.File;

public class InputCommand extends Command  {

    public String inputPath = null;

    public InputCommand(String options){
        super(options);

        inputPath = System.getProperty("user.dir") + '/' + getCommandOptions();
        System.out.println("Input path: " + inputPath);
    }

    @Override
    public boolean isValid() {
        if(getCommandOptions().isEmpty()) {
            System.out.println("No input JAR filename was specified!");
            return false;
        }

        if(!inputFileExists(inputPath)){
            System.out.println("Input file \"" + inputPath + "\" not found!");
            return false;
        }
        if(!inputFileIsJar(getCommandOptions())){
            System.out.println("Input file \"" + getCommandOptions() + "\" is not a valid Java Archive!");
            return false;
        }
        return true;
    }

    @Override
    public void execute() {
        //
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
