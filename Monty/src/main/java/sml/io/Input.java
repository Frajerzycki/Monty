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

package sml.io;

import ast.Block;
import ast.declarations.FunctionDeclarationNode;
import sml.NativeFunctionDeclarationNode;
import sml.data.string.MontyString;
import sml.data.tuple.Tuple;

import java.util.NoSuchElementException;
import java.util.Scanner;

public final class Input extends NativeFunctionDeclarationNode {

    private final static Scanner scanner = new Scanner(System.in);

    public Input() {
        super("input", FunctionDeclarationNode.EMPTY_PARAMETERS);
        setBody(new Block(null));
    }

    @Override
    public MontyString call(Tuple arguments, String callFileName, int callLine) {
        setArguments(arguments, callFileName, callLine);
        try {
            return new MontyString(scanner.nextLine());
        } catch (NoSuchElementException e) {
            return new MontyString("");
        }
    }

}
