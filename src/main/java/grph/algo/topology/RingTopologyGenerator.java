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

import java.util.Iterator;

import grph.Grph;
import grph.in_memory.InMemoryGrph;
import it.unimi.dsi.fastutil.ints.IntIterator;
import it.unimi.dsi.fastutil.ints.IntSet;
import toools.collections.primitive.SelfAdaptiveIntSet;
import toools.collections.primitive.IntCursor;
import toools.collections.primitive.LucIntSet;

/**
 * Link the given nodes as a chain.
 * 
 * @author lhogie
 * 
 */
public class RingTopologyGenerator extends RandomizedTopologyTransform
{
	public boolean isDirected()
	{
		return directed;
	}

	public void setDirected(boolean directed)
	{
		this.directed = directed;
	}

	private boolean directed;

	@Override
	public void compute(Grph graph)
	{
		ring(graph, graph.getVertices(), isDirected());
	}

	public static IntSet ring(Grph graph, IntSet vertices, boolean directed)
	{
		// as many edges as vertices
		IntSet edges = new SelfAdaptiveIntSet(vertices.size());

		if (vertices.size() > 1)
		{
			IntIterator i = vertices.iterator();
			int predecessor = i.nextInt();
			int first = predecessor;

			while (i.hasNext())
			{
				int a = i.nextInt();
				edges.add(graph.addSimpleEdge(predecessor, a, directed));
				predecessor = a;
			}

			edges.add(graph.addSimpleEdge(predecessor, first, directed));
		}

		return edges;
	}

	public static void main(String[] args)
	{
		Grph g = new InMemoryGrph();
		g.addNVertices(10);
		System.out.println("*** " + g.getVertices().size());
		g.ring();
		g.display();
	}

	public static IntSet ring(Grph g, boolean directed)
	{
		return ring(g, g.getVertices(), directed);
	}

}
