package nl.han.ica.icss.ast;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class VariableReference extends Expression {

    public String name;

    public VariableReference(String name) {
        super();
        this.name = name;
    }

    @Override
    public String getNodeLabel() {
        return "VariableReference (" + name + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        VariableReference that = (VariableReference) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    public String validate(ASTNode expression, List<VariableAssignment> availableVariables, Declaration declaration) {
        String variableName = ((VariableReference) expression).name;
        // Make sure to validate the variable name
        if (variableName == null || variableName.trim().isEmpty()) {
            return String.format("ExpressionReferenceError: expression '%s' is not defined", expression);
        }

        // Get the top level node to drill through the nested nodes to find it literal value
        Optional<VariableAssignment> variableObject = availableVariables.stream()
                .filter(o -> o.name.name.equals(variableName)).findFirst();
        boolean variableInScope = variableObject.isPresent();

        if (variableInScope) {
            var nestedNodes = variableObject.get().getChildren();
            if (nestedNodes != null && nestedNodes.size() > 0) {
                // Get the last element because that's the next property we need to evaluate.
                var lastElement = nestedNodes.get(nestedNodes.size() - 1);
                if(lastElement != null)
                    declaration.validate(lastElement, availableVariables);
            }
        } else {
            // Set the error on the VariableReference level.
            return String.format("VariableReferenceError: variable '%s' is not defined", variableName);
        }


        // Passed all validation checks
        return "";
    }
}
