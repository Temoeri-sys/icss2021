package nl.han.ica.icss.transforms;

//BEGIN UITWERKING

import nl.han.ica.icss.ast.*;
import nl.han.ica.icss.ast.helpers.ValidatorHelper;
import nl.han.ica.icss.ast.literals.BoolLiteral;

import java.util.ArrayList;
import java.util.List;
//EIND UITWERKING

public class RemoveIf implements Transform {
    // Since variables are only available within a specific scope, we'll use this to store them.
    // Whenever a variable will be accessed without it being added in the list we know there is a variable that is
    // being used outside of the scope if has been defined. E.g. the variable is used above and assigned below.
    private final List<VariableAssignment> _availableVariables = new ArrayList<>();

    @Override
    public void apply(AST ast) {
        removeIfClauseStructure(ast.root.getChildren());
    }

    private void removeIfClauseStructure(ArrayList<ASTNode> nodes) {
        // Check if nodes are actually filled before we continue the validation
        if (nodes == null || nodes.size() <= 0)
            return;

        for (ASTNode node : nodes) {
            // Would be better if we used a interface instead of a base class. Anyway.. this will do also.
            parseIfElseClauses(node);
        }
    }

    private void parseIfElseClauses(ASTNode node) {
        if (node instanceof VariableAssignment) {
            // Store the variables in a scope to reference from later on.
            _availableVariables.add(((VariableAssignment) node));
        } else if (node instanceof Stylerule) {
            for (ASTNode styleRuleNode : node.getChildren()) {
                if (styleRuleNode instanceof IfClause) {
                    var ifClauseNode = ((IfClause) styleRuleNode);
                    ASTNode conditionalValue = new ValidatorHelper()
                            .transformToLiteralNode(ifClauseNode.getConditionalExpression(), _availableVariables);
                    if (conditionalValue instanceof BoolLiteral) {
                        if (((BoolLiteral) conditionalValue).value) {
                            System.out.println("1");
                            // Only required when a else clause is actually used.
                            if (ifClauseNode.elseClause != null) {
                                ifClauseNode.elseClause.body.clear();
                            }
                        } else {
                            System.out.println("2");
                            ifClauseNode.body.clear();
                        }
                    } else {
                        // This can't be but if this happens remove the entire IF block so we don't render it.
                        node.removeChild(ifClauseNode);
                    }
                }
            }
        } else if (node instanceof IfClause) {
            parseIfElseClauses(node);
        }
    }

}
