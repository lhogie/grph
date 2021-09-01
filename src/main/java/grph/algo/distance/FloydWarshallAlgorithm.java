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
Julien Deantoni (I3S, Université Cote D'Azur, Saclay) 

*/
 
 

package grph.algo.distance;

import java.util.Random;

import grph.Grph;
import grph.algo.topology.ClassicalGraphs;
import grph.properties.NumericalProperty;
import it.unimi.dsi.fastutil.ints.IntSet;
import toools.collections.primitive.LucIntSet;

public class FloydWarshallAlgorithm extends WeightedDistanceMatrixAlgorithm
{

	public FloydWarshallAlgorithm(NumericalProperty edgeWeights)
	{
		super(edgeWeights);
	}

	@Override
	public DistanceMatrix compute(final Grph g)
	{
		assert g.getVertices().getDensity() == 1 : "cannot compute floyd warshall if the vertex id space is not contiguous";

		int n = g.getNumberOfVertices();
		DistanceMatrix distanceMatrix = new DistanceMatrix(n, n);
		distanceMatrix.fill(Integer.MAX_VALUE);
		int[][] dist = distanceMatrix.array;

		for (int v = 0; v < n; ++v)
		{
			dist[v][v] = 0;
		}

		for (int u = 0; u < n; ++u)
		{
			for (int v = 0; v < n; ++v)
			{
				IntSet edges = g.getEdgesConnecting(u, v);

				if (!edges.isEmpty())
				{
					int w = weight(edges);
					dist[u][v] = w;
				}
			}
		}

		for (int k = 0; k < n; ++k)
		{
			for (int i = 0; i < n; ++i)
			{
				for (int j = 0; j < n; ++j)
				{
					// in order to prevent numerical overflows
					int d = dist[i][k] == Integer.MAX_VALUE || dist[k][j] == Integer.MAX_VALUE ? Integer.MAX_VALUE : dist[i][k] + dist[k][j];

					if (d < dist[i][j])
					{
						dist[i][j] = d;
					}
				}
			}
		}

		return distanceMatrix;
	}

	private int weight(IntSet edges)
	{
		if (edges.isEmpty())
			throw new IllegalStateException();

		int w = Integer.MAX_VALUE;

		for (int e : edges.toIntArray())
		{
			int ew = getEdgeWeights().getValueAsInt(e);

			if (ew < w)
			{
				w = ew;
			}
		}

		return w;
	}

	public static void main(String[] args)
	{
		Grph g = ClassicalGraphs.PetersenGraph();
		NumericalProperty weights = new NumericalProperty("w", 4, 0);
		weights.randomize(g.getVertices(), new Random());
		System.out.println(weights.toString(g.getVertices()));
		System.out.println(new FloydWarshallAlgorithm(weights).compute(g));
	}

}
