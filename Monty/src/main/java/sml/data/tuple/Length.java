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

package sml.data.tuple;

import ast.declarations.FunctionDeclarationNode;
import sml.data.Method;

final class Length extends Method<Tuple> {

    Length(Tuple parent) {
        super(parent, "length", FunctionDeclarationNode.EMPTY_PARAMETERS);
    }

    @Override
    public Integer call(Tuple arguments, String callFileName, int callLine) {
        setArguments(arguments, callFileName, callLine);
        return parent.length();
    }

}
