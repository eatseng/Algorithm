//Edward Tseng
//11/30/13

import java.util.HashSet;
import java.util.Set;

public class BoggleSolver {
    private Node root;   // root of TST

    private static class Node {
        private String word;
        private Node[] next = new Node[26];
    }

	
	public static void main(String args[])
	{
		In in = new In(args[0]);
		//In in = new In("dictionary-yawl.txt");
		//In in = new In("dictionary-algs4.txt");
	    String[] dictionary = in.readAllStrings();
		BoggleSolver bs = new BoggleSolver(dictionary);

	    BoggleBoard board = new BoggleBoard(args[1]);
		//BoggleBoard board = new BoggleBoard("board-q.txt");
	    int score = 0;
	    for (String word : bs.getAllValidWords(board))
	    {
	        StdOut.println(word);
	        score += bs.scoreOf(word);
	    }
	    StdOut.println("Score = " + score);
	}
	
	// Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
    	//puts dictionary words into Tries
    	for (int i = 0; i < dictionary.length; i++)
    		put(dictionary[i], dictionary[i]);
    }
	
    
 // Returns all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
    	Node next_n;
    	int b_row = board.rows(), b_col = board.cols();
    	Set<String> valid_words = new HashSet<String>(3*b_row*b_col);
    	boolean[][] visited = new boolean[b_row][b_col];

    	//separating "Qu" case from normal letters case
    	for (int i = 0; i < b_row; i++)
    		for (int j = 0; j < b_col; j++) {
    			if (board.getLetter(i, j) == 'Q') {
    				next_n = get(root, 'Q', 0);
    				next_n = get(next_n, 'U', 0);
    			} else
    				next_n = get(root, board.getLetter(i, j), 0);

				if (next_n != null)
					dfs(board, visited, valid_words, i, j, next_n);
    		}

    	return valid_words;
    }
    
    private void dfs(BoggleBoard board, boolean[][] visited, Set<String> valid_words, int i, int j, Node x) {
    	Node next_n;
    	int b_row = board.rows(), b_col = board.cols();

    	visited[i][j] = true;
    	
    	//dfs into all possible neighbor/combination of directions
    	for(int adj_row = i - 1; adj_row <= i + 1; adj_row++)
    		for(int adj_col = j - 1; adj_col <= j + 1; adj_col++) {
    			if (adj_row == i && adj_col == j)
    				continue;
    			if (adj_row < b_row && adj_col < b_col && adj_row >= 0 && adj_col >= 0) {
		    		//if current letter has not been used yet
		    		if (!visited[adj_row][adj_col]) {
		    			if (board.getLetter(adj_row, adj_col) == 'Q') {
		    				next_n = get(x, 'Q', 0);
		    				next_n = get(next_n, 'U', 0);
		    			} else
		    				next_n = get(x, board.getLetter(adj_row, adj_col), 0);
		    			
		    			//proceed if current substring combination can form a valid word    			
		    			if (next_n != null)
		    				dfs(board, visited, valid_words, adj_row, adj_col, next_n);
		    		}
    			}
    		}
    	
    	//reset visited matrix for other possible combination of words in other path
    	visited[i][j] = false;
    	
    	if (x.word != null)
    		if (x.word.length() > 2)
    			valid_words.add(x.word);
    }
    
    //Returns the score of the given (not necessarily valid) word.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
    	if (word.length() >= 3 && get(word) != null)
	    	switch (word.length()) {
		    	case 3: return 1;
		    	case 4: return 1;
		    	case 5: return 2;
		    	case 6:	return 3;
		    	case 7:	return 5;
		    	default: return 11;
	    	}
    	else 
    		return 0;
    }
    
    
    /*************************************************************************
	 *  Using library code from algs4.jar
     *  Compilation:  javac TrieST.java
     *  Execution:    java TrieST < words.txt
     *  Dependencies: StdIn.java
     *
     *  A string symbol table for ASCII strings, implemented using a 256-way trie.
     *
     *************************************************************************/

    private String get(String key) {
        Node x = get(root, key, 0);
        if (x == null) return null;
        return x.word;
    }

    private Node get(Node x, String key, int d) {
        if (x == null) return null;
        if (d == key.length()) return x;
        char c = key.charAt(d);
        return get(x.next[c-65], key, d+1);
    }
    
    private Node get(Node x, char key, int d) {
        if (x == null) return null;
        if (d == 1) return x;
        return get(x.next[key-65], key, d+1);
    }
    
    private void put(String key, String word) {
        root = put(root, key, word, 0);
    }

    private Node put(Node x, String key, String word, int d) {
        if (x == null) x = new Node();
        if (d == key.length()) {
            x.word = word;
            return x;
        }
        char c = key.charAt(d);
        x.next[c-65] = put(x.next[c-65], key, word, d+1);
        return x;
    }
}
