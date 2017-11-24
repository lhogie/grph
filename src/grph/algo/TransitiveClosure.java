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
 
 

package grph.algo;

import grph.Grph;
import grph.algo.search.BFSAlgorithm;
import grph.algo.search.SearchResult;
import grph.algo.topology.GNPTopologyGenerator;
import grph.in_memory.InMemoryGrph;
import toools.StopWatch;

public class TransitiveClosure
{
	public static Grph computeTransitiveClosure(Grph g)
	{
		Grph resultingGraph = g.clone();

		for (int v : g.getVertices().toIntArray())
		{
			SearchResult bfsResult = new BFSAlgorithm().compute(g, v);
			int[] visitedVertices = bfsResult.visitOrder.toIntArray();

			// for every vertex w accessible from v
			for (int w : visitedVertices)
			{
				int d = bfsResult.distances[w];

				// but w is not a neighbor of v
				if (d > 1)
				{
					resultingGraph.addDirectedSimpleEdge(v, w);
				}
			}
		}

		return resultingGraph;
	}

	public static void main(String[] args)
	{
		Grph g = new InMemoryGrph();
		g.addNVertices(900);
		GNPTopologyGenerator.compute(g, 0.017);

		StopWatch sw = new StopWatch();
		g = computeTransitiveClosure(g);
		System.out.println(sw.getElapsedTime());
	}
}
