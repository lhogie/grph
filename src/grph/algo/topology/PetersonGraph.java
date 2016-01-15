package grph.algo.topology;

import grph.Grph;
import grph.in_memory.InMemoryGrph;

import com.carrotsearch.hppc.IntArrayList;

public class PetersonGraph
{
	/**
	 * Generates a Generates the <a
	 * href="http://en.wikipedia.org/wiki/Generalized_Petersen_graph"
	 * target="_blank">generalized Pertersen graph</a>, formed by connecting the
	 * vertices of a regular polygon to the corresponding vertices of a star
	 * polygon.
	 * 
	 * @param n
	 *            number of vertices of the two polygons
	 * @param k
	 *            vertex interval step from a regular polygon
	 */
	public static Grph petersenGraph(int n, int k)
	{
		Grph g = new InMemoryGrph();
		g.addNVertices(n);
		g.ring();

		IntArrayList interiorVertices = new IntArrayList();

		for (int exteriorVertex : g.getVertices().toIntArray())
		{
			int newInteriorVertex = g.addVertex();
			interiorVertices.add(newInteriorVertex);
			g.addUndirectedSimpleEdge(exteriorVertex, newInteriorVertex);
		}

		int numberOfLinks = interiorVertices.size();
		int _a = 0;

		for (int i = 0; i < numberOfLinks; ++i)
		{
			int _b = _a + k;
			g.addUndirectedSimpleEdge(
					interiorVertices.get(_a % interiorVertices.size()),
					interiorVertices.get(_b % interiorVertices.size()));
			_a = _b;
		}
		
		return g;
	}
}
