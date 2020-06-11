package com.sopristec.extractor;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * The ANTLR4 listener class.
 */
public class JarClassListener extends Java8ParserBaseListener {

    private ExtensionsXmlEncoder encoder;
    private Node lastClassNode;
    private String lastAccessor = "";
    private String lastModifier = "";

    public JarClassListener(ExtensionsXmlEncoder encoder){
        this.encoder = encoder;
        // There is only one pointcut node, as we cannot know the inter-dependency
        // between methods and set false start points.
        encoder.getPointcutElement(true);
    }

    @Override
    public void enterNormalClassDeclaration(Java8Parser.NormalClassDeclarationContext ctx) {
        super.enterNormalClassDeclaration(ctx);
        SopristecLogManager.logger.trace("enterNormalClassDeclaration.Identifier: " + ctx.Identifier().toString());
        lastClassNode = encoder.getClassNode(TextUtils.resetDots(ctx.Identifier().toString()));
        encoder.appendToPointcutNode(lastClassNode);
    }

    @Override
    public void enterClassModifier(Java8Parser.ClassModifierContext ctx) {
        super.enterClassModifier(ctx);
        SopristecLogManager.logger.trace("enterClassModifier: " + ctx.getText());
        lastAccessor = "";
        lastModifier = "";
        if(isAccessor(ctx.getText())){
            lastAccessor = ctx.getText();
        }
        if(isModifier(ctx.getText())){
            lastModifier = ctx.getText();
        }
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
    private boolean isAccessor(String input)
    {
        return  input.toLowerCase().trim().contains("public") ||
                input.toLowerCase().trim().contains("private") ||
                input.toLowerCase().trim().contains("protected");
    }

    private boolean isPublic(String input)
    {
        return input.toLowerCase().trim() == "public";
    }

    private boolean isProtected(String input)
    {
        return input.toLowerCase().trim() == "protected";
    }

    private boolean isModifier(String input)
    {
        return  input.toLowerCase().trim().contains("abstract");
    }

    private boolean isAbstract(String input)
    {
        return input.toLowerCase().trim() == "abstract";
    }
    // endregion

}
