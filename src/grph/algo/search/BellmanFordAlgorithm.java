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
import toools.NotYetImplementedException;
import toools.collections.primitive.IntCursor;

public class BellmanFordAlgorithm extends WeightedSingleSourceSearchAlgorithm
{

	public BellmanFordAlgorithm(NumericalProperty weights)
	{
		super(weights);
	}

	@Override
	public SearchResult compute(Grph graph, int source, Grph.DIRECTION d, GraphSearchListener listener)
	{
		if (d != Grph.DIRECTION.out)
			throw new NotYetImplementedException("this direction is not supported: " + d.name());

		SearchResult r = new SearchResult(graph.getVertices().getGreatest() + 1);

		for (int i = 1; i < r.predecessors.length; ++i)
		{
			r.distances[i] = Integer.MAX_VALUE;
		}

		if (listener != null)
			listener.searchStarted();

		r.predecessors[source] = 0;

		for (int i = 0; i < r.predecessors.length; ++i)
		{
			for (IntCursor e : IntCursor.fromFastUtil(graph.getEdges()))
			{
				int u = graph.getOneVertex(e.value);
				int v = graph.getTheOtherVertex(e.value, u);
				int weight = getWeightProperty().getValueAsInt(e.value);
				
				if (r.distances[u] + weight < r.distances[v])
				{
					if (listener != null)
						listener.vertexFound(v);

					r.distances[v] = r.distances[u] + weight;
					r.predecessors[v] = u;
				}
			}
		}

		assert !hasNegativeWeightCycles(graph, r) : "graph contains a negative-weight cycle";

		if (listener != null)
			listener.searchCompleted();

		return r;
	}

	// / Step 3: check for negative-weight cycles
	public boolean hasNegativeWeightCycles(Grph graph, SearchResult r)
	{
		for (IntCursor e : IntCursor.fromFastUtil(graph.getEdges()))
		{
			int u = graph.getOneVertex(e.value);
			int v = graph.getTheOtherVertex(e.value, u);

			if (r.distances[u] + getWeightProperty().getValueAsInt(e.value) < r.distances[v])
			{
				return true;
			}
		}

		return false;
	}

	@Override
	protected SearchResult[] createArray(int n)
	{
		return new SearchResult[n];
	}

	public static void main(String[] args)
	{
		Grph g = ClassicalGraphs.PetersenGraph();
		new BellmanFordAlgorithm(new NumericalProperty("")).compute(g, 0, new GraphSearchListener() {

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
