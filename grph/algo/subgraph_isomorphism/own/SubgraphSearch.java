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
import grph.algo.subgraph_isomorphism.GraphMatchingAlgorithm;
import grph.algo.topology.ClassicalGraphs;
import grph.util.Matching;

import java.util.ArrayList;
import java.util.List;

import com.carrotsearch.hppc.IntArrayList;

public class SubgraphSearch extends GraphMatchingAlgorithm
{

    @Override
    public List<Matching> findAllMatches(Grph g, Grph pattern)
    {
	return compute(g, pattern, false);
    }

    public List<Matching> compute(Grph g, Grph p, boolean induced)
    {
	if (g.getVertices().getDensity() < 1)
	    throw new IllegalArgumentException();

	if (p.getVertices().getDensity() < 1)
	    throw new IllegalArgumentException();

	IntArrayList l = new IntArrayList();
	List<IntArrayList> matchings = new ArrayList<IntArrayList>();

	compute(g, p, true, matchings, l, 0, induced);
	List<Matching> mm = new ArrayList<Matching>();

	for (IntArrayList l2 : matchings)
	{
	    Matching m = new Matching();

	    for (int i = 0; i < l2.size(); ++i)
	    {
		m.pattern2graph(i, l2.get(i));
	    }

	    mm.add(m);
	}

	return mm;
    }

    public boolean compute(Grph g, Grph h, boolean all, List<IntArrayList> matchings, IntArrayList l, int hv,
	    boolean induced)
    {
	if (!h.getVertices().contains(hv))
	{
	    if (all)
	    {
		matchings.add(l.clone());
		return false;
	    }
	    else
	    {
		return true;
	    }
	}

	boolean mistake = false;

	for (int gv : g.getVertices().toIntArray())
	{
	    if (l.contains(gv))
		continue;

	    mistake = false;

	    for (int hi = 0; hi < l.size(); ++hi)
	    {
		int gi = l.get(hi);
		boolean match = induced ? (g.areVerticesAdjacent(gi, gv) == h.areVerticesAdjacent(hi, hv)) : (g
			.areVerticesAdjacent(gi, gv) || !h.areVerticesAdjacent(hi, hv));

		boolean match2 = induced ? (g.areVerticesAdjacent(gv, gi) == h.areVerticesAdjacent(hv, hi)) : (g
			.areVerticesAdjacent(gv, gi) || !h.areVerticesAdjacent(hv, hi));

		if (!match && !match2)
		{
		    mistake = true;
		    break;
		}
	    }

	    if (mistake)
	    {
		continue;
	    }
	    else
	    {
		l.add(gv);

		if (compute(g, h, all, matchings, l, hv + 1, induced))
		{
		    return true;
		}
		else
		{
		    l.remove(l.size() - 1);
		}
	    }
	}

	return false;
    }

    public static void main(String[] args)
    {
	Grph g = ClassicalGraphs.grid(2, 2);
	Grph h = ClassicalGraphs.grid(2, 2);
	// Grph h = ClassicalGraphs.cycle(6);

	List<Matching> m = new SubgraphSearch().compute(g, h, false);

	System.out.println(m.size());

    }
}
