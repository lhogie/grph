package grph;

public interface DirectedSimpleGraph extends DirectedGraph
{
	int getNumberOfDirectedSimpleEdges();

	void addDirectedSimpleEdge(int src, int e, int dest);

	boolean isDirectedSimpleEdge(int e);

	int getDirectedSimpleEdgeTail(int e);

	int getDirectedSimpleEdgeHead(int e);
}
