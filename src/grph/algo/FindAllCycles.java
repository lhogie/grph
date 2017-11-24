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
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntSet;
import toools.collections.primitive.BitVectorSet;
import toools.collections.primitive.IntCursor;
import toools.collections.primitive.LucIntSet;
import grph.Grph;
import grph.algo.topology.ClassicalGraphs;
import toools.thread.Generator;

public class FindAllCycles extends Generator<IntArrayList>
{
	private final Grph g;
	private final int[][] adj;
	private final IntArrayList currentPath = new IntArrayList();
	private final IntSet alreadyVisited = new BitVectorSet(0);
	private final IntArrayList currentNeighbor = new IntArrayList();
	private final boolean only;
	private final IntSet startingVertices;

	public FindAllCycles(Grph g)
	{
		this(g, g.getVertices(), true);
	}

	public FindAllCycles(Grph g, IntSet startingVertices, boolean only)
	{
		if (startingVertices == null)
			throw new NullPointerException();

		this.g = g;
		this.startingVertices = startingVertices;
		this.only = only;
		this.adj = g.getOutNeighborhoods();
	}

	@Override
	public void produce()
	{
		for (IntCursor c : IntCursor.fromFastUtil(startingVertices))
		{
			int startingVertex = c.value;
			currentPath.add(startingVertex);
			currentNeighbor.add(0);
			alreadyVisited.add(startingVertex);

			if (only)
			{
				// add all vertices lower than startingVertex
				for (IntCursor cc : IntCursor.fromFastUtil(g.getVertices()))
				{
					if (cc.value < startingVertex)
					{
						alreadyVisited.add(cc.value);
					}
				}
			}

			while ( ! currentPath.isEmpty())
			{
				int u = currentPath.get(currentPath.size() - 1);
				int d = adj[u].length;

				// we already have explored all neighbors of u
				if (currentNeighbor.get(currentNeighbor.size() - 1) == d)
				{
					currentPath.remove(currentPath.size() - 1);
					currentNeighbor.remove(currentNeighbor.size() - 1);
					alreadyVisited.remove(u);
					continue;
				}

				final int nextNeighbor = adj[u][currentNeighbor.get(currentNeighbor
						.size() - 1)];
				currentNeighbor.set(currentNeighbor.size() - 1,
						currentNeighbor.get(currentNeighbor.size() - 1) + 1);

				// we have found a circuit
				if (nextNeighbor == startingVertex)
				{
					// yield(currentPath)
					deliver(currentPath);
					continue;
				}

				if (alreadyVisited.contains(nextNeighbor)
						|| ! pathExists(nextNeighbor, startingVertex, alreadyVisited))
					continue;

				currentPath.add(nextNeighbor);
				alreadyVisited.add(nextNeighbor);
				currentNeighbor.add(0);
			}
		}

		return;
	}

	private boolean pathExists(final int src, final int dest, IntSet verticesToAvoid)
	{
		IntArrayList queue = new IntArrayList(g.getNumberOfVertices());
		queue.add(src);
		IntSet set = new BitVectorSet(0);
		set.add(src);
		set.addAll(verticesToAvoid);

		while ( ! queue.isEmpty())
		{
			int v = queue.remove(0);

			for (int n : adj[v])
			{
				if (n == dest)
					return true;

				if ( ! set.contains(n))
				{
					set.add(n);
					queue.add(n);
				}
			}
		}

		return false;
	}
	
	public static void main(String[] args)
	{
		int n = 2;
		Grph g = ClassicalGraphs.grid(n, n);

		FindAllCycles algo = new FindAllCycles(g);

		for (IntArrayList v : algo)
		{
			System.out.println(v);
		}
		
		System.out.println("DONE");
	}
}
