package com.sopristec.code.identities;

import java.util.ArrayList;

/**
 * Common base for all Code Identities like Methods, Classes, Constructors.
 * This class let us define behaviours and properties in common, like modifiers and accessors.
 */
public abstract class CodeIdentity {

    protected String name;

    protected ArrayList<Modifier> modifiers = new ArrayList<>();

    protected CodeIdentity(String name) {
        this.name = name;
    }

    public void addModifier(String modifierStr)
    {
        Modifier[] modifierValues = Modifier.values();
        for(Modifier m: modifierValues) {
            if(m.name().equals(modifierStr.trim().toUpperCase()))
                modifiers.add(m);
        }
    }

    public boolean isPublic()
    {
        return modifiers.contains(Modifier.PUBLIC);
    }

    public boolean isStatic()
    {
        return modifiers.contains(Modifier.STATIC);
    }

    public boolean isPrivate()
    {
        return modifiers.contains(Modifier.PRIVATE);
    }

    public boolean isAbstract()
    {
        return modifiers.contains(Modifier.ABSTRACT);
    }

    public String getName() { return name; }

}
