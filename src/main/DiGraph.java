package main;

import edu.princeton.cs.algs4.Bag;
import java.util.HashSet;
import java.util.Set;

/**
 * A directed graph implementation for representing WordNet relationships.
 * Supports edge addition and path finding operations.
 */
public class DiGraph {
    private final Bag<Integer>[] adjacencyLists;
    private final int vertexCount;

    /**
     * Constructs a new directed graph with the specified number of vertices.
     * 
     * @param vertexCount the number of vertices in the graph
     * @throws IllegalArgumentException if vertexCount is negative
     */

    public DiGraph(int vertexCount) {
        if (vertexCount < 0) {
            throw new IllegalArgumentException("Vertex count cannot be negative: " + vertexCount);
        }
        
        this.vertexCount = vertexCount;
        this.adjacencyLists = (Bag<Integer>[]) new Bag[vertexCount];
        
        // Initialize adjacency lists
        for (int vertex = 0; vertex < vertexCount; vertex++) {
            adjacencyLists[vertex] = new Bag<>();
        }
    }

    /**
     * Adds a directed edge from vertex v to vertex w.
     * 
     * @param v the source vertex
     * @param w the target vertex
     */
    public void addEdge(int v, int w) {
        adjacencyLists[v].add(w);
    }

    /**
     * Returns an iterable of vertices adjacent to the specified vertex.
     * 
     * @param vertex the vertex to get adjacent vertices for
     * @return an iterable of adjacent vertices
     */
    public Iterable<Integer> getAdjacentVertices(int vertex) {
        return adjacencyLists[vertex];
    }

    /**
     * Returns all vertices reachable from the specified vertex.
     * 
     * @param sourceVertex the starting vertex
     * @return a set of all reachable vertices
     */
    public Set<Integer> getReachableVertices(int sourceVertex) {
        Set<Integer> reachableVertices = new HashSet<>();
        traverseGraph(reachableVertices, sourceVertex);
        return reachableVertices;
    }

    /**
     * Performs depth-first traversal to find all reachable vertices.
     * 
     * @param visitedVertices set to track visited vertices
     * @param currentVertex the current vertex being visited
     */
    private void traverseGraph(Set<Integer> visitedVertices, int currentVertex) {
        visitedVertices.add(currentVertex);
        for (int adjacentVertex : adjacencyLists[currentVertex]) {
            if (!visitedVertices.contains(adjacentVertex)) {
                traverseGraph(visitedVertices, adjacentVertex);
            }
        }
    }

    /**
     * Returns the number of vertices in this graph.
     * 
     * @return the vertex count
     */
    public int getVertexCount() {
        return vertexCount;
    }
}
