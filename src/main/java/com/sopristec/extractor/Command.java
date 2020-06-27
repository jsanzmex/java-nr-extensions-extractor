package com.sopristec.extractor;

//  Inspired by:
//  https://bugfactory.io/blog/organizing-java-command-line-utilities-with-the-command-pattern/

public abstract class Command {
    private String commandOptions;

    public Command(String options) {
        this.commandOptions = options;
    }

    public String getCommandOptions() {
        return this.commandOptions;
    }

    public abstract boolean isValid();

    public abstract void dumpTo(ExtractorConfig config);

    public static String dasherizeName(
            Class<? extends Command> cmd
    ) {
        String n = cmd.getSimpleName().
                replaceAll("Command$", "");
        return n.replaceAll("([a-z])([A-Z])", "$1-$2").
                replaceAll("_", "-").toLowerCase();
    }
}