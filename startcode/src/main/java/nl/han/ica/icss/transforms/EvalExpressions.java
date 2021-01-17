package nl.han.ica.icss.transforms;

import nl.han.ica.icss.ast.*;
import nl.han.ica.icss.ast.literals.ColorLiteral;
import nl.han.ica.icss.ast.operations.AddOperation;
import nl.han.ica.icss.ast.operations.MultiplyOperation;
import nl.han.ica.icss.ast.operations.SubtractOperation;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
            ((Declaration) node).expression = (Expression) transformToLiteralNode(expression);
        }

    }

    private ASTNode transformToLiteralNode(ASTNode expression) {
        if (expression instanceof VariableReference) {
            String variableName = ((VariableReference) expression).name;
            // Make sure to validate the variable name
            if (variableName != null && !variableName.trim().isEmpty()) {
                // Get the top level node to drill through the nested nodes to find it literal value
                Optional<VariableAssignment> node = _availableVariables.stream()
                        .filter(o -> o.name.name.equals(variableName)).findFirst();

                if (node.isPresent()) {
                    var nestedNodes = node.get().getChildren();
                    if (nestedNodes != null && nestedNodes.size() > 0) {
                        // Get the last element because that's the next property we need to evaluate.
                        var lastElement = nestedNodes.get(nestedNodes.size() - 1);
                        // Send it back to the method in case we're dealing with a nested VariableReference (sigh)
                        if (lastElement != null)
                            return transformToLiteralNode(lastElement);
                    }
                }
            }
        } else if (expression instanceof AddOperation){
            return ((AddOperation)expression).calculate(_availableVariables);
        } else if (expression instanceof MultiplyOperation){
            return ((MultiplyOperation)expression).calculate(_availableVariables);
        } else if (expression instanceof SubtractOperation){
            return ((SubtractOperation)expression).calculate(_availableVariables);
        } else {
            System.out.println(String.format("Unkown type? %s", expression));
        }

        // If the type is a literal just return it straight away. We don't need to do something special so.
        return expression;
    }


}
