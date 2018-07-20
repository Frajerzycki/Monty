package stdlib.casts;

import java.util.ArrayList;

import ast.Block;
import ast.declarations.FunctionDeclarationNode;
import ast.declarations.VariableDeclarationNode;
import ast.expressions.OperationNode;
import parser.DataTypes;

public class BooleanToFloat extends FunctionDeclarationNode {

	public BooleanToFloat() {
		super("booleanToFloat", DataTypes.FLOAT);
		setBody(new Block(null));
		addParameter(new VariableDeclarationNode("bool", DataTypes.BOOLEAN));
	}
	public static Float booleanToFloat(Boolean bool) {
		if (bool == true)
			return (Float) 1f;
		return (Float) 0f;
	}
	@Override
	public Object call(ArrayList<OperationNode> arguments) {
		setArguments(arguments);
		var bool = ((Boolean) getBody().getVariableByName("bool").getValue());
		return booleanToFloat(bool);
	}

}
