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
            SopristecLogManager.logger.warn("\"-Dmethods\" argument is invalid. Will extract PUBLIC methods.");
            shouldExtractPublic = true;
        }else{
            String publicStr = this.shouldExtractPublic ? "PUBLIC" : "";
            String privateStr = this.shouldExtractPrivate ? "PRIVATE" : "";
            SopristecLogManager.logger.info("You have configured the Extract " + publicStr + privateStr + " methods.");
        }
        return true;
    }

    @Override
    public void dumpTo(ExtractorConfig config) {
        config.shouldExtractPrivate = shouldExtractPrivate;
        config.shouldExtractPublic = shouldExtractPublic;
    }
}
