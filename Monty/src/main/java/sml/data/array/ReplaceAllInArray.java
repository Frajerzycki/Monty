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
package sml.data.array;

import java.util.ArrayList;

import ast.Block;
import ast.declarations.FunctionDeclarationNode;
import ast.declarations.VariableDeclarationNode;
import ast.expressions.OperationNode;
import parser.DataTypes;

public class ReplaceAllInArray extends FunctionDeclarationNode{

	public ReplaceAllInArray() {
		super("replaceAllInArray", DataTypes.ARRAY);
		setBody(new Block(null));
		addParameter(new VariableDeclarationNode("arr", DataTypes.ARRAY));
		addParameter(new VariableDeclarationNode("toBeReplaced", DataTypes.ANY));
		addParameter(new VariableDeclarationNode("replacement", DataTypes.ANY));


	}

	@Override
	public Object call(ArrayList<OperationNode> arguments) {
		setArguments(arguments);
		var body = getBody();
		var arr = (Array)body.getVariableByName("arr").getValue();
		var toBeReplaced = body.getVariableByName("toBeReplaced").getValue();
		var replacement = body.getVariableByName("replacement").getValue();

		return arr.replaceAll(toBeReplaced, replacement);
	}

}
