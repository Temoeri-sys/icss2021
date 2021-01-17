package nl.han.ica.icss.ast.helpers;

import nl.han.ica.icss.ast.ASTNode;
import nl.han.ica.icss.ast.Expression;
import nl.han.ica.icss.ast.VariableAssignment;
import nl.han.ica.icss.ast.VariableReference;
import nl.han.ica.icss.ast.literals.PercentageLiteral;
import nl.han.ica.icss.ast.literals.PixelLiteral;
import nl.han.ica.icss.ast.literals.ScalarLiteral;
import nl.han.ica.icss.ast.operations.AddOperation;
import nl.han.ica.icss.ast.operations.MultiplyOperation;
import nl.han.ica.icss.ast.operations.SubtractOperation;

import java.util.List;
import java.util.Optional;

public class ValidatorHelper {
    private final ASTNode _node;

    public ValidatorHelper(ASTNode astNode) {
        _node = astNode;
    }

    public ASTNode getLiteralNode(Expression expression, List<VariableAssignment> availableVariables) {
        if (expression instanceof VariableReference) {
            String errors = ((VariableReference) expression).validate(expression, availableVariables, null);
            if (errors == null || errors.trim().isEmpty()) {
                return getVariableNode(expression, availableVariables);
            } else {
                // We received an error from the VariableReference validation.
                _node.setError(errors);
            }
        } else if (expression instanceof MultiplyOperation) {
            String errors = ((MultiplyOperation) expression).validate(availableVariables);
            if (errors == null || errors.trim().isEmpty()) {
                return getMultiplyNode(expression, availableVariables);
            } else {
                _node.setError(errors);
            }
        } else if (expression instanceof AddOperation) {
            String errors = ((AddOperation) expression).validate(availableVariables);
            if (errors == null || errors.trim().isEmpty()) {
                return getAddNode(expression, availableVariables);
            } else {
                _node.setError(errors);
            }
        } else if (expression instanceof SubtractOperation) {
            String errors = ((SubtractOperation) expression).validate(availableVariables);
            if (errors == null || errors.trim().isEmpty()) {
                return getSubtractNode(expression, availableVariables);
            } else {
                _node.setError(errors);
            }
        } else if (expression instanceof PixelLiteral || expression instanceof PercentageLiteral
                || expression instanceof ScalarLiteral) {
            // Since Both types are the direct value we won't need to do additional validations.
            return expression;
        }

        // We could not validate the instance.
        return null;
    }

    private ASTNode getAddNode(Expression expression, List<VariableAssignment> availableVariables) {
        return ((AddOperation) expression).calculate(availableVariables);
    }

    private ASTNode getMultiplyNode(Expression expression, List<VariableAssignment> availableVariables) {
        return ((MultiplyOperation) expression).calculate(availableVariables);
    }

    private ASTNode getSubtractNode(Expression expression, List<VariableAssignment> availableVariables) {
        return ((SubtractOperation) expression).calculate(availableVariables);
    }

    private ASTNode getVariableNode(Expression expression, List<VariableAssignment> availableVariables) {
        // Property within scope, continue fetching it's value.
        Optional<VariableAssignment> node = availableVariables.stream()
                .filter(o -> o.name.name.equals(((VariableReference) expression).name)).findFirst();

        // Only continue if we found a node.
        if (node.isPresent()) {
            var nestedNodes = node.get().getChildren();
            if (nestedNodes != null && nestedNodes.size() > 0) {
                var lastElement = nestedNodes.get(nestedNodes.size() - 1);
                if (lastElement instanceof VariableAssignment) {
                    // Great we found a nested reference.. Just go down the tree and try to find the literal object.
                    return getVariableNode((Expression) lastElement, availableVariables);
                }
                return lastElement;
            }
        }
        return null;
    }
}
