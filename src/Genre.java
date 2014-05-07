import java.util.*;
import java.util.Map.Entry;

public class Genre {
	public ArrayList<SubGenre> subgenres = new ArrayList<SubGenre>();
	public String title;
	public Hashtable<String, Integer> dictionary;
	
	public Genre(String title) {
		this.title = title;
		this.dictionary = new Hashtable<String, Integer>();
	}
	
	public HuffmanTree buildTree() {
		PriorityQueue<HuffmanTree> tree = new PriorityQueue<HuffmanTree>();
		
		for (Entry<String, Integer> entry : this.dictionary.entrySet()) {
			tree.offer( new HuffmanLeaf(entry.getValue(), entry.getKey()) );
		}
		
		assert tree.size() > 0;
		
		while(tree.size() > 1) {
			HuffmanTree a = tree.poll();
			HuffmanTree b = tree.poll();
			
			tree.offer(new HuffmanNode(a, b));
		}
		
		return tree.poll();
	}
	
	public int huffCount(Hashtable<String, String> encodedHash) throws Exception {
		int count = 0;
		
		for(String key : this.dictionary.keySet()) {
			count += (encodedHash.get(key).length() * this.dictionary.get(key));
		}
		
		return count;
	}
	
	public int blockCount(Hashtable<String, String> encodedHash) throws Exception {
		//to get the number of bits for block encoding we count the number of words in the dictionary and multiply by the log base 2 of that number
		double log2s = Math.log10(Huffman.uniqueWords.size()) / Math.log10(2);
		int wordCount = 0;
		
		for(String key : this.dictionary.keySet()) {
			wordCount += this.dictionary.get(key);
		}
		
		return (int)(log2s * wordCount);
		/*
		int subCount = 0;
		int count = Huffman.uniqueWords.size();
		
		for(String key : this.dictionary.keySet()) {
			subCount += (this.dictionary.get(key));
		}
		
		subCount *= Math.log(count)/Math.log(2);
	    return subCount;
	    */
	}
}
