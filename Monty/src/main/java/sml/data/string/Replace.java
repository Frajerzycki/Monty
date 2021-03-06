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

package sml.data.string;

import sml.data.Method;
import sml.data.tuple.Tuple;

final class Replace extends Method<MontyString> {

    Replace(MontyString parent) {
        super(parent, "replace", new String[2]);
        addParameter("regex");
        addParameter("replacement");

    }

    @Override
    public MontyString call(Tuple arguments, String callFileName, int callLine) {
        setArguments(arguments, callFileName, callLine);
        return parent.replace(body.getStringVariableValue("regex", callFileName, callLine),
                body.getStringVariableValue("replacement", callFileName, callLine));
    }

}
