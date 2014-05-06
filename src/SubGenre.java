import java.util.*;

public class SubGenre {
	public ArrayList<Artist> artists = new ArrayList<Artist>();
	public String title;
	public Hashtable<String, Integer> dictionary;
	
	public SubGenre(String title) {
		this.title = title;
		this.dictionary = new Hashtable<String, Integer>();
	}
}
