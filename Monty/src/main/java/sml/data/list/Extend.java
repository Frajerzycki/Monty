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

package sml.data.list;

import sml.data.Method;
import sml.data.tuple.Tuple;

final class Extend extends Method<List> {

    Extend(List array) {
        super(array, "$a_add", new String[2]);
        addParameter("this");
        addParameter("other");
    }

    @Override
    public List call(Tuple arguments, String callFileName, int callLine) {
        setArguments(arguments, callFileName, callLine);
        var other = body.getVariable("other", callFileName, callLine).getValue();
        parent.doesCanBeExtendedWith(other, callFileName, callLine);
        return parent.extend((List) other);
    }

}
