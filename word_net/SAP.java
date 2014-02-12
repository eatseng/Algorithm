//Edward Tseng
//11/2/13

import java.util.*;

public class SAP {
	private static final int INFINITY = Integer.MAX_VALUE;
	private Digraph dg;
	private int[] distTo;      // distTo[v] = length of shortest s->v path
	private int[] vDistTo;
	private int shortest;
	private int ancestor;
	private int v_o;
	private Set<Integer> vmap;
	private Set<Integer> wmap;

	
	private SAP(String file, int n) {
		dg = new Digraph(n + 1);
		v_o = INFINITY;
		ancestor = -1;
	        distTo = new int[dg.V()];
	        vDistTo = new int[dg.V()];
	        vmap = new HashSet<Integer>();
	        wmap = new HashSet<Integer>();
	        
		In inSAP = new In(file);
		while(!inSAP.isEmpty()) {
			String[] inputLine = inSAP.readLine().split(",");
			for (int i = 1; i < inputLine.length; i++)
				dg.addEdge(Integer.parseInt(inputLine[0]), Integer.parseInt(inputLine[i]));
		}
		
		for (int v = 0; v < dg.V(); v++) {
	        	distTo[v] = INFINITY;
	        	vDistTo[v] = INFINITY;
	        }
	}
		
	// constructor takes a digraph (not necessarily a DAG)
	public SAP(Digraph G) {
		this.dg = new Digraph(G);
		v_o = INFINITY;
		ancestor = -1;
	        distTo = new int[dg.V()];
		vDistTo = new int[dg.V()];
	        vmap = new HashSet<Integer>();
	        wmap = new HashSet<Integer>();
		
		for (int v = 0; v < dg.V(); v++) {
	        	distTo[v] = INFINITY;
	        	vDistTo[v] = INFINITY;
	        }
	}
	
	private void vreset() {
		for (int i : vmap)
			distTo[i] = INFINITY;
		vmap = new HashSet<Integer>();
	}
	
	private void wreset() {
		for (int i : wmap)
			vDistTo[i] = INFINITY;
		wmap = new HashSet<Integer>();
	}
	
	// length of shortest ancestral path between v and w; -1 if no such path
	public int length(int v, int w) {
		if (v < 0 || v > (dg.V() - 1) || w < 0 || w > (dg.V() - 1))
			throw new java.lang.IndexOutOfBoundsException("V or W outside of vertex boundary/s");

		if (v != v_o) {
			vreset();
			bfs(dg, v);
			v_o = v;
		}
		ancestor = -1;
		shortest = INFINITY;
		wreset();
		vbfs(dg,w);
		if (ancestor == -1)
			return -1;
		return distTo[ancestor] + vDistTo[ancestor];
	}

	// a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
	public int ancestor(int v, int w) {
		if (v < 0 || v > (dg.V() - 1) || w < 0 || w > (dg.V() - 1))
			throw new java.lang.IndexOutOfBoundsException("V or W outside of vertex boundary/s");
		
		if (v != v_o) {
			vreset();
			bfs(dg, v);
			v_o = v;
		}
		ancestor = -1;
		shortest = INFINITY;
		wreset();
		vbfs(dg, w);

		return ancestor;
	}

	// length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
	public int length(Iterable<Integer> v, Iterable<Integer> w) {
		for (int i : v)
			if (i < 0 || i > (dg.V() - 1))
				throw new java.lang.IndexOutOfBoundsException("V or W outside of vertex boundary/s");
		for (int i : w)
			if (i < 0 || i > (dg.V() - 1))
				throw new java.lang.IndexOutOfBoundsException("V or W outside of vertex boundary/s");
		v_o = INFINITY;
		vreset();
		wreset();
		ancestor = -1;
		shortest = INFINITY;
		
		bfs(dg, v);
		vbfs(dg,w);
		if (ancestor == -1)
			return -1;
		return distTo[ancestor] + vDistTo[ancestor];
	}

