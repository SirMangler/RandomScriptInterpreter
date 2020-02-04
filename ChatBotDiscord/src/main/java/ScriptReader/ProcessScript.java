package ScriptReader;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author SirMangler
 *
 * @date 15 Jul 2019
 */
public class ProcessScript {
	
	public static HashMap<String, Key> keys = new HashMap<String, Key>();
	public static HashMap<String, String[]> synon = new HashMap<String, String[]>();
	public static HashMap<String, String[]> words = new HashMap<String, String[]>();
	
	
	public static boolean loadScript() throws FileNotFoundException {
		BufferedReader reader = new BufferedReader(new FileReader("script"));
		
		String line;
		try {
			String tag = null;
			Key key = null;
			
			while ((line = reader.readLine()) != null) {
				if (line.isEmpty()) continue;
				if (line.startsWith("#")) continue;
				
				if (line.startsWith("[")) {
					if (key != null) {
						keys.put(tag, key);
						key = null;
					}
					
					tag = line.substring(1, line.indexOf(']'));
				} else {
					String[] split = line.split(" = ", 2);
					if (tag.equalsIgnoreCase("arrays")) {
						words.put(split[0], split[1].split(";"));
						
						continue;
					}
					
					if (tag.equalsIgnoreCase("synon")) {
						synon.put(split[0], split[1].split(";"));
						
						continue;
					}			
					
					if (split[0].equalsIgnoreCase("structure")) {
						if (key == null) key = new Key(); 
						
						key.structure=split[1];
					} else if (split[0].equalsIgnoreCase("outputs")) {
						if (key == null) key = new Key();
						
						key.outputs=split[1].split(";");
					} else if (split[0].equalsIgnoreCase("weight")) {
						if (key == null) key = new Key();
						
						key.weight = Integer.parseInt(split[1]);
					}
				}
			}
			
			if (key != null) {
				keys.put(tag, key);
				key = null;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println(keys);
		System.out.println(synon);
		System.out.println(words);
		
		return true;
	}
	
}
