package nl.han.ica.icss.ast.operations;

import nl.han.ica.icss.ast.ASTNode;
import nl.han.ica.icss.ast.Operation;
import nl.han.ica.icss.ast.VariableAssignment;
import nl.han.ica.icss.ast.literals.*;

import java.util.List;

public class MultiplyOperation extends Operation {

    @Override
    public String getNodeLabel() {
        return "Multiply";
    }

    public String validate(List<VariableAssignment> availableVariables) {
        ASTNode lhsNode = getLiteralNode(lhs, availableVariables);
        ASTNode rhsNode = getLiteralNode(rhs, availableVariables);

        if (lhsNode != null && rhsNode != null) {
            String lhsType = lhsNode.getClass().getSimpleName();
            String rhsType = rhsNode.getClass().getSimpleName();

            // Make sure that the types are not the same. If that's the case it's valid (only requirement)
            if (lhsType != rhsType) {
                if (lhsNode instanceof ColorLiteral || rhsNode instanceof ColorLiteral) {
                    return "IncalculableValueError: Color is not a valid type.";
                }
                if (lhsNode instanceof BoolLiteral || rhsNode instanceof BoolLiteral) {
                    return "IncalculableValueError: Boolean is not a valid type.";
                }
                return "";
            }
        }

        // Whenever we don't pass the validation above we'll end up at the error message we need. (Less if/else clauses)
        return "SameTypeError: Multiplying with the same type is not allowed.";
    }

    public ASTNode calculate(List<VariableAssignment> availableVariables) {
        ASTNode lhsNode = getLiteralNode(lhs, availableVariables);
        ASTNode rhsNode = getLiteralNode(rhs, availableVariables);

        if (lhsNode != null && rhsNode != null) {
            String lhsType = lhsNode.getClass().getSimpleName();
            String rhsType = rhsNode.getClass().getSimpleName();

            // Make sure that the types are not the same. If that's the case it's valid (only requirement)
            if (lhsType != rhsType) {
                if (lhsNode instanceof ColorLiteral || rhsNode instanceof ColorLiteral) {
                    setError("IncalculableValueError: Color is not a valid type.");
                    return null;
                }
                if (lhsNode instanceof BoolLiteral || rhsNode instanceof BoolLiteral) {
                    setError("IncalculableValueError: Boolean is not a valid type.");
                    return null;
                }

                // Either Lhs or Rhs is a Scalar. Use it's value with the value of the node to recalculate the value
                if (lhsNode instanceof PixelLiteral) {
                    return recalculate(rhsNode, ((ScalarLiteral) rhsNode).value);
                } else if (lhsNode instanceof PercentageLiteral) {
                    return recalculate(rhsNode, ((ScalarLiteral) rhsNode).value);
                }
                if (rhsNode instanceof PixelLiteral) {
                    return recalculate(rhsNode, ((ScalarLiteral) lhsNode).value);
                } else if (rhsNode instanceof PercentageLiteral) {
                    return recalculate(rhsNode, ((ScalarLiteral) lhsNode).value);
                }
            }
            setError("SameTypeError: Multiplying with the same type is not allowed.");
        }

        // Yikes, something went wrong.
        return null;
    }

    private ASTNode recalculate(ASTNode node, int value) {
        if (node instanceof PixelLiteral) {
            return new PixelLiteral(Multiply(((PixelLiteral) node).value, value));
        } else if (node instanceof PercentageLiteral) {
            return new PercentageLiteral(Multiply(((PercentageLiteral) node).value, value));
        }
        setError("TypeNotDefinedError: Could not recalculate value. Type is unknown.");
        return node;
    }

    private int Multiply(int a, int b) {
        return a * b;
    }

}
