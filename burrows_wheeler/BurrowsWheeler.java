//Edward Tseng
//12/22/13

import java.util.Arrays;

public class BurrowsWheeler {
	
	// apply Burrows-Wheeler encoding, reading from standard input and writing to standard output
    public static void encode() {
    	while (!BinaryStdIn.isEmpty()) { 
	       	String st = BinaryStdIn.readString();
    		//String st = "ABRACADABRA!";
	       	CircularSuffixArray csa = new CircularSuffixArray(st);
	      
	        //output index of first
	        for (int i = 0; i < csa.length(); i++) 
	         	if (csa.index(i) == 0) {
	          		BinaryStdOut.write(i);
	           		break;
	           	}
	            
	        for (int i = 0; i < csa.length(); i++)
	           	BinaryStdOut.write(suffixfn(st, csa.index(i), csa.length()));
	        
	        BinaryStdOut.close();
    	}
    }
    
    private static char suffixfn(String s, int shift, int n) {
    	if (shift == 0)
    		return s.charAt(n - 1);
    	else
    		return s.charAt(shift - 1);
    }
    
    // apply Burrows-Wheeler decoding, reading from standard input and writing to standard output
    public static void decode() {
	int first = BinaryStdIn.readInt();

	while (!BinaryStdIn.isEmpty()) {
		String s = BinaryStdIn.readString();
		int N = s.length();
		int[] next = new int[N];
		
		int[] count = new int[256+1];
		for (int d = N - 1; d >= 0; d--)
			count[s.charAt(d) + 1]++;
			
		for (int r = 0; r < 256; r++)
			count[r + 1] += count[r];
			
		for (int i = 0; i < N; i++) {
			next[count[s.charAt(i)]++] = i;
		}
		
		int index = next[first];
		for (int i = 0; i < N; i++) {
			BinaryStdOut.write(s.charAt(index));
			index = next[index];
        }
		BinaryStdOut.close();
	}
}

/*    public static void decode() {
    	int first = BinaryStdIn.readInt();

    	while (!BinaryStdIn.isEmpty()) {
    		char[] s = BinaryStdIn.readString().toCharArray();
    		int n = s.length;
    		int[] next = new int[n];
    		char[] f = new char[n];
            for (int i = 0; i < n; i++) {
            	next[i] = i;
            	f[i] = s[i];
            }
    		
    		mergeSort(f, next);
    		int index = next[first];
    		for (int i = 0; i < n; i++) {
    			BinaryStdOut.write(s[index]);
    			index = next[index];
            }
    		BinaryStdOut.close();
    	}
    }*/

    // if args[0] is '-', apply Burrows-Wheeler encoding
    // if args[0] is '+', apply Burrows-Wheeler decoding
    public static void main(String[] args) {
    	if (args[0].equals("-")) 
    		encode();
    	else if (args[0].equals("+"))
    		decode();
    }
}