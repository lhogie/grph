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
