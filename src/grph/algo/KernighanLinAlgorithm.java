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
import grph.GrphAlgorithm;
import grph.in_memory.InMemoryGrph;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import toools.StopWatch;
import toools.set.IntSet;
import toools.set.IntSets;

import com.carrotsearch.hppc.IntArrayList;
import com.carrotsearch.hppc.cursors.IntCursor;
/**
 * Cuts the graph according to the Kernighan Lin algorithm (KL)
 * @author lhogie
 *
 */
 class KernighanLinAlgorithm extends GrphAlgorithm
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

	Iterator<IntCursor> iterator = vertices.iterator();
	int j = 0;

	for (; j < nVertices / 2; ++j)
	{
	    A.add(iterator.next().value);
	}
	for (; j < nVertices; ++j)
	{
	    B.add(iterator.next().value);
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
	    IntSet Ac = IntSets.from(A.toArray());
	    IntSet Bc = IntSets.from(B.toArray());

	    for (int p = 0; p < nVertices / 2; ++p)
	    {
		int damax = Integer.MIN_VALUE;
		int amax = -1;

		// computes all D(v, l);
		for (IntCursor v : Ac)
		{
		    int test = d(v.value, Ac, Bc, g);

		    if (test > damax)
		    {
			damax = test;
			amax = v.value;
		    }
		}

		int dbmax = Integer.MIN_VALUE;
		int bmax = -1;

		// computes all D(v, l);
		for (IntCursor v : Bc)
		{
		    int test = d(v.value, Bc, Ac, g);

		    if (test > dbmax)
		    {
			dbmax = test;
			bmax = v.value;
		    }
		}

		gArray[p] = damax + dbmax - (g.getEdgesConnecting(amax, bmax).isEmpty() ? 0 : 2);
		aArray[p] = amax;
		bArray[p] = bmax;

		Ac.remove(amax);
		Bc.remove(bmax);
	    }

	    int sum = 0;
	    G = Integer.MIN_VALUE;
	    int k = -1;

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
		    A.remove(aArray[i]);
		    A.add(bArray[i]);
		    B.remove(bArray[i]);
		}
	    }

	    iterationN++;
	} while (G > 0);

	System.out.println(iterationN);
	Set<IntSet> res = new HashSet();
	res.add(IntSets.from(A.toArray()));
	res.add(IntSets.from(B.toArray()));
	return res;
    }

    int d(int v, IntSet container, IntSet other, Grph g)
    {
	int iv = 0, ev = 0;
	IntSet neighbors = g.getOutNeighbors(v);
	neighbors.remove(v);

	for (IntCursor n : neighbors)
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

	for (IntCursor v : a)
	{
	    for (IntCursor n : g.getOutNeighbors(v.value))
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
