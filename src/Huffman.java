import java.io.*;
import java.util.*;
import java.util.Map.Entry;

public class Huffman {
	public ArrayList<Genre> genres = new ArrayList<Genre>();
	public Hashtable<String, String> uniqueWords = new Hashtable<String, String>();
	
	public Huffman(String baseDir) {
		File base = new File(baseDir);
		File[] contents = base.listFiles();
		
		for(File g : contents) {
			if(g.isDirectory()) {
				Genre genre = new Genre(g.getName());
				genres.add(genre);
				File gdir = new File(g.getPath());
				File[] gcontents = gdir.listFiles();
				
				for(File sg : gcontents) {
					if(sg.isDirectory()) {
						SubGenre subgenre = new SubGenre(sg.getName());
						genre.subgenres.add(subgenre);
						File arts = new File(sg.getPath());
						File[] sgcontents = arts.listFiles();
						
						for(File art : sgcontents) {
							if(art.isFile()) {
								Artist artist = new Artist(art.getName().substring(0, art.getName().length() - 4));
								artist.path = art.getPath();
								subgenre.artists.add(artist);
								System.out.println("Adding artist " + artist.name + " in sg " + subgenre.title + " in g " + genre.title);
							}
						}
					}
				}
			}
		}
	}
	
	public void countSubs() {
		//Count all of the words
		for(Genre g : genres) {
			for (SubGenre sg : g.subgenres) {
				for(Artist a : sg.artists) {
					File f = new File(a.path);
					Scanner s;
					try {
						s = new Scanner(f);
					} catch(FileNotFoundException e) {
						return;
					}
					String word;
					while(s.hasNext()) {
						word = s.next();
						word = word.toLowerCase().replaceAll("[^a-zA-Z ]", "");
						if(sg.dictionary.get(word) == null) {
							sg.dictionary.put(word, 1);
						} else {
							int newCount = sg.dictionary.get(word) + 1;
							sg.dictionary.remove(word);
							sg.dictionary.put(word, newCount);
						}
						
						uniqueWords.put(word, "");
					}
				}
			}
		}
	}
	
	public HuffmanTree buildTree(SubGenre sg) {
		PriorityQueue<HuffmanTree> tree = new PriorityQueue<HuffmanTree>();
		
		for (Entry<String, Integer> entry : sg.dictionary.entrySet()) {
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
	
	
	public Hashtable<String, String> encodeWords(HuffmanTree tree, StringBuffer prefix) {
		
		Hashtable<String, String> encodedHash = new Hashtable<String, String>();
		
        assert tree != null;
        if (tree instanceof HuffmanLeaf) {
            HuffmanLeaf leaf = (HuffmanLeaf)tree;
 
            // print out character, frequency, and code for this leaf (which is just the prefix)
            // System.out.println(leaf.word + "\t" + leaf.frequency + "\t" + prefix);
            
            //create a new word with the same name but different value (instead of frequency we use its code)
	        encodedHash.put(leaf.word, prefix.toString());
 
        } else if (tree instanceof HuffmanNode) {
            HuffmanNode node = (HuffmanNode)tree;
 
            // traverse left
            prefix.append('0');
            encodedHash.putAll(encodeWords(node.left, prefix));
            prefix.deleteCharAt(prefix.length()-1);
 
            // traverse right
            prefix.append('1');
            encodedHash.putAll(encodeWords(node.right, prefix));
            prefix.deleteCharAt(prefix.length()-1);
        }
        
        return encodedHash;
    }
}
