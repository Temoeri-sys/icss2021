package nl.han.ica.icss.ast;

import jdk.jshell.spi.ExecutionControl;
import nl.han.ica.icss.checker.SemanticError;

import java.util.ArrayList;
import java.util.List;

public class ASTNode {

    private SemanticError error = null;

    /*
     This method is used in the GUI to create an appropriate label
     in the tree visualisation.
      */
    public String getNodeLabel() {
        return "ASTNode";
    }

    /*
     Different AST nodes use different attributes to store their children.
     This method provides a unified interface.
     */
    public ArrayList<ASTNode> getChildren() {
        return new ArrayList<>();
    }

    /*
    By implementing this method in a subclass you can easily create AST nodes
      incrementally.
    */
    public ASTNode addChild(ASTNode child) {
        return this;
    }

    /*
     * By implementing this method you can easily make transformations that prune the AST.
     */
    public ASTNode removeChild(ASTNode child) {
        return this;
    }

    public SemanticError getError() {
        return this.error;
    }

    public void setError(String description) {
        this.error = new SemanticError(description);
    }

    public boolean hasError() {
        return error != null;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        toString(result);
        return result.toString();
    }

    private void toString(StringBuilder builder) {
        builder.append("[");
        builder.append(getNodeLabel());
        builder.append("|");
        for (ASTNode child : getChildren()) {
            child.toString(builder);
        }
        builder.append("]");
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ASTNode))
            return false;
        //Compare all children
        List<ASTNode> thisChildren = this.getChildren();
        List<ASTNode> otherChildren = ((ASTNode) o).getChildren();
        if (otherChildren.size() != thisChildren.size())
            return false;
        for (int i = 0; i < thisChildren.size(); i++) {
            if (!thisChildren.get(i).equals(otherChildren.get(i))) {
                return false;
            }
        }
        return true;
    }

    public void SetErrorIfAny(String error) {
        if (error != null && !error.trim().isEmpty()) {
            this.setError(error);
        }
    }

}
