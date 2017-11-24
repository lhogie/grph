/* (C) Copyright 2009-2013 CNRS (Centre National de la Recherche Scientifique).

Licensed to the CNRS under one
or more contributor license agreements.  See the NOTICE file
distributed with this work for additional information
regarding copyright ownership.  The CNRS licenses this file
to you under the Apache License, Version 2.0 (the
"License"); you may not use this file except in compliance
with the License.  You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing,
software distributed under the License is distributed on an
"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
KIND, either express or implied.  See the License for the
specific language governing permissions and limitations
under the License.

*/

/* Contributors:

Luc Hogie (CNRS, I3S laboratory, University of Nice-Sophia Antipolis) 
Aurelien Lancin (Coati research team, Inria)
Christian Glacet (LaBRi, Bordeaux)
David Coudert (Coati research team, Inria)
Fabien Crequis (Coati research team, Inria)
Grégory Morel (Coati research team, Inria)
Issam Tahiri (Coati research team, Inria)
Julien Fighiera (Aoste research team, Inria)
Laurent Viennot (Gang research-team, Inria)
Michel Syska (I3S, Université Cote D'Azur)
Nathann Cohen (LRI, Saclay) 
Julien Deantoin (I3S, Université Cote D'Azur, Saclay) 

*/
 
 package grph.algo.labelling;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntSet;
import grph.Grph;
import grph.GrphAlgorithm;
import grph.in_memory.InMemoryGrph;
import toools.collections.Collections;
import toools.collections.primitive.SelfAdaptiveIntSet;
import toools.collections.primitive.LucIntSet;

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
	IntArrayList sortedVertices = new IntArrayList(sortedVerticesArray);
	int color = 0;

	// Each vertex will attempted to be colored with a single color each
	// iteration, and these vertices will be removed from the graph at the
	// end of each iteration
	while (!sg.getVertices().isEmpty())
	{
	    // This set will contain vertices that are colored with the
	    // current color of this iteration
	    IntSet currentColor = new SelfAdaptiveIntSet();

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
