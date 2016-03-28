#Shortest Path
Contributor: Peng Li, Nan Zhang

##Summary
All requirements from Level 1 and Level 2 are implemented and tested.

In Level 1, given an input graph, the program finds the most efficient algorithm (BFS, Dijkstra's algorithm, DAG shortest paths, and Bellman-Ford algorithm) for finding the shortest path from the source node. If no shortest path can be found due to a negative cycle reachable from source node, the program indicates user such negative cycle exists.

In Level 2, given an input graph, the program finds the number of simple shortest path for each nodes reachable from source node. If a non-positive cycle exisits in input graph, the program finds a such cycle.


##Input output specification

###Level 1

A directed graph is given as input, in the format expected by readGraph method. If there is a command line argument, input is read from that file. Otherwise, read input from the console. Assume that the source is vertex 1.

In the first line of the output, print the name of the algorithm run (BFS, DAG, Dij, or B-F), the sum of shortest path lengths from s to every node of G that is reachable from s. If |V| is less than or equal to 100, in the next lines, output the lengths of shortest paths from s to each vertex u in the graph that is reachable from s, and the predecessor node of u in that shortest path. If there is no path from s to a vertex, print INF as the length. In the following example, node 8 is not reachable from s, and the output in line 1 is the sum of u.distance for all u for which u.distance != Infinity.

```
Sample input (level 1):
8 12
1 2 2
1 4 1
2 5 10
2 4 3
5 7 6
3 1 4
3 6 5
4 3 2
7 6 1
4 5 2
4 7 4
4 6 8

Output (level 1):
Dij 20
1 0 -
2 2 1
3 3 4
4 1 1
5 3 4
6 6 7
7 5 4
8 INF -
```


###Level 2
A directed graph is given as input, in the format expected by readGraph method. If there is a command line argument, input is read from that file. Otherwise, read input from the console. Assume that the source is vertex 1.

In the first line of the output, print the sum of number of shortest paths from s to every node of G. If |V| is less than or equal to 100, in the next lines, for each vertex u, output the length of a shortest path from s to u, and the number of shortest paths from s to u. If there is no path from s to a vertex, print INF as the length, and the number of paths as 0.

Note that in this example, there is a negative cycle, 6->7->6 of length -2. But, since there is no path from s to either of these nodes, and therefore, the algorithm can correctly output the answers. Also, there are 2 shortest paths from 1 to 5: 1->2->4->5, and 1->3->4->5. Both paths have length 8, but they are not disjoint.

```
Sample input (level 2):
7 8
1 2 2
1 3 3
2 4 5
3 4 4
4 5 1
5 1 -7
6 7 -1
7 6 -1

Output (level 2):
7
1 0 1
2 2 1
3 3 1
4 7 2
5 8 2
6 INF 0
7 INF 0
```

##Class description
```
./DEF.java					- Definition of global constant
./Level1.java				- Driver program for Level 1
./Level2.java				- Driver program for Level 2
./ShortestPath.java			- Implementation of all shortest path algorithms

./graph/Edge.java			- Edge class for graph representation
./graph/Graph.java			- Graph class for graph representation
./graph/Vertex.java			- Vertex class for graph representation

./pq/BinaryHeap.java		- Binary heap implementation
./pq/Index.java				- Index interface definition
./pq/IndexedHeap.java		- Indexed binary heap implementation
./pq/PQ.java				- Priority Queue interface definition
```

##How to run

To run Level 1:

```
$ javac Level1.java
$ java Level1 [input graph file]
```

To run Level 2:

```
$ javac Level2.java
$ java Level2 [input graph file]
```

##Running time analysis

* BFS for uniform positive weight edge graph: O(|E|)
* DAG shortest path algorithm: O(|E|)
* Dijkstra: O(|E| * log|V|), can be further improved to O(|E| + |V| * log|V|) if using a fibonacci heap
* Bellman-Form: O(|E| * |V|)






