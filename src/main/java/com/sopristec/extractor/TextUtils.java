package com.sopristec.extractor;

public class TextUtils {
    private static final String DOT = "DOTOD";


    public static String unsetDots(String input){
        // DOTS (.) are not considered in the ANTLR4 Java's grammar.
        // But since "javap" returns fully qualified class names,
        // we need a way to deal with those long "dotted" class names.
        // The easies solution is to replace all DOT occurrences by a
        // replacement, rather than updating the grammar:
        return input.replaceAll("\\.", DOT);
    }

    public static String resetDots(String input){
        // The opposite operation as above's method:
        return input.replaceAll(DOT,"\\.");
    }

}
