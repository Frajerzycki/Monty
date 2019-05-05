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

package sml.math;

import ast.Block;
import ast.declarations.FunctionDeclarationNode;
import ast.expressions.OperationNode;
import parser.LogError;
import sml.data.StaticStruct;
import sml.data.tuple.Tuple;

import java.util.ArrayList;

public final class Round extends FunctionDeclarationNode {

    public Round() {
        super("round", new String[1]);
        setBody(new Block(null));
        addParameter("f");
    }

    @Override
    public Object call(Tuple arguments, String callFileName, int callLine) {
        setArguments(arguments, callFileName, callLine);
        var f = getBody().getVariableValue("f", callFileName, callLine);
        if (f instanceof Double)
            return Math.round((double) f);
        return new LogError("Can't round not a number.", callFileName, callLine);
    }

}
