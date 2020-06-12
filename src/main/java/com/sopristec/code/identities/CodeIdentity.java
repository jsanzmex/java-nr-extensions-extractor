package com.sopristec.code.identities;

import java.util.ArrayList;

/**
 * Common base for all Code Identities like Methods, Classes, Constructors.
 * This class let us define behaviours and properties in common, like modifiers and accessors.
 */
public abstract class CodeIdentity {

    protected ArrayList<Modifier> modifiers;

    public void addModifier(String modifierStr)
    {
        Modifier[] modifierValues = Modifier.values();
        for(Modifier m: modifierValues) {
            if(m.name() == modifierStr.trim().toUpperCase())
                modifiers.add(m);
        }
    }

    public boolean isPublic()
    {
        return modifiers.indexOf(Modifier.PUBLIC) >= 0;
    }

    public boolean isStatic()
    {
        return modifiers.indexOf(Modifier.STATIC) >= 0;
    }

    public boolean isPrivate()
    {
        return modifiers.indexOf(Modifier.PRIVATE) >= 0;
    }

    public boolean isAbstract()
    {
        return modifiers.indexOf(Modifier.ABSTRACT) >= 0;
    }
}
