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

package grph.algo.k_shortest_paths;

import edu.asu.emit.qyan.alg.control.YenTopKShortestPathsAlg;
import edu.asu.emit.qyan.alg.model.Graph;
import edu.asu.emit.qyan.alg.model.abstracts.BaseVertex;
import grph.Grph;
import grph.in_memory.InMemoryGrph;
import grph.path.ArrayListPath;
import grph.properties.NumericalProperty;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import toools.UnitTests;

public class YenTopKShortestPathsAlgorithm extends KShortestPathsAlgorithm
{
	@Override
	public List<ArrayListPath> compute(Grph g, int s, int t, int k, NumericalProperty weights)
	{

		if (weights != null)
			throw new IllegalArgumentException("unsupported");
		
		g = g.clone();

		while (g.getVertices().getDensity() < 1)
		{
			g.addVertex();
		}

		Graph h = new Graph();

		for (int v : g.getVertices().toIntArray())
		{
			h.addVertex(v);
		}

		for (int e : g.getEdges().toIntArray())
		{
			int a = g.getOneVertex(e);
			int b = g.getTheOtherVertex(e, a);
			h.add_edge(a, b, 1);
		}

		YenTopKShortestPathsAlg alg = new YenTopKShortestPathsAlg(h);
		List<edu.asu.emit.qyan.alg.model.Path> paths = alg.get_shortest_paths(h.get_vertex(s), h.get_vertex(t), k);

		List<ArrayListPath> grphPaths = new ArrayList();

		for (edu.asu.emit.qyan.alg.model.Path p : paths)
		{
			ArrayListPath pb = new ArrayListPath();

			for (BaseVertex v : p.get_vertices())
			{
				pb.extend(v.get_id());
			}

			grphPaths.add(pb);
		}

		return grphPaths;
	}

	public static void main(String[] args)
	{
		Grph g = new InMemoryGrph();
		g.grid(5, 5);
		g.addDirectedSimpleEdge(0, 24);
		g.display();
		List<ArrayListPath> paths = new YenTopKShortestPathsAlgorithm().compute(g, 0, 24, 2, null);

		for (int i = 0; i < paths.size(); ++i)
		{
			ArrayListPath path = paths.get(i);
			path.setColor(g, 5);
			System.out.println("(" + path + ")");
		}
	}

	private static void test()
	{
		Grph g = new InMemoryGrph();
		g.grid(5, 5);
		g.addDirectedSimpleEdge(0, 24);
		List<ArrayListPath> paths = new YenTopKShortestPathsAlgorithm().compute(g, 0, 24, 2, null);
		UnitTests.ensure(Arrays.equals(paths.get(0).toVertexArray(), new int[] { 0, 24 }));
		// UnitTests.ensureEquals(paths.get(1).getVertexSequence(),
		// IntArrayList.from(0, 5, 6, 7, 8, 13, 14, 19, 24));
	}

}
