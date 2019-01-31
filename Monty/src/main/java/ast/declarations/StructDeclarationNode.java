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

package ast.declarations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ast.Block;
import ast.NodeTypes;
import ast.expressions.OperationNode;
import lexer.Token;
import parser.DataTypes;
import parser.LogError;

public class StructDeclarationNode extends Block implements Cloneable {

	private static int actualStructNumber = -1;
	private static int number = -1;
	private int structNumber;
	private int instanceNumber;
	private String name;

	public StructDeclarationNode(Block parent, String name) {
		super(parent);
		super.nodeType = NodeTypes.STRUCT_DECLARATION;
		this.name = name;
		structNumber = ++actualStructNumber;
	}

	public void addNewStruct(Block block, Token token) {
		var struct = this;
		var newStructFunction = new FunctionDeclarationNode(name, DataTypes.ANY) {

			@Override
			public Object call(ArrayList<OperationNode> arguments, String callFileName, int callLine) {
				var newStruct = struct.copy();
				var thisVariable = new VariableDeclarationNode("this", DataTypes.ANY);
				thisVariable.setValue(newStruct);
				newStruct.addVariable(thisVariable);
				if (newStruct.doesContainFunction("init")) {
					var function = newStruct.getFunctionByName("init");
					if (!function.getType().equals(DataTypes.VOID)) {
						String[] fileNames = { function.getFileName(), callFileName };
						int[] lines = { function.getLine(), callLine };
						new LogError("Init method have to be void", fileNames, lines);
					}
					function.call(arguments, callFileName, callLine);
				}
				newStruct.incrementNumber();
				return newStruct;
			}
		};
		newStructFunction.setBody(new Block(null));
		block.addFunction(newStructFunction, token);
		var checkingFunction = new FunctionDeclarationNode("is" + name, DataTypes.BOOLEAN) {

			@Override
			public Object call(ArrayList<OperationNode> arguments, String callFileName, int callLine) {
				setArguments(arguments, callFileName, callLine);
				var other = getBody().getVariableByName("other").getValue();
				if (other instanceof StructDeclarationNode)
					return struct.instanceOfMe(((StructDeclarationNode) other));
				return false;
			}

		};
		checkingFunction.setBody(new Block(null));
		checkingFunction.addParameter(new VariableDeclarationNode("other", DataTypes.ANY));
		block.addFunction(checkingFunction, token);
	}

	@Override
	public StructDeclarationNode copy() {
		StructDeclarationNode copied = null;
		try {
			copied = (StructDeclarationNode) clone();
			var variables = new HashMap<String, VariableDeclarationNode>();
			var variablesSet = copied.getVariables().entrySet();
			for (Map.Entry<String, VariableDeclarationNode> entry : variablesSet) {
				var key = entry.getKey();
				variables.put(key, entry.getValue().copy());
			}
			copied.setVariables(variables);
			var functions = new HashMap<String, FunctionDeclarationNode>();
			var functionsSet = copied.getFunctions().entrySet();
			for (Map.Entry<String, FunctionDeclarationNode> entry : functionsSet) {
				var key = entry.getKey();
				var value = entry.getValue().copy();
				var body = value.getBody();
				body.setParent(copied);
				functions.put(key, value);
			}
			copied.setFunctions(functions);

		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return copied;
	}

	public int getInstanceNumber() {
		return instanceNumber;
	}

	public String getName() {
		return name;
	}

	public int getStructNumber() {
		return structNumber;
	}

	public void incrementNumber() {
		instanceNumber = ++number;
	}

	public boolean instanceOfMe(StructDeclarationNode s) {
		return s.getStructNumber() == structNumber;
	}

	public void setFunctions(HashMap<String, FunctionDeclarationNode> functions) {
		this.functions = functions;
	}

	@Override
	public String toString() {
		return name + "#" + getInstanceNumber();
	}
}
