package nl.han.ica.icss.ast;

import nl.han.ica.icss.ast.literals.ColorLiteral;
import nl.han.ica.icss.ast.literals.PixelLiteral;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/*
 * A Declaration defines a style property. Declarations are things like "width: 100px"
 */
public class Declaration extends ASTNode {
	public PropertyName property;
	public Expression expression;

	private final String COLOR = "color";
	private final String BACKGROUND_COLOR = "background-color";
	private final String HEIGHT = "height";
	private final String WIDTH = "width";
	// Limit to specific set of properties. One of the requirements.
	private List<String> supportedProperties = Arrays.asList(COLOR, BACKGROUND_COLOR, HEIGHT, WIDTH);

	public Declaration() {
		super();
	}
	public Declaration(String property) {
		super();
		this.property = new PropertyName(property);
	}
	@Override
	public String getNodeLabel() {
	    return "Declaration";
	}

	@Override
	public ArrayList<ASTNode> getChildren() {

		ArrayList<ASTNode> children = new ArrayList<>();
		if(property != null)
		    children.add(property);
		if(expression != null)
		    children.add(expression);
		return children;
	}
	@Override
	public ASTNode addChild(ASTNode child) {
		if(child instanceof PropertyName) {
			property = (PropertyName) child;
		} else if(child instanceof Expression) {
			expression = (Expression) child;
		}
		return this;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		if (!super.equals(o)) return false;
		Declaration that = (Declaration) o;
		return Objects.equals(property, that.property) &&
				Objects.equals(expression, that.expression);
	}
	@Override
	public int hashCode() {
		return Objects.hash(property, expression);
	}

	public void validate(){
		if(this.property.name == null)
		{
			setError("Property is undefined!");
			return;
		}
		var property = this.property.name.toLowerCase();
		boolean isSupported = supportedProperties.contains(property);
		if(!isSupported){
			setError(String.format("Only the following properties are supported: %s", supportedProperties));
			return;
		}

		if(expression instanceof PixelLiteral)
		{
			// Perform default validations required by PixelLiteral.
			((PixelLiteral) expression).validate();

			// Perform declaration validations.
			if (property.equals(COLOR))
				setError(String.format("%spx is not a supported value for '%s'", ((PixelLiteral) expression).value, COLOR));
			else if (property.equals(BACKGROUND_COLOR))
				setError(String.format("%spx is not a supported value for '%s'", ((PixelLiteral) expression).value, BACKGROUND_COLOR));
		}
		else if(expression instanceof ColorLiteral)
		{
			// Perform default validations required by ColorLiteral
			((ColorLiteral) expression).validate();

			// Perform declaration validations.
			if (property.equals(WIDTH))
				setError(String.format("%s is not a supported value for '%s'", ((ColorLiteral) expression).value, WIDTH));
			else if (property.equals(HEIGHT))
				setError(String.format("%s is not a supported value for '%s'", ((ColorLiteral) expression).value, HEIGHT));
		}
	}
}
