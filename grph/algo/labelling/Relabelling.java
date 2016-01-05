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

package grph.algo.labelling;

import grph.Grph;
import grph.GrphAlgorithm;
import grph.in_memory.InMemoryGrph;
import toools.Clazz;
import toools.NotYetImplementedException;
import toools.set.DefaultIntSet;
import toools.set.IntSet;

import com.carrotsearch.hppc.cursors.IntCursor;

/**
 * produces a graph with the same topology but relabelled.
 * 
 * @author lhogie
 * 
 */
public abstract class Relabelling extends GrphAlgorithm<Grph>
{
	public abstract int getVertexLabel(int v);

	public abstract int getEdgeLabel(int e);

	@Override
	public Grph compute(Grph g)
	{
		Grph gg = Clazz.makeInstance(g.getClass());

		for (IntCursor vc : g.getVertices())
		{
			gg.addVertex(getVertexLabel(vc.value));
		}

		for (IntCursor ec : g.getEdges())
		{
			int e = ec.value;

			if (g.isUndirectedSimpleEdge(e))
			{
				int a = g.getOneVertex(e);
				int b = g.getTheOtherVertex(e, a);
				gg.addUndirectedSimpleEdge(getEdgeLabel(e), getVertexLabel(a), getVertexLabel(b));
			}
			else
			{
				throw new NotYetImplementedException();
			}
		}

		return gg;
	}

	public IntSet computeVertexSet(IntSet s)
	{
		IntSet r = new DefaultIntSet();

		for (IntCursor c : s)
		{
			r.add(getVertexLabel(c.value));
		}

		return r;
	}

	public IntSet computeEdgeSet(IntSet s)
	{
		IntSet r = new DefaultIntSet();

		for (IntCursor c : s)
		{
			r.add(getEdgeLabel(c.value));
		}

		return r;
	}

	public static void main(String[] args)
	{
		Grph g = new InMemoryGrph();
		g.addUndirectedSimpleEdge(6, 4, 12);
		g.addUndirectedSimpleEdge(9, 4, 120);
		g.addUndirectedSimpleEdge(876, 67, 12);
		g.addUndirectedSimpleEdge(56, 4, 67);
		g.display();

		new ContiguousLabelling().compute(g).display();
	}
}
