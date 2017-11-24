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

import java.util.Random;

import grph.Grph;
import grph.in_memory.InMemoryGrph;
import grph.path.Path;

public class PlanarGraphTopologyGenerator extends RandomizedTopologyTransform
{
	private Random prng = new Random();

	public void setPrng(Random prng)
	{
		this.prng = prng;
	}

	public Random getPrng()
	{
		return prng;
	}

	/**
	 * For every edge: - pick random in and out edge degree and connect it to
	 * random neighbors
	 * 
	 * @param g
	 * @param n
	 * @param prng
	 */
	@Override
	public void compute(Grph g)
	{
		new AsymmetricTopologyGenerator().compute(g);
		removeDoubleLinks(g);
		removeSomeEdges(g, 0);
	}

	private void removeSomeEdges(Grph g, double ratio)
	{
		int attempts = 0;
		int maxAttemps = 1000;
		int targetNumberOfEdges = (int) (g.getNumberOfEdges() * ratio);

		while (g.getNumberOfEdges() > targetNumberOfEdges)
		{
			int e = g.getEdges().pickRandomElement(prng);

			int tail = g.getDirectedSimpleEdgeTail(e);
			int head = g.getDirectedSimpleEdgeHead(e);
			g.removeEdge(e);

			if (!g.isStronglyConnected())
			{
				g.addDirectedSimpleEdge(tail, head);

				if (++attempts == maxAttemps)
				{
					return;
				}
			}
			else
			{
				attempts = 0;
			}
		}
	}

	private void removeDoubleLinks(Grph g)
	{
		int attempts = 0;
		int maxAttemps = 1000;

		while (true)
		{
			int e = g.getEdges().pickRandomElement(prng);

			if (!g.getOppositeEdges(e).isEmpty())
			{
				int tail = g.getDirectedSimpleEdgeTail(e);
				int head = g.getDirectedSimpleEdgeHead(e);
				g.removeEdge(e);

				if (!g.isStronglyConnected())
				{
					g.addDirectedSimpleEdge(tail, head);

					if (++attempts == maxAttemps)
					{
						return;
					}
				}
				else
				{
					attempts = 0;
				}
			}
			else
			{
				if (++attempts == maxAttemps)
				{
					break;
				}
			}
		}
	}

	public static Grph perform(Grph g, Random prng)
	{
		if (g.getNumberOfEdges() > 0)
			throw new IllegalArgumentException();

		int side = 5;
		g.grid(side, side, true, true, false);
		PlanarGraphTopologyGenerator tg = new PlanarGraphTopologyGenerator();
		tg.setPrng(prng);
		tg.compute(g);
		return g;
	}

	public static void main(String[] args)
	{
		Grph g = new InMemoryGrph();
		g.grid(10, 10, true, true, false);
		new PlanarGraphTopologyGenerator().compute(g);

		Random r = new Random();

		// get a vertex on the border
		int src = g.getFartestVertex(g.getVertices().pickRandomElement(r));

		// get a vertex far from it
		int dest = g.getFartestVertex(src);
		
		System.out.println(src + " => " + dest);
		
		g.display();

		g.getVertexShapeProperty().setValue(src, 1);
		g.getVertexLabelProperty().setValue(src, "D");
		g.getVertexShapeProperty().setValue(dest, 1);
		g.getVertexLabelProperty().setValue(dest, "A");
		
		Path p = g.getShortestPath(src, dest);
		p.setColor(g, 4);
		g.display();
	}

}
