import java.io.*;
import java.util.*;
import java.util.Map.Entry;

public class Huffman {
	public ArrayList<Genre> genres = new ArrayList<Genre>();
	public ArrayList<String> uniqueWords = new ArrayList<String>();
	
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
						if(!uniqueWords.contains(word)){
							uniqueWords.add(word);
						}
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
}
