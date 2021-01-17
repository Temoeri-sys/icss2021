package nl.han.ica.icss.checker;

import nl.han.ica.icss.ast.*;
import nl.han.ica.icss.ast.ASTNode;
import nl.han.ica.icss.ast.Declaration;
import nl.han.ica.icss.ast.operations.AddOperation;
import nl.han.ica.icss.ast.operations.MultiplyOperation;
import nl.han.ica.icss.ast.operations.SubtractOperation;

import java.util.ArrayList;
import java.util.List;


public class Checker {
    // Since variables are only available within a specific scope, we'll use this to store them.
    // Whenever a variable will be accessed without it being added in the list we know there is a variable that is
    // being used outside of the scope if has been defined. E.g. the variable is used above and assigned below.
    private List<VariableAssignment> _availableVariables = new ArrayList<>();

    public void check(AST ast) {
        performValidationOnAST(ast.root.getChildren());
    }

    private void performValidationOnAST(ArrayList<ASTNode> nodes) {
        // Check if nodes are actually filled before we continue the validation
        if (nodes == null || nodes.size() < 0)
            return;

        for (ASTNode node : nodes) {
            // Would be better if we used a interface instead of a base class. Anyway.. this will do also.
            validateNode(node);

            // Validate also the nodes tree recursively
            performValidationOnAST(node.getChildren());
        }
    }

    // Most of the validation is done by the G4 regex. These are some custom business rules that we can't cover with
    // the regex patterns and need to validate the structure.
    private void validateNode(ASTNode node) {

        if (node instanceof VariableAssignment) {
            // If we receive a VariableAssigment add it to the list of the _availableVariables. This will mark that
            // the variable is accessible within the scope. By doing so, when another type is called, we'll have a list
            // to compare with and see if it's already been defined before the actual variable is being used.
            // Whenever the variable is used BEFORE it's defined, it'll not be within this list and the specific
            // type will raise a error.
            _availableVariables.add(((VariableAssignment) node));
        } else if (node instanceof Declaration) {
            ((Declaration) node).validate(_availableVariables);
        } else if (node instanceof AddOperation) {
            node.SetErrorIfAny(((AddOperation) node).validate(_availableVariables));
        } else if (node instanceof SubtractOperation) {
            node.SetErrorIfAny(((SubtractOperation) node).validate(_availableVariables));
        } else if (node instanceof MultiplyOperation) {
            node.SetErrorIfAny(((MultiplyOperation) node).validate(_availableVariables));
        } else if (node instanceof IfClause) {
            node.SetErrorIfAny(((IfClause) node).validate(_availableVariables));
        }

    }
}
