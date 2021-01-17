package nl.han.ica.icss.ast.operations;

import nl.han.ica.icss.ast.*;
import nl.han.ica.icss.ast.literals.*;

import java.util.List;

public class AddOperation extends Operation {


    @Override
    public String getNodeLabel() {
        return "Add";
    }

    public String validate(List<VariableAssignment> availableVariables) {
        ASTNode lhsNode = getLiteralNode(lhs, availableVariables);
        ASTNode rhsNode = getLiteralNode(rhs, availableVariables);

        if (lhsNode != null && rhsNode != null) {
            String lhsType = lhsNode.getClass().getSimpleName();
            String rhsType = rhsNode.getClass().getSimpleName();

            if (lhsType == rhsType) {
                if (lhsNode instanceof ColorLiteral || rhsNode instanceof ColorLiteral)
                    return "IncalculableValueError: Color is not a valid type.";
                if (lhsNode instanceof BoolLiteral || rhsNode instanceof BoolLiteral)
                    return "IncalculableValueError: Boolean is not a valid type.";
                return "";
            }
        }

        return "TypeMismatchError: The 'add' operation only support values of the same type.";
    }


    public ASTNode calculate(List<VariableAssignment> availableVariables) {
        ASTNode lhsNode = getLiteralNode(lhs, availableVariables);
        ASTNode rhsNode = getLiteralNode(rhs, availableVariables);

        if (lhsNode != null && rhsNode != null) {
            String lhsType = lhsNode.getClass().getSimpleName();
            String rhsType = rhsNode.getClass().getSimpleName();

            // Make sure that the types are not the same. If that's the case it's valid (only requirement)
            if (lhsType == rhsType) {
                if (lhsNode instanceof ColorLiteral || rhsNode instanceof ColorLiteral) {
                    setError("IncalculableValueError: Color is not a valid type.");
                    return null;
                }
                if (lhsNode instanceof BoolLiteral || rhsNode instanceof BoolLiteral) {
                    setError("IncalculableValueError: Boolean is not a valid type.");
                    return null;
                }
                if (lhsNode instanceof PixelLiteral && rhsNode instanceof PixelLiteral) {
                    return new PixelLiteral(Add(((PixelLiteral) lhsNode).value, ((PixelLiteral) rhsNode).value));
                } else if (lhsNode instanceof PercentageLiteral && rhsNode instanceof PercentageLiteral) {
                    return new PercentageLiteral(Add(((PixelLiteral) lhsNode).value, ((PixelLiteral) rhsNode).value));
                } else {
                    setError("TypeNotDefinedError: Could not recalculate value. Type is unknown.");
                }
            }
            setError("TypeMismatchError: The 'add' operation only support values of the same type.");
        }

        // Yikes, something went wrong.
        return null;
    }

    private int Add(int a, int b) {
        return a + b;
    }
}
