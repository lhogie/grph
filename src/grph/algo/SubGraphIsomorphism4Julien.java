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

import java.util.Arrays;

import com.carrotsearch.hppc.IntIntMap;

public class SubGraphIsomorphism4Julien
{

    public IntIntMap compute(Grph g, Grph pattern)
    {

	for (int v : g.getVertices().toIntArray())
	{
	    IntIntMap matching = foo(g, pattern, v);

	    if (matching != null)
	    {
		return matching;
	    }
	}

	return null;
    }

    private IntIntMap foo(Grph g, Grph pattern, int v)
    {
	int pv = pattern.getVertices().iterator().next().value;
	int[] p2g = new int[g.getVertices().size()];
	int[] g2p = new int[g.getVertices().size()];
	Arrays.fill(p2g, -1);
	Arrays.fill(g2p, -1);
	p2g[pv] = v;
	g2p[v] = pv;

	for (int pn : pattern.getNeighbours(pv).toIntArray())
	{
	    if (p2g[pn] == -1)
	    {
		
	    }
	}

	return null;
    }

}
