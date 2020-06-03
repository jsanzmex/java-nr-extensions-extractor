package com.sopristec.extractor;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

/**
 * Processes the command line "methods" argument: "-Dmethods"
 * Possible values: ["+","-","+-","-+"]
 * Default value:   "+"
 */
public class MethodsCommand extends Command {

    private final String inputFilename;
    private boolean shouldExtractPrivate = false;
    private boolean shouldExtractPublic = false;

    public MethodsCommand(String options, String inputFilename){
        super(options);
        if (getCommandOptions() != null && !getCommandOptions().isEmpty()){
            this.shouldExtractPrivate = getCommandOptions().contains("-");
            this.shouldExtractPublic = getCommandOptions().contains("+");
        }
        this.inputFilename = inputFilename;
    }

    @Override
    public boolean isValid() {
        if (!shouldExtractPrivate && !shouldExtractPublic){
            System.out.println("Methods argument is invalid. Default behaviour is to extract PUBLIC methods.");
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
