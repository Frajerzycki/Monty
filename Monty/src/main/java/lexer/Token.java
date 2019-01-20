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

public class Token implements Cloneable {
	private String text;
	private String fileName;
	TokenTypes type;
	private int line;

	public Token(TokenTypes type) {
		this.type = type;
	}

	public Token(TokenTypes type, String text, String fileName, int line) {
		this.type = type;
		this.text = text;
		this.fileName = fileName;
		this.line = line;
	}

	public Token copy() throws CloneNotSupportedException {
		return (Token) super.clone();
	}

	public String getFileName() {
		return fileName;
	}

	public int getLine() {
		return line;
	}

	public String getText() {
		return text;
	}

	public TokenTypes getType() {
		return type;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public void setLine(int line) {
		this.line = line;
	}

	public void setText(String text) {
		this.text = text;
	}


}
