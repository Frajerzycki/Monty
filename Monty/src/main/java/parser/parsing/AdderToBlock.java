/*
Copyright 2018-2019 Szymon Perlicki

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package parser.parsing;

import ast.Block;
import ast.NodeTypes;
import ast.declarations.CustomFunctionDeclarationNode;
import ast.declarations.StructDeclarationNode;
import ast.declarations.VariableDeclarationNode;
import ast.expressions.FunctionCallNode;
import ast.expressions.OperationNode;
import ast.expressions.VariableNode;
import ast.statements.BreakStatementNode;
import ast.statements.ChangeToStatementNode;
import ast.statements.ContinueStatementNode;
import ast.statements.DoWhileStatementNode;
import ast.statements.ForStatementNode;
import ast.statements.IfStatementNode;
import ast.statements.ReturnStatementNode;
import ast.statements.ThreadStatement;
import ast.statements.WhileStatementNode;
import lexer.OptimizedTokensArray;
import lexer.TokenTypes;
import parser.DataTypes;
import parser.LogError;
import parser.Tokens;

public abstract class AdderToBlock {
	public static void addBreakStatement(Block block, String fileName, int line) {
		block.addChild(new BreakStatementNode(fileName, line));
	}

	public static void addChangeToStatement(Block block, OptimizedTokensArray tokens) {
		block.addChild(new ChangeToStatementNode(new VariableNode(tokens.get(1).getText()),
				Tokens.getDataType(tokens.get(3).getType()), tokens.get(0).getFileName(), tokens.get(0).getLine()));
	}

	public static void addContinueStatement(Block block, String fileName, int line) {
		block.addChild(new ContinueStatementNode(fileName, line));
	}

	public static Block addDoWhileStatement(Block block, OptimizedTokensArray tokens) {
		var whileStatement = new DoWhileStatementNode(ExpressionParser.parse(block, tokens.subarray(2, tokens.length())),
				tokens.get(0).getFileName(), tokens.get(0).getLine());
		whileStatement.setBody(new Block(block));
		block.addChild(whileStatement);
		return whileStatement.getBody();

	}

	public static Block addElseStatement(Block block, OptimizedTokensArray tokens) {
		if (!block.getNodeType().equals(NodeTypes.IF_STATEMENT))
			new LogError("Unexpected \"else\" keyword", tokens.get(0));
		var elseBlock = new Block(block.getParent(), NodeTypes.ELSE_BLOCK);
		elseBlock.setFileName(tokens.get(0).getFileName());
		elseBlock.setLine(tokens.get(0).getLine());
		((IfStatementNode) block).setElseBody(elseBlock);

		return elseBlock;
	}

	public static void addExpression(Block block, OptimizedTokensArray tokensBeforeSemicolon) {
		block.addChild(ExpressionParser.parse(block, tokensBeforeSemicolon));
	}

	public static Block addForStatement(Block block, OptimizedTokensArray tokens) {
		var forStatement = new ForStatementNode(tokens.get(1).getText(),
				ExpressionParser.parse(block, tokens.subarray(3, tokens.length())), tokens.get(0).getFileName(),
				tokens.get(0).getLine());
		forStatement.setBody(new Block(block));

		block.addChild(forStatement);
		return forStatement.getBody();
	}

	public static Block addFunctionDeclaration(Block block, OptimizedTokensArray tokens) {
		var function = new CustomFunctionDeclarationNode(tokens.get(2).getText(),
				Tokens.getDataType(tokens.get(1).getType()));
		DataTypes type = null;
		String name = null;
		for (int i = 3; i < tokens.length(); i++) {
			var tokenType = tokens.get(i).getType();
			var isTokenTypeEqualsComma = tokens.get(i).getType().equals(TokenTypes.COMMA);
			if (tokenType.equals(TokenTypes.IDENTIFIER))
				name = tokens.get(i).getText();
			else if (!isTokenTypeEqualsComma)
				type = Tokens.getDataType(tokenType);
			if (isTokenTypeEqualsComma || i + 1 >= tokens.length())
				function.addParameter(new VariableDeclarationNode(name, type));
		}
		function.setBody(new Block(block));
		block.addFunction(function, tokens.get(1));
		return function.getBody();
	}

	public static Block addIfStatement(Block block, OptimizedTokensArray tokens) {
		var ifStatement = new IfStatementNode(block, ExpressionParser.parse(block, tokens.subarray(1, tokens.length())),
				tokens.get(0).getFileName(), tokens.get(0).getLine());
		block.addChild(ifStatement);
		return ifStatement;

	}

	public static void addReturnStatement(Block block, OptimizedTokensArray tokens) {
		OperationNode expression = null;
		if (tokens.length() > 1)
			expression = ExpressionParser.parse(block, tokens.subarray(1, tokens.length()));
		else
			expression = new OperationNode(new FunctionCallNode("nothing"), block);
		block.addChild(new ReturnStatementNode(expression, tokens.get(0).getFileName(), tokens.get(0).getLine()));
	}

	public static void addThreadStatement(Block block, OptimizedTokensArray tokens) {
		block.addChild(new ThreadStatement(ExpressionParser.parse(block, tokens.subarray(1, tokens.length())),
				tokens.get(0).getFileName(), tokens.get(0).getLine()));
	}

	public static void addVariableDeclaration(Block block, OptimizedTokensArray tokens) {
		var dataType = Tokens.getDataType(tokens.get(1).getType());
		var variable = new VariableDeclarationNode(tokens.get(2).getText(), dataType);
		variable.setDynamic(tokens.get(0).getType().equals(TokenTypes.DYNAMIC_KEYWORD));
		block.addVariable(variable, tokens.get(1));
		if (tokens.length() > 3)
			addExpression(block, tokens.subarray(2, tokens.length()));
		else
			variable.setValue(DataTypes.getNeutralValue(dataType));

	}

	public static Block addStructDeclaration(Block block, OptimizedTokensArray tokens) {
		var name = tokens.get(1).getText();
		if (Character.isLowerCase(name.charAt(0)))
			new LogError("Struct name should start with upper case", tokens.get(0));
		var struct = new StructDeclarationNode(block, name);
		struct.addNewStruct(block, tokens.get(0));
		block.addChild(struct);
		return struct;
	}

	public static Block addWhileStatement(Block block, OptimizedTokensArray tokens) {
		var whileStatement = new WhileStatementNode(ExpressionParser.parse(block, tokens.subarray(1, tokens.length())),
				tokens.get(0).getFileName(), tokens.get(0).getLine());
		whileStatement.setBody(new Block(block));
		block.addChild(whileStatement);
		return whileStatement.getBody();

	}
}
