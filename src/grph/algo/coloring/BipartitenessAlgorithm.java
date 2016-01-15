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

package grph.algo.coloring;

import grph.Grph;
import grph.GrphAlgorithm;
import grph.algo.topology.ClassicalGraphs;
import toools.UnitTests;

import com.carrotsearch.hppc.IntArrayDeque;
import com.carrotsearch.hppc.IntIntOpenHashMap;

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
		IntIntOpenHashMap color = new IntIntOpenHashMap();
		IntArrayDeque queue = new IntArrayDeque();

		for (int u : g.getVertices().toIntArray())
		{
			// If a vertex is not colored, we take it...
			if (!color.containsKey(u))
			{
				queue.addLast(u);
				color.put(u, 0);

				// ... and explore the connected component it belongs to
				while (!queue.isEmpty())
				{
					int v = queue.removeFirst();
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
							queue.addLast(w);
						}
					}
				}
			}
		}

		// Return the bipartition
		GraphColoring gc = new GraphColoring(2);

		for (int v : color.keys)
			gc.addVertexToClass(v, color.get(v));

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
