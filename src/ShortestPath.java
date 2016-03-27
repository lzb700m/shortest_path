import graph.Edge;
import graph.Graph;
import graph.Vertex;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import pq.IndexedHeap;

public class ShortestPath {
	public static void sp_bfs(Graph g, Vertex s) {
		int edgeWeight = Integer.MIN_VALUE;
		boolean initialized = false;
		for (Vertex v : g) {
			for (Edge e : v.Adj) {
				if (edgeWeight == Integer.MIN_VALUE) {
					edgeWeight = e.Weight;
					initialized = true;
				}
				if (initialized) {
					break;
				}
			}
			if (initialized) {
				break;
			}
		}

		initGraph(g);
		Queue<Vertex> visited = new LinkedList<Vertex>();
		visited.offer(s);
		s.distance = 0;
		s.seen = true;

		while (!visited.isEmpty()) {
			Vertex u = visited.poll();
			for (Edge e : u.Adj) {
				Vertex v = e.otherEnd(u);
				if (!v.seen) {
					v.parent = u;
					v.distance = u.distance + edgeWeight;
					v.seen = true;
					visited.offer(v);
				}
			}
		}
	}

	public static void sp_dag(Graph g, Vertex s) {
		List<Vertex> topOrder = topSort(g);
		initGraph(g);
		s.distance = 0;

		for (Vertex u : topOrder) {
			for (Edge e : u.Adj) {
				Vertex v = e.otherEnd(u);
				relax(u, v, e);
			}
		}
	}

	public static void sp_dijkstra(Graph g, Vertex s) {
		initGraph(g);
		s.distance = 0;
		Vertex comp = new Vertex(0);
		IndexedHeap<Vertex> pq = new IndexedHeap<Vertex>(g.numNodes, comp);
		for (Vertex v : g) {
			pq.insert(v);
		}

		while (!pq.isEmpty()) {
			Vertex u = pq.deleteMin();
			u.seen = true;
			for (Edge e : u.Adj) {
				Vertex v = e.otherEnd(u);
				if (!v.seen) {
					if (relax(u, v, e)) {
						pq.decreaseKey(v);
					}
				}
			}
		}
	}

	public static boolean sp_bf(Graph g, Vertex s) {
		initGraph(g);
		s.distance = 0;
		s.seen = true;
		Queue<Vertex> queue = new LinkedList<Vertex>();
		queue.offer(s);

		while (!queue.isEmpty()) {
			Vertex u = queue.poll();
			u.seen = false;
			u.count++;
			if (u.count >= g.numNodes) {
				return false; // negative cycle
			}

			for (Edge e : u.Adj) {
				Vertex v = e.otherEnd(u);
				if (relax(u, v, e)) {
					if (!v.seen) {
						queue.offer(v);
						v.seen = true;
					}
				}
			}
		}
		return true;
	}

	public static int sp_categorizer(Graph g) {
		if (hasUniformEdgeWeight(g)) {
			return DEF.UNIFORM_WEIGHT;
		} else if (!hasCycle(g)) {
			return DEF.DAG;
		} else if (!hasNegEdgeWeight(g)) {
			return DEF.NON_NEG_WEIGHT;
		} else {
			return DEF.OTHER;
		}
	}

	private static boolean hasUniformEdgeWeight(Graph g) {
		int uniformWeight = Integer.MIN_VALUE;
		boolean initialized = false;

		for (Vertex v : g) {
			for (Edge e : v.Adj) {
				uniformWeight = (uniformWeight == Integer.MIN_VALUE) ? e.Weight
						: uniformWeight;
				if (uniformWeight != Integer.MIN_VALUE) {
					initialized = true;
					break;
				}
			}
			if (initialized) {
				break;
			}
		}

		if (uniformWeight <= 0) {
			return false;
		}

		for (Vertex v : g) {
			for (Edge e : v.Adj) {
				if (e.Weight != uniformWeight) {
					return false;
				}
			}
		}
		return true;
	}

	private static boolean hasCycle(Graph g) {
		return topSort(g) == null;
	}

	private static List<Vertex> topSort(Graph g) {
		int[] unvisitedEdge = new int[g.numNodes + 1];
		Queue<Vertex> zeroDegreeVertices = new LinkedList<Vertex>();

		for (Vertex v : g) {
			int inDegree = v.revAdj.size();
			unvisitedEdge[v.name] = inDegree;
			if (inDegree == 0) {
				zeroDegreeVertices.offer(v);
			}
		}

		List<Vertex> ret = new LinkedList<Vertex>();
		while (!zeroDegreeVertices.isEmpty()) {
			Vertex u = zeroDegreeVertices.poll();
			ret.add(u);
			for (Edge e : u.Adj) {
				Vertex v = e.otherEnd(u);
				unvisitedEdge[v.name]--;
				if (unvisitedEdge[v.name] == 0) {
					zeroDegreeVertices.offer(v);
				}
			}
		}

		for (int i = 1; i < unvisitedEdge.length; i++) {
			if (unvisitedEdge[i] > 0) {
				return null;
			}
		}
		return ret;
	}

	private static boolean hasNegEdgeWeight(Graph g) {
		for (Vertex v : g) {
			for (Edge e : v.Adj) {
				if (e.Weight < 0) {
					return true;
				}
			}
		}
		return false;
	}

	private static void initGraph(Graph g) {
		for (Vertex v : g) {
			v.distance = Integer.MAX_VALUE;
			v.parent = null;
			v.seen = false;
			v.count = 0;
			for (Edge e : v.Adj) {
				e.seen = false;
			}
		}
	}

	private static boolean relax(Vertex u, Vertex v, Edge e) {
		if (u.distance != Integer.MAX_VALUE) {
			int temp = u.distance + e.Weight;
			if (v.distance > temp) {
				v.distance = temp;
				v.parent = u;
				return true;
			}
		}
		return false;
	}
}
