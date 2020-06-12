package com.sopristec.code.identities;

import com.sopristec.extractor.SopristecLogManager;

import java.util.ArrayList;

/**
 * Core representation of a Class, renamed to Klass to avoid using reserved names.
 */
public class Klass extends CodeIdentity {

    private String name;
    private Constructor constructor;
    private ArrayList<RegularMethod> regularMethodList;

    public Klass(String name) {
        this.name = name;
    }

    public void addMethod(Method method)
    {
        if(method instanceof Constructor){
            constructor = (Constructor) method;
            return;
        }
        if(method instanceof RegularMethod){
            regularMethodList.add((RegularMethod) method);
            return;
        }
        SopristecLogManager.logger.error("Passed Method " +method.toString() + " is not of a valid type!");
    }

    public Method lastRegularMethod()
    {
        return regularMethodList.get(regularMethodList.size()-1);
    }

    public String getName()
    {
        return name;
    }
}
