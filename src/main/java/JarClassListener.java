public class JarClassListener extends Java8ParserBaseListener {
    @Override
    public void enterClassBodyDeclaration(Java8Parser.ClassBodyDeclarationContext ctx) {
        super.enterClassBodyDeclaration(ctx);
        System.out.println("enterClassBodyDeclaration");
    }

    @Override
    public void enterClassBody(Java8Parser.ClassBodyContext ctx) {
        super.enterClassBody(ctx);
        System.out.println("enterClassBody");
    }

    @Override
    public void enterMethodDeclaration(Java8Parser.MethodDeclarationContext ctx) {
        super.enterMethodDeclaration(ctx);
        System.out.println("enterMethodDeclaration");
    }
}
