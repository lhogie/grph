/*
 * (C) Copyright 2009-2013 CNRS.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser General Public License
 * (LGPL) version 2.1 which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl-2.1.html
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * Contributors:

    Luc Hogie (CNRS, I3S laboratory, University of Nice-Sophia Antipolis) 
    Aurelien Lancin (Coati research team, Inria)
    Christian Glacet (LaBRi, Bordeaux)
    David Coudert (Coati research team, Inria)
    Fabien Crequis (Coati research team, Inria)
    Grégory Morel (Coati research team, Inria)
    Issam Tahiri (Coati research team, Inria)
    Julien Fighiera (Aoste research team, Inria)
    Laurent Viennot (Gang research-team, Inria)
    Michel Syska (I3S, University of Nice-Sophia Antipolis)
    Nathann Cohen (LRI, Saclay) 
 */

package grph.algo.search;

import grph.Grph;
import grph.algo.topology.ClassicalGraphs;
import grph.properties.NumericalProperty;
import toools.NotYetImplementedException;
import toools.set.DefaultIntSet;
import toools.set.IntSet;

import com.carrotsearch.hppc.cursors.IntCursor;

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
		IntSet notYetVisitedVertices = new DefaultIntSet();
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

		for (IntCursor c : notYetVisited)
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

		for (IntCursor c : connectingEdges)
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
