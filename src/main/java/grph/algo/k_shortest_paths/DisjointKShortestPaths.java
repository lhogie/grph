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
import java.util.List;
import java.util.Random;

import grph.Grph;
import grph.in_memory.InMemoryGrph;
import grph.path.Path;
import grph.properties.NumericalProperty;

public class DisjointKShortestPaths extends KShortestPathsAlgorithm
{
	@Override
	public List<Path> compute(Grph g, final int s, final int t, final int k, NumericalProperty weights)
	{
		List<Path> r = new ArrayList();

		// we work on a clone because we will alter the graph
		g = g.clone();

		while (r.size() < k)
		{
			Path p = g.getShortestPath(s, t, weights);
			int[] seq = p.toVertexArray();

			if (seq == null)
			{
				break;
			}
			else
			{
				r.add(p);

				for (int i = 1; i < seq.length - 1; ++i)
				{
					g.removeVertex(seq[i]);
				}
			}
		}

		return r;
	}

	public static void main(String[] args)
	{
		Grph g = new InMemoryGrph();
		g.grid(6, 6);
		g.getShortestPath(0, 37, null).toVertexArray();

		g.display();
		int s = g.getVertices().pickRandomElement(new Random());
		int t = g.getVertices().pickRandomElement(new Random());
		List<Path> paths = new DisjointKShortestPaths().compute(g, s, t, 10, null);

		for (Path p : paths)
		{
			p.setColor(g, 0);
		}

		g.highlightVertex(s, 1);
		g.highlightVertex(t, 2);
	}
}
