package nl.han.ica.icss.ast;

import nl.han.ica.icss.ast.helpers.ValidatorHelper;

import java.util.ArrayList;
import java.util.List;

public abstract class Operation extends Expression {

    public Expression lhs;
    public Expression rhs;
    public final ValidatorHelper _helper;

    public Operation(){
        _helper = new ValidatorHelper(this);
    }

    @Override
    public ArrayList<ASTNode> getChildren() {
        ArrayList<ASTNode> children = new ArrayList<>();
        if(lhs != null)
            children.add(lhs);
        if(rhs != null)
            children.add(rhs);
        return children;
    }

    @Override
    public ASTNode addChild(ASTNode child) {
        if(lhs == null) {
            lhs = (Expression) child;
        } else if(rhs == null) {
            rhs = (Expression) child;
        }
        return this;
    }

    public ASTNode getLiteralNode(Expression expression, List<VariableAssignment> availableVariables) {
        return _helper.getLiteralNode(expression, availableVariables);
    }
}
