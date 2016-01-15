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

import com.carrotsearch.hppc.IntStack;
import com.carrotsearch.hppc.cursors.IntCursor;

/**
 * BellmanFord using two stacks.
 * 
 * @author lhogie
 * 
 */
public class StackBasedBellmanFordAlgorithm extends WeightedSingleSourceSearchAlgorithm
{

	public StackBasedBellmanFordAlgorithm(NumericalProperty weightProperty)
	{
		super(weightProperty);
	}

	@Override
	public SearchResult compute(Grph graph, int source, Grph.DIRECTION direction, GraphSearchListener listener)
	{

		if (direction != Grph.DIRECTION.out)
			throw new NotYetImplementedException();

		SearchResult entries = new SearchResult(graph.getVertices().getGreatest() + 1);

		for (IntCursor vertex : graph.getVertices())
		{
			entries.predecessors[vertex.value] = -1;
			entries.distances[vertex.value] = Integer.MAX_VALUE;
		}

		if (listener != null)
			listener.searchStarted();

		entries.distances[source] = 0;

		IntStack P1 = new IntStack();
		IntStack P2 = new IntStack();
		P1.push(source);

		while (!P1.isEmpty())
		{
			P2.clear();

			while (!P1.isEmpty())
			{
				int u = P1.pop();

				for (int e : graph.getOutEdges(u).toIntArray())
				{
					int n = graph.getTheOtherVertex(e, u);
					int d = getWeightProperty().getValueAsInt(e);

					if (entries.distances[u] + d < entries.distances[n])
					{
						if (listener != null)
							listener.vertexFound(u);

						entries.distances[n] = entries.distances[u] + d;
						entries.predecessors[n] = u;

						if (!P2.contains(n))
						{
							P2.push(n);
						}
					}
				}
			}

			IntStack Ptmp = P1;
			P1 = P2;
			P2 = Ptmp;
		}
		if (listener != null)
			listener.searchCompleted();

		return entries;
	}

	@Override
	protected SearchResult[] createArray(int n)
	{
		return new SearchResult[n];
	}

	public static void main(String[] args)
	{
		Grph g = ClassicalGraphs.PetersenGraph();
		new StackBasedBellmanFordAlgorithm(new NumericalProperty("")).compute(g, 0, Grph.DIRECTION.out, new GraphSearchListener() {

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
	}
}
