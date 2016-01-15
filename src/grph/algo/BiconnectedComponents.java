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
import grph.algo.topology.ClassicalGraphs;

import java.util.Arrays;
import java.util.Collection;

import toools.set.DefaultIntSet;
import toools.set.IntSet;

public class BiconnectedComponents
{
    public static IntSet computeCutEdges(Grph g)
    {
	return computeCutEdges(computeArticulationPoints(g), g);
    }

    public static Collection<IntSet> computeBiconnectedComponents(Grph g)
    {
	Grph h = g.clone();
	h.removeEdges(computeCutEdges(g));
	return h.getConnectedComponents();
    }

    public static IntSet computeArticulationPoints(Grph g)
    {
	int n = g.getVertices().size();
	int[] low = new int[n];
	Arrays.fill(low, -1);
	int[] pre = new int[n];
	Arrays.fill(pre, -1);
	IntSet articulation = new DefaultIntSet();

	for (int v : g.getVertices().toIntArray())
	{
	    if (pre[v] == -1)
	    {
		dfs(g, v, v, articulation, low, pre, 0);
	    }
	}

	return articulation;
    }

    private static IntSet dfs(Grph g, int u, int v, IntSet articulation, int[] low, int[] pre, int cnt)
    {
	int children = 0;
	pre[v] = cnt++;
	low[v] = pre[v];

	for (int w : g.getOutNeighborhoods()[v])
	{
	    if (pre[w] == -1)
	    {
		children++;
		dfs(g, v, w, articulation, low, pre, cnt);

		// update low number
		low[v] = Math.min(low[v], low[w]);

		// non-root of DFS is an articulation point if low[w] >= pre[v]
		if (low[w] >= pre[v] && u != v)
		{
		    articulation.add(v);
		}
	    }
	    else if (w != u)
	    {
		// update low number - ignore reverse of edge leading to v
		low[v] = Math.min(low[v], pre[w]);
	    }
	}

	// root of DFS is an articulation point if it has more than 1 child
	if (u == v && children > 1)
	{
	    articulation.add(v);
	}

	return articulation;
    }

    public static IntSet computeCutEdges(IntSet articulationPoints, Grph g)
    {
	IntSet cutEdges = new DefaultIntSet();

	for (int a : articulationPoints.toIntArray())
	{
	    for (int b : articulationPoints.toIntArray())
	    {
		if (a != b)
		{
		    cutEdges.addAll(g.getEdgesConnecting(a, b));
		}
	    }
	}

	return cutEdges;
    }

    // test client
    public static void main(String[] args)
    {
	int s = 3;
	Grph g = ClassicalGraphs.grid(s, s);
	Grph h = ClassicalGraphs.grid(s, s);
	Grph p = ClassicalGraphs.grid(s, s);
	g.addGraph(h);
	g.addGraph(p);
	g.addUndirectedSimpleEdge(6, 10);
	g.addUndirectedSimpleEdge(14, 21);
	g.display();

	BiconnectedComponents bic = new BiconnectedComponents();
	IntSet ap = bic.computeArticulationPoints(g);
	// print out articulation points
	g.highlightVertices(ap, 6);
	g.highlightEdges(computeCutEdges(g), 6);
	System.out.println("ap " + ap);

	for (IntSet bc : computeBiconnectedComponents(g))
	{
	    g.highlightVertices(bc);
	}
    }

}
