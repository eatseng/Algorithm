//Edward Tseng
//12/21/13

public class MoveToFront {
	
	private static char[] get_asc_array() {
		char[] array = new char[256];
	    	for (int i = 0; i < 256; i++) {
	    		array[i] = (char) i;
	    	}
	    	return array;
	}
	
	public static void encode() {
		char[] array = get_asc_array();
		
		while(!BinaryStdIn.isEmpty()) {
	    	char c = BinaryStdIn.readChar();
	    	for (int k = 0; k < array.length; k++)
	    		if (array[k] == c) {
	    			System.arraycopy(array, 0, array, 1, k);
	    			array[0] = c;
	    			BinaryStdOut.write((char) k);
	    		}
	    }
		BinaryStdOut.close();
	}
	
    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {
    	char[] array = get_asc_array();
    	int asc;
    	char temp;
    	    	
    	while (!BinaryStdIn.isEmpty()) {
        	asc = (int) BinaryStdIn.readChar();
        	temp = array[asc];
        	BinaryStdOut.write(temp);
			if (asc != 0) {
				System.arraycopy(array, 0, array, 1, asc);
				array[0] = temp;
			}
			BinaryStdOut.flush();
        }
     }


    // if args[0] is '-', apply move-to-front encoding
    // if args[0] is '+', apply move-to-front decoding
    public static void main(String[] args) {
    	int[] a = {1, 2,3, 4, 5};
    	
    	for (int i = 1; i < a.length; i++)
    		a[i] = a[i-1];
    	for (int i = 0; i < a.length; i++)
    		System.out.println(a[i]);
    		
    	if (args[0].equals("-")) 
    		encode();
    	else if (args[0].equals("+"))
    		decode();
    		
    }
}
