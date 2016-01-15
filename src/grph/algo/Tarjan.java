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
import grph.in_memory.InMemoryGrph;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

import toools.collections.IntQueue;
import toools.set.DefaultIntSet;
import toools.set.IntSet;

public class Tarjan extends StronglyConnectedComponentsAlgorithm
{
	public static Collection<IntSet> tarjan(Grph g)
	{
		return new Tarjan().compute(g);
	}

	@Override
	public Collection<IntSet> compute(Grph g)
	{
		int[] indexes = new int[g.getVertices().getGreatest() + 1];
		Arrays.fill(indexes, -1);

		int[] lowlink = new int[g.getVertices().getGreatest() + 1];
		Arrays.fill(lowlink, -1);

		IntQueue S = new IntQueue();
		Collection<IntSet> cycles = new HashSet<IntSet>();

		for (int v : g.getVertices().toIntArray())
		{
			if (indexes[v] == -1)
			{
				strongconnect(v, g, indexes, lowlink, S, cycles);
			}
		}

		return cycles;
	}

	int index = 0;

	private void strongconnect(int v, Grph g, int[] indexes, int[] lowlink, IntQueue stack, Collection<IntSet> cycles)
	{
		// Set the depth index for v to the smallest unused index
		indexes[v] = lowlink[v] = index++;
		stack.add(v);

		// Consider successors of v
		for (int w : g.getOutNeighbors(v).toIntArray())
		{
			if (indexes[w] == -1)
			{
				// Successor w has not yet been visited; recurse on it
				strongconnect(w, g, indexes, lowlink, stack, cycles);
				lowlink[v] = Math.min(lowlink[v], lowlink[w]);
			}
			else if (stack.contains(w))
			{
				// Successor w is in stack S and hence in the current SCC
				lowlink[v] = Math.min(lowlink[v], indexes[w]);
			}
		}

		// If v is a root node, pop the stack and generate an SCC
		if (lowlink[v] == indexes[v])
		{
			// start a new strongly connected component
			IntSet s = new DefaultIntSet();
			int w = 0;

			do
			{
				w = stack.pop();
				// add w to current strongly connected component
				s.add(w);
			} while (w != v);

			// output the current strongly connected component
			cycles.add(s);
		}
	}

	public static void main(String[] args)
	{
		Grph g = new InMemoryGrph();
		g.addDirectedSimpleEdge(1, 2);
		g.addDirectedSimpleEdge(2, 3);
		g.addDirectedSimpleEdge(3, 4);
		g.addDirectedSimpleEdge(4, 3);
		g.addDirectedSimpleEdge(4, 8);
		g.addDirectedSimpleEdge(8, 4);
		g.addDirectedSimpleEdge(8, 7);
		g.addDirectedSimpleEdge(7, 6);
		g.addDirectedSimpleEdge(6, 7);
		g.addDirectedSimpleEdge(5, 6);
		g.addDirectedSimpleEdge(5, 1);
		g.addDirectedSimpleEdge(2, 5);
		g.addDirectedSimpleEdge(2, 6);
		g.addDirectedSimpleEdge(3, 7);

		g.display();
		System.out.println(tarjan(g));
	}
}
