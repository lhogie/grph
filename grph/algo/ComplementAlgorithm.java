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
import grph.GrphAlgorithm;
import toools.Clazz;
import toools.set.DefaultIntSet;
import toools.set.IntSet;

import com.carrotsearch.hppc.cursors.IntCursor;

/**
 * Computes the complement (or inverse) of the given graph.
 * @author lhogie
 *
 */
@SuppressWarnings("serial")
public class ComplementAlgorithm extends GrphAlgorithm<Grph>
{
	@Override
	public Grph compute(Grph g)
	{
		if (g.isMixed())
			throw new IllegalArgumentException("cannot get the inverse of a mixed graph");

		if (g.getNumberOfHyperEdges() > 0)
			throw new IllegalArgumentException("cannot get the inverse of an hypergraph");

		// if this is a digraph
		if (g.getNumberOfDirectedSimpleEdges() > 0)
		{
			Grph complement = Clazz.makeInstance(g.getClass());

			for (IntCursor c : g.getEdges())
			{
				int e = c.value;
				int src = g.getDirectedSimpleEdgeTail(e);
				int dest = g.getDirectedSimpleEdgeHead(e);
				complement.addDirectedSimpleEdge(dest, e, src);
			}

			return complement;
		}
		else
		{
			Grph complement = Clazz.makeInstance(g.getClass());
			IntSet vg = g.getVertices();
			complement.addVertices(vg);
			int[] vertices = vg.toIntArray();
			
			IntSet visited = new DefaultIntSet();
			for (int a : vertices)
			{
				for (int b : vertices)
				{
					if (a != b && !visited.contains(b))
					{
						// if the two edges are not connected
						if (!g.areVerticesAdjacent(a, b))
						{
							complement.addUndirectedSimpleEdge(a, b);
						}
					}
				}

				visited.add(a);
			}

			return complement;
		}
	}
}