	// a common ancestor that participates in shortest ancestral path; -1 if no such path
	public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
		for (int i : v)
			if (i < 0 || i > (dg.V() - 1))
				throw new java.lang.IndexOutOfBoundsException("V or W outside of vertex boundary/s");
		for (int i : w)
			if (i < 0 || i > (dg.V() - 1))
				throw new java.lang.IndexOutOfBoundsException("V or W outside of vertex boundary/s");
		v_o = INFINITY;
		vreset();
		wreset();
		ancestor = -1;
		shortest = INFINITY;
		bfs(dg, v);
		vbfs(dg, w);
		return ancestor;
	}
	
    // BFS from single source
    private void bfs(Digraph G, int s) {
        Queue<Integer> q = new Queue<Integer>();
        distTo[s] = 0;
        q.enqueue(s);
        vmap.add(s);
        while (!q.isEmpty()) {
            int v = q.dequeue();
            for (int w : G.adj(v)) {
            	if (distTo[w] == INFINITY) {
                    distTo[w] = distTo[v] + 1;
                    q.enqueue(w);
                    vmap.add(w);
                }
            }
        }
    }

    // BFS from multiple sources
    private void bfs(Digraph G, Iterable<Integer> sources) {
        Queue<Integer> q = new Queue<Integer>();
        for (int s : sources) {
            distTo[s] = 0;
            q.enqueue(s);
            vmap.add(s);
        }
        while (!q.isEmpty()) {
            int v = q.dequeue();
            for (int w : G.adj(v)) {
            	if (distTo[w] == INFINITY) {
                    distTo[w] = distTo[v] + 1;
                    q.enqueue(w);
                    vmap.add(w);
                }
            }
        }
    }

    // BFS from single source
    private void vbfs(Digraph G, int s) {
        int dist;
    	Queue<Integer> q = new Queue<Integer>();
        vDistTo[s] = 0;
        q.enqueue(s);
        wmap.add(s);
        if (distTo[s] != INFINITY) {
        	dist = distTo[s] + vDistTo[s];
	        if (dist < shortest) {
	        	ancestor = s;
	        	shortest = dist;
	        }
        }
        
        while (!q.isEmpty()) {
            int v = q.dequeue();
            for (int w : G.adj(v)) {
            	if (vDistTo[w] == INFINITY) {
                    vDistTo[w] = vDistTo[v] + 1;
                    q.enqueue(w);
                    wmap.add(w);
                }
            	if (distTo[w] != INFINITY) {
                	dist = distTo[w] + vDistTo[w];
	                if (dist < shortest) {
	                	ancestor = w;
	                	shortest = dist;
	                }
            	}
            }
        }
    }

    // BFS from multiple sources
    private void vbfs(Digraph G, Iterable<Integer> sources) {
        Queue<Integer> q = new Queue<Integer>();
        int dist = INFINITY;
        for (int s : sources) {
            vDistTo[s] = 0;
            q.enqueue(s);
            wmap.add(s);
            if (distTo[s] != INFINITY) {
	            dist = distTo[s] + vDistTo[s];
	            if (dist < shortest) {
	            	ancestor = s;
	            	shortest = dist;
	            }
            }
        }
        
        while (!q.isEmpty()) {
            int v = q.dequeue();
            //System.out.println("dequeue: " + v);
            for (int w : G.adj(v)) {
            	if (vDistTo[w] == INFINITY) {
                    vDistTo[w] = vDistTo[v] + 1;
                    //System.out.println("vDistTo: " + vDistTo[w]);
                    q.enqueue(w);
                    wmap.add(w);
                }
            	if (distTo[w] != INFINITY) {
            		//System.out.println("vDistTo: " + vDistTo[w]);
	                dist = distTo[w] + vDistTo[w];
	                if (dist < shortest) {
	                	ancestor = w;
	                	shortest = dist;
	                }
                }
            }
        }
    }


	// for unit testing of this class (such as the one below)
	public static void main(String[] args) {
/*	    In in = new In(args[0]);
	    Digraph G = new Digraph(in);
	    SAP sap = new SAP(G);
	    while (!StdIn.isEmpty()) {
	        int v = StdIn.readInt();
	        int w = StdIn.readInt();
	        int length   = sap.length(v, w);
	        int ancestor = sap.ancestor(v, w);
	        StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
	    }

		SAP s = new SAP("digraph2.txt", 100);
		int v = 1;
		int w = 5;
		//System.out.println(v + " -> " + w + "(" + s.length(v, w) + ")");
	
		System.out.println("ancestor of " + v + " -> " + w + " is (" + s.ancestor(v, w) + ")");
		System.out.println("sap of " + v + " -> " + w + " is (" + s.length(v, w) + ")");
		
		v = 2;
		w = 0;
		//System.out.println(v + " -> " + w + "(" + s.length(v, w) + ")");
	
		System.out.println("ancestor of " + v + " -> " + w + " is (" + s.ancestor(v, w) + ")");
		System.out.println("sap of " + v + " -> " + w + " is (" + s.length(v, w) + ")");
		
		

		SAP s = new SAP("digraph1.txt", 12);
		Set<Integer> vset = new HashSet<Integer>();
		//vset.add(11);
		vset.add(11);
		Set<Integer> wset = new HashSet<Integer>();
		wset.add(3);
		//wset.add(12);
		
		
		//for (int i : vset)
		//	System.out.println("vset: " + i);
		System.out.println("ancestor set is (" + s.ancestor(vset, wset) + ")");
		System.out.println("sap of set is (" + s.length(vset, wset) + ")");
		
	
		wset.add(3);
		wset.add(10);
		
		
		//for (int i : vset)
		//	System.out.println("vset: " + i);
		System.out.println("ancestor set is (" + s.ancestor(vset, wset) + ")");
		System.out.println("sap of set is (" + s.length(vset, wset) + ")");
*/
	}

}
