package nl.han.ica.icss.checker;

import com.sun.tools.jconsole.JConsoleContext;
import nl.han.ica.datastructures.IHANLinkedList;
import nl.han.ica.icss.ast.*;
import nl.han.ica.icss.ast.ASTNode;
import nl.han.ica.icss.ast.Declaration;
import nl.han.ica.icss.ast.literals.ColorLiteral;
import nl.han.ica.icss.ast.literals.PixelLiteral;
import nl.han.ica.icss.ast.selectors.ClassSelector;
import nl.han.ica.icss.ast.selectors.IdSelector;
import nl.han.ica.icss.ast.selectors.TagSelector;
import nl.han.ica.icss.ast.types.ExpressionType;

import java.util.ArrayList;
import java.util.HashMap;


// TODO: Currently supports only level 0. Add support for th others also.
public class Checker {
    private IHANLinkedList<HashMap<String, ExpressionType>> variableTypes;

    public void check(AST ast) {
        // variableTypes = new HANLinkedList<>();
        performValidationOnAST(ast.root.getChildren());
    }

    private void performValidationOnAST(ArrayList<ASTNode> children) {
        // Check if children are actually filled before we continue the validation
        if(children == null || children.size() < 0)
            return;

        for (ASTNode child: children) {
            // Would be better if we used a interface instead of a base class. Anyway.. this will do also.
            invokeValidation(child);

            // Validate also the children tree recursively
            performValidationOnAST(child.getChildren());
        }
    }

    // This is really ulgy but will do for now.
    private void invokeValidation(ASTNode child) {
        if(child instanceof TagSelector){
            ((TagSelector) child).validate();
        }
        else if (child instanceof Declaration){
            ((Declaration) child).validate();
        }
        else if (child instanceof ColorLiteral){
            ((ColorLiteral) child).validate();
        }
        else if (child instanceof PixelLiteral){
            ((PixelLiteral) child).validate();
        }
        else if (child instanceof IdSelector){
            ((IdSelector) child).validate();
        }
        else if (child instanceof ClassSelector){
            ((ClassSelector) child).validate();
        }
    }
}
