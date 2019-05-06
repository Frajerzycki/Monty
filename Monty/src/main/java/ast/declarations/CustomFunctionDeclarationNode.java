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

package ast.declarations;

import ast.statements.ContinueStatementNode;
import parser.LogError;
import sml.data.returning.BreakType;
import sml.data.returning.Nothing;
import sml.data.tuple.Tuple;

public final class CustomFunctionDeclarationNode extends FunctionDeclarationNode {


    public CustomFunctionDeclarationNode(String name, String[] parameters, int lastNotNullParameterIndex) {
        super(name, parameters, lastNotNullParameterIndex);
    }

    private CustomFunctionDeclarationNode workingCopy() {
        var copy = (CustomFunctionDeclarationNode) copy();
        copy.body.copyVariables();
        return copy;
    }

    @Override
    public final Object call(Tuple arguments, String callFileName, int callLine) {
        var workingCopy = workingCopy();
        workingCopy.setArguments(arguments, callFileName, callLine);
        String[] fileNames = {callFileName, getFileName()};
        int[] lines = {callLine, getLine()};
        Object result = null;
        try {
            result = workingCopy.getBody().run();
        } catch (StackOverflowError e) {
            new LogError("Stack overflow at " + name + " function call", fileNames, lines);
        }

        if (result == null)
            result = Nothing.nothing;
        else {
            if (result instanceof BreakType)
                new LogError("Trying to break function " + getName(), fileNames, lines);
            if (result instanceof ContinueStatementNode)
                new LogError("Trying to continue function " + getName(), fileNames, lines);
        }
        return result;
    }

    @Override
    public CustomFunctionDeclarationNode copy() {
        var copied = new CustomFunctionDeclarationNode(name, parameters, lastNotNullParameterIndex);
        copied.setBody(body.copy());
        return copied;
    }
}
