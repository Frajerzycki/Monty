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

package sml.threading;

import java.math.BigInteger;
import java.util.ArrayList;

import ast.Block;
import ast.declarations.FunctionDeclarationNode;
import ast.declarations.VariableDeclarationNode;
import ast.expressions.OperationNode;
import parser.DataTypes;
import parser.MontyException;
import sml.data.returning.Nothing;

public class Sleep extends FunctionDeclarationNode {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8040321011869157333L;

	public Sleep() {
		super("sleep", DataTypes.VOID);
		setBody(new Block(null));
		addParameter(new VariableDeclarationNode("millis", DataTypes.INTEGER));
	}

	@Override
	public Object call(ArrayList<OperationNode> arguments) {
		setArguments(arguments);
		var millis = ((BigInteger) getBody().getVariableByName("millis").getValue()).intValue();
		try {
			Thread.sleep((long) millis);
		} catch (InterruptedException e) {
			new MontyException("Sleep for " + millis + " was interrupted");
		}
		return Nothing.nothing;
	}

}