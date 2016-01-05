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
			int[] visitedVertices = bfsResult.visitOrder.toArray();

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
