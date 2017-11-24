package grph;

import toools.collections.primitive.LucIntSet;

public interface DirectedHyperGraph extends DirectedGraph
{
	void addDirectedHyperEdge(int e);

	boolean isDirectedHyperEdge(int e);

	int getNumberOfDirectedHyperEdges();

	LucIntSet getDirectedHyperEdgeHead(int e);

	LucIntSet getDirectedHyperEdgeTail(int e);

	void addToDirectedHyperEdgeTail(int e, int v);

	void addToDirectedHyperEdgeHead(int e, int v);

	void removeFromDirectedHyperEdgeHead(int e, int v);

	void removeFromDirectedHyperEdgeTail(int e, int v);
}
