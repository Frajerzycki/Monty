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

import java.util.HashMap;
import java.util.Map;

import ast.Block;
import ast.NodeTypes;

public class StructDeclarationNode extends Block implements Cloneable {
	private static final long serialVersionUID = -8205779269625980876L;
	private static int number = -1;
	private int instanceNumber;
	private String name;
	public String getName() {
		return name;
	}

	public int getInstanceNumber() {
		return instanceNumber;
	}
	
	public void incrementNumber() {
		instanceNumber = ++number;
	}

	public StructDeclarationNode(Block parent, String name) {
		super(parent);
		super.nodeType = NodeTypes.STRUCT_DECLARATION;
		this.name = name;
	}

	public void setFunctions(HashMap<String, FunctionDeclarationNode> functions) {
		this.functions = functions;
	}
	
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
}
