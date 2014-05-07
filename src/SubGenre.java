import java.util.*;
import java.util.Map.Entry;

public class SubGenre {
	public ArrayList<Artist> artists = new ArrayList<Artist>();
	public String title;
	public Hashtable<String, Integer> dictionary;

	public SubGenre(String title) {
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

		Hashtable<String, Integer> entries = this.dictionary;
		int count = 0;

		for(String key : entries.keySet()) {
			// multiply length of bits by how many times the word appears
			count += (encodedHash.get(key).length() * entries.get(key));
		}

		return count;
	}
	
	public int blockCount(Hashtable<String, String> encodedHash) throws Exception {
		//to get the number of bits for block encoding we count the number of words in the dictionary and multiply by the log base 2 of that number
		int subCount = 0;
		int count = Huffman.uniqueWords.size();
		
		Hashtable<String, Integer> entries = this.dictionary;
		
		for(String key : entries.keySet()) {
			subCount += (entries.get(key));
		}
		
		subCount *= Math.log(count)/Math.log(2);
	    return subCount;
	}
}
