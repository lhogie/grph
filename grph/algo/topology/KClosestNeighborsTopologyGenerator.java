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

package grph.algo.topology;

import grph.Grph;
import grph.in_memory.InMemoryGrph;
import toools.set.IntSet;

import com.carrotsearch.hppc.cursors.IntCursor;

/**
 * Connect each node to its k closest neighbors (k > 1).
 * 
 * @author lhogie
 * 
 */
public class KClosestNeighborsTopologyGenerator extends RandomizedTopologyTransform
{
	private int k = 1;

	public int getK()
	{
		return k;
	}

	public void setK(int k)
	{
		if (k < 0)
			throw new IllegalArgumentException("k must be >= 0");

		this.k = k;
	}

	@Override
	public void compute(Grph graph)
	{
		// the K-neighbors must be computed first because the algo will
		// change them
		IntSet[] neighbors = new IntSet[graph.getVertices().getGreatest() + 1];

		for (IntCursor v : graph.getVertices())
		{
			neighbors[v.value] = graph.getKClosestNeighbors(v.value, k, null);
		}

		// for (V v : new ArrayList<V>(graph.getVertices()))
		for (IntCursor v : graph.getVertices())
		{
			for (IntCursor n : neighbors[v.value])
			{
				if (graph.getEdgesConnecting(v.value, n.value).isEmpty())
				{
					graph.addUndirectedSimpleEdge(v.value, n.value);
				}
			}
		}
	}

	public static void main(String[] args)
	{
		Grph g = new InMemoryGrph();
		g.addNVertices(10);
		RingTopologyGenerator.ring(g, false);
		KClosestNeighborsTopologyGenerator.compute(g, 3);
		g.display();
	}

	public static void compute(Grph g, int i)
	{
		KClosestNeighborsTopologyGenerator tg = new KClosestNeighborsTopologyGenerator();
		tg.setK(i);
		tg.compute(g);
	}

}
