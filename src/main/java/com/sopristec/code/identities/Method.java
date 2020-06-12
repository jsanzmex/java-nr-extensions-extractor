package com.sopristec.code.identities;

import java.util.ArrayList;

/**
 * Abstract representation of methods, including constructors.
 */
public abstract class Method extends CodeIdentity {

    protected ArrayList<String> parameterList;

    public void addParameter(String type)
    {
        parameterList.add(type);
    }

}
