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

package grph.algo.topology;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import grph.Grph;
import grph.in_memory.InMemoryGrph;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import toools.collections.Lists;
import toools.math.MathsUtilities;

/**
 * 3-chordal topology.
 * 
 * @author Issam Tahiri
 * 
 */
public class ChordalTopologyGenerator extends RandomizedTopologyTransform
{

	public static void clique(Grph graph, IntArrayList vertices, boolean[][] connected,
			IntArrayList unconnectedV)
	{
		for (int i = 0; i < vertices.size(); i++)
		{
			for (int j = 0; j < i; j++)
			{
				int p = unconnectedV.indexOf(vertices.getInt(i));
				int q = unconnectedV.indexOf(vertices.getInt(j));
				if ( ! connected[p][q])
				{
					graph.addUndirectedSimpleEdge(vertices.getInt(i), vertices.getInt(j));
					connected[p][q] = true;
					connected[q][p] = true;
				}
			}
		}
	}

	@Override
	public void compute(Grph graph)
	{
		IntArrayList unconnectedV = new IntArrayList(graph.getVertices());

		List<IntArrayList> Q = new ArrayList<IntArrayList>();
		IntArrayList Q0 = new IntArrayList();
		int cardCliqueInitiale = (int) (MathsUtilities.pickRandomBetween(1, 10,
				getPRNG()));

		for (int a = 0; a < cardCliqueInitiale; a++)
		{
			Q0.add(unconnectedV.getInt(a));
		}

		Q.add(Q0);
		IntArrayList S = new IntArrayList();
		S.add(Q0.size());

		int l = 1;

		for (int vertexIndex = cardCliqueInitiale; vertexIndex < graph.getVertices()
				.size(); vertexIndex++)
		{
			int i = (int) (MathsUtilities.pickRandomBetween(0, l, getPRNG()));
			int t = (int) (MathsUtilities.pickRandomBetween(1, S.getInt(i) + 1,
					getPRNG()));

			if (t == S.getInt(i))
			{
				Q.get(i).add(unconnectedV.getInt(vertexIndex));
				S.set(i, S.getInt(i) + 1);
			}
			else
			{
				IntArrayList Qtemp = (IntArrayList) Lists.getRandomSubset(Q.get(i), t,
						getPRNG());
				++l;
				Qtemp.add(unconnectedV.getInt(vertexIndex));
				Q.add(Qtemp);
				S.add(t + 1);
			}
		}

		int size = unconnectedV.size();
		boolean[][] connected = new boolean[size][size];

		for (IntArrayList clique : Q)
		{
			clique(graph, clique, connected, unconnectedV);
		}
	}

	public static Grph chordalGraph(int n, long seed)
	{
		Grph graph = new InMemoryGrph();
		graph.addNVertices(n);
		Random r = new Random(seed);
		ChordalTopologyGenerator g = new ChordalTopologyGenerator();
		g.setPRNG(r);

		g.compute(graph);

		return graph;
	}

}
