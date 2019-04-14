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

package ast;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ast.declarations.FunctionDeclarationNode;
import ast.declarations.VariableDeclarationNode;
import ast.statements.BreakStatementNode;
import ast.statements.ContinueStatementNode;
import ast.statements.ForStatementNode;
import ast.statements.ReturnStatementNode;
import lexer.Token;
import parser.LogError;

public class Block extends NodeWithParent implements Cloneable {

	private ArrayList<RunnableNode> children = new ArrayList<>();
	protected HashMap<String, FunctionDeclarationNode> functions = new HashMap<>();
	private HashMap<String, VariableDeclarationNode> variables = new HashMap<>();
	protected Block parent;

	public Block(Block parent) {
		this.parent = parent;
	}

	public void addChild(RunnableNode child) {
		children.add(child);
	}

	public void addFunction(FunctionDeclarationNode function) {
		String name = function.getName();
		if (functions.containsKey(name))
			new LogError("Function " + name + " already exists");
		functions.put(name, function);
	}

	public void addFunction(FunctionDeclarationNode function, String fileName, int line) {
		String name = function.getName();
		function.setFileName(fileName);
		function.setLine(line);
		if (functions.containsKey(name)) {
			var existing_function = functions.get(name);
			int[] lines = { existing_function.getLine(), function.getLine() };
			String[] fileNames = { existing_function.getFileName(), function.getFileName() };
			new LogError("Function " + name + " already exists", fileNames, lines);
		}
		functions.put(name, function);
	}

	public void addFunction(FunctionDeclarationNode function, Token token) {
		String name = function.getName();
		function.setFileName(token.getFileName());
		function.setLine(token.getLine());
		if (functions.containsKey(name)) {
			var existing_function = functions.get(name);
			int[] lines = { existing_function.getLine(), function.getLine() };
			String[] fileNames = { existing_function.getFileName(), function.getFileName() };
			new LogError("Function " + name + " already exists", fileNames, lines);
		}
		functions.put(name, function);
	}

	public void addVariable(VariableDeclarationNode variable) {
		String name = variable.getName();
		if (variables.containsKey(name))
			new LogError("Variable " + name + " already exists");
		variables.put(name, variable);
	}

	public void addVariable(VariableDeclarationNode variable, String fileName, int line) {
		String name = variable.getName();
		variable.setFileName(fileName);
		variable.setLine(line);
		if (variables.containsKey(name)) {
			var existing_variable = variables.get(name);
			int[] lines = { existing_variable.getLine(), variable.getLine() };
			String[] fileNames = { existing_variable.getFileName(), variable.getFileName() };
			new LogError("Variable " + name + " already exists", fileNames, lines);
		}
		variables.put(name, variable);
	}

	public void addVariable(VariableDeclarationNode variable, Token token) {
		String name = variable.getName();
		variable.setFileName(token.getFileName());
		variable.setLine(token.getLine());
		if (variables.containsKey(name)) {
			var existing_variable = variables.get(name);
			int[] lines = { existing_variable.getLine(), variable.getLine() };
			String[] fileNames = { existing_variable.getFileName(), variable.getFileName() };
			new LogError("Variable " + name + " already exists", fileNames, lines);
		}
		variables.put(name, variable);
	}

	public void concat(Block block) {
		block.run();
		var variablesSet = block.getVariables().entrySet();
		for (Map.Entry<String, VariableDeclarationNode> entry : variablesSet) {
			addVariable(entry.getValue());
		}
		var functionsSet = block.getFunctions().entrySet();
		for (Map.Entry<String, FunctionDeclarationNode> entry : functionsSet) {
			addFunction(entry.getValue());
		}
	}

	public Block copy() {
		try {
			var body = (Block) clone();
			var children = getChildren();
			for (int i = 0; i < children.size(); i++) {
				if (children.get(i) instanceof NodeWithParent) {
					var castedChildCopy = ((NodeWithParent) children.get(i)).copy();
					children.set(i, castedChildCopy);
					castedChildCopy.setParent(body);
				}
			}
			body.setChildren(children);
			return body;
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return null;
	}

	public boolean hasFunction(String name) {
		return functions.containsKey(name);
	}

	public boolean hasVariable(String name) {
		return variables.containsKey(name);
	}

	public ArrayList<RunnableNode> getChildren() {
		return children;
	}

	public FunctionDeclarationNode getFunction(String name) {
		Block block = this;
		while (!block.functions.containsKey(name)) {
			var parent = block.getParent();
			if (parent == null)
				new LogError("There isn't function with name:\t" + name);
			block = parent;
		}
		return block.functions.get(name);

	}

	public FunctionDeclarationNode getFunction(String name, String fileName, int line) {
		Block block = this;
		while (!block.functions.containsKey(name)) {
			var parent = block.getParent();
			if (parent == null)
				new LogError("There isn't function with name:\t" + name, fileName, line);
			block = parent;
		}
		return block.functions.get(name);

	}

	public HashMap<String, FunctionDeclarationNode> getFunctions() {
		return functions;
	}

	public void setChildren(ArrayList<RunnableNode> children) {
		this.children = children;
	}

	public Block getParent() {
		return parent;
	}

	public VariableDeclarationNode getVariable(String name) {
		Block block = this;
		while (!block.variables.containsKey(name)) {
			var parent = block.getParent();
			if (parent == null)
				new LogError("There isn't variable with name:\t" + name);
			block = parent;
		}
		return block.variables.get(name);
	}

	public VariableDeclarationNode getVariable(String name, String fileName, int line) {
		Block block = this;
		while (!block.variables.containsKey(name)) {
			var parent = block.getParent();
			if (parent == null)
				new LogError("There isn't variable with name:\t" + name, fileName, line);
			block = parent;
		}
		return block.variables.get(name);
	}

	public BigInteger getIntVariableValue(String name) {
		return (BigInteger) getVariable(name).getValue();
	}

	public BigDecimal getRealVariableValue(String name) {
		return (BigDecimal) getVariable(name).getValue();
	}

	public Boolean getBooleanVariableValue(String name) {
		return (Boolean) getVariable(name).getValue();
	}

	public String getStringVariableValue(String name) {
		return getVariable(name).getValue().toString();
	}

	public Object getVariableValue(String name) {
		return getVariable(name).getValue();
	}

	public HashMap<String, VariableDeclarationNode> getVariables() {
		return variables;
	}

	@Override
	public Object run() {
		Object result = null;
		for (RunnableNode child : children) {
			result = child.run();
			if (child instanceof ReturnStatementNode || child instanceof BreakStatementNode
					|| child instanceof ContinueStatementNode
					|| ((child instanceof ConditionalNode || child instanceof ForStatementNode) && result != null))
				return result;

		}
		return null;
	}

	@Override
	public void setParent(Block parent) {
		this.parent = parent;
	}

	public void setVariables(HashMap<String, VariableDeclarationNode> variables) {
		this.variables = variables;
	}
}
