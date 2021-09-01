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
 
 

package grph.algo.k_shortest_paths;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.asu.emit.qyan.alg.control.YenTopKShortestPathsAlg;
import edu.asu.emit.qyan.alg.model.Graph;
import edu.asu.emit.qyan.alg.model.abstracts.BaseVertex;
import grph.Grph;
import grph.in_memory.InMemoryGrph;
import grph.path.ArrayListPath;
import grph.properties.NumericalProperty;
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
		UnitTests.ensure(paths != null);
		UnitTests.ensure(paths.get(0) != null);
		UnitTests.ensure(paths.get(0).toVertexArray() != null);
		// UnitTests.ensureEquals(paths.get(1).getVertexSequence(),
		// IntArrayList.from(0, 5, 6, 7, 8, 13, 14, 19, 24));
	}

}
