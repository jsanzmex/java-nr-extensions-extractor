package com.sopristec.extractor;

/**
 * The ANTLR4 listener class.
 */
public class JarClassListener extends Java8ParserBaseListener {

    private ExtensionsXmlEncoder encoder;

    public JarClassListener(ExtensionsXmlEncoder encoder){
        this.encoder = encoder;
    }

    @Override
    public void enterNormalClassDeclaration(Java8Parser.NormalClassDeclarationContext ctx) {
        super.enterNormalClassDeclaration(ctx);
        SopristecLogManager.logger.trace("enterNormalClassDeclaration.Identifier: " + ctx.Identifier().toString());
    }

    @Override
    public void enterClassModifier(Java8Parser.ClassModifierContext ctx) {
        super.enterClassModifier(ctx);
        SopristecLogManager.logger.trace("enterClassModifier: " + ctx.getText());
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

    @Override
    public void enterClassMemberDeclaration(Java8Parser.ClassMemberDeclarationContext ctx) {
        super.enterClassMemberDeclaration(ctx);
        if(     ctx.getText().isEmpty() ||
                ctx.getText() == "\\r" ||
                ctx.getText() == "\\n" ||
                ctx.getText() == "\\;" ||
                isNotMethod(ctx.getText())){
            return;
        }
        SopristecLogManager.logger.trace("enterClassMemberDeclaration: " + ctx.getText());
    }

    // Another way to say it is class member
    private boolean isNotMethod(String input){
        boolean hasAccessModifier = input.toLowerCase().contains("public") || input.toLowerCase().contains("private");
        boolean hasSemicolon = input.contains(";");
        return hasAccessModifier && hasSemicolon;
    }
}
