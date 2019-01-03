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

package sml.casts;

import java.util.ArrayList;

import ast.Block;
import ast.declarations.FunctionDeclarationNode;
import ast.declarations.VariableDeclarationNode;
import ast.expressions.OperationNode;
import parser.DataTypes;
import sml.data.array.Array;
import sml.data.list.List;
import sml.data.stack.Stack;

public class ToList extends FunctionDeclarationNode {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3165880297105294653L;

	public static Object toList(Object a, String callFileName, int callLine) {
		if (a instanceof List)
			return a;
		if (a instanceof Array)
			return ((Array) a).toList();
		if (a instanceof Stack)
			return ((Stack) a).toArray().toList();
		return new List(a);
	}

	public ToList() {
		super("toList", DataTypes.LIST);
		setBody(new Block(null));
		addParameter(new VariableDeclarationNode("a", DataTypes.ANY));
	}

	@Override
	public Object call(ArrayList<OperationNode> arguments, String callFileName, int callLine) {
		setArguments(arguments, callFileName, callLine);
		var a = getBody().getVariableByName("a").getValue();
		return toList(a, callFileName, callLine);
	}

}