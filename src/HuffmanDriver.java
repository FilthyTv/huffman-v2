import java.util.Hashtable;
import java.util.Map.Entry;



public class HuffmanDriver {

	public static void main(String[] args) {
		Huffman huf = new Huffman("lyricdata");
		HuffmanTree genreTree;
		HuffmanTree subgenreTree;
		Hashtable<String, String> encodedHash;
		double huffCount, blockCount;
		
		System.out.println("\nBuilding word count for all artists");
		huf.countSubs();
		System.out.println("Words have been counted!\n");
		
		/***** First Part, Compress each subgenre and genre with its own tree *********/
		for(Genre g : huf.genres) {
			System.out.println("Building tree for " + g.title);
			genreTree = g.buildTree();
			System.out.println("Tree built!\n\nEncoding words...");
			encodedHash = huf.encodeWords(genreTree, new StringBuffer());
			System.out.println("Words Encoded!");
			try {
				huffCount = g.huffCount(encodedHash);
				blockCount = g.blockCount(encodedHash);
				System.out.println("\nRatio for " + g.title + "\t" + huffCount/blockCount);
			} catch (Exception e) {
				System.out.println("Error in compression");
				e.printStackTrace();
			}
			for(SubGenre sg : g.subgenres) {
				System.out.println("Building tree for " + sg.title);
				subgenreTree = sg.buildTree();
				System.out.println("Tree built!\n\nEncoding words...");
				encodedHash = huf.encodeWords(subgenreTree, new StringBuffer());
				System.out.println("Words Encoded!");
				try {
					huffCount = sg.huffCount(encodedHash);
					blockCount = sg.blockCount(encodedHash);
					System.out.println("\nRatio for " + sg.title + "\t" + huffCount/blockCount);
				} catch (Exception e) {
					System.out.println("Error in compression");
					e.printStackTrace();
				}
			}
			
		}
		
		
		/*
		SubGenre bluegrass = huf.genres.get(0).subgenres.get(0);
		
		System.out.println("\nBuilding the tree");
		HuffmanTree tree = huf.buildTree(bluegrass);
		System.out.println("Tree built!\n");
		
		System.out.println("Encoding Words...");
		Hashtable<String, String> encodedHash = huf.encodeWords(tree, new StringBuffer());
		System.out.println("Successfully Encoded!\n");
		
		try {
			System.out.println("huffcount = " + huf.huffCount(bluegrass, encodedHash));
			System.out.println("blockcount = " + huf.blockCount(bluegrass, encodedHash));
			System.out.println("ratio = " + ((double)(huf.huffCount(bluegrass, encodedHash) / (double)huf.blockCount(bluegrass, encodedHash))));
		} catch (Exception e) {
			e.printStackTrace();
		};
		// */
	}
}
