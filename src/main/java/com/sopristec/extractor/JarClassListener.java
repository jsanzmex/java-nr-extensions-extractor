package com.sopristec.extractor;

import com.sopristec.code.identities.Klass;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.ArrayList;

/**
 * The ANTLR4 listener class.
 */
public class JarClassListener extends Java8ParserBaseListener {

    private ArrayList<Klass> klassList = new ArrayList<>();

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
    public void enterConstructorDeclarator(Java8Parser.ConstructorDeclaratorContext ctx) {
        super.enterConstructorDeclarator(ctx);
        SopristecLogManager.logger.trace("enterConstructorDeclarator.simpleTypeName: " + ctx.simpleTypeName().getText());
    }

    @Override
    public void enterMethodModifier(Java8Parser.MethodModifierContext ctx) {
        super.enterMethodModifier(ctx);
        SopristecLogManager.logger.trace("enterMethodModifier: " + ctx.getText());
    }

    @Override
    public void enterMethodDeclarator(Java8Parser.MethodDeclaratorContext ctx) {
        super.enterMethodDeclarator(ctx);
        SopristecLogManager.logger.trace("enterMethodDeclarator: " + ctx.getText());
    }

    @Override
    public void enterFormalParameterList(Java8Parser.FormalParameterListContext ctx) {
        SopristecLogManager.logger.trace("enterFormalParameterList: " + ctx.getText());
        super.enterFormalParameterList(ctx);
    }

    // region Utils
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
