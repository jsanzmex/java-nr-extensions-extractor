package com.sopristec.code.identities;

import com.sopristec.extractor.SopristecLogManager;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Abstract representation of methods, including constructors.
 */
public abstract class Method extends CodeIdentity {

    protected ArrayList<String> parameterList = new ArrayList<>();

    public Method(String simpleTypeName) {
        super("");

        String METHOD_NAME_PATTERN = "([a-zA-Z][a-zA-Z0-9]+)\\(";
        Pattern pattern = Pattern.compile(METHOD_NAME_PATTERN);
        Matcher matcher = pattern.matcher(simpleTypeName);
        if (matcher.find()) {
            try {
                SopristecLogManager.logger.trace("Matched method name: " + matcher.group(1));
                this.name = matcher.group(1);
            }catch(Exception e){
                SopristecLogManager.logger.trace("simpleTypeName matches but could not extract method name from: " + simpleTypeName);
            }
        } else {
            if (this instanceof Constructor ){
                this.name = simpleTypeName;
            }else{
                SopristecLogManager.logger.trace("Could not find a valid method name for a RegularMethod in 'simple type name' : " + simpleTypeName);
            }
        }

    }

    public void addParameter(String type)
    {
        parameterList.add(type);
    }

    public ArrayList<String> getParameterList()
    {
        return new ArrayList<>(parameterList);
    }

}
