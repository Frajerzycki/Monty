package parser.parsing;

import java.util.List;

import ast.Block;
import ast.NodeTypes;
import ast.declarations.CustomFunctionDeclarationNode;
import ast.declarations.VariableDeclarationNode;
import ast.expressions.OperationNode;
import ast.statements.IfStatementNode;
import ast.statements.PrintStatementNode;
import ast.statements.ReturnStatementNode;
import ast.statements.WhileStatementNode;
import lexer.MontyToken;
import lexer.TokenTypes;
import parser.DataTypes;
import parser.MontyException;
import parser.Tokens;

public abstract class AdderToBlock {

	public static void addExpression(Block block, List<MontyToken> tokens) {
		block.addChild(ExpressionParser.parse(block, tokens));
	}

	public static void addPrintStatement(Block block, List<MontyToken> tokens) {
		block.addChild(new PrintStatementNode(ExpressionParser.parse(block, tokens.subList(1, tokens.size())),
				tokens.get(0).getText().equals("println")));
	}

	public static void addReturnStatement(Block block, List<MontyToken> tokens) {
		var expression = (OperationNode) null;
		if (tokens.size() > 1)
			expression = ExpressionParser.parse(block, tokens.subList(1, tokens.size()));
		block.addChild(new ReturnStatementNode(expression));
	}

	public static void addVariableDeclaration(Block block, List<MontyToken> tokens) {
		block.addVariable(
				new VariableDeclarationNode(tokens.get(2).getText(), Tokens.getDataType(tokens.get(1).getType())));
		addExpression(block, tokens.subList(2, tokens.size()));
	}

	public static Block addFunctionDeclaration(Block block, List<MontyToken> tokens) {
		var function = new CustomFunctionDeclarationNode(tokens.get(2).getText(),
				Tokens.getDataType(tokens.get(1).getType()));
		var type = (DataTypes) null;
		var name = (String) null;
		for (int i = 3; i < tokens.size(); i++) {
			if (tokens.get(i).getType().equals(TokenTypes.IDENTIFIER))
				name = tokens.get(i).getText();
			else if (tokens.get(i).getType().equals(TokenTypes.INTEGER_KEYWORD)
					|| tokens.get(i).getType().equals(TokenTypes.FLOAT_KEYWORD)
					|| tokens.get(i).getType().equals(TokenTypes.STRING_KEYWORD)
					|| tokens.get(i).getType().equals(TokenTypes.BOOLEAN_KEYWORD)
					|| tokens.get(i).getType().equals(TokenTypes.VOID_KEYWORD))
				type = Tokens.getDataType(tokens.get(i).getType());
			if (tokens.get(i).getType().equals(TokenTypes.COMMA) || i + 1 >= tokens.size())
				function.addParameter(new VariableDeclarationNode(name, type));
		}
		function.setBody(new Block(block));
		block.addFunction(function);
		return function.getBody();
	}

	public static Block addIfStatement(Block block, List<MontyToken> tokens) {
		var ifStatement = new IfStatementNode(block, ExpressionParser.parse(block, tokens.subList(1, tokens.size())));
		block.addChild(ifStatement);
		return ifStatement;

	}

	public static Block addElseStatement(Block block, List<MontyToken> tokens) {
		if (!block.getNodeType().equals(NodeTypes.IF_STATEMENT))
			new MontyException("Unexpected \"else\" keyword.");
		var elseBlock = new Block(block.getParent());
		((IfStatementNode) block).setElseBody(elseBlock);
		return elseBlock;
	}

	public static Block addWhileStatement(Block block, List<MontyToken> tokens) {
		var whileStatement = new WhileStatementNode(ExpressionParser.parse(block, tokens.subList(1, tokens.size())));
		whileStatement.setBody(new Block(block));
		block.addChild(whileStatement);
		return whileStatement.getBody();

	}

	public static void addStdlib(Block block) {
		block.addFunction(new stdlib.io.Input());

		block.addFunction(new stdlib.system.Exit());
		block.addFunction(new stdlib.system.Argv());

		block.addFunction(new stdlib.casts.StringToInt());
		block.addFunction(new stdlib.casts.StringToBoolean());
		block.addFunction(new stdlib.casts.StringToFloat());

		block.addFunction(new stdlib.casts.IntToString());
		block.addFunction(new stdlib.casts.IntToBoolean());
		block.addFunction(new stdlib.casts.IntToFloat());

		block.addFunction(new stdlib.casts.FloatToInt());
		block.addFunction(new stdlib.casts.FloatToString());
		block.addFunction(new stdlib.casts.FloatToBoolean());

		block.addFunction(new stdlib.casts.BooleanToInt());
		block.addFunction(new stdlib.casts.BooleanToString());
		block.addFunction(new stdlib.casts.BooleanToFloat());

		block.addFunction(new stdlib.casts.IntToChar());
		block.addFunction(new stdlib.casts.CharToInt());

		block.addFunction(new stdlib.math.PowerFloat());
		block.addFunction(new stdlib.math.PowerInt());
		block.addFunction(new stdlib.math.SqrtInt());
		block.addFunction(new stdlib.math.SqrtFloat());

	}
}
