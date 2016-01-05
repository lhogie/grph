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

package grph.algo.search;

import grph.path.ArrayListPath;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import toools.set.IntSet;

import com.carrotsearch.hppc.IntArrayList;

public class SearchResult
{
	public final int[] distances;
	public final int[] predecessors;
	public final IntArrayList visitOrder = new IntArrayList();


	public SearchResult(int i)
	{
		this.distances = new int[i];
		Arrays.fill(this.distances, -1);

		this.predecessors = new int[i];
		Arrays.fill(this.predecessors, -1);
	}

	public int farestVertex()
	{
		return visitOrder.get(visitOrder.size() - 1);
	}

	public int maxDistance()
	{
		return distances[farestVertex()];
	}
	
	public ArrayListPath computePathTo(int v)
	{
		ArrayListPath p = new ArrayListPath();

		while (v >= 0)
		{
			p.extend(v);
			v = predecessors[v];
		}

		p.reverse();
		return p;
	}

	
	
	public String toString(IntSet vertices)
	{
		List<String> b = new ArrayList<String>();
		
		for (int v : vertices.toIntArray())
		{
			b.add(v + " d=" + distances[v]);
		}
		
		return b.toString();
	}

}
