package com.sopristec.extractor;

import java.util.Arrays;

/**
 * Processes the command line "nrversion" argument: "-Dnrversion"
 * Possible values: [5.9.0, 4.3.0]
 * Default Value: "5.9.0"
 */
public class NewRelicAgentVersionCommand extends Command {

    private String agentVersion;
    private Boolean foundValidVersion = false;

    private final String DEFAULT_VERSION = "5.9.0";
    private final String[] ALL_VERSIONS =  new String[] {"4.3.0", "5.9.0"};

    public NewRelicAgentVersionCommand(String options){
        super(options);

        if (getCommandOptions() != null && !getCommandOptions().isEmpty()){
            if(Arrays.asList(ALL_VERSIONS).contains(getCommandOptions().trim())){
                agentVersion = getCommandOptions().trim();
                foundValidVersion = true;
            }
        }else{
            SopristecLogManager.logger.info("No New Relic Agent Version was specified!");
        }
    }

    @Override
    public boolean isValid() {

         if(!foundValidVersion) {
            SopristecLogManager.logger.warn(String.format("New Relic Agent Version [%s] is not supported. Will use default version [%s]", getCommandOptions(), DEFAULT_VERSION));
            agentVersion = DEFAULT_VERSION;
         }else{
             SopristecLogManager.logger.info("You have configured Extractor to conform resulting XML to New Relic Agent Version: " + agentVersion);
         }
         return true;
    }

    @Override
    public void dumpTo(ExtractorConfig config) {
        config.newRelicAgentversion = agentVersion;
    }

}
