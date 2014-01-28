//Edward Tseng
//11/30/13

import java.util.HashSet;
import java.util.Set;


public class BoggleSolverTST {
    private Node root;   // root of TST
    private int N;
    private int counter;

    private class Node {
        private char c;                 // character
        private Node left, mid, right;  // left, middle, and right subtries
        private boolean val;              // value associated with string
    }

	
	public static void main(String args[])
	{
		//In in = new In(args[0]);
		//In in = new In("dictionary-yawl.txt");
		In in = new In("dictionary-algs4.txt");
		//In in = new In("dict.txt");
	    String[] dictionary = in.readAllStrings();
		BoggleSolverTST bs = new BoggleSolverTST(dictionary);
		
		//for (String word : bs.keys())
			//System.out.println(word);
		
	    BoggleBoard board = new BoggleBoard("board-q.txt");
	    int score = 0;
	    for (String word : bs.getAllValidWords(board))
	    {
	        StdOut.println(word);
	        score += bs.scoreOf(word);
	    }
	    StdOut.println("Score = " + score);
	    
	    StdOut.println("counter = " + bs.counter);
	    
	    
	    //System.out.println("Score of " + bs.scoreOf("ABCD"));
	}
	
	// Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolverTST(String[] dictionary) {
    	//puts dictionary words into Tries
    	for (int i = 0; i < dictionary.length; i++)
    		put(dictionary[i], true);
    }
	
    
 // Returns all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
    	Set<String> valid_words = new HashSet<String>();
    	boolean[][] visited = new boolean[board.rows()][board.cols()];

    	//separating "Qu" from normal letters
    	for (int i = 0; i < board.rows(); i++)
    		for (int j = 0; j < board.cols(); j++)
    			if (board.getLetter(i, j) == 'Q') {
    				Node next_n = getNode(root, "QU", 0);
    				if (next_n != null)
    					dfs(board, visited, valid_words, "QU", i, j, next_n);
    			} else {
    				String letter = Character.toString(board.getLetter(i, j));
    				Node next_n = getNode(root, letter, 0);
    				if (next_n != null)
    					dfs(board, visited, valid_words, letter, i, j, next_n);
    			}	

    	return valid_words;
    }
    
    private Node getNode(Node x, String key, int d) {
    	if (x == null) return null;
    	counter++;
        char c = key.charAt(d);
        if      (c < x.c)              return getNode(x.left,  key, d);
        else if (c > x.c)              return getNode(x.right, key, d);
        else if (d < key.length() - 1) return getNode(x.mid,   key, d+1);
        else				           return x;
        
    }
    
    private void dfs(BoggleBoard board, boolean[][] visited, Set<String> valid_words, String substring, int i, int j, Node x) {
    	visited[i][j] = true;
    	String letter;
    	if (x == null) return;
    	//System.out.println(substring);
    	//System.out.println("x.c :" + x.c);
    	counter++;
    	//dfs into all possible neighbor/combination of directions
    	for(int[] adj_coord : adj(board, i, j)) {
    		int adj_row = adj_coord[0];
    		int adj_col = adj_coord[1];
    		//if current letter has not been used yet
    		if (!visited[adj_row][adj_col]) {
    			if (board.getLetter(adj_row, adj_col) == 'Q')
    				letter = "QU";
    			else
    				letter = Character.toString(board.getLetter(adj_row, adj_col));
    			//proceed if current substring combination can form a valid word    			
    			
    			Node next_n = getNode(x.mid, letter, 0);
    			if (next_n != null)
    				dfs(board, visited, valid_words, substring + letter, adj_row, adj_col, next_n);
    		}
    	}
    	//reset visited matrix for other possible combination of words in other path
    	visited[i][j] = false;
    	
    	if (substring.length() > 2)
    		if (x.val == true)
    			valid_words.add(substring);
    }
    
    private Iterable<int[]> adj(BoggleBoard board, int row, int col) {
    	//prints out valid adjacent coordinates on the board
    	Set<int[]> adj = new HashSet<int[]>();
    	
    	for(int i = row - 1; i <= row + 1; i++)
    		for(int j = col - 1; j <= col + 1; j++) {
    			if (i == row && j == col)
    				continue;
    			if (i < board.rows() && j < board.cols() && i >= 0 && j >= 0) {
    				int[] adj_coord = new int[2];
    				adj_coord[0] = i;
    				adj_coord[1] = j;
    				adj.add(adj_coord);
    			}
    		}
    	return adj;
    }
    
  
  
    
     //Returns the score of the given (not necessarily valid) word.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
    	if (word.length() >= 3 && get(word) != false)
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
    
    
    
    private boolean contains(String key) {
        return get(key);
    }
    
    private boolean get(String key) {
        if (key == null) throw new NullPointerException();
        if (key.length() == 0) throw new IllegalArgumentException("key must have length >= 1");
        Node x = get(root, key, 0);
        if (x == null) return false;
        return (boolean) x.val;
    }

    // return subtrie corresponding to given key
    private Node get(Node x, String key, int d) {
        if (key == null) throw new NullPointerException();
        if (key.length() == 0) throw new IllegalArgumentException("key must have length >= 1");
        if (x == null) return null;
        char c = key.charAt(d);
        if      (c < x.c)              return get(x.left,  key, d);
        else if (c > x.c)              return get(x.right, key, d);
        else if (d < key.length() - 1) return get(x.mid,   key, d+1);
        else                           return x;
    }
    
    private void put(String s, boolean val) {
        if (!contains(s)) N++;
        root = put(root, s, val, 0);
    }

    private Node put(Node x, String s, boolean val, int d) {
    	char c = s.charAt(d);
        if (x == null) {
            x = new Node();
            x.c = c;
            x.mid = null;
            x.val = false;
        }
        if      (c < x.c)             x.left  = put(x.left,  s, val, d);
        else if (c > x.c)             x.right = put(x.right, s, val, d);
        else if (d < s.length() - 1)  x.mid   = put(x.mid,   s, val, d+1);
        else                          x.val   = val;
        return x;
    }
    
    private boolean prefixMatch(String prefix) {
    	if (prefix == null) throw new NullPointerException();
        if (prefix.length() == 0) throw new IllegalArgumentException("key must have length >= 1");
        Node x = getPrefix(root, prefix, 0);
        if (x == null) return false;
        return true;
    }
    
    // return subtrie corresponding to given key
    private Node getPrefix(Node x, String key, int d) {
        if (key == null) throw new NullPointerException();
        if (key.length() == 0) throw new IllegalArgumentException("key must have length >= 1");
        if (x == null) return null;
        char c = key.charAt(d);
        if      (c < x.c)              return get(x.left,  key, d);
        else if (c > x.c)              return get(x.right, key, d);
        else if (d < key.length() - 1) return get(x.mid,   key, d+1);
        else                           return x;
    }
    
    // all keys in symbol table
    private Iterable<String> keys() {
        Queue<String> queue = new Queue<String>();
        collect(root, "", queue);
        return queue;
    }
    
    // all keys in subtrie rooted at x with given prefix
    private void collect(Node x, String prefix, Queue<String> queue) {
        if (x == null) return;
        collect(x.left,  prefix,       queue);
        if (x.val == true) queue.enqueue(prefix + x.c);
        collect(x.mid,   prefix + x.c, queue);
        collect(x.right, prefix,       queue);
    }


}
