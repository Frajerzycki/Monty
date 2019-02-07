/*
Copyright 2018-2019 Szymon Perlicki

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUObject WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package sml.math;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;

import ast.Block;
import ast.declarations.FunctionDeclarationNode;
import ast.expressions.OperationNode;
import parser.DataTypes;

public class Ln extends FunctionDeclarationNode {

	public static final BigDecimal ln(BigDecimal x, int scale) {
		var y = BigDecimal.valueOf(4.6);
		var p = BigDecimal.ONE.movePointLeft(scale);
		y.setScale(scale);
		while (Exp.exp(y, scale).subtract(x).abs().compareTo(p) > 0)
			y = y.add(x.multiply(Exp.exp(y.negate(), scale))).subtract(BigDecimal.ONE);
		return y;
	}

	public Ln() {
		super("ln", DataTypes.FLOAT);
		setBody(new Block(null));
		addParameter("f", DataTypes.FLOAT);
		addParameter("scale", DataTypes.INTEGER);

	}

	@Override
	public BigDecimal call(ArrayList<OperationNode> arguments, String callFileName, int callLine) {
		setArguments(arguments, callFileName, callLine);
		var body = getBody();
		return ln((BigDecimal) body.getVariable("f").getValue(),
				((BigInteger) body.getVariable("scale").getValue()).intValue());
	}

}