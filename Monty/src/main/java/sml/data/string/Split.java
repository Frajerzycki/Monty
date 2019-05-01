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

package sml.data.string;

import ast.expressions.OperationNode;
import sml.data.Method;
import sml.data.list.List;

import java.util.ArrayList;

final class Split extends Method<StringStruct> {

    Split(StringStruct parent) {
        super(parent, "split");
        addParameter("regex");
    }

    @Override
    public List call(ArrayList<OperationNode> arguments, String callFileName, int callLine) {
        setArguments(arguments, callFileName, callLine);
        var body = getBody();
        return new List(parent.getString().split(body.getStringVariableValue("regex", callFileName, callLine)));
    }

}
