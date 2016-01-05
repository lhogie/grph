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

package grph.in_memory;

import grph.Grph;
import grph.TopologyListener;
import toools.collections.AutoGrowingArrayList;
import toools.set.IntSet;

import com.carrotsearch.hppc.cursors.IntCursor;

abstract class NeighboursCache_OLD
{
	private final AutoGrowingArrayList<IntSet> neighborSets;
	private final Grph g;

	public NeighboursCache_OLD(Grph graph)
	{
		this.g = graph;
		neighborSets = new AutoGrowingArrayList<IntSet>();
		graph.getTopologyListeners().add(new L());
	}

	protected Grph getGraph()
	{
		return g;
	}

	public IntSet getNeighbours(int v)
	{
		assert g.getVertices().contains(v) : "vertex " + v + " not in graph";
		IntSet n = neighborSets.get(v);

		if (n == null)
		{
			synchronized (this)
			{
				neighborSets.set(v, n = computeNeighbors(v));
			}
		}

		return n;
	}

	protected abstract IntSet computeNeighbors(int vertex);

	private class L implements TopologyListener
	{
		@Override
		public void vertexAdded(Grph graph, int vertex)
		{
			// the new vertex is isolated, neighborhood hence don't change
		}

		@Override
		public void vertexRemoved(Grph graph, int vertex)
		{
			// free cache entry
			neighborSets.set(vertex, null);
		}

		@Override
		public void directedSimpleEdgeAdded(Grph graph, int edge,
				int src, int dest)
		{
			neighborSets.set(src, null);
			neighborSets.set(dest, null);
		}

		@Override
		public void undirectedSimpleEdgeAdded(Grph graph, int edge,
				int a, int b)
		{
			neighborSets.set(a, null);
			neighborSets.set(b, null);
		}

		@Override
		public void undirectedHyperEdgeAdded(Grph graph, int edge)
		{
		}

		@Override
		public void directedHyperEdgeAdded(Grph graph, int edge)
		{
		}

		@Override
		public void directedSimpleEdgeRemoved(Grph graph, int edge,
				int a, int b)
		{
			neighborSets.set(a, null);
			neighborSets.set(b, null);
		}

		@Override
		public void undirectedSimpleEdgeRemoved(Grph graph, int edge,
				int a, int b)
		{
			neighborSets.set(a, null);
			neighborSets.set(b, null);
		}

		@Override
		public void undirectedHyperEdgeRemoved(Grph graph, int edge,
				IntSet incidentVertices)
		{
			for (IntCursor v : incidentVertices)
			{
				neighborSets.set(v.value, null);
			}
		}

		@Override
		public void directedHyperEdgeRemoved(Grph graph, int edge,
				IntSet src, IntSet dest)
		{
			for (IntCursor v : src)
			{
				neighborSets.set(v.value, null);
			}

			for (IntCursor v : dest)
			{
				neighborSets.set(v.value, null);
			}
		}

		@Override
		public void vertexAddedToDirectedHyperEdgeTail(Grph graph,
				int e, int v)
		{
			for (IntCursor s : graph.getDirectedHyperEdgeHead(e))
			{
				neighborSets.set(s.value, null);
			}

			neighborSets.set(v, null);
		}

		@Override
		public void vertexAddedToDirectedHyperEdgeHead(Grph graph,
				int e, int v)
		{
			for (IntCursor s : graph.getDirectedHyperEdgeTail(e))
			{
				neighborSets.set(s.value, null);
			}
			neighborSets.set(v, null);
		}

		@Override
		public void vertexAddedToUndirectedSimpleEdge(Grph graph,
				int edge, int vertex)
		{
			for (IntCursor s : graph.getUndirectedHyperEdgeVertices(edge))
			{
				neighborSets.set(s.value, null);
			}

			neighborSets.set(vertex, null);
		}

		@Override
		public void vertexRemovedFromUndirectedHyperEdge(Grph g,
				int edge, int vertex)
		{
			for (IntCursor s : g.getUndirectedHyperEdgeVertices(edge))
			{
				neighborSets.set(s.value, null);
			}

			neighborSets.set(vertex, null);
		}

		@Override
		public void vertexRemovedFromDirectedHyperEdgeTail(Grph g,
				int e, int v)
		{
			for (IntCursor s : g.getDirectedHyperEdgeHead(e))
			{
				neighborSets.set(s.value, null);
			}

			neighborSets.set(v, null);
		}

		@Override
		public void vertexRemovedFromDirectedHyperEdgeHead(Grph g,
				int e, int v)
		{
			for (IntCursor s : g.getDirectedHyperEdgeTail(e))
			{
				neighborSets.set(s.value, null);
			}

			neighborSets.set(v, null);
		}

	}
}
