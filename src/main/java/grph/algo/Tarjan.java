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
Julien Deantoni (I3S, Université Cote D'Azur, Saclay) 

*/
 
 
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

package grph.algo;

import java.util.Arrays;
import java.util.BitSet;
import java.util.Collection;
import java.util.HashSet;

import grph.Grph;
import grph.in_memory.InMemoryGrph;
import it.unimi.dsi.fastutil.ints.IntSet;
import toools.UnitTests;
import toools.collections.LucIntSets;
import toools.collections.primitive.IntQueue;
import toools.collections.primitive.SelfAdaptiveIntSet;

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
		Arrays.fill(indexes, - 1);

		int[] lowlink = new int[g.getVertices().getGreatest() + 1];
		Arrays.fill(lowlink, - 1);

		IntQueue S = new IntQueue();
		BitSet presenceInStack = new BitSet();
		Collection<IntSet> cycles = new HashSet<IntSet>();

		for (int v : g.getVertices().toIntArray())
		{
			if (indexes[v] == - 1)
			{
				strongconnect(v, g, indexes, lowlink, S, presenceInStack, cycles);
			}
		}

		return cycles;
	}

	int index = 0;

	private void strongconnect(int v, Grph g, int[] indexes, int[] lowlink,
			IntQueue stack, BitSet presenceInStack, Collection<IntSet> cycles)
	{
		// Set the depth index for v to the smallest unused index
		indexes[v] = lowlink[v] = index++;
		stack.add(v);
		presenceInStack.set(v);

		// Consider successors of v
		for (int w : g.getOutNeighbors(v).toIntArray())
		{
			if (indexes[w] == - 1)
			{
				// Successor w has not yet been visited; recurse on it
				strongconnect(w, g, indexes, lowlink, stack, presenceInStack, cycles);
				lowlink[v] = Math.min(lowlink[v], lowlink[w]);
			}
			else if (presenceInStack.get(w))
			{
				// Successor w is in stack S and hence in the current SCC
				lowlink[v] = Math.min(lowlink[v], indexes[w]);
			}
		}

		// If v is a root node, pop the stack and generate an SCC
		if (lowlink[v] == indexes[v])
		{
			// start a new strongly connected component
			IntSet s = new SelfAdaptiveIntSet(0);
			int w = 0;

			do
			{
				w = stack.pop();
				presenceInStack.clear(w);
				// add w to current strongly connected component
				s.add(w);
			}
			while (w != v);

			// output the current strongly connected component
			cycles.add(s);
		}
	}

	private static void test(String[] args)
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

		Collection<IntSet> r = tarjan(g);

		UnitTests.ensure(r.contains(LucIntSets.create(1, 2, 5)));
		UnitTests.ensure(r.contains(LucIntSets.create(6, 7)));
		UnitTests.ensure(r.contains(LucIntSets.create(3, 4, 8)));
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
