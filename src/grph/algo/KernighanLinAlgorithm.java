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

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import grph.DefaultIntSet;
import grph.Grph;
import grph.GrphAlgorithm;
import grph.in_memory.InMemoryGrph;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntIterator;
import it.unimi.dsi.fastutil.ints.IntSet;
import toools.StopWatch;
import toools.collections.primitive.IntCursor;

/**
 * Cuts the graph according to the Kernighan Lin algorithm (KL)
 * 
 * @author lhogie
 *
 */
class KernighanLinAlgorithm extends GrphAlgorithm<Set<IntSet>>
{
	@Override
	public Set<IntSet> compute(Grph g)
	{
		return compute(g, 2);
	}

	public Set<IntSet> compute(Grph g, int n)
	{
		return compute(g, g.getVertices(), n);
	}

	private Set<IntSet> compute(Grph g, IntSet vertices, int n)
	{
		// if n is not a power of two, we can't divide
		if (Integer.bitCount(n) != 1)
			throw new IllegalArgumentException("n must be a power of 2");

		if (n == 2)
		{
			return cutIn2(g, vertices);
		}
		else
		{
			HashSet<IntSet> cut = new HashSet();

			for (IntSet c : cutIn2(g, vertices))
			{
				cut.addAll(compute(g, c, n / 2));
			}

			return cut;
		}
	}

	private Set<IntSet> cutIn2(Grph g, IntSet vertices)
	{
		IntArrayList A = new IntArrayList();
		IntArrayList B = new IntArrayList();
		int nVertices = vertices.size();

		if (nVertices % 2 != 0)
			throw new IllegalArgumentException("number of vertices must be even");

		// List<V> tmp = new ArrayList<V>(g.getVertices());
		// Collections.shuffle(tmp, new Random(0));

		IntIterator iterator = vertices.iterator();
		int j = 0;

		for (; j < nVertices / 2; ++j)
		{
			A.add(iterator.nextInt());
		}
		for (; j < nVertices; ++j)
		{
			B.add(iterator.nextInt());
		}
		// at this step, A and B are balanced

		int G;
		int[] gArray = new int[nVertices / 2];
		int[] aArray = new int[nVertices / 2];
		int[] bArray = new int[nVertices / 2];
		int iterationN = 1;

		do
		{
			// copy A and B to A1 and B1
			IntSet Ac = new DefaultIntSet();
			Ac.addAll(A);

			IntSet Bc = new DefaultIntSet();
			Bc.addAll(B);

			for (int p = 0; p < nVertices / 2; ++p)
			{
				int damax = Integer.MIN_VALUE;
				int amax = - 1;

				// computes all D(v, l);
				for (IntCursor v : IntCursor.fromFastUtil(Ac))
				{
					int test = d(v.value, Ac, Bc, g);

					if (test > damax)
					{
						damax = test;
						amax = v.value;
					}
				}

				int dbmax = Integer.MIN_VALUE;
				int bmax = - 1;

				// computes all D(v, l);
				for (IntCursor v : IntCursor.fromFastUtil(Bc))
				{
					int test = d(v.value, Bc, Ac, g);

					if (test > dbmax)
					{
						dbmax = test;
						bmax = v.value;
					}
				}

				gArray[p] = damax + dbmax
						- (g.getEdgesConnecting(amax, bmax).isEmpty() ? 0 : 2);
				aArray[p] = amax;
				bArray[p] = bmax;

				Ac.remove(amax);
				Bc.remove(bmax);
			}

			int sum = 0;
			G = Integer.MIN_VALUE;
			int k = - 1;

			for (int i = 0; i < nVertices / 2; ++i)
			{
				sum += gArray[i];

				if (sum > G)
				{
					k = i;
					G = sum;
				}
			}

			if (G > 0)
			{
				for (int i = 0; i <= k; ++i)
				{
					B.add(aArray[i]);
					A.removeInt(aArray[i]);
					A.add(bArray[i]);
					B.removeInt(bArray[i]);
				}
			}

			iterationN++;
		}
		while (G > 0);



		Set<IntSet> res = new HashSet<>();

		IntSet Aset = new DefaultIntSet();

		Aset.addAll(A);
		res.add(Aset);

		IntSet Bset = new DefaultIntSet();
		Bset.addAll(B);
res.add(Bset);
		return res;
	}

	int d(int v, IntSet container, IntSet other, Grph g)
	{
		int iv = 0, ev = 0;
		IntSet neighbors = g.getOutNeighbors(v);
		neighbors.remove(v);

		for (IntCursor n : IntCursor.fromFastUtil(neighbors))
		{
			if (container.contains(n.value))
			{
				++iv;
			}
			else if (other.contains(n.value))
			{
				++ev;
			}
		}

		return ev - iv;
	}

	public static void main(String[] args)
	{
		StopWatch sw = new StopWatch();
		Grph g = new InMemoryGrph();
		g.addNVertices(4000);
		g.glp();
		System.out.println("GLP generated in " + sw.getElapsedTime() + "ms");

		sw.reset();
		final KernighanLinAlgorithm kl = new KernighanLinAlgorithm();
		Set<IntSet> cut = kl.compute(g);
		System.out.println("KL computed in " + sw.getElapsedTime() + "ms");

		Iterator<IntSet> i = cut.iterator();
		IntSet a = i.next();
		IntSet b = i.next();

		System.out.println("cut size=" + g.getCutSize(a, b));
		System.out.println(a.size());
		System.out.println(b.size());
		Set duplica = new HashSet();

		for (IntCursor v : IntCursor.fromFastUtil(a))
		{
			for (IntCursor n : IntCursor.fromFastUtil(g.getOutNeighbors(v.value)))
			{
				if (b.contains(n.value))
				{
					duplica.add(n);
				}
			}
		}

		System.out.println(duplica.size() + " duplica");

	}
}
