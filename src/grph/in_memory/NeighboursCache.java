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
 
 

package grph.in_memory;

import grph.Grph;
import grph.TopologyListener;
import it.unimi.dsi.fastutil.ints.IntSet;
import toools.collections.AutoGrowingArrayList;
import toools.collections.primitive.IntCursor;
import toools.collections.primitive.LucIntSet;

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
			for (IntCursor v : IntCursor.fromFastUtil(incidentVertices))
			{
				neighborSets.set(v.value, null);
			}
		}

		@Override
		public void directedHyperEdgeRemoved(Grph graph, int edge,
				IntSet src, IntSet dest)
		{
			for (IntCursor v : IntCursor.fromFastUtil(src))
			{
				neighborSets.set(v.value, null);
			}

			for (IntCursor v : IntCursor.fromFastUtil(dest))
			{
				neighborSets.set(v.value, null);
			}
		}

		@Override
		public void vertexAddedToDirectedHyperEdgeTail(Grph graph,
				int e, int v)
		{
			for (IntCursor s : IntCursor.fromFastUtil(graph.getDirectedHyperEdgeHead(e)))
			{
				neighborSets.set(s.value, null);
			}

			neighborSets.set(v, null);
		}

		@Override
		public void vertexAddedToDirectedHyperEdgeHead(Grph graph,
				int e, int v)
		{
			for (IntCursor s : IntCursor.fromFastUtil(graph.getDirectedHyperEdgeTail(e)))
			{
				neighborSets.set(s.value, null);
			}
			neighborSets.set(v, null);
		}

		@Override
		public void vertexAddedToUndirectedSimpleEdge(Grph graph,
				int edge, int vertex)
		{
			for (IntCursor s : IntCursor.fromFastUtil(graph.getUndirectedHyperEdgeVertices(edge)))
			{
				neighborSets.set(s.value, null);
			}

			neighborSets.set(vertex, null);
		}

		@Override
		public void vertexRemovedFromUndirectedHyperEdge(Grph g,
				int edge, int vertex)
		{
			for (IntCursor s : IntCursor.fromFastUtil(g.getUndirectedHyperEdgeVertices(edge)))
			{
				neighborSets.set(s.value, null);
			}

			neighborSets.set(vertex, null);
		}

		@Override
		public void vertexRemovedFromDirectedHyperEdgeTail(Grph g,
				int e, int v)
		{
			for (IntCursor s : IntCursor.fromFastUtil(g.getDirectedHyperEdgeHead(e)))
			{
				neighborSets.set(s.value, null);
			}

			neighborSets.set(v, null);
		}

		@Override
		public void vertexRemovedFromDirectedHyperEdgeHead(Grph g,
				int e, int v)
		{
			for (IntCursor s : IntCursor.fromFastUtil(g.getDirectedHyperEdgeTail(e)))
			{
				neighborSets.set(s.value, null);
			}

			neighborSets.set(v, null);
		}

	}
}
