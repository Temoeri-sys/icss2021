package nl.han.ica.icss.transforms;

import nl.han.ica.icss.ast.*;
import nl.han.ica.icss.ast.helpers.ValidatorHelper;

import java.util.ArrayList;
import java.util.List;

public class EvalExpressions implements Transform {
    // Since variables are only available within a specific scope, we'll use this to store them.
    // Whenever a variable will be accessed without it being added in the list we know there is a variable that is
    // being used outside of the scope if has been defined. E.g. the variable is used above and assigned below.
    private final List<VariableAssignment> _availableVariables = new ArrayList<>();

    public EvalExpressions() {
    }

    @Override
    public void apply(AST ast) {
        transformAstStructure(ast.root.getChildren());
    }

    private void transformAstStructure(ArrayList<ASTNode> nodes) {
        // Check if nodes are actually filled before we continue the validation
        if (nodes == null || nodes.size() <= 0)
            return;

        for (ASTNode node : nodes) {
            // Would be better if we used a interface instead of a base class. Anyway.. this will do also.
            transformNode(node);

            // Let each node transform itself to the correct type recursively.
            transformAstStructure(node.getChildren());
        }
    }

    private void transformNode(ASTNode node) {
        // Lets start with variables
        if (node instanceof VariableAssignment) {
            // Store the variables in a scope to reference from later on.
            _availableVariables.add(((VariableAssignment) node));
        } else if (node instanceof Declaration) {
            Expression expression = ((Declaration) node).expression;
            ((Declaration) node).expression = (Expression) new ValidatorHelper().transformToLiteralNode(expression, _availableVariables);
        }
    }

}
