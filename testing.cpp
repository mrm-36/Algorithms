/* 
2-SAT, 401, 491, (Bellman Ford, Dijkstra, DFS, SCC, Bridges)

A bridge is an edge in a graph, such that when you remove it, the graph becomes disconnected
Theorem: In a tree, all edges are bridges
Do you know how to get the longest path in an unweighted graph?
easy
BFS from a vertex
the last vertex you reach, do another BFS from it
the last vertex you reach,
the distance between it and the second vertex you BFSed from, is the longest path
so just 2 BFSes
Do you know how to get the longest path in a DAG? Easy Dp[
Do you know how to find the shortest path in a graph with positive weights? Dijkstra
Positive + Negative? Bellman-Ford

How to detect negative-weight cycles? Bellman-Ford, but then run one more relaxation round. If something decreases => there is a negative weight cycle

Do you know how to find bridges? There is an easy DFS that does it, and it uses start time and finishing time
Theorem: 2-colorable Graph = Bipartite -- easy to check, just color vertex yellow, DFS, and color things opposite to parents. If you reach contradiction => NOT 2-colorable and NOT bipartite. Otherwise, bipartite

How to topologically sort?
Easy, just throw all vertices with in-degree 0 in a queue
and go over that queue
each time you remove a vertex, decrease in-degree of vertices it goes to
when in-degree of something decreases to 0, add it to the queue
you will get a topological sorting when this is over IF AND ONLY IF the graph has no directed cycles
it's an easy way to check for cycles as well
another way to check for cycles is via DFS
DFS with the parent you came from, as you know

*/