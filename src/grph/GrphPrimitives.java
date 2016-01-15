package grph;

import grph.Grph.DIRECTION;
import toools.set.IntSet;

public interface GrphPrimitives
{
	DIRECTION getNavigation();

	// vertices
	IntSet getVertices();

	int getNextVertexAvailable();

	void removeVertex(int v);

	void addVertex(int v);
	boolean containsVertex(int v);

	IntSet getOutOnlyElements(int v);

	IntSet getInOnlyElements(int v);

	IntSet getInOutOnlyElements(int v);

	// edges
	boolean storeEdges();
	boolean containsEdge(int e);

	int getNextEdgeAvailable();

	IntSet getEdges();

	void removeEdge(int e);

	// undirected simple edges

	int getNumberOfUndirectedSimpleEdges();

	void addUndirectedSimpleEdge(int e, int v1, int v2);

	boolean isUndirectedSimpleEdge(int e);

	int getOneVertex(int e);

	int getTheOtherVertex(int e, int v);

	// directed simple edge
	int getNumberOfDirectedSimpleEdges();

	void addDirectedSimpleEdge(int src, int e, int dest);

	boolean isDirectedSimpleEdge(int e);

	int getDirectedSimpleEdgeTail(int e);

	int getDirectedSimpleEdgeHead(int e);

	// undirected hyper edges
	int getNumberOfUndirectedHyperEdges();

	void addUndirectedHyperEdge(int e);

	boolean isUndirectedHyperEdge(int e);

	void addToUndirectedHyperEdge(int edge, int vertex);

	void removeFromHyperEdge(int e, int v);

	IntSet getUndirectedHyperEdgeVertices(int e);

	// directed hyper edges
	void addDirectedHyperEdge(int e);

	boolean isDirectedHyperEdge(int e);

	int getNumberOfDirectedHyperEdges();

	IntSet getDirectedHyperEdgeHead(int e);

	IntSet getDirectedHyperEdgeTail(int e);

	void addToDirectedHyperEdgeTail(int e, int v);

	void addToDirectedHyperEdgeHead(int e, int v);

	void removeFromDirectedHyperEdgeHead(int e, int v);

	void removeFromDirectedHyperEdgeTail(int e, int v);

	void removeEdge(int u, int v);
}
