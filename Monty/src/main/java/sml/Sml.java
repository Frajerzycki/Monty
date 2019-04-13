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

package sml;

import java.util.ArrayList;
import java.util.HashMap;

import ast.expressions.OperationNode;
import monty.IOBlocks;
import monty.Library;
import sml.casts.*;
import sml.data.*;
import sml.data.string.*;
import sml.data.checking.*;
import sml.files.*;
import sml.language.Run;
import sml.math.*;
import sml.system.*;
import sml.threading.*;
import sml.time.*;

public final class Sml extends Library {
	public static final ArrayList<OperationNode> emptyArgumentList = new ArrayList<>();

	public Sml() {
		super("sml");
	}

	@Override
	public void setLibrary() {
		var sml = getSublibraries();
		var casts = new HashMap<String, Object>();
		var math = new HashMap<String, Object>();
		var system = new HashMap<String, Object>();
		var data = new HashMap<String, Object>();
		var threading = new HashMap<String, Object>();
		var time = new HashMap<String, Object>();
		var checking = new HashMap<String, Object>();
		var string = new HashMap<String, Object>();
		var errors = new HashMap<String, Object>();
		var files = new HashMap<String, Object>();
		var language = new HashMap<String, Object>();

		sml.put("casts", casts);
		sml.put("math", math);
		sml.put("system", system);
		sml.put("data", data);
		sml.put("threading", threading);
		sml.put("time", time);
		sml.put("math", math);
		sml.put("errors", errors);
		sml.put("files", files);
		sml.put("language", language);

		casts.put("toBoolean", new ToBoolean());
		casts.put("toReal", new ToReal());
		casts.put("toInt", new ToInt());
		casts.put("toString", new ToString());
		casts.put("toChar", new ToChar());
		casts.put("ord", new Ord());

		data.put("checking", checking);
		data.put("string", string);
		data.put("length", new Length());

		checking.put("isInt", new IsInt());
		checking.put("isIterable", new IsIterable());
		checking.put("isReal", new IsReal());
		checking.put("isBoolean", new IsBoolean());
		checking.put("isString", new IsString());
		checking.put("isList", new IsList());
		checking.put("isObject", new IsObject());

		string.put("charAt", new CharAt());
		string.put("endsWith", new EndsWith());
		string.put("equalsIgnoreCase", new EqualsIgnoreCase());
		string.put("strlen", new Strlen());
		string.put("replace", new Replace());
		string.put("replaceFirst", new ReplaceFirst());
		string.put("startsWith", new StartsWith());
		string.put("substring", new Substring());
		string.put("lowerCase", new LowerCase());
		string.put("upperCase", new UpperCase());
		string.put("split", new Split());

		system.put("argv", new Argv());
		system.put("argc", new Argc());
		system.put("exit", new Exit());

		threading.put("sleep", new Sleep());

		time.put("unixTime", new UnixTime());
		time.put("unixTimeMillis", new UnixTimeMillis());

		math.put("Pi", new Pi());
		math.put("E", new E());
		math.put("pow", new Pow());
		math.put("root", new Root());
		math.put("min", new Min());
		math.put("max", new Max());
		math.put("random", new Random());
		math.put("round", new Round());
		math.put("factorial", new Factorial());
		math.put("scale", new Scale());
		math.put("exp", new Exp());
		math.put("log", new Log());
		math.put("abs", new Abs());

		errors.put("logError", IOBlocks.logError);

		files.put("separator", new Separator());
		files.put("listDir", new ListDir());
		files.put("absPath", new AbsPath());
		files.put("write", new Write());
		files.put("read", new Read());
		files.put("exists", new Exists());
		files.put("isAbsolute", new IsAbsolute());
		files.put("isDir", new IsDir());
		files.put("isFile", new IsFile());

		language.put("run", new Run());
	}

}
