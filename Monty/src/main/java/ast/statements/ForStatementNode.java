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

package ast.statements;

import ast.Block;
import ast.declarations.TypeDeclarationNode;
import ast.expressions.OperationNode;
import sml.casts.ToBoolean;
import sml.data.returning.BreakType;
import sml.data.returning.ContinueType;

public final class ForStatementNode extends Block {
    private final OperationNode iterable;
    private final String variableName;

    public ForStatementNode(String variableName, OperationNode iterable, String fileName, int line, Block parent) {
        super(parent);
        this.iterable = iterable;
        this.variableName = variableName;
        setFileName(fileName);
        setLine(line);
    }

    public static boolean isIterable(Object toCheck, String callFileName, int callLine) {
        if (!(toCheck instanceof TypeDeclarationNode))
            return false;

        var structToCheck = (TypeDeclarationNode) toCheck;
        if (!(structToCheck.hasFunction("Iterator") || structToCheck.hasType("Iterator")))
            return false;

        var iteratorStruct = structToCheck.getType("Iterator");
        if (iteratorStruct.hasFunction("init") && iteratorStruct.getFunction("init", callFileName, callLine).getParametersLength() > 0)
            return false;


        return (iteratorStruct.hasFunction("hasNext") && iteratorStruct.getFunction("hasNext", callFileName,
                callLine).getParametersLength() == 0) && (iteratorStruct.hasFunction("next") && iteratorStruct.
                getFunction("next", callFileName, callLine).getParametersLength() == 0);
    }

    @Override
    public ForStatementNode copy() {
        var copied = new ForStatementNode(variableName, iterable.copy(), getFileName(), getLine(), getParent());
        copied.setChildren(getChildren());
        copied.copyChildren();

        return copied;
    }


    @Override
    public Object run() {
        Object result;
        var isNotNameUnderscore = !variableName.equals("_");
        var isConst = Character.isUpperCase(variableName.charAt(0));
        var toBeIterated = iterable.run();
        var variable = getVariable(variableName, getFileName(), getLine());
        var fileName = getFileName();
        var line = getLine();
        if (isIterable(toBeIterated, getFileName(), getLine())) {
            var iterator = (TypeDeclarationNode) ((TypeDeclarationNode) toBeIterated).getFunction("Iterator", getFileName(), getLine())
                    .call(OperationNode.emptyTuple, getFileName(), getLine());
            var hasNext = iterator.getFunction("hasNext", getFileName(), getLine());
            var next = iterator.getFunction("next", getFileName(), getLine());

            if (isNotNameUnderscore) {
                while (ToBoolean.toBoolean(hasNext.call(OperationNode.emptyTuple, fileName, line), fileName, line)) {
                    variable.setConst(false);
                    variable.setValue(next.call(OperationNode.emptyTuple, fileName, line), fileName, line);
                    variable.setConst(isConst);
                    result = super.run();
                    if (result instanceof BreakType)
                        break;
                    if (result instanceof ContinueType)
                        continue;
                    if (result != null)
                        return result;
                }
            } else
                while (ToBoolean.toBoolean(hasNext.call(OperationNode.emptyTuple, fileName, line), fileName, line)) {
                    next.call(OperationNode.emptyTuple, getFileName(), getLine());
                    result = super.run();
                    if (result instanceof BreakType)
                        break;
                    if (result instanceof ContinueType)
                        continue;
                    if (result != null)
                        return result;
                }
        } else {
            if (isNotNameUnderscore) {
                variable.setConst(false);
                variable.setValue(toBeIterated, fileName, line);
                variable.setConst(isConst);
            }
            result = super.run();
            if (!(result == null || (result instanceof BreakType || result instanceof ContinueType)))
                return result;
        }
        return null;
    }

    @Override
    public void setParent(Block parent) {
        super.setParent(parent);
        iterable.setParent(parent);
    }
}
