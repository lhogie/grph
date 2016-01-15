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
import grph.in_memory.InMemoryGrph;
import toools.set.IntHashSet;
import toools.set.IntSet;

import com.carrotsearch.hppc.cursors.IntCursor;

/**
 * Compute the set of isolated vertices.
 * 
 * @author lhogie
 * 
 */
public class IsolatedVerticesAlgorithm extends GrphAlgorithm<IntSet>
{

    @Override
    public IntSet compute(Grph g)
    {
	IntSet s = new IntHashSet();

	for (IntCursor v : g.getVertices())
	{
	    if (g.getNeighbours(v.value).isEmpty())
	    {
		s.add(v.value);
	    }
	}

	return s;
    }

    
    public static void main(String[] args)
    {
	Grph g = new InMemoryGrph();
	g.grid(4, 4);
	System.out.println(g.getIsolatedVertices().isEmpty());
	g.addVertex();
	System.out.println(g.getIsolatedVertices().isEmpty());
    }
}
