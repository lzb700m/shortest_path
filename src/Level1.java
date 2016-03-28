/**
 * Driver program for LP3 Level 1
 * 
 * @author Peng Li
 * @author Nan Zhang
 */
import graph.Graph;
import graph.Vertex;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Level1 {
	public static void main(String[] args) throws FileNotFoundException {
		// read in a directed graph
		Scanner in;
		if (args.length != 0) {
			in = new Scanner(new File(args[0]));
		} else {
			in = new Scanner(System.in);
		}
		Graph input = Graph.readGraph(in, true);
		in.close();

		// find the source vertex for shortest path calculation according to the
		// specification, source vertex is defined in DEF.java
		Vertex source = ShortestPath.findSource(input);
		// zeroCycleSensitive indicates if zero weight cycle will be treated as
		// illegal input. For Level 1, zero weight cycle in input graph is
		// allowed.
		boolean zeroCycleSensitive = false;
		String algoType = "";
		boolean hasNegCycle = false;

		int graphType = ShortestPath.sp_categorizer(input, zeroCycleSensitive);

		switch (graphType) {
		case DEF.UNIFORM_WEIGHT:
			algoType = "BFS";
			ShortestPath.sp_bfs(input, source);
			break;

		case DEF.DAG:
			algoType = "DAG";
			ShortestPath.sp_dag(input, source, zeroCycleSensitive);
			break;

		case DEF.NON_NEG_WEIGHT:
			algoType = "Dij";
			ShortestPath.sp_dijkstra(input, source, zeroCycleSensitive);
			break;

		case DEF.OTHER:
			algoType = "B-F";
			hasNegCycle = !ShortestPath
					.sp_bf(input, source, zeroCycleSensitive);
			break;

		default:
			break;
		}

		if (hasNegCycle) {
			System.out.println(DEF.ERROR_LEVEL1);
		} else {
			printOutput(input, source, algoType);
		}
	}

	private static void printOutput(Graph g, Vertex source, String algoType) {
		long spSum = 0;

		// calculate and output shorted path length sum of all vertices
		// reachable from source
		for (Vertex v : g) {
			if (v.distance != Integer.MAX_VALUE) {
				spSum += v.distance;
			}
		}
		System.out.println(algoType + " " + spSum);

		// output the shortest path for each vertex
		if (g.numNodes <= DEF.SIZE_CUT_OFF) {
			StringBuilder line = new StringBuilder();
			for (Vertex v : g) {
				line.setLength(0);
				line.append(v.name);
				line.append(" ");
				if (v.distance != Integer.MAX_VALUE) {
					line.append(v.distance);
				} else {
					line.append("INF");
				}
				line.append(" ");
				if (v.parent != null) {
					line.append(v.parent);
				} else {
					line.append("-");
				}
				System.out.println(line.toString());
			}
		}
	}
}