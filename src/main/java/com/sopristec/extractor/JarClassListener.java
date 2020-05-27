package com.sopristec.extractor;

public class JarClassListener extends Java8ParserBaseListener {

    private Java8Parser parser;

    public JarClassListener(Java8Parser parser){
        this.parser = parser;
    }

    @Override
    public void enterClassType(Java8Parser.ClassTypeContext ctx) {
        super.enterClassType(ctx);
        System.out.println("enterClassType: " + ctx.getText());
    }

    @Override
    public void enterMethodDeclaration(Java8Parser.MethodDeclarationContext ctx) {
        super.enterMethodDeclaration(ctx);
        System.out.println("enterMethodDeclaration: " + ctx.getText());
    }

    @Override
    public void exitMethodDeclaration(Java8Parser.MethodDeclarationContext ctx) {
        super.exitMethodDeclaration(ctx);
        System.out.println("exitMethodDeclaration: " + ctx.getText());
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
    public void enterMethodHeader(Java8Parser.MethodHeaderContext ctx) {
        super.enterMethodHeader(ctx);
        System.out.println("enterMethodHeader: " + ctx.getText());
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
    public void exitFormalParameterList(Java8Parser.FormalParameterListContext ctx) {
        System.out.println("exitFormalParameterList: " + ctx.getText());
        super.exitFormalParameterList(ctx);
    }

    @Override
    public void enterClassMemberDeclaration(Java8Parser.ClassMemberDeclarationContext ctx) {
        super.enterClassMemberDeclaration(ctx);
        if(ctx.getText().isEmpty() || isNotMethod(ctx.getText())){
            return;
        }
        System.out.println("enterClassMemberDeclaration: " + ctx.getText());
    }

    // Another way to say it is class member
    private boolean isNotMethod(String input){
        boolean hasAccessModifier = input.toLowerCase().indexOf("public") >= 0 || input.toLowerCase().indexOf("private") >= 0;
        boolean hasSemicolon = input.indexOf(";") >= 0;
        return hasAccessModifier && hasSemicolon;
    }
}
