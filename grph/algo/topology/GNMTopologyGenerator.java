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
import toools.math.MathsUtilities;

/**
 * Erdos and Renyi GNM model: adds n edges to the graph, connecting random pairs
 * of vertices.
 * 
 * @author lhogie
 * 
 */
public class GNMTopologyGenerator extends ErdosRenyiRandomizedScheme
{
	private int numberOfEdges = -1;

	public int getNumberOfEdges()
	{
		return numberOfEdges;
	}

	public void setNumberOfEdges(int numberOfEdges)
	{
		if (numberOfEdges < 0)
			throw new IllegalArgumentException("invalid number of edges: " + numberOfEdges);

		this.numberOfEdges = numberOfEdges;
	}

	public static void compute(Grph g, int m, boolean acceptLoops)
	{
		GNMTopologyGenerator tg = new GNMTopologyGenerator();
		tg.setNumberOfEdges(m);
		tg.setAcceptLoops(acceptLoops);
		tg.compute(g);
	}

	@Override
	public void compute(Grph g)
	{
		int n = g.getVertices().size();

		if (n <= 0)
			throw new IllegalArgumentException("getNumberOfVertices() should be positive");

		int m = getNumberOfEdges();

		if (m < 0)
			throw new IllegalArgumentException("getNumberOfEdges() should be positive");

		int maximumNumberOfEdges = (n * (n - 1)) / 2;

		if (m > maximumNumberOfEdges)
			throw new IllegalArgumentException("too many edges requested, max is " + maximumNumberOfEdges);

		if (m < maximumNumberOfEdges / 2)
		{
			int[] verticesList = g.getVertices().toIntArray();

			while (g.getEdges().size() < m)
			{
				// gets 2 random vertices
				int v1 = verticesList[(int) MathsUtilities.pickRandomBetween(0, n, getPRNG())];
				int v2 = verticesList[(int) MathsUtilities.pickRandomBetween(0, n, getPRNG())];

				if (!g.areVerticesAdjacent(v1, v2) && (v1 != v2 || (v1 == v2 && acceptLoops())))
				{
					g.addUndirectedSimpleEdge(v1, v2);
				}
			}
		}
		else
		{
			// faster to create all possible edges and remove some
			g.clique();

			while (g.getEdges().size() > m)
			{
				int e = g.getEdges().pickRandomElement(getPRNG());
				g.removeEdge(e);
			}
		}
	}

	public static void main(String[] args)
	{
		Grph g = new InMemoryGrph();
		int n = 1000;
		int m = (n * (n - 1)) / 2;
		g.addNVertices(n);
		StopWatch sw = new StopWatch();
		GNMTopologyGenerator.compute(g, m, false);
		System.out.println(sw.getElapsedTime() + "ms");
		System.out.println(g.getEdges().size());

	}

}
