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

package lexer;

public enum TokenTypes {
    BOOLEAN_LITERAL, OPENING_BRACKET, CLOSING_BRACKET, OPENING_SQUARE_BRACKET, CLOSING_SQUARE_BRACKET,
    ELSE_KEYWORD, END_KEYWORD, FLOAT_LITERAL, FN_KEYWORD, IDENTIFIER, IF_KEYWORD, IMPORT_KEYWORD, INTEGER_LITERAL,
    OPERATOR, RETURN_KEYWORD, SEMICOLON, STRING_LITERAL, WHILE_KEYWORD, DOT, DO_KEYWORD, BREAK_KEYWORD, CONTINUE_KEYWORD,
    FOR_KEYWORD, IN_KEYWORD, TYPE_KEYWORD, FUNCTION, EMPTY_TUPLE, NAMESPACE_KEYWORD,ARROW
}
