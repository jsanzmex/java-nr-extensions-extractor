import java.io.*;

public class OutputCommand extends Command {

    private String extensionsBody;

    public OutputCommand(String options, String extensionsBody){
        super(options);
        this.extensionsBody = extensionsBody;
    }

    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public void execute() {
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(getCommandOptions()), "utf-8"))) {
            writer.write(extensionsBody);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
