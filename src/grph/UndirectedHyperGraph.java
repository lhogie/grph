package grph;

import toools.collections.primitive.LucIntSet;

public interface UndirectedHyperGraph extends UndirectedGraph
{
	int getNumberOfUndirectedHyperEdges();

	void addUndirectedHyperEdge(int e);

	boolean isUndirectedHyperEdge(int e);

	void addToUndirectedHyperEdge(int edge, int vertex);

	void removeFromHyperEdge(int e, int v);

	LucIntSet getUndirectedHyperEdgeVertices(int e);
}
