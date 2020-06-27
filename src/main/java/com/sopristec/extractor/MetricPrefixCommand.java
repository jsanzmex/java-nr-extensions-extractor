package com.sopristec.extractor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Processes the command line "metricPrefix" argument: "-DmetricPrefix"
 * Possible values: Any alpha-numeric value that does not start with a number.
 *                  Spaces will
 * Default Value: "Custom"
 */
public class MetricPrefixCommand extends Command {

    private String prefix;
    private final String CUSTOM = "Custom";

    public MetricPrefixCommand(String options) {
        super(options);
        if(getCommandOptions() != null){
            prefix = getCommandOptions()
                    .replaceAll("\\.","-")
                    .replaceAll(" ", "-");
        }else{
            prefix = CUSTOM;
        }
    }

    @Override
    public boolean isValid() {
        Pattern pattern = Pattern.compile("^[a-zA-Z_$][a-zA-Z_$0-9]*$");
        Matcher matcher = pattern.matcher(prefix);
        if (!matcher.matches()){
            prefix = CUSTOM;
        }
        return true;
    }

    @Override
    public void dumpTo(ExtractorConfig config) {
        config.metricPrefix = prefix;
    }
}
