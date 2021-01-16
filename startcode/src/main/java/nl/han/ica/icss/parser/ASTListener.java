package nl.han.ica.icss.parser;

import nl.han.ica.datastructures.IHANStack;
import nl.han.ica.icss.ast.*;
import nl.han.ica.icss.ast.literals.*;
import nl.han.ica.icss.ast.operations.AddOperation;
import nl.han.ica.icss.ast.operations.MultiplyOperation;
import nl.han.ica.icss.ast.operations.SubtractOperation;
import nl.han.ica.icss.ast.selectors.ClassSelector;
import nl.han.ica.icss.ast.selectors.IdSelector;
import nl.han.ica.icss.ast.selectors.TagSelector;
import nl.han.ica.icss.lists.HANStack;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.checkerframework.checker.units.qual.C;

import java.io.Console;

/**
 * This class extracts the ICSS Abstract Syntax Tree from the Antlr Parse tree.
 */
public class ASTListener extends ICSSBaseListener {

    //Accumulator attributes:
    private final AST ast;

    //Use this to keep track of the parent nodes when recursively traversing the ast
    private final IHANStack<ASTNode> currentContainer;

    public ASTListener() {
        ast = new AST();
        currentContainer = new HANStack<>();
    }

    @Override
    public void enterStylesheet(ICSSParser.StylesheetContext ctx) {
        currentContainer.push(new Stylesheet());
        super.enterStylesheet(ctx);
    }

    @Override
    public void exitStylesheet(ICSSParser.StylesheetContext ctx) {
        ast.setRoot((Stylesheet) currentContainer.peek());
        super.exitStylesheet(ctx);

    }

    @Override
    public void enterVariableAssignment(ICSSParser.VariableAssignmentContext ctx) {
        currentContainer.push(new VariableAssignment());
        super.enterVariableAssignment(ctx);
    }

    @Override
    public void exitVariableAssignment(ICSSParser.VariableAssignmentContext ctx) {
        VariableAssignment element = (VariableAssignment) currentContainer.pop();
        currentContainer.peek().addChild(element);
        super.exitVariableAssignment(ctx);
    }

    @Override
    public void enterStylerule(ICSSParser.StyleruleContext ctx) {
        currentContainer.push(new Stylerule());
        super.enterStylerule(ctx);
    }

    @Override
    public void exitStylerule(ICSSParser.StyleruleContext ctx) {
        Stylerule element = (Stylerule) currentContainer.pop();
        currentContainer.peek().addChild(element);
        super.exitStylerule(ctx);
    }

    @Override
    public void enterIfClause(ICSSParser.IfClauseContext ctx) {
        currentContainer.push(new IfClause());
        super.enterIfClause(ctx);
    }

    @Override
    public void exitIfClause(ICSSParser.IfClauseContext ctx) {
        IfClause element = (IfClause) currentContainer.pop();
        currentContainer.peek().addChild(element);
        super.exitIfClause(ctx);
    }

    @Override
    public void enterElseClause(ICSSParser.ElseClauseContext ctx) {
        currentContainer.push(new ElseClause());
        super.enterElseClause(ctx);
    }

    @Override
    public void exitElseClause(ICSSParser.ElseClauseContext ctx) {
        ElseClause element = (ElseClause) currentContainer.pop();
        currentContainer.peek().addChild(element);
        super.exitElseClause(ctx);
    }

    @Override
    public void enterVariableReference(ICSSParser.VariableReferenceContext ctx) {
        if (ctx.CAPITAL_IDENT() != null)
            currentContainer.push(new VariableReference(ctx.CAPITAL_IDENT().toString()));
        super.enterVariableReference(ctx);
    }

    @Override
    public void exitVariableReference(ICSSParser.VariableReferenceContext ctx) {
        VariableReference element = (VariableReference) currentContainer.pop();
        currentContainer.peek().addChild(element);
        super.exitVariableReference(ctx);
    }

    @Override
    public void enterBoolLiteral(ICSSParser.BoolLiteralContext ctx) {
        if (ctx.TRUE() != null)
            currentContainer.push(new BoolLiteral(ctx.TRUE().toString()));
        else if (ctx.FALSE() != null)
            currentContainer.push(new BoolLiteral(ctx.FALSE().toString()));
        super.enterBoolLiteral(ctx);
    }

    @Override
    public void exitBoolLiteral(ICSSParser.BoolLiteralContext ctx) {
        BoolLiteral element = (BoolLiteral) currentContainer.pop();
        currentContainer.peek().addChild(element);
        super.exitBoolLiteral(ctx);
    }

    @Override
    public void enterDeclaration(ICSSParser.DeclarationContext ctx) {
        currentContainer.push(new Declaration());
        super.enterDeclaration(ctx);
    }

    @Override
    public void exitDeclaration(ICSSParser.DeclarationContext ctx) {
        Declaration element = (Declaration) currentContainer.pop();
        currentContainer.peek().addChild(element);
        super.exitDeclaration(ctx);
    }

    @Override
    public void enterAddOperation(ICSSParser.AddOperationContext ctx) {
        currentContainer.push(new AddOperation());
        super.enterAddOperation(ctx);
    }

    @Override
    public void exitAddOperation(ICSSParser.AddOperationContext ctx) {
        AddOperation element = (AddOperation) currentContainer.pop();
        currentContainer.peek().addChild(element);
        super.exitAddOperation(ctx);
    }

