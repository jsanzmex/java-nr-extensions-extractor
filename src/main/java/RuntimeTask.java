import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class RuntimeTask {

    public static List<String> executeCommand(String[] commands) throws IOException {
        Runtime rt = Runtime.getRuntime();
        Process prc = rt.exec(commands);
/*
        for(String s: commands){
            System.out.println("commands: " + s);
        }
*/
        BufferedReader stdInput = new BufferedReader(new
                InputStreamReader(prc.getInputStream()));

        BufferedReader stdError = new BufferedReader(new
                InputStreamReader(prc.getErrorStream()));

        // System.out.println("Here is the standard output of the command:\n");
        List<String> output = new ArrayList<String>();
        String s = null;
        while ((s = stdInput.readLine()) != null) {
            //System.out.println(s);
            output.add(s);
        }

        // System.out.println("Here is the standard error of the command (if any):\n");
        while ((s = stdError.readLine()) != null) {
            System.out.println(s);
        }
        return output;
    }
}
