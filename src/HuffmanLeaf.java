
public class HuffmanLeaf extends HuffmanTree {
	public final String word;  //the word that is stored being referenced
	
	public HuffmanLeaf(int freq, String wordIn) {
		super(freq);
		word = wordIn;
	}
}
