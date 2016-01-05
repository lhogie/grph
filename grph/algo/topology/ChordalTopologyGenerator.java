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

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import toools.math.MathsUtilities;
import toools.set.IntSets;

import com.carrotsearch.hppc.IntArrayList;

/**
 * 3-chordal topology.
 * 
 * @author Issam Tahiri
 * 
 */
public class ChordalTopologyGenerator extends RandomizedTopologyTransform
{

    public static void clique(Grph graph, IntArrayList vertices, boolean[][] connected, IntArrayList unconnectedV)
    {
	for (int i = 0; i < vertices.size(); i++)
	{
	    for (int j = 0; j < i; j++)
	    {
		int p = unconnectedV.indexOf(vertices.get(i));
		int q = unconnectedV.indexOf(vertices.get(j));
		if (!connected[p][q])
		{
		    graph.addUndirectedSimpleEdge(vertices.get(i), vertices.get(j));
		    connected[p][q] = true;
		    connected[q][p] = true;
		}
	    }
	}
    }

    @Override
    public void compute(Grph graph)
    {
	IntArrayList unconnectedV = graph.getVertices().toIntArrayList();

	List<IntArrayList> Q = new ArrayList<IntArrayList>();
	IntArrayList Q0 = new IntArrayList();
	int cardCliqueInitiale = (int) (MathsUtilities.pickRandomBetween(1, 10, getPRNG()));

	for (int a = 0; a < cardCliqueInitiale; a++)
	{
	    Q0.add(unconnectedV.get(a));
	}

	Q.add(Q0);
	IntArrayList S = new IntArrayList();
	S.add(Q0.size());

	int l = 1;

	for (int vertexIndex = cardCliqueInitiale; vertexIndex < graph.getVertices().size(); vertexIndex++)
	{
	    int i = (int) (MathsUtilities.pickRandomBetween(0, l, getPRNG()));
	    int t = (int) (MathsUtilities.pickRandomBetween(1, S.get(i) + 1, getPRNG()));

	    if (t == S.get(i))
	    {
		Q.get(i).add(unconnectedV.get(vertexIndex));
		S.set(i, S.get(i) + 1);
	    }
	    else
	    {
		IntArrayList Qtemp = (IntArrayList) IntSets.getRandomSubset(Q.get(i), t, getPRNG());
		++l;
		Qtemp.add(unconnectedV.get(vertexIndex));
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
