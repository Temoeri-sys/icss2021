package nl.han.ica.icss.generator;


import nl.han.ica.icss.ast.*;
import nl.han.ica.icss.ast.literals.ColorLiteral;
import nl.han.ica.icss.ast.literals.PercentageLiteral;
import nl.han.ica.icss.ast.literals.PixelLiteral;
import nl.han.ica.icss.ast.operations.AddOperation;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class Generator {

	public String generate(AST ast) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("/* \n");
		stringBuilder.append(" * Copyright (C) 2021 ICSS - All Rights Reserved \n");
		stringBuilder.append(" * \n");
		stringBuilder.append(String.format(" * Version 1.0 - generated on %s \n",
				new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(Calendar.getInstance().getTime())
		));
		stringBuilder.append(" * This product is protected by copyright and distributed under \n");
		stringBuilder.append(" * licenses restricting copying, distribution and decompilation. \n");
		stringBuilder.append(" * \n");
		stringBuilder.append(" */ \n\n\n");

		return convertAstToCss2(ast, stringBuilder).toString();
	}

	private StringBuilder convertAstToCss2(AST ast, StringBuilder stringBuilder) {
		for (ASTNode node: ast.root.getChildren()) {
			// If some element isn't a StyleRule, we can't use it. So only accept style rules and ignore the rest.
			// We don't have to worry for variable references etc because they get transformed into the correct value
			if(node instanceof Stylerule){
				generateStyleRuleCss2((Stylerule)node, stringBuilder);
			}
		}

		return stringBuilder;
	}

	private void generateStyleRuleCss2(Stylerule node, StringBuilder stringBuilder) {
		String selectorSeparator = node.selectors.size() > 1 ? ", " : "";

		for (Selector selector: node.selectors) {
			stringBuilder.append(selector);
			stringBuilder.append(selectorSeparator);
		}
		stringBuilder.append(" { \n");
		for (ASTNode bodyNode: node.body) {
			// Only option available tbh. If we receive something else, there is something wrong!
			if(bodyNode instanceof Declaration){
				var property = ((Declaration)bodyNode).property.name;
				stringBuilder.append(String.format("  %s: %s; \n", property, getLiteralValue(((Declaration)bodyNode).expression)));
			}
		}

		stringBuilder.append("} \n\n");
	}

	private String getLiteralValue(Expression expression) {
		if(expression instanceof PixelLiteral){
			return String.format("%spx", ((PixelLiteral)expression).value);
		} else if (expression instanceof ColorLiteral){
			return ((ColorLiteral)expression).value;
		} else if (expression instanceof PercentageLiteral){
			return String.format("%s\\%", ((PercentageLiteral)expression).value);
		}
		// If it's a type we don't know and it passed the validators just return empty.
		return "";
	}


}
