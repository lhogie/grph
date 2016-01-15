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
 
 package grph.algo.covering_packing;

import grph.Grph;
import grph.GrphAlgorithm;
import grph.algo.MaxFlowAlgorithm;
import grph.algo.MaxFlowAlgorithmResult;
import grph.algo.coloring.BipartitenessAlgorithm;
import grph.algo.coloring.GraphColoring;
import toools.set.DefaultIntSet;
import toools.set.IntSet;
import toools.set.IntSets;

import com.carrotsearch.hppc.IntDoubleMap;
import com.carrotsearch.hppc.cursors.IntDoubleCursor;

/**
 * An algorithm to solve the maximum bipartite matching problem. It is based on
 * the classical reduction to a network flow problem, described by a linear
 * program and then solved by a solver.
 * 
 * @author Gregory Morel, Vincent Levorato, Jean-Francois Lalande
 */

@SuppressWarnings("serial")
public class BipartiteMaximumMatchingAlgorithm extends GrphAlgorithm<IntSet>
{

    /**
     * Implementation of the algorithm described above.
     * 
     * @param g
     *            a bipartite graph.
     * @return an IntSet that consists of the set of edges in a maximum
     *         bipartite matching, or null if g is not bipartite.
     */
    @Override
    public IntSet compute(Grph g)
    {
	if (!g.isBipartite())
	    return null;

	Grph flow = buildBipartiteFlow(g);

	int source = flow.getNumberOfVertices() - 2;
	int sink = flow.getNumberOfVertices() - 1;

	MaxFlowAlgorithmResult mfar = new MaxFlowAlgorithm().compute(flow, source, sink, null);
	IntDoubleMap idm = mfar.getAssigments();
	IntSet is = new DefaultIntSet();

	for (IntDoubleCursor i : idm)
	{
	    if (i.value > 0)
	    {
		is.add(i.key);
	    }
	}

	return IntSets.intersection(is, g.getEdges());
    }

    /**
     * This method transforms the specified bipartite graph into a network.
     * 
     * @param g
     *            a bipartite graph
     * @return the corresponding network
     */
    private Grph buildBipartiteFlow(Grph g)
    {
	// Maybe to be changed later in an other form
	BipartitenessAlgorithm ba = new BipartitenessAlgorithm();
	GraphColoring c = ba.compute(g);

	assert c != null;

	Grph flow = g.clone();

	int n = flow.getNumberOfVertices();
	int source = n;
	int sink = n + 1;

	flow.addVertex(source);
	flow.addVertex(sink);

	for (int v : c.getColorClass(0).toIntArray())
	{
	    for (int w : flow.getNeighbours(v).toIntArray())
	    {
		flow.disconnect(v, w);
		flow.addDirectedSimpleEdge(v, w);
	    }

	    // Add an edge from the source to each vertex in the left side of
	    // the bipartition
	    flow.addDirectedSimpleEdge(source, v);
	}

	for (int w : c.getColorClass(1).toIntArray())
	{
	    // Add an edge from each vertex in the right side of the bipartition
	    // to the sink
	    flow.addDirectedSimpleEdge(w, sink);
	}

	return flow;
    }
}
