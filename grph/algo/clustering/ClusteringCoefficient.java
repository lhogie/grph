package grph.algo.clustering;

import grph.Grph;
import toools.math.Distribution;

import com.carrotsearch.hppc.cursors.IntCursor;

public class ClusteringCoefficient
{


	/**
	 * Computes the local clustering coefficient of the given vertex
	 * <code>v</code>, defined as the number of edges in G[N(v)] over the
	 * maximum possible number of edges in G[N(v)] if it was a clique.
	 * 
	 * @param vertex
	 * @return
	 */
	public static double getLocalClusteringCoefficient(Grph g, int vertex)
	{
		assert vertex >= 0;
		int[] neighbors = g.getOutNeighbors(vertex).toIntArray();
		int degree = neighbors.length;

		if (degree <= 1)
		{
			return 0;
		}
		else
		{
			int n = 0;

			for (int v1 : neighbors)
			{
				for (int v2 : neighbors)
				{
					if (v1 != v2)
					{
						if (!g.getEdgesConnecting(v1, v2).isEmpty())
						{
							++n;
						}
					}
				}
			}

			return n / ((double) (degree * (degree - 1)));
		}
	}

	/**
	 * Computes a clustering coefficient distribution for this graph
	 * 
	 * @return the distribution object
	 */
	public static Distribution<Double> getClusteringCoefficientDistribution(
			Grph g)
	{
		Distribution<Double> d = new Distribution<Double>(
				"Clustering coefficient distribution");

		for (IntCursor thisVertex : g.getVertices())
		{
			d.addOccurence(getLocalClusteringCoefficient(g, thisVertex.value));
		}

		return d;
	}
}
