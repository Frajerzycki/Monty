package ast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ast.declarations.FunctionDeclarationNode;
import ast.declarations.VariableDeclarationNode;
import ast.expressions.FunctionCallNode;
import ast.expressions.OperationNode;
import ast.statements.IfStatementNode;
import ast.statements.PrintStatementNode;
import ast.statements.ReturnStatementNode;
import ast.statements.WhileStatementNode;
import parser.MontyException;

public class Block extends Node {
	private ArrayList<Node> children = new ArrayList<>();
	HashMap<String, FunctionDeclarationNode> functions = new HashMap<>();
	private Block parent;
	HashMap<String, VariableDeclarationNode> variables = new HashMap<>();

	public Block(Block parent) {
		this.parent = parent;
	}

	public void addChild(Node child) {
		children.add(child);
	}

	public void addFunction(FunctionDeclarationNode function) {
		String name = function.getName();
		if (variables.containsKey(name))
			new MontyException("Function " + name + " already exists");
		functions.put(name, function);
	}

	public void addVariable(VariableDeclarationNode variable) {
		String name = variable.getName();
		if (variables.containsKey(name)) {
			System.out.println(variables);
			new MontyException("Variable " + name + " already exists");
		}
		variables.put(name, variable);
	}

	public void concat(Block block) {
		var variablesSet = block.getVariables().entrySet();
		for (Map.Entry<String, VariableDeclarationNode> entry : variablesSet) {
			addVariable((VariableDeclarationNode) entry.getValue());
		}

		var functionsSet = block.getFunctions().entrySet();
		for (Map.Entry<String, FunctionDeclarationNode> entry : functionsSet) {
			addFunction(((FunctionDeclarationNode) entry.getValue()));
		}

		var children = block.getChildren();
		for (Node child : children) {
			addChild(child);
		}
	}

	public boolean doesContainVariable(String name) {
		return variables.containsKey(name);
	}

	public ArrayList<Node> getChildren() {
		return children;
	}

	public FunctionDeclarationNode getFunctionByName(String name) {
		Block block = this;
		while (true) {
			if (block.functions.containsKey(name))
				return block.functions.get(name);
			var parent = block.getParent();
			if (parent == null)
				new MontyException("There isn't function with name:\t" + name);
			block = parent;
		}
	}

	private HashMap<String, FunctionDeclarationNode> getFunctions() {
		return functions;
	}

	public Block getParent() {
		return parent;
	}

	public VariableDeclarationNode getVariableByName(String name) {
		Block block = this;
		while (true) {
			if (block.variables.containsKey(name))
				return block.variables.get(name);
			var parent = block.getParent();
			if (parent == null)
				new MontyException("There isn't variable with name:\t" + name);
			block = parent;
		}
	}

	public HashMap<String, VariableDeclarationNode> getVariables() {
		return variables;
	}

	public Object run() {
		for (Node child : children) {
			switch (child.getNodeType()) {
			case OPERATION:
				var childCastedToVariable = ((OperationNode) child);
				if (childCastedToVariable.getOperand().equals(NodeTypes.FUNCTION_CALL)) {
					var functionToCall = ((FunctionCallNode) child);
					var function = getFunctionByName(functionToCall.getName());
					function.call(functionToCall.getArguments());
				} else if (!(childCastedToVariable.getRightOperand() != null
						&& childCastedToVariable.getLeftOperand().getNodeType().equals(NodeTypes.VARIABLE)
						&& childCastedToVariable.getRightOperand().getOperand().toString().contains("="))) {
					childCastedToVariable.run();
				} else
					new MontyException("Some expression hasn't got any sense!!!");
				
				break;
			case PRINT_STATEMENT:
				((PrintStatementNode) child).run();
				break;
			case IF_STATEMENT:
				var childCastedToIfStatement = ((IfStatementNode) child);
				var elseBody = childCastedToIfStatement.getElseBody();
				if ((boolean) childCastedToIfStatement.getCondition().run()) {
					var result = childCastedToIfStatement.run();
					if (result != null)
						return result;
				} else if (elseBody != null) {
					var result = elseBody.run();
					if (result != null)
						return result;
				}
				break;
			case WHILE_STATEMENT:
				var childCastedToWhileStatement = ((WhileStatementNode) child);
				while ((boolean) childCastedToWhileStatement.getCondition().run()) {
					var body = childCastedToWhileStatement.getBody();
					var result = body.run();
					if (result != null)
						return result;
				}
				break;
			case RETURN_STATEMENT:
				return ((ReturnStatementNode) child).getExpression().run();
			default:
				break;
			}
		}
		return null;
	}

	public void setVariables(HashMap<String, VariableDeclarationNode> variables) {
		this.variables = variables;
	}
}
