package nl.han.ica.icss.transforms;

import nl.han.ica.icss.ast.*;

import java.util.ArrayList;

public class EvalExpressions implements Transform {

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

    // TODO: Transform into correct data types so it'll be easier when generating the CSS.
    private void transformNode(ASTNode node) {
        System.out.println(String.format("Transform the current node!! %s", node));
    }


}
