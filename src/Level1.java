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

		Vertex source = findSource(input);
		int graphType = ShortestPath.sp_categorizer(input);
		String algoType = "";

		switch (graphType) {
		case DEF.UNIFORM_WEIGHT:
			algoType = "BFS";
			ShortestPath.sp_bfs(input, source);
			break;

		case DEF.DAG:
			algoType = "DAG";
			ShortestPath.sp_dag(input, source);
			break;

		case DEF.NON_NEG_WEIGHT:
			ShortestPath.sp_dijkstra(input, source);
			algoType = "Dij";
			break;

		case DEF.OTHER:
			ShortestPath.sp_bf(input, source);
			algoType = "B-F";
			break;

		default:
			break;
		}
		printOutput(input, source, algoType);
	}

	private static Vertex findSource(Graph g) {
		Vertex ret = null;
		for (Vertex v : g) {
			if (v.name == DEF.SOURCE) {
				ret = v;
			}
		}
		return ret;
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
