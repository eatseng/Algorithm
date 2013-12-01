Algorithm
=========

Computer Science Algorithm Projects

Baseball Mathematical Elimination (Max Flow/Min Cut/Flow Network)

	http://coursera.cs.princeton.edu/algs4/assignments/baseball.html
	
	Construct: A Flow Network is constructed using number of remaining games as flow edges from the source. The capacity of flow edges to the sink is constructed using the maximum number of games team under investigation (team UI) can possibly win against team n. That maximum number of wins is restricted by the number of wins team n already has. If team n's flow edge to the sink is saturated (occupying 100% flow capacity,) it means team UI will not achieve as many wins as team i; therefore, team UI is mathematically eliminated by team n.
	
	*Optimization: FordFulkerson algorithm is used to calculate flow within the network. It does so by looping and updating network with flow information until flow from source = flow to sink. To shorten execution time, number of looping and updating have to be minimized. This can be achieved by integrating FF algorithm into the code to allow continuous update to flow network, rearranging the team by the number of games won + remaining games in ascending order, and adding additional flow edges as teams down the list have greater number of games won + remaining games. By doing so, flow in network can be continuously updated rather than starting from scratch each time a different team is investigated.

	*yet to be complete, but the current passes project spec 100%.


The following projects uses library file algs4.jar and stdlib.jar. These library files are part of Coursera coursework created by Kevin Wayne and Robert Sedgewick.
