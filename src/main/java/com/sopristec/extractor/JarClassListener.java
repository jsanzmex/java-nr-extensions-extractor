package com.sopristec.extractor;

import com.sopristec.code.identities.Constructor;
import com.sopristec.code.identities.Klass;
import com.sopristec.code.identities.RegularMethod;

import java.util.ArrayList;

/**
 * The ANTLR4 listener class.
 */
public class JarClassListener extends Java8ParserBaseListener {

    private final ArrayList<Klass> klassList = new ArrayList<>();

    @Override
    public void enterNormalClassDeclaration(Java8Parser.NormalClassDeclarationContext ctx) {
        super.enterNormalClassDeclaration(ctx);
        SopristecLogManager.logger.trace("enterNormalClassDeclaration.Identifier: " + ctx.Identifier().toString());
        klassList.add(new Klass(TextUtils.resetDots(ctx.Identifier().toString())));
    }

    @Override
    public void enterClassModifier(Java8Parser.ClassModifierContext ctx) {
        super.enterClassModifier(ctx);
        SopristecLogManager.logger.trace("enterClassModifier: " + ctx.getText());
        lastKlass().addModifier(ctx.getText());
    }

    @Override
    public void enterConstructorModifier(Java8Parser.ConstructorModifierContext ctx) {
        super.enterConstructorModifier(ctx);
        SopristecLogManager.logger.trace("enterConstructorModifier: " + ctx.getText());
        lastKlass().addUnassignedMethodModifier(ctx.getText());
    }

    @Override
    public void enterConstructorDeclarator(Java8Parser.ConstructorDeclaratorContext ctx) {
        super.enterConstructorDeclarator(ctx);
        SopristecLogManager.logger.trace("enterConstructorDeclarator.simpleTypeName: " + ctx.simpleTypeName().getText());
        lastKlass().addMethod(new Constructor(TextUtils.resetDots(ctx.simpleTypeName().getText())));
    }

    @Override
    public void enterMethodModifier(Java8Parser.MethodModifierContext ctx) {
        super.enterMethodModifier(ctx);
        SopristecLogManager.logger.trace("enterMethodModifier: " + ctx.getText());
        lastKlass().addUnassignedMethodModifier(ctx.getText());
    }

    @Override
    public void enterMethodDeclarator(Java8Parser.MethodDeclaratorContext ctx) {
        super.enterMethodDeclarator(ctx);
        SopristecLogManager.logger.trace("enterMethodDeclarator: " + ctx.getText());
        lastKlass().addMethod(new RegularMethod(TextUtils.resetDots(ctx.getText())));
    }

    @Override
    public void enterFormalParameterList(Java8Parser.FormalParameterListContext ctx) {
        SopristecLogManager.logger.trace("enterFormalParameterList: " + ctx.getText());
        super.enterFormalParameterList(ctx);
        String[] parameters = ctx.getText().split(",");
        for(String parameter: parameters){
            lastKlass().lastMethod().addParameter(TextUtils.resetDots(parameter));
        }
    }

    // region Private Members
    private Klass lastKlass()
    {
        return klassList.get(klassList.size() - 1);
    }
    // endregion

    // region API
    public ArrayList<Klass> getKlasses()
    {
        return new ArrayList<>(klassList);
    }
    // endregion

}
