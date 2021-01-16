package nl.han.ica.icss.ast.literals;

import nl.han.ica.icss.ast.ASTNode;
import nl.han.ica.icss.ast.Literal;

import javax.swing.text.html.Option;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class ColorLiteral extends Literal {
    public String value;

    // Properties available to use with ColorLiteral.
    private final String COLOR = "color";
    private final String BACKGROUND_COLOR = "background-color";

    // NOT ALLOWED
    private final String HEIGHT = "height";
    private final String WIDTH = "width";

    // Limit to specific set of properties. One of the requirements.
    private List<String> supportedProperties = Arrays.asList(COLOR, BACKGROUND_COLOR);

    public ColorLiteral(String value) {
        this.value = value;
    }

    @Override
    public String getNodeLabel() {
        return "Color literal (" + value + ")";
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ColorLiteral that = (ColorLiteral) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {

        return Objects.hash(value);
    }

    public String validate(String property, ASTNode expression) {
        boolean isSupported = supportedProperties.contains(property.toLowerCase());
        if (!isSupported) {
            return String.format("Only the following properties are supported: %s", supportedProperties);
        }

        // Perform declaration validations.
        if (property.equals(WIDTH))
            return String.format("%s is not a supported value for '%s'", ((ColorLiteral) expression).value, WIDTH);
        else if (property.equals(HEIGHT))
            return String.format("%s is not a supported value for '%s'", ((ColorLiteral) expression).value, HEIGHT);

        // Validation passed
        return "";
    }
}
