package parser.parsing;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ast.Block;
import ast.declarations.FunctionDeclarationNode;
import lexer.LexerConfig;
import lexer.MontyToken;
import lexer.TokenTypes;
import monty.FileIO;
import monty.Main;
import parser.Identificator;
import parser.MontyException;
import parser.Tokens;

public class Parser {
	public static HashMap<String, Object> libraries;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void setStdlib() {
		libraries = new HashMap<>();
		libraries.put("stdlib", new HashMap<>());

		var stdlib = (HashMap) libraries.get("stdlib");
		stdlib.put("casts", new HashMap<>());
		stdlib.put("math", new HashMap<>());
		stdlib.put("io", new HashMap<>());
		stdlib.put("system", new HashMap<>());
		stdlib.put("data", new HashMap<>());

		var casts = (HashMap) stdlib.get("casts");
		casts.put("booleanToFloat", new stdlib.casts.BooleanToFloat());
		casts.put("booleanToInt", new stdlib.casts.BooleanToInt());
		casts.put("booleanToString", new stdlib.casts.BooleanToString());

		casts.put("floatToBoolean", new stdlib.casts.FloatToBoolean());
		casts.put("floatToInt", new stdlib.casts.FloatToInt());
		casts.put("floatToString", new stdlib.casts.FloatToString());

		casts.put("intToBoolean", new stdlib.casts.IntToBoolean());
		casts.put("intToFloat", new stdlib.casts.IntToFloat());
		casts.put("intToString", new stdlib.casts.IntToString());
		casts.put("stringToBoolean", new stdlib.casts.StringToBoolean());
		casts.put("stringToFloat", new stdlib.casts.StringToFloat());
		casts.put("stringToInt", new stdlib.casts.StringToInt());

		var data = (HashMap) stdlib.get("data");
		data.put("array", new HashMap<>());
		var array = (HashMap) data.get("array");
		array.put("arrayOf", new stdlib.data.array.ArrayOf());
		array.put("extendArray", new stdlib.data.array.ExtendArray());
		array.put("getFromArray", new stdlib.data.array.getFromArray());
		array.put("lengthOfArray", new stdlib.data.array.LengthOfArray());

		var io = (HashMap) stdlib.get("io");
		io.put("input", new stdlib.io.Input());

		var math = (HashMap) stdlib.get("math");
		math.put("powerFloat", new stdlib.math.PowerFloat());
		math.put("powerInt", new stdlib.math.PowerInt());

		math.put("sqrtFloat", new stdlib.math.SqrtFloat());
		math.put("sqrtInt", new stdlib.math.SqrtInt());

		var system = (HashMap) stdlib.get("system");
		system.put("argv", new stdlib.system.Argv());

		system.put("exit", new stdlib.system.Exit());
	}

	@SuppressWarnings("unchecked")
	public static void importFile(Block block, List<MontyToken> tokens) {
		var partOfPath = Tokens.getText(tokens.subList(1, tokens.size()));
		var path = new File(Main.path).getParent() + File.separatorChar + partOfPath.replace('.', File.separatorChar)
				+ ".mt";
		if (new File(path).exists()) {
			var text = FileIO.readFile(new File(path).getAbsolutePath());
			var importedTokens = LexerConfig.getLexer(text).getAllTokens();
			var parsed = Parser.parse(importedTokens);
			block.concat(parsed);
		} else {
			var splited = partOfPath.split("\\.");
			var toSearch = libraries;
			Object function = null;
			for (String toImport : splited) {
				if (!toSearch.containsKey(toImport)) {
					new MontyException("There isn't file to import:\t" + partOfPath);
				} else if ((function = toSearch.get(toImport)) instanceof FunctionDeclarationNode) {
					block.addFunction((FunctionDeclarationNode) function);
					break;
				} else {
					toSearch = (HashMap<String, Object>) function;
				}

			}
		}
	}

	public static Block parse(List<MontyToken> tokens) {
		var tokensBeforeSemicolon = new ArrayList<MontyToken>();
		var block = new Block(null);
		for (MontyToken token : tokens) {
			if (token.getType().equals(TokenTypes.SEMICOLON)) {
				if (tokensBeforeSemicolon.size() == 0)
					continue;
				if (Identificator.isExpression(tokensBeforeSemicolon)) {
					AdderToBlock.addExpression(block, tokensBeforeSemicolon);
				} else if (Identificator.isPrintStatement(tokensBeforeSemicolon)) {
					AdderToBlock.addPrintStatement(block, tokensBeforeSemicolon);
				} else if (Identificator.isVariableDeclaration(tokensBeforeSemicolon)) {
					AdderToBlock.addVariableDeclaration(block, tokensBeforeSemicolon);
				} else if (Identificator.isReturnStatement(tokensBeforeSemicolon)) {
					AdderToBlock.addReturnStatement(block, tokensBeforeSemicolon);
				} else if (Identificator.isFunctionDeclaration(tokensBeforeSemicolon)) {
					block = AdderToBlock.addFunctionDeclaration(block, tokensBeforeSemicolon);
				} else if (Identificator.isIfStatement(tokensBeforeSemicolon)) {
					block = AdderToBlock.addIfStatement(block, tokensBeforeSemicolon);
				} else if (Identificator.isElseStatement(tokensBeforeSemicolon)) {
					block = AdderToBlock.addElseStatement(block, tokensBeforeSemicolon);
					if (tokensBeforeSemicolon.size() > 1)
						block = AdderToBlock.addIfStatement(block,
								tokensBeforeSemicolon.subList(1, tokensBeforeSemicolon.size()));
				} else if (Identificator.isWhileStatement(tokensBeforeSemicolon)) {
					block = AdderToBlock.addWhileStatement(block, tokensBeforeSemicolon);
				} else if (Identificator.isImport(tokensBeforeSemicolon)) {
					importFile(block, tokensBeforeSemicolon);
				} else if (Identificator.isChangeToStatement(tokensBeforeSemicolon)) {
					AdderToBlock.addChangeToStatement(block, tokensBeforeSemicolon);
				} else if (Identificator.isEndKeyword(tokensBeforeSemicolon)) {
					var parent = block.getParent();
					if (parent == null)
						new MontyException("Nothing to end!");
					block = block.getParent();
				}
				tokensBeforeSemicolon.clear();
			} else
				tokensBeforeSemicolon.add(token);
		}
		while (true) {
			var parent = block.getParent();
			if (parent == null)
				break;
			block = parent;
		}
		return block;

	}
}
