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

package grph.in_memory;

import grph.Grph;
import toools.set.IntHashSet;
import toools.set.IntSet;

import com.carrotsearch.hppc.cursors.IntCursor;

class OutNeighboursCache_OLD extends NeighboursCache_OLD
{

	public OutNeighboursCache_OLD(Grph graph)
	{
		super(graph);
	}

	@Override
	protected IntSet computeNeighbors(int vertex)
	{
		Grph g = getGraph();
		assert g.getVertices().contains(vertex) : vertex;

		IntSet outs = new IntHashSet();

		for (IntCursor c : g.getOutEdges(vertex))
		{
			int e = c.value;

			if (g.isSimpleEdge(e))
			{
				outs.add(g.getTheOtherVertex(e, vertex));
			}
			else if (g.isUndirectedHyperEdge(e))
			{
				outs.addAll(g.getUndirectedHyperEdgeVertices(e));
			}
			else if (g.isDirectedHyperEdge(e))
			{
				outs.addAll(g.getDirectedHyperEdgeHead(e));
			}
		}

		return outs;
	}
}
