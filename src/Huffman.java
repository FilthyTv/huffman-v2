import java.io.*;
import java.util.*;

public class Huffman {
	int numberOfWords = 0;
	public ArrayList<Genre> genres = new ArrayList<Genre>();
	public static Hashtable<String, String> uniqueWords = new Hashtable<String, String>();
	
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
						numberOfWords++;
						word = s.next();
						word = word.toLowerCase().replaceAll("[^a-zA-Z ]", "");
						if(sg.dictionary.get(word) == null) {
							sg.dictionary.put(word, 1);
						} else {
							sg.dictionary.put(word, sg.dictionary.get(word) + 1);
						}
						
						if(g.dictionary.get(word) == null) {
							g.dictionary.put(word, 1);
						} else {
							g.dictionary.put(word, g.dictionary.get(word) + 1);
						}
						
						uniqueWords.put(word, "");
					}
					s.close();
				}
			}
		}
		//TODO: look into "add one"
		/*********************** Add One Operation ********************************/
		// Unique words has been built by now, we need to to through each subgenres
		// dictionary and make sure it includes everyword in our entire set at least
		// once.  This is performing the "add one" operation.  
		/* Activate this comment to disable the "add one" operation
		for(String word : uniqueWords.keySet()) {
			for(Genre g : genres) {
				for(SubGenre sg : g.subgenres) {
					if(sg.dictionary.get(word) == null) {
						sg.dictionary.put(word, 1);
					} else {
						sg.dictionary.put(word, sg.dictionary.get(word) + 1);
					}
				}
			}
		}
		// */
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
