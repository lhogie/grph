/* (C) Copyright 2009-2013 CNRS (Centre National de la Recherche Scientifique).

Licensed to the CNRS under one
or more contributor license agreements.  See the NOTICE file
distributed with this work for additional information
regarding copyright ownership.  The CNRS licenses this file
to you under the Apache License, Version 2.0 (the
"License"); you may not use this file except in compliance
with the License.  You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing,
software distributed under the License is distributed on an
"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
KIND, either express or implied.  See the License for the
specific language governing permissions and limitations
under the License.

*/

/* Contributors:

Luc Hogie (CNRS, I3S laboratory, University of Nice-Sophia Antipolis) 
Aurelien Lancin (Coati research team, Inria)
Christian Glacet (LaBRi, Bordeaux)
David Coudert (Coati research team, Inria)
Fabien Crequis (Coati research team, Inria)
Grégory Morel (Coati research team, Inria)
Issam Tahiri (Coati research team, Inria)
Julien Fighiera (Aoste research team, Inria)
Laurent Viennot (Gang research-team, Inria)
Michel Syska (I3S, Université Cote D'Azur)
Nathann Cohen (LRI, Saclay) 
Julien Deantoin (I3S, Université Cote D'Azur, Saclay) 

*/
 
 package grph.algo.clustering;

import grph.Grph;
import toools.collections.primitive.IntCursor;
import toools.math.Distribution;

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

		for (IntCursor thisVertex : IntCursor.fromFastUtil(g.getVertices()))
		{
			d.addOccurence(getLocalClusteringCoefficient(g, thisVertex.value));
		}

		return d;
	}
}
