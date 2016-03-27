import graph.Edge;
import graph.Graph;
import graph.Vertex;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;

public class Level2 {
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
		boolean zeroCycleSensitive = true;
		boolean hasNegCycle = false;

		int graphType = ShortestPath.sp_categorizer(input, zeroCycleSensitive);

		switch (graphType) {
		case DEF.UNIFORM_WEIGHT:
			ShortestPath.sp_bfs(input, source);
			break;

		case DEF.DAG:
			ShortestPath.sp_dag(input, source, zeroCycleSensitive);
			break;

		case DEF.NON_NEG_WEIGHT:
			ShortestPath.sp_dijkstra(input, source, zeroCycleSensitive);
			break;

		case DEF.OTHER:
			hasNegCycle = !ShortestPath
					.sp_bf(input, source, zeroCycleSensitive);
			break;

		default:
			break;
		}

		if (hasNegCycle) {
			System.out.println(DEF.ERROR_LEVEL2);
			List<Edge> nonPosCycle = ShortestPath.findNonPosCycle(input);
			for (Edge e : nonPosCycle) {
				System.out.println(e);
			}
		} else {
			Graph d = ShortestPath.countSPPath(input);
			printOutput(d);
		}
	}

	private static void printOutput(Graph g) {
		int pathCountSum = 0;

		for (Vertex v : g) {
			pathCountSum += v.spCount;
		}
		System.out.println(pathCountSum);

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
				line.append(v.spCount);
				System.out.println(line.toString());
			}
		}
	}
}