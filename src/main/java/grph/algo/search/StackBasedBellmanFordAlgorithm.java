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
 
 

package grph.algo.search;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntStack;

import grph.Grph;
import grph.algo.topology.ClassicalGraphs;
import grph.properties.NumericalProperty;
import toools.collections.primitive.IntCursor;
import toools.exceptions.NotYetImplementedException;

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

		for (IntCursor vertex : IntCursor.fromFastUtil(graph.getVertices()))
		{
			entries.predecessors[vertex.value] = -1;
			entries.distances[vertex.value] = Integer.MAX_VALUE;
		}

		if (listener != null)
			listener.searchStarted();

		entries.distances[source] = 0;

		IntArrayList P1 = new IntArrayList();
		IntArrayList P2 = new IntArrayList();
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

			IntArrayList Ptmp = P1;
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
