Algorithm
=========

Computer Science Algorithm Projects

Baseball Mathematical Elimination 
--------
using (Max Flow/Min Cut/Flow Network)

http://coursera.cs.princeton.edu/algs4/assignments/baseball.html

Construct: A Flow Network is constructed using number of remaining games as flow edges from the source. The capacity of flow edges to the sink is constructed using the maximum number of games team under investigation (team UI) can possibly win against team n. That maximum number of wins is restricted by the number of wins team n already has. If team n's flow edge to the sink is saturated (occupying 100% flow capacity,) it means team UI will not achieve as many wins as team i; therefore, team UI is mathematically eliminated by team n.
	
*Optimization: FordFulkerson algorithm is used to calculate flow within the network. It does so by looping and updating network with flow information until flow from source = flow to sink. To shorten execution time, number of looping and updating have to be minimized. This can be achieved by integrating FF algorithm into the code to allow continuous update to flow network, rearranging the team by the number of games won + remaining games in ascending order, and adding additional flow edges as teams down the list have greater number of games won + remaining games. By doing so, flow in network can be continuously updated rather than starting from scratch each time a different team is investigated.

*yet to be complete, but the current passes project spec 100%.

WordNet
-------
using BFS and Digraph to implement shortest path

http://coursera.cs.princeton.edu/algs4/assignments/wordnet.html

Construct: A wordnet group words into sets of synonyms called synsets. One such synset relationship is hyponym. The purpose of this program is the calculate the distance to a common ancestor between two words in wordnet. The algorithm I employed for such calculation is BFS, coupled with digraph datastructure. For optimiziation, I used two BFS, first BFS tracks one wordnet word back to the root, and second BFS tracks the other wordnet word back to the root but it stops if it encounters the path of the first BFS. When the second BFS detects the the path of the first BFS, it will return the distance it had traveled to reach that node as well as the node itself (the node becomes the common ancestor of the two words.)


SeamCarver
----------
using acyclic digraph and shortest path to remove the seam with lowest sum of color energy.

http://coursera.cs.princeton.edu/algs4/assignments/seamCarving.html

Construct: Seam-carving is a content-aware image resizing technique where the image is reduced in size by one pixel of height (or width) at a time. Optimization is done by identifying optimum energy calculation logic to avoid duplicate energy calculation, using lowest level RGB color datastructure, caching the coordinates of the seam that has been removed, and recalculating only the energy level around the seam that has been removed instead of recalculating energy level for the entire picture.


Boggle
-----
using recursive DFS and ternary search trie

http://coursera.cs.princeton.edu/algs4/assignments/boggle.html

Construct: Using DFS to explore every possible combination of word spelling on the Boggle board. Optimization involves checking the trie for the next possible character in the word's spelling - if no such word spelling existing, DFS for that particular path ends.


Burrows-Wheeler 
===============
using Most-Significant-Digit-first string (radix) sort, key index counting algorithm, Move-to-front encoding, Circular suffix array, and Burrows-Wheeler transform

http://coursera.cs.princeton.edu/algs4/assignments/burrows.html

This revolutionary algorithm outcompresses gzip and PKZIP.

Construct: 

Move-to-front encoding simply reorder the currently accessed ASCII character from its original ASCII order to the beginning of the ASCII list (index 0 position.) Decoding utilize the same logic to extract the original message.

Circular suffix function encode using MSD for the first 2~3 characters of every words in the entire word list (may contain thousands of words,) then switches to quicksort to sort the remaining words, when most of the words in the list has been sorted, the algorithm switches to insertion sort to sort the smaller list.

Burrows-Wheeler encoding is a wrapper over circular suffix function. The decoding is a simple key index counting algorithm.

Optimization: In circular suffix function, instead of storing rotated string in an array of string, a simple numeric array that tracks how the string is rotated is used. This eliminates writing and rewriting of array of string to simple add and subtract of numeric value inside 1d array. The switching point between MSD/quicksort/insertion sort also greatly impacts the performance, and this is done through iterative data collection and testing.


Note
----
The following projects uses library file algs4.jar and stdlib.jar. These library files are part of Coursera coursework created by Kevin Wayne and Robert Sedgewick.
