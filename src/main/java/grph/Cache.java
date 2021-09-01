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
 
 

package grph;

import java.io.Serializable;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntSet;

public class Cache implements Serializable
{
	private final Int2ObjectMap<Object> key_value = new Int2ObjectOpenHashMap<>();

	public Cache(Grph g)
	{
		g.getTopologyListeners().add(new TopologyListener() {

			@Override
			public void vertexRemoved(Grph graph, int vertex)
			{
				clear();
			}

			@Override
			public void vertexAdded(Grph graph, int vertex)
			{
				clear();
			}

			@Override
			public void directedSimpleEdgeAdded(Grph Grph, int edge, int src, int dest)
			{
				clear();
			}

			@Override
			public void undirectedSimpleEdgeAdded(Grph Grph, int edge, int a, int b)
			{
				clear();
			}

			@Override
			public void undirectedHyperEdgeAdded(Grph graph, int edge)
			{
				clear();
			}

			@Override
			public void directedHyperEdgeAdded(Grph graph, int edge)
			{
				clear();
			}

			@Override
			public void directedSimpleEdgeRemoved(Grph Grph, int edge, int a, int b)
			{
				clear();
			}

			@Override
			public void undirectedSimpleEdgeRemoved(Grph Grph, int edge, int a, int b)
			{
				clear();
			}

			@Override
			public void undirectedHyperEdgeRemoved(Grph graph, int edge, IntSet incidentVertices)
			{
				clear();
			}

			@Override
			public void directedHyperEdgeRemoved(Grph graph, int edge, IntSet src, IntSet dest)
			{
				clear();
			}

			@Override
			public void vertexAddedToDirectedHyperEdgeTail(Grph Grph, int e, int v)
			{
				clear();
			}

			@Override
			public void vertexAddedToDirectedHyperEdgeHead(Grph Grph, int e, int v)
			{
				clear();
			}

			@Override
			public void vertexAddedToUndirectedSimpleEdge(Grph Grph, int edge, int vertex)
			{
				clear();
			}

			@Override
			public void vertexRemovedFromUndirectedHyperEdge(Grph g, int edge, int vertex)
			{
				clear();
			}

			@Override
			public void vertexRemovedFromDirectedHyperEdgeTail(Grph g, int e, int v)
			{
				clear();
			}

			@Override
			public void vertexRemovedFromDirectedHyperEdgeHead(Grph g, int e, int v)
			{
				clear();
			}
		});

	}

	public void put(Object value, Object algo, Object... parameters)
	{
		key_value.put(computeKey(algo, parameters), value);
	}

	public Object get(Object algo, Object... parameters)
	{
		return key_value.get(computeKey(algo, parameters));
	}

	protected int computeKey(Object algo, Object... parameters)
	{
		StringBuilder b = new StringBuilder();
		b.append(algo.getClass().hashCode());
		b.append(':');

		for (Object o : parameters)
		{
			b.append(o.hashCode());
			b.append(':');
		}

		return b.hashCode();
	}

	private void clear()
	{
		key_value.clear();
	}
}
