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

package grph.algo.k_shortest_paths;

import grph.Grph;
import grph.in_memory.InMemoryGrph;
import grph.path.Path;
import grph.properties.NumericalProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DisjointKShortestPaths extends KShortestPathsAlgorithm
{
	@Override
	public List<Path> compute(Grph g, final int s, final int t, final int k, NumericalProperty weights)
	{
		List<Path> r = new ArrayList();

		// we work on a clone because we will alter the graph
		g = g.clone();

		while (r.size() < k)
		{
			Path p = g.getShortestPath(s, t, weights);
			int[] seq = p.toVertexArray();

			if (seq == null)
			{
				break;
			}
			else
			{
				r.add(p);

				for (int i = 1; i < seq.length - 1; ++i)
				{
					g.removeVertex(seq[i]);
				}
			}
		}

		return r;
	}

	public static void main(String[] args)
	{
		Grph g = new InMemoryGrph();
		g.grid(6, 6);
		g.getShortestPath(0, 37, null).toVertexArray();

		g.display();
		int s = g.getVertices().pickRandomElement(new Random());
		int t = g.getVertices().pickRandomElement(new Random());
		List<Path> paths = new DisjointKShortestPaths().compute(g, s, t, 10, null);

		for (Path p : paths)
		{
			p.setColor(g, 0);
		}

		g.highlightVertex(s, 1);
		g.highlightVertex(t, 2);
	}
}
