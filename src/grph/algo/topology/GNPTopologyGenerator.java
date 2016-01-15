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

import java.util.Random;

import toools.set.IntSet;

/**
 * Erdos and Renyi GNP model: connect each pair of vertices according to a given
 * probability.
 * 
 * @author lhogie
 * 
 */
public class GNPTopologyGenerator extends ErdosRenyiRandomizedScheme
{
	private double p = 0.5;

	public double getProbability_()
	{
		return p;
	}

	public void setProbability(double probability_)
	{
		if (probability_ < 0 || probability_ > 1)
			throw new IllegalArgumentException("invalid probability: " + probability_);

		this.p = probability_;
	}

	public static void compute(Grph g, double p)
	{
		GNPTopologyGenerator tg = new GNPTopologyGenerator();
		tg.setProbability(p);
		tg.compute(g);
	}

	@Override
	public void compute(Grph graph)
	{
		int n = graph.getVertices().size();
		int[] vertices = graph.getVertices().toIntArray();

		// vertices are ordered

		for (int i = 0; i < n; ++i)
		{
			int v1 = vertices[i];
			int loopEnd = acceptLoops() ? i : i - 1;

			for (int j = 0; j <= loopEnd; ++j)
			{
				if (getPRNG().nextDouble() < p)
				{
					// connects the two vertices
					int v2 = vertices[j];
					graph.addUndirectedSimpleEdge(v1, v2);
				}
			}
		}
	}

	public static void main(String[] args)
	{
		ClassLoader.getSystemClassLoader().setDefaultAssertionStatus(true);
		int n = 50;
		double p = 0.01;
		System.out.println("np=" + (n * p));
		Grph g = new InMemoryGrph();
		g.addNVertices(n);
		GNPTopologyGenerator.compute(g, p, false, new Random(1));
		System.out.println(g.getDescription());
		IntSet gcc = g.getLargestConnectedComponent();
		System.out.println("size of the greatest cc is " + gcc.size());
		// g.display();

	}

	public static void compute(Grph g, double p, boolean acceptLoops, Random r)
	{
		GNPTopologyGenerator tg = new GNPTopologyGenerator();
		tg.setProbability(p);
		tg.setAcceptLoops(acceptLoops);
		tg.setPRNG(r);
		tg.compute(g);
	}
}
