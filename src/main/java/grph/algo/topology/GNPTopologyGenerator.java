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

import java.util.Random;

import grph.Grph;
import grph.in_memory.InMemoryGrph;
import it.unimi.dsi.fastutil.ints.IntSet;
import toools.collections.primitive.LucIntSet;

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
