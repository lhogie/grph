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
import toools.StopWatch;

/**
 * 
 * From the SageMath documentation: First create a ring over n nodes. Then each
 * node in the ring is connected with its k nearest neighbors. Then shortcuts
 * are created by adding new edges as follows: for each edge u-v in the
 * underlying 'n-ring with k nearest' neighbors'; with probability p add a new
 * edge u-w with" + "randomly-chosen existing node w. Takes a non-empty graph as
 * input.
 * 
 * @author lhogie
 * 
 */

public class RandomNewmanWattsStrogatzTopologyGenerator extends RandomizedTopologyTransform
{
	private int k = 1;
	private double p = 0.5;

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

	public double getP()
	{
		return p;
	}

	public void setP(double p)
	{
		if (p < 0 || p > 1)
			throw new IllegalArgumentException("invalid probability: " + p);

		this.p = p;
	}

	@Override
	public void compute(Grph g)
	{
		int n = g.getVertices().size();
		g.ring();

		g.connectToKClosestNeighbors(k);

		// if the network is a clique
		// creating shortcuts does not make sense
		if (k < n / 2)
		{
			for (int u : g.getVertices().toIntArray())
			{
				for (int e : g.getEdgesIncidentTo(u).toIntArray())
				{
					// if the vertex is not connected to all vertices, there's
					// room for a shortcut
					if (g.getVertexDegree(u, Grph.TYPE.vertex, Grph.DIRECTION.in_out) < n - 1)
					{
						int v = g.getTheOtherVertex(e, u);

						if (getPRNG().nextDouble() < getP())
						{
							System.out.println(1);
							int w;
							do
							{
								w = g.getVertices().pickRandomElement(getPRNG());

							} while (w == u || g.areVerticesAdjacent(u, w));

							System.out.println(2);
							g.addUndirectedSimpleEdge(u, w);
							g.removeEdge(e);
						}
					}
				}
			}
		}
	}

	public static void main(String[] args)
	{
		System.out.println("coucou");
		StopWatch t = new StopWatch();
		Grph g = new InMemoryGrph();
		g.addNVertices(15);
		RandomNewmanWattsStrogatzTopologyGenerator.compute(g, 3, 0.0);
		g.display();
	}

	public static void compute(Grph g, int k, double p)
	{
		RandomNewmanWattsStrogatzTopologyGenerator tg = new RandomNewmanWattsStrogatzTopologyGenerator();
		tg.setK(k);
		tg.setP(p);
		tg.compute(g);
	}

}
