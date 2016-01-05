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

package grph;

import toools.set.IntSet;

import com.carrotsearch.hppc.IntObjectMap;
import com.carrotsearch.hppc.IntObjectOpenHashMap;

public class Cache
{
	private final IntObjectMap<Object> key_value = new IntObjectOpenHashMap();

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
