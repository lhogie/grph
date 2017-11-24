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

package grph.algo.coloring;

import grph.Grph;
import grph.GrphAlgorithm;
import grph.algo.topology.ClassicalGraphs;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import toools.UnitTests;

/**
 * Implementation of the bipartite testing algorithm. The algorithm traverses
 * the graph with depth-first search and tries to color vertices properly with
 * two colors. If it fails, the graph is not bipartite.
 * 
 * @author Gregory Morel
 */

@SuppressWarnings("serial")
public class BipartitenessAlgorithm extends GrphAlgorithm<GraphColoring>
{
	/**
	 * Implementation of the algorithm described above.
	 * 
	 * @param g
	 *            : the graph that has to be tested bipartite or not
	 * @return the bipartition (that consists of an instance of GraphColoring)
	 *         if g is bipartite, null otherwise
	 */
	@Override
	public GraphColoring compute(Grph g)
	{
		Int2IntOpenHashMap color = new Int2IntOpenHashMap();
		IntArrayList queue = new IntArrayList();

		for (int u : g.getVertices().toIntArray())
		{
			// If a vertex is not colored, we take it...
			if ( ! color.containsKey(u))
			{
				queue.add(u);
				color.put(u, 0);

				// ... and explore the connected component it belongs to
				while ( ! queue.isEmpty())
				{
					int v = queue.removeInt(0);
					int c = 1 - color.get(v);

					for (int w : g.getNeighbours(v).toIntArray())
					{
						if (color.containsKey(w))
						{
							if (color.get(w) == color.get(v))
								return null;
						}
						else
						{
							color.put(w, c);
							queue.add(w);
						}
					}
				}
			}
		}

		// Return the bipartition
		GraphColoring gc = new GraphColoring(2);

		for (int v : color.keySet())
		{
			gc.addVertexToClass(v, color.get(v));
		}

		return gc;
	}

	/**
	 * Test method
	 */
	private static void test()
	{
		Grph g = ClassicalGraphs.completeBipartiteGraph(5, 5);
		UnitTests.ensure(g.isBipartite());
	}
}
