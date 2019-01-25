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

package sml.data.array;

import java.util.ArrayList;

import ast.Block;
import ast.declarations.FunctionDeclarationNode;
import ast.declarations.VariableDeclarationNode;
import ast.expressions.OperationNode;
import parser.DataTypes;
import parser.LogError;

public class ExtendArray extends FunctionDeclarationNode {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4407198079905020979L;
	Array array;
	public ExtendArray(Array array) {
		super("extend", DataTypes.VOID);
		this.array = array;
		setBody(new Block(array));
		addParameter(new VariableDeclarationNode("arrayToExtend", DataTypes.ANY));
	}

	@Override
	public Object call(ArrayList<OperationNode> arguments, String callFileName, int callLine) {
		setArguments(arguments, callFileName, callLine);
		var arrayToExtend = (Array) getBody().getVariableByName("arrayToExtend").getValue();
		if (!(arrayToExtend instanceof Array))
			new LogError("Can't extend array with something that isn't array", callFileName, callLine);
		return array.extend(arrayToExtend);
	}

}
