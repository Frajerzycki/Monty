/*
Copyright 2018 Szymon Perlicki

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

package sml.data.list;

import java.math.BigInteger;
import java.util.ArrayList;

import ast.Block;
import ast.declarations.FunctionDeclarationNode;
import ast.declarations.VariableDeclarationNode;
import ast.expressions.OperationNode;
import parser.DataTypes;

public class Sublist extends FunctionDeclarationNode {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9201866361416331207L;

	public Sublist() {
		super("subList", DataTypes.LIST);
		setBody(new Block(null));
		addParameter(new VariableDeclarationNode("lst", DataTypes.LIST));
		addParameter(new VariableDeclarationNode("begin", DataTypes.INTEGER));
		addParameter(new VariableDeclarationNode("end", DataTypes.INTEGER));

	}

	@Override
	public Object call(ArrayList<OperationNode> arguments) {
		setArguments(arguments);
		var body = getBody();
		var arr = (List) body.getVariableByName("lst").getValue();
		var begin = ((BigInteger) body.getVariableByName("begin").getValue()).intValue();
		var end = ((BigInteger) body.getVariableByName("end").getValue()).intValue();
		return arr.sublist(begin, end);
	}

}