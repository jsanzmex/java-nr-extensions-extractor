package com.sopristec.code.identities;

import com.sopristec.extractor.SopristecLogManager;

import java.util.ArrayList;

/**
 * Core representation of a Class, renamed to Klass to avoid using reserved names.
 */
public class Klass extends CodeIdentity {

    private Constructor constructor;
    private final ArrayList<Method> methodList = new ArrayList<Method>();
    private final ArrayList<Modifier> unassignedMethodModifierList = new ArrayList<Modifier>();

    public Klass(String name) {
        super(name);
    }

    // region API

    public void addMethod(Method method)
    {
        if(method instanceof Constructor){
            constructor = (Constructor) method;
            claimUnassignedModifiers(method);
            methodList.add(method);
        }else if(method instanceof RegularMethod){
            claimUnassignedModifiers(method);
            methodList.add(method);
        }else{
            SopristecLogManager.logger.error("Passed Method " +method.toString() + " is not of a valid type!");
        }
    }

    /**
     * All modifiers are identified before its' methods.
     * That is why all method modifiers added to a class are
     * unassigned. The assignation is done when a method claims
     * them.
     * @param modifierStr
     */
    public void addUnassignedMethodModifier(String modifierStr)
    {
        Modifier[] modifierValues = Modifier.values();
        for(Modifier m: modifierValues) {
            if(m.name().equals(modifierStr.trim().toUpperCase()))
                unassignedMethodModifierList.add(m);
        }
    }

    public Method lastMethod()
    {
        return methodList.get(methodList.size()-1);
    }

    public String getName()
    {
        return name;
    }
    // endregion

    // region Private Members
    private void claimUnassignedModifiers(Method method)
    {
        method.modifiers = new ArrayList<>(unassignedMethodModifierList);
        unassignedMethodModifierList.clear();
    }
    // endregion
}
