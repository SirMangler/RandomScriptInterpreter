package ChatBot;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Random;

import ScriptReader.Key;
import ScriptReader.ProcessScript;

/**
 * @author SirMangler
 *
 * @date 15 Jul 2019
 */
public class Chat {

	/**
	 * @param args
	 */

	static Random r = new Random();
	
	public static String cipher(String input, String name) {
		if (input.contains(" ")) {
			for (String word : input.split(" ")) {
				if (word.startsWith("%")) {
					String[] arr = ProcessScript.words.get(word.substring(1));
					
					input = input.replace(word, 
							arr[r.nextInt(arr.length)]);
				}
			}
		} else {
			if (input.startsWith("%")) {
				String[] arr = ProcessScript.words.get(input.substring(1));
				
				input = input.replace(input, 
						arr[r.nextInt(arr.length)]);
			}
		}

		if (input.contains("&p")) {
			input = input.replace("&p", name);
		}
		
		return fixGrammar(input);
	}
	
	public static String fixGrammar(String input) {
		input = input.replaceAll("  ", " ").trim();
		
		String[] inputWords = input.split(" ");
		
		if (input.contains(" ")) {
			for (int i = 0; i < inputWords.length; i++) {
				if ((i + 1) > inputWords.length) break;
				
				if (inputWords[i].equalsIgnoreCase("the")) {
					if (inputWords[i+1].equalsIgnoreCase("a")) {
						input = input.replace("a ", "");
					}
				}
				
				if (inputWords[i].equalsIgnoreCase("you're")) {
					if (!inputWords[i+1].equalsIgnoreCase("a") && !inputWords[i+1].equalsIgnoreCase("an")) {
						input = input.replace("you're", "you're a");
					}
				}
			}
		}
		
		return input;
	}
	
	public static String processInput(String input, String name) {
		for (Entry<String, String[]> entries : ProcessScript.synon.entrySet()) {
			for (String synon : entries.getValue()) {
				input = input.replace(synon, entries.getKey());
			}
		}

		HashMap<String, Integer> outputs = new HashMap<String, Integer>();
		
		for (Entry<String, Key> entry : ProcessScript.keys.entrySet()) {
			String[] inputWords = input.split(" ");
			String[] structures = entry.getValue().structure.split(";");

			for (String structure : structures) {
				String[] keyWords = structure.split(" ");
				
				int bounds;
				if (inputWords.length < keyWords.length) 
					bounds = inputWords.length;
				else bounds = keyWords.length;
				
				for (int i = 0; i < bounds; i++) {

					String inputWord = inputWords[i];
					String keyWord = keyWords[i];

					if (keyWord.startsWith("%")) {
						String[] arr = ProcessScript.words.get(keyWord.substring(1));
						
						for (String arrWord : arr) {
							if (inputWord.equalsIgnoreCase(arrWord)) {
								String output = entry.getValue().outputs[r.nextInt(entry.getValue().outputs.length)];
								
								if (inputWords.length > 1) {
									String[] inputSplit = input.split(arrWord);
									output = output.replace("(1)", inputSplit[1]);
								}

								outputs.put(output, entry.getValue().weight);
								break;
							}
						}
					}
					
					if (inputWord.equalsIgnoreCase(keyWord)) {
						String output = entry.getValue().outputs[r.nextInt(entry.getValue().outputs.length)];
						
						if (inputWords.length > 1) {
							String[] inputSplit = input.split(keyWord);
							output = output.replace("(1)", inputSplit[1]);
						}

						outputs.put(output, entry.getValue().weight);
					}
				}
			}
		}
		
		int weight = 0;
		String output = null;
		for (String o : outputs.keySet()) {
			int i = outputs.get(o) ;
			
			if (i > weight) {
				output = o;
				weight = i;
			}
		}

		return cipher(output, name);
	}
}
