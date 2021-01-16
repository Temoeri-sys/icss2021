package nl.han.ica.icss.checker;

import com.sun.tools.jconsole.JConsoleContext;
import nl.han.ica.datastructures.IHANLinkedList;
import nl.han.ica.icss.ast.*;
import nl.han.ica.icss.ast.ASTNode;
import nl.han.ica.icss.ast.Declaration;
import nl.han.ica.icss.ast.lists.HANLinkedList;
import nl.han.ica.icss.ast.literals.ColorLiteral;
import nl.han.ica.icss.ast.literals.PercentageLiteral;
import nl.han.ica.icss.ast.literals.PixelLiteral;
import nl.han.ica.icss.ast.selectors.ClassSelector;
import nl.han.ica.icss.ast.selectors.IdSelector;
import nl.han.ica.icss.ast.selectors.TagSelector;
import nl.han.ica.icss.ast.types.ExpressionType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Checker {

    // private IHANLinkedList<HashMap<String, ExpressionType>> variableTypes;

    // Since variables are only available within a specific scope, we'll use this to store them.
    // Whenever a variable will be accessed without it being added in the list we know there is a variable that is
    // being used outside of the scope if has been defined. E.g. the variable is used above and assigned below.
    private List<VariableAssignment> _availableVariables = new ArrayList<>();

    public void check(AST ast) {
        // variableTypes = new HANLinkedList<>();
        performValidationOnAST(ast.root.getChildren());
    }

    private void performValidationOnAST(ArrayList<ASTNode> children) {
        // Check if children are actually filled before we continue the validation
        if (children == null || children.size() < 0)
            return;

        for (ASTNode child : children) {
            // Would be better if we used a interface instead of a base class. Anyway.. this will do also.
            invokeValidation(child);

            // Validate also the children tree recursively
            performValidationOnAST(child.getChildren());
        }
    }

    // Most of the validation is done by the G4 regex. These are some custom business rules that we can't cover with
    // the regex patterns and need to validate the structure.
    private void invokeValidation(ASTNode child) {
        if (child instanceof VariableAssignment) {
            // If we receive a VariableAssigment add it to the list of the _availableVariables. This will mark that
            // the variable is accessable within the scope. By doing so, when another type is called, we'll have a list
            // to compare with and see if it's already been defined before the actual variable is being used.
            // Whenever the variable is used BEFORE it's defined, it'll not be within this list and the specific
            // type will raise a error.
            _availableVariables.add(((VariableAssignment) child));
        } else if (child instanceof Declaration) {
            ((Declaration) child).validate(_availableVariables);
        }
    }
}
