package nl.han.ica.icss.ast.literals;

import nl.han.ica.icss.ast.ASTNode;
import nl.han.ica.icss.ast.Literal;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class PixelLiteral extends Literal {
    public int value;

    // Properties available to use with PixelLiteral.
    private final String HEIGHT = "height";
    private final String WIDTH = "width";

    // NOT ALLOWED
    private final String COLOR = "color";
    private final String BACKGROUND_COLOR = "background-color";

    // Limit to specific set of properties. One of the requirements.
    private List<String> supportedProperties = Arrays.asList(HEIGHT, WIDTH);

    public PixelLiteral(int value) {
        this.value = value;
    }

    public PixelLiteral(String text) {
        this.value = Integer.parseInt(text.substring(0, text.length() - 2));
    }

    @Override
    public String getNodeLabel() {
        return "Pixel literal (" + value + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        PixelLiteral that = (PixelLiteral) o;
        return value == that.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    // Current version doesn't need to have any validation. The G4 solves the requirements. So skip for now.
    public String validate(String property, ASTNode expression) {
        // validate if the property is actually supported.
        boolean isSupported = supportedProperties.contains(property.toLowerCase());
        if (!isSupported) {
            return String.format("Only the following properties are supported: %s", supportedProperties);
        }
        int value = ((PixelLiteral) expression).value;

        // Perform declaration validations.
        if (property.equals(COLOR))
            return String.format("%spx is not a supported value for '%s'", value, COLOR);

        if (property.equals(BACKGROUND_COLOR))
            return String.format("%spx is not a supported value for '%s'", value, BACKGROUND_COLOR);

        // Passed all validation checks
        return "";
    }
}