    @Override
    public void enterSubtractOperation(ICSSParser.SubtractOperationContext ctx) {
        currentContainer.push(new SubtractOperation());
        super.enterSubtractOperation(ctx);
    }

    @Override
    public void exitSubtractOperation(ICSSParser.SubtractOperationContext ctx) {
        SubtractOperation element = (SubtractOperation) currentContainer.pop();
        currentContainer.peek().addChild(element);
        super.exitSubtractOperation(ctx);
    }

    @Override
    public void enterMultiplyOperation(ICSSParser.MultiplyOperationContext ctx) {
        currentContainer.push(new MultiplyOperation());
        super.enterMultiplyOperation(ctx);
    }

    @Override
    public void exitMultiplyOperation(ICSSParser.MultiplyOperationContext ctx) {
        MultiplyOperation element = (MultiplyOperation) currentContainer.pop();
        currentContainer.peek().addChild(element);
        super.exitMultiplyOperation(ctx);
    }

    @Override
    public void enterTagSelector(ICSSParser.TagSelectorContext ctx) {
        if (ctx.LOWER_IDENT() != null)
            currentContainer.push(new TagSelector(ctx.LOWER_IDENT().toString()));
        super.enterTagSelector(ctx);
    }

    @Override
    public void exitTagSelector(ICSSParser.TagSelectorContext ctx) {
        TagSelector element = (TagSelector) currentContainer.pop();
        currentContainer.peek().addChild(element);
        super.exitTagSelector(ctx);
    }

    @Override
    public void enterIdSelector(ICSSParser.IdSelectorContext ctx) {
        if (ctx.ID_IDENT() != null)
            currentContainer.push(new IdSelector(ctx.ID_IDENT().toString()));
        super.enterIdSelector(ctx);
    }

    @Override
    public void exitIdSelector(ICSSParser.IdSelectorContext ctx) {
        IdSelector element = (IdSelector) currentContainer.pop();
        currentContainer.peek().addChild(element);
        super.exitIdSelector(ctx);
    }

    @Override
    public void enterClassSelector(ICSSParser.ClassSelectorContext ctx) {
        if (ctx.CLASS_IDENT() != null)
            currentContainer.push(new ClassSelector(ctx.CLASS_IDENT().toString()));
        super.enterClassSelector(ctx);
    }

    @Override
    public void exitClassSelector(ICSSParser.ClassSelectorContext ctx) {
        ClassSelector element = (ClassSelector) currentContainer.pop();
        currentContainer.peek().addChild(element);
        super.exitClassSelector(ctx);
    }

    @Override
    public void enterColorLiteral(ICSSParser.ColorLiteralContext ctx) {
        if (ctx.COLOR() != null)
            currentContainer.push(new ColorLiteral(ctx.COLOR().toString()));
        super.enterColorLiteral(ctx);
    }

    @Override
    public void exitColorLiteral(ICSSParser.ColorLiteralContext ctx) {
        ColorLiteral element = (ColorLiteral) currentContainer.pop();
        currentContainer.peek().addChild(element);
        super.exitColorLiteral(ctx);
    }

    @Override
    public void enterPixelLiteral(ICSSParser.PixelLiteralContext ctx) {
        if (ctx.PIXELSIZE() != null)
            currentContainer.push(new PixelLiteral(ctx.PIXELSIZE().toString()));
        super.enterPixelLiteral(ctx);
    }

    @Override
    public void exitPixelLiteral(ICSSParser.PixelLiteralContext ctx) {
        PixelLiteral element = (PixelLiteral) currentContainer.pop();
        currentContainer.peek().addChild(element);
        super.exitPixelLiteral(ctx);
    }

    @Override
    public void enterPercentageLiteral(ICSSParser.PercentageLiteralContext ctx) {
        if (ctx.PERCENTAGE() != null)
            currentContainer.push(new PercentageLiteral(ctx.PERCENTAGE().toString()));
        super.enterPercentageLiteral(ctx);

    }

    @Override
    public void exitPercentageLiteral(ICSSParser.PercentageLiteralContext ctx) {
        PercentageLiteral element = (PercentageLiteral) currentContainer.pop();
        currentContainer.peek().addChild(element);
        super.exitPercentageLiteral(ctx);
    }

    @Override
    public void enterProperty(ICSSParser.PropertyContext ctx) {
        if (ctx.LOWER_IDENT() != null)
            currentContainer.push(new PropertyName(ctx.LOWER_IDENT().toString()));
        super.enterProperty(ctx);
    }

    @Override
    public void exitProperty(ICSSParser.PropertyContext ctx) {
        PropertyName element = (PropertyName) currentContainer.pop();
        currentContainer.peek().addChild(element);
        super.enterProperty(ctx);
    }

    @Override
    public void enterScalarLiteral(ICSSParser.ScalarLiteralContext ctx) {
        if (ctx.SCALAR() != null)
            currentContainer.push(new ScalarLiteral(ctx.SCALAR().toString()));
        super.enterScalarLiteral(ctx);
    }

    @Override
    public void exitScalarLiteral(ICSSParser.ScalarLiteralContext ctx) {
        ScalarLiteral element = (ScalarLiteral) currentContainer.pop();
        currentContainer.peek().addChild(element);
        super.exitScalarLiteral(ctx);
    }

    public AST getAST() {
        return ast;
    }

}