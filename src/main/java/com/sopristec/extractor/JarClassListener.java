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
        System.out.println("enterNormalClassDeclaration.Identifier: " + ctx.Identifier().toString());
    }

    @Override
    public void enterClassModifier(Java8Parser.ClassModifierContext ctx) {
        super.enterClassModifier(ctx);
        System.out.println("enterClassModifier: " + ctx.getText());
    }

    @Override
    public void enterMethodModifier(Java8Parser.MethodModifierContext ctx) {
        super.enterMethodModifier(ctx);
        System.out.println("enterMethodModifier: " + ctx.getText());
    }

    @Override
    public void enterMethodDeclarator(Java8Parser.MethodDeclaratorContext ctx) {
        super.enterMethodDeclarator(ctx);
        System.out.println("enterMethodDeclarator: " + ctx.getText());
    }

    @Override
    public void enterFormalParameterList(Java8Parser.FormalParameterListContext ctx) {
        System.out.println("enterFormalParameterList: " + ctx.getText());
        super.enterFormalParameterList(ctx);
    }

    @Override
    public void enterClassBodyDeclaration(Java8Parser.ClassBodyDeclarationContext ctx) {
        System.out.println("enterClassBodyDeclaration: " + ctx.getText());
        super.enterClassBodyDeclaration(ctx);
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
        System.out.println("enterClassMemberDeclaration: " + ctx.getText());
    }

    // Another way to say it is class member
    private boolean isNotMethod(String input){
        boolean hasAccessModifier = input.toLowerCase().contains("public") || input.toLowerCase().contains("private");
        boolean hasSemicolon = input.contains(";");
        return hasAccessModifier && hasSemicolon;
    }
}
