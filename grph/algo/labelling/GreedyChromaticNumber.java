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
import toools.collections.Collections;
import toools.set.DefaultIntSet;
import toools.set.IntSet;

import com.carrotsearch.hppc.IntArrayList;

/**
 * Compute the chromatic number of the given graph, with a greedy algorithm.
 * 
 * @author lhogie
 * 
 */
public class GreedyChromaticNumber extends GrphAlgorithm<Integer>
{
    // ~ Methods
    // ----------------------------------------------------------------

    /**
     * Finds the number of colors required for a greedy coloring of the graph.
     * 
     * @param g
     *            an undirected graph to find the chromatic number of
     * 
     * @return integer the approximate chromatic number from the greedy
     *         algorithm
     */
    @Override
    public Integer compute(Grph g)
    {
	if (!g.isUndirectedSimpleGraph())
	    throw new IllegalArgumentException("undirected simple graph expected");

	// A copy of the graph is made, so that elements of the graph may be
	// removed to carry out the algorithm
	Grph sg = g.clone();

	// The Vertices will be sorted in decreasing order by degree, so that
	// higher degree vertices have priority to be colored first
	int[] sortedVerticesArray = sg.sortVerticesByDegree();
	Collections.reverse(sortedVerticesArray);
	IntArrayList sortedVertices = IntArrayList.from(sortedVerticesArray);
	int color = 0;

	// Each vertex will attempted to be colored with a single color each
	// iteration, and these vertices will be removed from the graph at the
	// end of each iteration
	while (!sg.getVertices().isEmpty())
	{
	    // This set will contain vertices that are colored with the
	    // current color of this iteration
	    IntSet currentColor = new DefaultIntSet();

	    // the size will change along the execution
	    for (int i = 0; i < sortedVertices.size(); ++i)
	    {
		int v = sortedVertices.get(i);

		// Add new vertices to be colored as long as they are not
		// adjacent with any other vertex that has already been colored
		// with the current color
		boolean flag = true;

		for (int tmp : currentColor.toIntArray())
		{
		    if (sg.areVerticesAdjacent(tmp, v))
		    {
			flag = false;
			break;
		    }
		}

		if (flag)
		{
		    currentColor.add(v);
		    sortedVertices.remove(i);
		}
	    }

	    // Remove vertices from the graph and then repeat the process for
	    // the next iteration
	    sg.getVertices().removeAll(currentColor);
	    ++color;
	}

	return color;
    }

    public static void main(String[] args)
    {
	Grph g = new InMemoryGrph();
	g.grid(1, 1);
	int c = new GreedyChromaticNumber().compute(g);
	System.out.println(c);
    }
}
