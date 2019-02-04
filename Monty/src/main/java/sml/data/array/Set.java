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

import java.math.BigInteger;
import java.util.ArrayList;

import ast.expressions.OperationNode;
import parser.DataTypes;
import parser.LogError;
import sml.data.Method;

class Set extends Method<Array> {

	public Set(Array array) {
		super(array, "set", DataTypes.ANY);
		addParameter("index", DataTypes.INTEGER);
		addParameter("value", DataTypes.ANY);
	}

	@Override
	public Array call(ArrayList<OperationNode> arguments, String callFileName, int callLine) {
		setArguments(arguments, callFileName, callLine);
		var body = getBody();
		var length = parent.length();
		var index = ((BigInteger) body.getVariable("index").getValue()).intValue();
		if (index >= length)
			new LogError("Index " + index + " is too big for " + length + " length", callFileName, callLine);
		return parent.set(index, body.getVariable("value").getValue());
	}

}