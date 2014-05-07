import org.jfree.data.xy.*;

import java.util.*;

public class HuffmanDriver {

	public static void main(String[] args) {
		Huffman huf = new Huffman("lyricdata");
		HuffmanTree genreTree;
		HuffmanTree subgenreTree;
		HuffmanTree artistTree;
		Hashtable<String, String> encodedHash;
		double huffCount, blockCount;
		
		System.out.println("\nBuilding word count for all artists");
		huf.countWords();
		System.out.println("Words have been counted!\n");
		
		/***** First Part, Compress each subgenre and genre with its own tree *********/
		System.out.println("\n:::::::::::::::::: Part A, encode each subgenre and genre with itself :::::::::::::::::::::\n");
		for(Genre g : huf.genres) {
			System.out.println("\n---ALL " + g.title.toUpperCase() + "---\nBuilding tree for all " + g.title);
			genreTree = g.buildTree();
			System.out.println("Tree built!\n\nEncoding words...");
			encodedHash = huf.encodeWords(genreTree, new StringBuffer());
			System.out.println("Words Encoded!");
			try {
				huffCount = g.huffCount(encodedHash);
				blockCount = g.blockCount(encodedHash);
				System.out.println("\nRatio for all " + g.title + ": " + huffCount/blockCount);
			} catch (Exception e) {
				System.out.println("Error in compression");
				e.printStackTrace();
			}
			for(SubGenre sg : g.subgenres) {
				System.out.println("\n---" + sg.title.toUpperCase() + " " + g.title.toUpperCase() + "---\nBuilding tree for " + sg.title + " " + g.title);
				subgenreTree = sg.buildTree();
				System.out.println("Tree built!\n\nEncoding words...");
				encodedHash = huf.encodeWords(subgenreTree, new StringBuffer());
				System.out.println("Words Encoded!");
				try {
					huffCount = sg.huffCount(encodedHash);
					blockCount = sg.blockCount(encodedHash);
					System.out.println("\nRatio for " + sg.title + " " + g.title + ": " + huffCount/blockCount);
				} catch (Exception e) {
					System.out.println("Error in compression");
					e.printStackTrace();
				}
			}
			
		}
		
		/***** Second Part, Apply a single subgenre to all other subgenres *********/
		System.out.println("\n:::::::::::::::::: Part B, encode each subgenre with every other subgenre :::::::::::::::::::::\n");
		int count = 0;
		for(Genre g : huf.genres) {
			for(SubGenre sg : g.subgenres) {
				System.out.println("\n---" + sg.title.toUpperCase() + " as the base---");
				subgenreTree = sg.buildTree();
				encodedHash = huf.encodeWords(subgenreTree, new StringBuffer());
				ArrayList<XYDataItem> items = new ArrayList<XYDataItem>();
				for(SubGenre s : g.subgenres) {
					System.out.println("Encoding " + s.title + " " + g.title + " with " + sg.title + " " + g.title);
					try {
						huffCount = s.huffCount(encodedHash);
						blockCount = s.blockCount(encodedHash);
						items.add(new XYDataItem(count, (huffCount/blockCount)));
						System.out.println("Ratio: " + huffCount/blockCount);
					} catch (Exception e) {
						System.out.println("************Failed to encode");
						e.printStackTrace();
					}
				}
				try {
					huf.makeGraph("title", sg.title, "", items);
				} catch (Exception e) {
					e.printStackTrace();
				}
				items.clear();
				count++;
			}
		}
		
		/***** Third Part, Apply each artist to its subgenre and genre *********/
		System.out.println("\n:::::::::::::::::: Part C, encode each artists genre and subgenre using just the artist :::::::::::::::::::::\n");
		for(Genre g : huf.genres) {
			for(SubGenre sg : g.subgenres) {
				for(Artist a : sg.artists) {
					System.out.println("\n---" + a.name + " as the base---");
					artistTree = a.buildTree();
					encodedHash = huf.encodeWords(artistTree, new StringBuffer());
					try {
						System.out.println("Encoding owners subgenre");
						huffCount = a.subgenre.huffCount(encodedHash);
						blockCount = a.subgenre.blockCount(encodedHash);
						System.out.println("Ratio: " + huffCount/blockCount);
						System.out.println("\nEncoding owners genre");
						huffCount = a.genre.huffCount(encodedHash);
						blockCount = a.genre.blockCount(encodedHash);
						System.out.println("Ratio: " + huffCount/blockCount);
					} catch (Exception e) {
						System.out.println("**************Failed to encode");
						e.printStackTrace();
					}
				}
			}
		}
		
		System.out.println("\n\nDONE!");
	}
}
