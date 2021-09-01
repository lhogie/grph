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
 
 

package grph.algo.topology;

import grph.Grph;
import grph.in_memory.InMemoryGrph;
import it.unimi.dsi.fastutil.ints.IntSet;
import toools.collections.primitive.IntCursor;

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

		for (IntCursor v : IntCursor.fromFastUtil(graph.getVertices()))
		{
			neighbors[v.value] = graph.getKClosestNeighbors(v.value, k, null);
		}

		// for (V v : new ArrayList<V>(graph.getVertices()))
		for (IntCursor v : IntCursor.fromFastUtil(graph.getVertices()))
		{
			for (IntCursor n : IntCursor.fromFastUtil(neighbors[v.value]))
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