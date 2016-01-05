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

package grph.algo.distance;

import grph.Grph;
import grph.GrphAlgorithm;
import grph.algo.search.DistanceListsBasedDiameterAlgorithm;
import grph.algo.search.SearchResult;
import grph.in_memory.InMemoryGrph;

import java.util.Random;

/**
 * Computes the two-sweeps approximated diameter of the graph.
 * 
 * @author lhogie
 * 
 */
public class TwoSweepBFSDiameterApproximationAlgorithm extends GrphAlgorithm<Integer>
{

	@Override
	public Integer compute(Grph g)
	{
		if (g.getNumberOfUndirectedSimpleEdges() != g.getEdges().size())
			throw new IllegalArgumentException("this approximation of the diameter is applicable only on graphs with undirected simple edges");

		if (g.isNull())
		{
			throw new IllegalStateException("cannot compute the diameter of a null graph");
		}
		else if (g.isTrivial())
		{
			return 0;
		}
		else
		{
			if (g.isConnected())
			{
				if (g.getVertices().size() == 2)
				{
					return 1;
				}
				else
				{
					// starts from any vertex
					int randomVertex = g.getVertices().pickRandomElement(new Random());

					// performs the first BFS
					SearchResult r = g.bfs(randomVertex);

					// peforms the second BFS
					return g.bfs(r.farestVertex()).maxDistance();
				}
			}
			else
			{
				throw new IllegalStateException("cannot compute the diameter of a non-connected graph");
			}
		}
	}

	public static void main(String[] args)
	{
		Grph g = new InMemoryGrph();
		g.grid(100, 100);
		System.out.println("compute");
		System.out.println(new TwoSweepBFSDiameterApproximationAlgorithm().compute(g));
		System.out.println(new DistanceListsBasedDiameterAlgorithm().compute(g));
	}

}
