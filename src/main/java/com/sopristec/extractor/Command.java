package com.sopristec.extractor;

//  Inspired by:
//  https://bugfactory.io/blog/organizing-java-command-line-utilities-with-the-command-pattern/

public abstract class Command {
    private String commandOptions = new String();

    public Command(String options) {
        this.commandOptions = options;
    }

    public String getCommandOptions() {
        return this.commandOptions;
    }

    public abstract boolean isValid();

    public abstract void execute();

    public static String dasherizeName(
            Class<? extends Command> cmd
    ) {
        String n = cmd.getSimpleName().
                replaceAll("Command$", "");
        return n.replaceAll("([a-z])([A-Z])", "$1-$2").
                replaceAll("_", "-").toLowerCase();
    }
}