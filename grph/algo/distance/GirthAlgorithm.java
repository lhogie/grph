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
 
 package grph.algo.distance;

import grph.Grph;
import grph.GrphAlgorithm;
import grph.algo.topology.ClassicalGraphs;
import toools.set.DefaultIntSet;
import toools.set.IntSet;

import com.carrotsearch.hppc.IntIntMap;
import com.carrotsearch.hppc.IntIntOpenHashMap;
import com.carrotsearch.hppc.cursors.IntCursor;

/**
 * @author Gregory Morel
 */
@SuppressWarnings("serial")
public class GirthAlgorithm extends GrphAlgorithm<IntSet>
{

    /**
     * @return an IntSet that contains vertices of a shortest cycle of
     *         <code>g</code>
     */
    @Override
    public IntSet compute(Grph g)
    {
	int girth = Integer.MAX_VALUE;
	IntSet cycle = new DefaultIntSet();

	// We perform a BFS for each vertex
	for (IntCursor c : g.getVertices())
	{
	    int v = c.value;

	    IntSet visited = new DefaultIntSet();

	    IntSet queue = new DefaultIntSet();
	    queue.add(v);

	    IntIntMap parent = new IntIntOpenHashMap();
	    parent.put(v, -1);

	    IntIntMap distance = new IntIntOpenHashMap();
	    distance.put(v, 0);

	    while (!queue.isEmpty())
	    {
		int x = queue.getGreatest(); // No matter what element we pick
					     // from R
		visited.add(x);
		queue.remove(x);
		for (int y : g.getNeighbours(x).toIntArray())
		{
		    if (y != parent.get(x))
		    { // Do not merge the if
			if (!visited.contains(y))
			{
			    parent.put(y, x);
			    distance.put(y, distance.get(x) + 1);
			    queue.add(y);
			}
			else
			{
			    int d = distance.get(x) + distance.get(y) + 1;
			    if (d < girth)
			    {
				girth = d;
				cycle = new DefaultIntSet();

				int u = x;
				while (u != -1)
				{
				    cycle.add(u);
				    u = parent.get(u);
				}
			    }
			}
		    }
		}
	    }
	}

	return cycle;
    }

    public static void main(String[] args)
    {
	Grph g = ClassicalGraphs.path(4).getComplement();
	g.display();
	IntSet shortestCycle = g.getShortestCycle();
	System.out.println("A shortest cycle of g is induced by " + shortestCycle + " so the girth is "
		+ shortestCycle.size());
    }
}
