package sml.data.string;

import ast.expressions.OperationNode;
import sml.data.Method;

import java.util.ArrayList;

public class ConcatRight extends Method<StringStruct> {

    ConcatRight(StringStruct parent) {
        super(parent, "$r_add");
        addParameter("other");
        addParameter("this");

    }

    @Override
    public Object call(ArrayList<OperationNode> arguments, String callFileName, int callLine) {
        setArguments(arguments, callFileName, callLine);
        return getBody().getVariable("other", callFileName, callLine).getValue().toString() + parent.getString();
    }

}
