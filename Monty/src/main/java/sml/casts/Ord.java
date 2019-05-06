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

package sml.casts;

import ast.Block;
import parser.LogError;
import sml.NativeFunctionDeclarationNode;
import sml.data.tuple.Tuple;

import java.math.BigInteger;

public final class Ord extends NativeFunctionDeclarationNode {

    public Ord() {
        super("ord", new String[1]);
        setBody(new Block(null));
        addParameter("chr");
    }

    @Override
    public Object call(Tuple arguments, String callFileName, int callLine) {
        setArguments(arguments, callFileName, callLine);
        var chr = (String) getBody().getVariableValue("chr", callFileName, callLine);
        if (chr.length() != 1)
            new LogError("Expected one character, but got " + chr.length() + ":\t" + chr, callFileName, callLine);
        return BigInteger.valueOf(chr.charAt(0));
    }

}
