//Edward Tseng
//11/2/13

public class Outcast {
	private WordNet wn;

	// constructor takes a WordNet object
	public Outcast(WordNet wordnet) {
		this.wn = wordnet;
	}

	// given an array of WordNet nouns, return an outcast
	public String outcast(String[] nouns) {
		//initialize array to store distance information
		int[] distance = new int[nouns.length];
		for (int i = 0; i < distance.length; i++)
			distance[i] = 0;
		
		//find distance
		for (int i = 0; i < nouns.length ; i++)
			for (int j = 0; j < nouns.length; j++) {
				if (i == j) continue;
				distance[j] += wn.distance(nouns[i], nouns[j]); 
			}
		
		//find max distance
		int max = 0;
		int max_index = 0;
		for (int i = 0; i < distance.length; i++)
			if (distance[i] > max ) {
				max = distance[i];
				max_index = i;
			}
		
		return nouns[max_index];
	}

	// for unit testing of this class (such as the one below)
	public static void main(String[] args) {
		/*WordNet wordnet = new WordNet("synsets.txt", "hypernyms.txt");
	    Outcast outcast = new Outcast(wordnet);
	    String file = "outcast5.txt";
	    String[] nouns = In.readStrings(file);
	    StdOut.println(file + ": " + outcast.outcast(nouns));
	    */
	}
}
