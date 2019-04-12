package sml.functional.function;

import java.util.ArrayList;

import ast.Block;
import ast.declarations.FunctionDeclarationNode;
import ast.expressions.OperationNode;
import ast.expressions.VariableNode;
import parser.DataTypes;
import parser.LogError;

public final class FunctionByName extends FunctionDeclarationNode {

	public FunctionByName() {
		super("f", DataTypes.ANY);
		setBody(new Block(null));
		addParameter("name", DataTypes.ANY);
	}

	@Override
	public Object call(ArrayList<OperationNode> arguments, String callFileName, int callLine) {
		var argumentsSize = arguments.size();
		if (argumentsSize > 1)
			new LogError("Too many arguments in " + name + " function call.", callFileName, callLine);
		else if (argumentsSize < 1)
			new LogError("Too few arguments in " + name + " function call.", callFileName, callLine);
		var firstArgument = arguments.get(0);
		var argument = firstArgument.getOperand();
		if (!(argument instanceof VariableNode))
			new LogError("Argument should be just a function name.", callFileName, callLine);
		return new Function(firstArgument.getParent().getFunction(((VariableNode) argument).getName()));
	}

}