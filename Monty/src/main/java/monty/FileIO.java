package monty;

/*
Copyright 2018 Szymon Perlicki

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
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import parser.MontyException;

public abstract class FileIO {
	public static String readFile(String path) {
		BufferedReader br = null;
		var text = new StringBuilder();
		try {
			var fr = (FileReader) null;
			try {
				fr = new FileReader(path);
			} catch (FileNotFoundException e) {
				new MontyException("This file isn't exists:\t" + path);
			}
			br = new BufferedReader(fr);
			String line;
			while ((line = br.readLine()) != null) {
				text.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null) {
					br.close();
				}
			} catch (IOException ex) {
				new MontyException("Failed to read file " + path);
			}
		}
		return text.toString();
	}

	public static void writeFile(String path, String text, boolean isAppend) {
		try {
			File file = new File(path);
			FileWriter fileWriter = new FileWriter(file,isAppend);
			fileWriter.write(text);
			fileWriter.flush();
			fileWriter.close();
		} catch (IOException e) {
			new MontyException("Failed to write file " + path);
			e.printStackTrace();
		}

	}
}
