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

		Vertex source = ShortestPath.findSource(input);
		//
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

		for (Vertex v : g) {
			if (v.distance != Integer.MAX_VALUE) {
				spSum += v.distance;
			}
		}
		System.out.println(algoType + " " + spSum);

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