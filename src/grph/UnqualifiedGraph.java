package grph;


/**
 * A graph with no information on the type of its edges
 * @author lhogie
 *
 */
public interface UnqualifiedGraph extends VertexSet, EdgeSet
{
	void removeEdge(int u, int v);
}
