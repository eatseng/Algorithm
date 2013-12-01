//Edward Tseng
//11/16/13

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class BaseballElimination {
	private int[] w, l, r;
	private int[][] g;
	private int num_teams;
	private String[] teams;
	private String prev_team;
	private Set<String> certification;
	private boolean peliminated;
	
	public static void main(String[] args) {
		Stopwatch sw = new Stopwatch();
		
		//BaseballElimination division = new BaseballElimination("teams4.txt");
		BaseballElimination division = new BaseballElimination(args[0]);
		for (String team : division.teams()) {
	        if (division.isEliminated(team)) {
	            StdOut.print(team + " is eliminated by the subset R = { ");
	            for (String t : division.certificateOfElimination(team))
	                StdOut.print(t + " ");
	            StdOut.println("}");
	        }
	        else {
	            StdOut.println(team + " is not eliminated");
	        }
	    }
		
		System.out.println("Resizing time: " + sw.elapsedTime() + " seconds.");
	}
	
	public BaseballElimination(String filename) {
		// create a baseball division from given filename in format specified below
		In inputData = new In(filename);

		this.num_teams = Integer.parseInt(inputData.readLine());
		this.w = new int[num_teams];
		this.l = new int[num_teams];
		this.r = new int[num_teams];
		this.teams = new String[num_teams];
		this.g = new int[num_teams][num_teams];
		
		for(int i = 0; i < num_teams; i++) {
			String temp = inputData.readLine();
			for(int l = 0; l < temp.length(); l++)
				if (temp.charAt(l) != ' ') {
					temp = temp.substring(l);
					break;
				}
			String[] teamData = temp.split(" +");
			this.teams[i] = teamData[0];
			this.w[i] = Integer.parseInt(teamData[1]);
			this.l[i] = Integer.parseInt(teamData[2]);
			this.r[i] = Integer.parseInt(teamData[3]);
			for (int j = 0; j < num_teams; j++) {
				this.g[i][j] = Integer.parseInt(teamData[j + 4]);
			}
		}
	}
	
	public int numberOfTeams() {
		// number of teams
		return num_teams;
	}
	
	public Iterable<String> teams() {
		// all teams
		return Arrays.asList(teams);
	}
	
	public int wins(String team) {
		// number of wins for given team
		for (int i = 0; i < numberOfTeams(); i++)
			if (teams[i].equals(team))
				return w[i];
		throw new java.lang.IllegalArgumentException("No such team");
	}
	
	public int losses(String team) {
		// number of losses for given team
		for (int i = 0; i < numberOfTeams(); i++)
			if (teams[i].equals(team))
				return l[i];
		throw new java.lang.IllegalArgumentException("No such team");
	}
	
	public int remaining(String team) {
		// number of remaining games for given team
		for (int i = 0; i < numberOfTeams(); i++)
			if (teams[i].equals(team))
				return r[i];
		throw new java.lang.IllegalArgumentException("No such team");
	}
	
	public int against(String team1, String team2) {
		// number of remaining games between team1 and team2
		int x = -1, y = -1;
		for (int i = 0; i < numberOfTeams(); i++)
			if (teams[i].equals(team1)) {
				x = i;
				break;
			}
		for (int i = 0; i < numberOfTeams(); i++)
			if (teams[i].equals(team2)) {
				y = i;
				break;
			}
		if (y < 0 || x < 0)
			throw new java.lang.IllegalArgumentException("No such team");
		return g[x][y];
	}
	
	public boolean isEliminated(String team) {
		// returned buffered results since isEliminated and certificateOfElimination share
		//identical code
		if (team.equals(prev_team))
			return peliminated; 
	
		//get current team index and store it in x
		int x = -1;
		for (int i = 0; i < numberOfTeams(); i++)
			if (teams[i].equals(team))
				x = i;
		
		if (x == -1)
			throw new java.lang.IllegalArgumentException("No such team");

		prev_team = team;
		this.certification = new HashSet<String>();
		peliminated = false;
		
		//determine if current team is trivially eliminated
		for (String t : teams)
			if (w[x] + r[x] < wins(t)) {
				certification.add(t);
				peliminated = true;
			}
	
		if (peliminated)			
			return true;
		
		//if a team is not trivially eliminated proceed to setup flow network
		int v = 2 + num_teams;		//track number of vertices
		for(int i = 0; i < num_teams; i++) 
			for (int j = i + 1; j < num_teams; j++)
				if (g[i][j] != 0)
					v++;
		
		FlowNetwork fn = new FlowNetwork(v);
		
		//connect start to games, and games to teams
		int k = 0;
		for(int i = 0; i < num_teams; i++) 
			for (int j = i + 1; j < num_teams; j++)
				if (g[i][j] != 0) {
					fn.addEdge(new FlowEdge(num_teams, num_teams + k + 2, g[i][j]) );
					fn.addEdge(new FlowEdge(num_teams + k + 2, i, Double.POSITIVE_INFINITY));
					fn.addEdge(new FlowEdge(num_teams + k + 2, j, Double.POSITIVE_INFINITY));
					k++;
				}
		
		//connect teams to sink
		for(int i = 0; i < num_teams; i++) 
			fn.addEdge(new FlowEdge(i, num_teams + 1, w[x] + r[x] - w[i]));

		//source at index number = num_teams and sink at index number = num_team + 1 
		FordFulkerson ff = new FordFulkerson(fn, num_teams, num_teams + 1);
	
		//check if any team node is saturated, if it is, add team to certification
		k = 0;
		for(int i = 0; i < num_teams; i++)
			if (i != x && ff.inCut(i)) {
				certification.add(teams[i]);
				k++;
			}
		
		if (k != 0) {
			peliminated = true;
			return true;
		}

		return false;
	}
	
	public Iterable<String> certificateOfElimination(String team) {
		//see comments for isEliminated
		if (team.equals(prev_team))
			if(certification.isEmpty())
				return null;
			else
				return certification; 
		
		int x = -1;
		for (int i = 0; i < numberOfTeams(); i++)
			if (teams[i].equals(team))
				x = i;
		
		if (x == -1)
			throw new java.lang.IllegalArgumentException("No such team");

		this.certification = new HashSet<String>();
		prev_team = team;
		peliminated = false;
		
		for (String t : teams)
			if (w[x] + r[x] < wins(t)) {
				certification.add(t);
				peliminated = true;
			}
	
		if (peliminated)			
			return certification;
		
		
		int v = 2 + num_teams;		//track number of vertices
		for(int i = 0; i < num_teams; i++) 
			for (int j = i + 1; j < num_teams; j++)
				if (g[i][j] != 0)
					v++;
		
		FlowNetwork fn = new FlowNetwork(v);
		
		//connect source to games, and games to teams
		int k = 0;
		for(int i = 0; i < num_teams; i++) 
			for (int j = i + 1; j < num_teams; j++)
				if (g[i][j] != 0) {
					fn.addEdge(new FlowEdge(num_teams, num_teams + k + 2, g[i][j]));
					fn.addEdge(new FlowEdge(num_teams + k + 2, i, Double.POSITIVE_INFINITY));
					fn.addEdge(new FlowEdge(num_teams + k + 2, j, Double.POSITIVE_INFINITY));
					k++;
				}
		
		//connect teams to sink
		for(int i = 0; i < num_teams; i++) 
			fn.addEdge(new FlowEdge(i, num_teams + 1, w[x] + r[x] - w[i]));
			
		FordFulkerson ff = new FordFulkerson(fn, num_teams, num_teams + 1);
	
		k = 0;
		for(int i = 0; i < num_teams; i++)
			if (i != x && ff.inCut(i)) {
				certification.add(teams[i]);
				k++;
			}
		
		if (k != 0) {
			peliminated = true;
			return certification;
		}
		
		// subset R of teams that eliminates given team; null if not eliminated
		return null;
	}
}
