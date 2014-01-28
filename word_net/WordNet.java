//Edward Tseng
//11/2/13

import java.util.*;

public class WordNet {
	private HashMap <String, HashSet> synsets;
	private HashMap <Integer, String> nounsets;
	private SAP sap;
	private boolean[] marked;  // marked[v] = is there an s->v path?
	private boolean rooted;
	private int rootDAG;

	// constructor takes the name of the two input files
	public WordNet(String synsets, String hypernyms) {
		this.synsets = new HashMap <String, HashSet>();
		this.nounsets = new HashMap <Integer, String>();
		int synsCounter = 0;
		rooted = false;
		rootDAG = Integer.MAX_VALUE;
		
		In inSyn = new In(synsets);
		while(!inSyn.isEmpty()) {
			String[] inputLine = inSyn.readLine().split(",");
			String[] nouns = inputLine[1].split(" ");
			
			for (String noun : nouns) {
				if (this.synsets.containsKey(noun)) {
					this.synsets.get(noun).add(Integer.parseInt(inputLine[0]));
					
				} else {
					Set <Integer> set = new HashSet <Integer>();
					set.add(Integer.parseInt(inputLine[0]));
					this.synsets.put(noun, (HashSet)set);
					this.nounsets.put(Integer.parseInt(inputLine[0]), noun);
				}
			}
			synsCounter++;
		}
	
		In inHyper = new In(hypernyms);
		Digraph dg = new Digraph(synsCounter + 1);
		while(!inHyper.isEmpty()) {
			String[] inputLine = inHyper.readLine().split(",");
			for (int i = 1; i < inputLine.length; i++)
				dg.addEdge(Integer.parseInt(inputLine[0]), Integer.parseInt(inputLine[i]));
		}

		marked = new boolean[dg.V()];
		
		if (new DirectedCycle(dg).hasCycle())
			throw new java.lang.IllegalArgumentException("Cycle Detected, graph is not a DAG!");
		
		this.sap = new SAP(dg);
		bfs(dg, dg.V() - 2);
		if (!rooted)
			throw new java.lang.IllegalArgumentException("Graph is not a ROOTED DAG!");
			
	}
	
	private void bfs(Digraph G, int s) {
		Queue<Integer> q = new Queue<Integer>();
		marked[s] = true;
		q.enqueue(s);
		while (!q.isEmpty()) {
			int v = q.dequeue();
			if (G.adj(v).iterator().hasNext() == false)
				if (rooted == false) {
					rooted = true;
					rootDAG = v;
				} else
					if (rootDAG != v) {
						rooted = false;
						break;
					}
					
			for (int w : G.adj(v)) {
				if (!marked[w]) {
					marked[w] = true;
					q.enqueue(w);
	                }
	            }
	        }
	    }


	// the set of nouns (no duplicates), returned as an Iterable
	public Iterable<String> nouns() {
		return new HashSet<String>(synsets.keySet());
	}

	// is the word a WordNet noun?
	public boolean isNoun(String word) {
		return synsets.keySet().contains(word);
	}

	// distance between nounA and nounB (defined below)
	public int distance(String nounA, String nounB) {
		if (!isNoun(nounA) || !isNoun(nounB))
			throw new java.lang.IllegalArgumentException("Invalid noun/s");
		
		HashSet <Integer> vset = synsets.get(nounA);
		HashSet <Integer> wset = synsets.get(nounB);
		
		return sap.length(vset, wset);
	}

	// a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
	// in a shortest ancestral path (defined below)
	public String sap(String nounA, String nounB) {
		if (!isNoun(nounA) || !isNoun(nounB))
			throw new java.lang.IllegalArgumentException("Invalid noun/s");

		HashSet <Integer> vset = synsets.get(nounA);
		HashSet <Integer> wset = synsets.get(nounB);
		
		return nounsets.get(sap.ancestor(vset, wset));
	}

	// for unit testing of this class
	public static void main(String[] args) {
/*		//WordNet wn = new WordNet("synsets.txt", "hypernyms.txt");
		WordNet wn = new WordNet("synsets.txt", "digraph1.txt");
		
		String v = "Ambrose";
		String w = "St._Ambrose";
		
		System.out.println("ancestor of " + v + " -> " + w + " is (" + wn.sap(v, w) + ")");
//		System.out.println("sap of " + v + " -> " + w + " is (" + wn.distance(v, w) + ")");

		
		/*
		System.out.println(wn.isNoun("word"));
		for (String s : wn.nouns())
			System.out.println(s);
		
		int counter = 0;
		for (String value : wn.nouns()) {
			System.out.println(counter + " " + value);
			counter++;
		}
		*/
		/*HashMap <String, HashSet> synsets = new HashMap <String, HashSet>();
		int synsCounter = 0;
		In inSyn = new In("synsets.txt");
		
		while(!inSyn.isEmpty()) {
			String[] inputLine = inSyn.readLine().split(",");
			String[] nouns = inputLine[1].split(" ");
			
			for (String noun : nouns) {
				//System.out.println(noun);
				if (synsets.containsKey(noun)) {
					synsets.get(noun).add(Integer.parseInt(inputLine[0]));
					
				} else {
					Set <Integer> set = new HashSet <Integer>();
					set.add(Integer.parseInt(inputLine[0]));
					synsets.put(noun, (HashSet)set);
				}
			}
			synsCounter++;
		}
		
		String str1 = "Ambrose";
		if (synsets.containsKey(str1)) {
			System.out.println(str1);
			HashSet <Integer> id = synsets.get(str1);
			for (int i : id)
				System.out.println(i);
		}
		String str2 = "Saint_Ambrose";
		if (synsets.containsKey(str2)) {
			System.out.println(str2);
			HashSet <Integer> id = synsets.get(str2);
			for (int i : id)
				System.out.println(i);
		}
		String str3 = "St._Ambrose";
		if (synsets.containsKey(str3)) {
			System.out.println(str3);
			HashSet <Integer> id = synsets.get(str3);
			for (int i : id)
				System.out.println(i);
		}*/
				
	}
	
	
}
