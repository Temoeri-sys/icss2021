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

        if (lhsNode != null || rhsNode != null) {
            String lhsType = lhsNode.getClass().getSimpleName();
            String rhsType = rhsNode.getClass().getSimpleName();

            if(lhsType == rhsType){
                if(lhsNode instanceof ColorLiteral || rhsNode instanceof ColorLiteral)
                    return "IncalculableValueError: Color is not a valid type.";
                if(lhsNode instanceof BoolLiteral || rhsNode instanceof BoolLiteral)
                    return "IncalculableValueError: Boolean is not a valid type.";
                return "";
            }
        }

        return "TypeMismatchError: The 'add' operation only support values of the same type.";
    }


    public ASTNode calculate(List<VariableAssignment> availableVariables) {
        ASTNode lhsNode = getLiteralNode(lhs, availableVariables);
        ASTNode rhsNode = getLiteralNode(rhs, availableVariables);

        if (lhsNode != null || rhsNode != null) {
            String lhsType = lhsNode.getClass().getSimpleName();
            String rhsType = rhsNode.getClass().getSimpleName();

            // Make sure that the types are not the same. If that's the case it's valid (only requirement)
            if(lhsType == rhsType){
                if (lhsNode instanceof ColorLiteral || rhsNode instanceof ColorLiteral) {
                    setError("IncalculableValueError: Color is not a valid type.");
                    return null;
                }
                if (lhsNode instanceof BoolLiteral || rhsNode instanceof BoolLiteral) {
                    setError("IncalculableValueError: Boolean is not a valid type.");
                    return null;
                }
                // Either Lhs or Rhs is a Scalar. Use it's value with the value of the node to recalculate the value
                if (lhsNode instanceof PixelLiteral){
                    return recalculate(rhsNode, ((ScalarLiteral) rhsNode).value);
                } else if (lhsNode instanceof PercentageLiteral){
                    return recalculate(rhsNode, ((ScalarLiteral) rhsNode).value);
                }
                if (rhsNode instanceof PixelLiteral){
                    return recalculate(rhsNode, ((ScalarLiteral) lhsNode).value);
                } else if (rhsNode instanceof PercentageLiteral){
                    return recalculate(rhsNode, ((ScalarLiteral) lhsNode).value);
                }
            }
            setError("TypeMismatchError: The 'add' operation only support values of the same type.");
        }

        // Yikes, something went wrong.
        return null;
    }

    private ASTNode recalculate(ASTNode node, int value){
        if (node instanceof PixelLiteral){
            var calculatedValue = Add(((PixelLiteral) node).value, value);
            ((PixelLiteral) node).value = calculatedValue;
            return node;
        } else if (node instanceof PercentageLiteral){
            var calculatedValue = Add(((PercentageLiteral) node).value, value);
            ((PercentageLiteral) node).value = calculatedValue;
            return node;
        }
        setError("TypeNotDefinedError: Could not recalculate value. Type is unknown.");
        return node;
    }

    private int Add(int a, int b){
        return a + b;
    }
}
