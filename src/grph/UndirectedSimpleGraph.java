package grph;

public interface UndirectedSimpleGraph extends UndirectedGraph
{
	int getNumberOfUndirectedSimpleEdges();

	void addUndirectedSimpleEdge(int e, int v1, int v2);

	boolean isUndirectedSimpleEdge(int e);

	int getOneVertex(int e);

	int getTheOtherVertex(int e, int v);
}
