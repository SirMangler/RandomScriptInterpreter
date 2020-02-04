package ScriptReader;

/**
 * @author SirMangler
 *
 * @date 15 Jul 2019
 */
public class Key {

	public String structure;
	public String[] outputs;
	public int weight;
	
	@Override
	public String toString() {
		return "{ structure="+structure+", weight="+weight+", outputs="+outputs+" }";
	}
}
