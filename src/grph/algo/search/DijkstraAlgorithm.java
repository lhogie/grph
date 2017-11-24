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
 
 

package grph.algo.search;

import grph.Grph;
import grph.algo.topology.ClassicalGraphs;
import grph.properties.NumericalProperty;
import it.unimi.dsi.fastutil.ints.IntSet;
import toools.NotYetImplementedException;
import toools.collections.primitive.IntCursor;
import toools.collections.primitive.SelfAdaptiveIntSet;

/**
 * Computes the shortest paths in the graph, using the Dijkstra algorithm.
 * 
 * @author lhogie
 * 
 */
public class DijkstraAlgorithm extends WeightedSingleSourceSearchAlgorithm
{
	public DijkstraAlgorithm(NumericalProperty weightProperty)
	{
		super(weightProperty);
	}

	@Override
	public SearchResult compute(Grph g, int source, Grph.DIRECTION d, GraphSearchListener listener)
	{
		if (d != Grph.DIRECTION.out)
			throw new NotYetImplementedException("this direction is not supported: " + d.name());

		SearchResult r = new SearchResult(g.getVertices().getGreatest() + 1);

		for (int i = 0; i < r.distances.length; ++i)
		{
			r.distances[i] = Integer.MAX_VALUE;
			r.predecessors[i] = -1;
		}

		r.distances[source] = 0;

		if (listener != null)
			listener.searchStarted();

		int[][] neighbors = g.getOutNeighborhoods();
		IntSet notYetVisitedVertices = new SelfAdaptiveIntSet();
		notYetVisitedVertices.addAll(g.getVertices());

		while (!notYetVisitedVertices.isEmpty())
		{
			int minVertex = findMinVertex(notYetVisitedVertices, r.distances);
			r.visitOrder.add(minVertex);

			if (listener != null)
				listener.vertexFound(minVertex);

			if (minVertex == -1)
			{
				break;
			}
			else
			{
				notYetVisitedVertices.remove(minVertex);

				for (int n : neighbors[minVertex])
				{
					int newDistance = r.distances[minVertex] + weight(g, minVertex, n, getWeightProperty());

					if (newDistance < r.distances[n])
					{
						r.predecessors[n] = minVertex;
						r.distances[n] = newDistance;
					}
				}
			}
		}

		if (listener != null)
			listener.searchCompleted();

		return r;
	}

	private int findMinVertex(IntSet notYetVisited, int[] distances)
	{
		int minD = Integer.MAX_VALUE;
		int minV = -1;

		for (IntCursor c : IntCursor.fromFastUtil(notYetVisited))
		{
			int v = c.value;

			if (distances[v] < minD)
			{
				minV = v;
				minD = distances[v];
			}
		}

		return minV;
	}

	private int weight(Grph g, int src, int dest, NumericalProperty weightProperty)
	{
		IntSet connectingEdges = g.getEdgesConnecting(src, dest);

		if (connectingEdges.isEmpty())
			throw new IllegalStateException("vertices are not connected");

		int w = Integer.MAX_VALUE;

		for (IntCursor c : IntCursor.fromFastUtil(connectingEdges))
		{
			int e = c.value;
			int p = weightProperty == null ? 1 : weightProperty.getValueAsInt(e);

			if (p < w)
			{
				w = p;
			}
		}

		return w;
	}

	@Override
	protected SearchResult[] createArray(int n)
	{
		return new SearchResult[n];
	}

	public static void main(String[] args)
	{
		Grph g = ClassicalGraphs.grid(3, 3);
		NumericalProperty weightProperty = new NumericalProperty("weights", 4, 1);

		for (int e : g.getEdges().toIntArray())
		{
			weightProperty.setValue(e, (int) (Math.random() * 10));
		}

		g.setEdgesLabel(weightProperty);
		g.display();

		SearchResult r = new DijkstraAlgorithm(weightProperty).compute(g, 0, new GraphSearchListener() {

			@Override
			public DECISION vertexFound(int v)
			{
				System.out.println("found vertex: " + v);
				return DECISION.CONTINUE;
			}

			@Override
			public void searchStarted()
			{
				System.out.println("search starting");
			}

			@Override
			public void searchCompleted()
			{
				System.out.println("search terminated");
			}
		});

		System.out.println(r.toString(g.getVertices()));
		System.out.println(r.visitOrder);
		System.out.println(r.farestVertex());
	}

}
