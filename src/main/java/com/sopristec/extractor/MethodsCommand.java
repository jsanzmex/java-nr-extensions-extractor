package com.sopristec.extractor;

/**
 * Processes the command line "methods" argument: "-Dmethods"
 * Possible values: ["+","-","+-","-+"]
 * Default value:   "+"
 */
public class MethodsCommand extends Command {

    private boolean shouldExtractPrivate = false;
    private boolean shouldExtractPublic = false;

    public MethodsCommand(String options){
        super(options);
        if (getCommandOptions() != null && !getCommandOptions().isEmpty()){
            this.shouldExtractPrivate = getCommandOptions().contains("-");
            this.shouldExtractPublic = getCommandOptions().contains("+");
        }
    }

    @Override
    public boolean isValid() {
        if (!shouldExtractPrivate && !shouldExtractPublic){
            SopristecLogManager.logger.info("\"-Dmethods\" argument is invalid. Will extract PUBLIC methods.");
            shouldExtractPublic = true;
        }
        return true;
    }

    @Override
    public void setup(ExtractorConfig config) {
        config.shouldExtractPrivate = shouldExtractPrivate;
        config.shouldExtractPublic = shouldExtractPublic;
    }
}
