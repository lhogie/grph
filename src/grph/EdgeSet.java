package grph;

import toools.collections.primitive.LucIntSet;

public interface EdgeSet
{
	boolean storeEdges();

	boolean containsEdge(int e);

	int getNextEdgeAvailable();

	LucIntSet getEdges();

	void removeEdge(int e);
}
