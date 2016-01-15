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

package grph.algo.subgraph_isomorphism.own;

import grph.Grph;
import grph.GrphAlgorithm;
import grph.algo.topology.ClassicalGraphs;
import grph.path.Path;
import grph.path.PathExtender;

import java.util.HashSet;
import java.util.Set;

public class FindAllCycles extends GrphAlgorithm<Set<Path>>
{
	@Override
	public Set<Path> compute(Grph g)
	{
		Set<Path> cycles = new HashSet();

		for (Path p : g.getAllPaths())
		{
			int s = p.getSource();
			int d = p.getDestination();

			// if the source is a successor of the destination of this path
			if (g.getInNeighbors(s).contains(d))
			{
				for (int e : g.getOutEdges(d).toIntArray())
				{
					int ov = g.getTheOtherVertex(e, d);

					if (ov == s)
					{
						cycles.add(new PathExtender(p, e, s));
					}
				}
			}
		}

		return cycles;
	}

	public static void main(String[] args)
	{
		Grph g = ClassicalGraphs.grid(2, 2);
		Set<Path> r = new FindAllCycles().compute(g);

		for (Path p : r)
		{
			System.out.println(p);
		}
	}
}
