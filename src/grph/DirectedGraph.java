package grph;

import grph.Grph.DIRECTION;
import toools.collections.primitive.LucIntSet;

/*
 * We just know that the graph is directed. It may be simple of hyper.
 */

public interface DirectedGraph extends UnqualifiedGraph
{
	DIRECTION getNavigation();

	LucIntSet getOutOnlyElements(int v);

	LucIntSet getInOnlyElements(int v);

	LucIntSet getInOutOnlyElements(int v);
}
