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
 
 package grph.algo;

import grph.Grph;
import grph.algo.search.SearchResult;
import grph.algo.topology.ClassicalGraphs;
import grph.in_memory.InMemoryGrph;
import grph.properties.NumericalProperty;
import it.unimi.dsi.fastutil.ints.IntSet;
import toools.collections.primitive.LucIntSet;

public class SpanningTree
{
	public static Grph computeBFSBasedSpanningTree(Grph g)
	{
		return computeBFSBasedSpanningTree(g, g.getVertices().getGreatest(), g.isDirected());
	}

	public static Grph computeBFSBasedSpanningTree(Grph g, int root, boolean directed)
	{
		SearchResult bfs = g.bfs(root);
		Grph spanningTree = new InMemoryGrph();

		for (int v : g.getVertices().toIntArray())
		{
			if (v != root)
			{
				int predecessor = bfs.predecessors[v];
				IntSet edges = g.getEdgesConnecting(predecessor, v);
				int anEdge = edges.iterator().nextInt();
				spanningTree.addSimpleEdge(predecessor, anEdge, v, directed);
			}
		}

		return spanningTree;
	}

	public static void main(String[] args)
	{
		Grph g = ClassicalGraphs.grid(5, 5);
	
		System.out.println(g.isDirected());
		Grph spanningTree = computeBFSBasedSpanningTree(g);

		spanningTree.setVerticesLabel(new NumericalProperty("vl") {
			@Override
			public long getValue(int e)
			{
				return e;
			}
		});
		spanningTree.display();
	}
}
