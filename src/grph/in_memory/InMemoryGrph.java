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
import grph.algo.topology.ClassicalGraphs;
import grph.properties.NumericalProperty;
import toools.NotYetImplementedException;
import toools.UnitTests;
import toools.set.IntHashSet;
import toools.set.IntSet;
import toools.set.UnmodifiableIntSet;

import com.carrotsearch.hppc.IntArrayList;

/**
 * The basic data-structure. It mainly defines and implements the data structure
 * required for vertex/edge incidence.
 * 
 * The methods in this API are written using two rules: 1. they are
 * non-permissive: for example deleting an element that is not in the graph will
 * cause an error
 * 
 * 2. elements are created on-the-fly, when possible.
 * 
 * 
 * @author lhogie
 * 
 */
public class InMemoryGrph extends Grph
{
	private final GraphElementSet vertexSet;
	private final GraphElementSet edgeSet;

	private final IncidenceList v_out_only;
	private final IncidenceList v_in_only;
	private final IncidenceList v_in_out_only;

	private final NumericalProperty simple_edge_directivity;

	private final IntArrayList se_a;
	private final IntArrayList se_b;

	private final IncidenceList undirectedHyperEdgeVertices;
	private final IncidenceList directedHyperEdgeTail;
	private final IncidenceList directedHyperEdgeHead;

	private int numberOfUndirectedSimpleEdges = 0;
	private int numberOfDirectedSimpleEdges = 0;
	private int numberOfUndirectedHyperEdges = 0;
	private int numberOfDirectedHyperEdges = 0;

	// private transient final NeighboursCache outNeighborsCache = new
	// OutNeighboursCache(this);

	private final boolean storeEdges;
	private final DIRECTION navigation;

	private final String gid;

	public InMemoryGrph()
	{
		this("a graph", true, DIRECTION.in_out);
	}

	public InMemoryGrph(String gid, boolean storeEdges, DIRECTION navigation)
	{
		super(gid);
		vertexSet = new GraphElementSet();

		assert gid != null;
		this.gid = gid;
		this.storeEdges = storeEdges;

		assert navigation != null;
		this.navigation = navigation;


		v_in_only = navigation == DIRECTION.in || navigation == DIRECTION.in_out ? new IncidenceList() : null;
		v_out_only = navigation == DIRECTION.out || navigation == DIRECTION.in_out ? new IncidenceList() : null;
		v_in_out_only = new IncidenceList();

		edgeSet = storeEdges ? new GraphElementSet() : null;
		simple_edge_directivity = storeEdges ? new NumericalProperty(null, 1, 0) : null;
		undirectedHyperEdgeVertices = storeEdges ? new IncidenceList() : null;

		se_a = storeEdges ? new IntArrayList() : null;
		se_b = storeEdges ? new IntArrayList() : null;
		directedHyperEdgeTail = storeEdges && (navigation == DIRECTION.in || navigation == DIRECTION.in_out) ? new IncidenceList() : null;
		directedHyperEdgeHead = navigation == DIRECTION.out || navigation == DIRECTION.in_out ? new IncidenceList() : null;
	}

	/**
	 * Checks if the two given edges have the same vertices incident to them.
	 * 
	 * @param a
	 *            an edge
	 * @param b
	 *            another edge
	 * @return true if the two given edges have the same vertices incident to
	 *         them
	 * 
	 *         public boolean edgesHaveSameIncidence(int a, int b) { return (a
	 *         == e.a && b == e.b) || (a == e.b && b == e.a);
	 * 
	 *         return edgeSet.get(a).equals(edgeSet.get(b)); }
	 */

	@Override
	public boolean storeEdges()
	{
		return storeEdges;
	}

	@Override
	public DIRECTION getNavigation()
	{
		return navigation;
	}

	private static IntSet ensureNotDangerous(IntSet s)
	{
		// if this is an set used in the implementation of the data structure,
		// we need to return a read-only copy of it
		if (s.getClass() == GrphInternalSet.class)
		{
			return ((GrphInternalSet) s).makeReadOnly();
		}
		else
		{
			return s;
		}
	}

	/**
	 * Computes the set of vertices incident to the given hyper-edge.
	 * 
	 * @param edge
	 *            an hyper-edge
	 * @return a set of vertices
	 */
	@Override
	public IntSet getUndirectedHyperEdgeVertices(int edge)
	{
		assert isUndirectedHyperEdge(edge);
		return ensureNotDangerous(undirectedHyperEdgeVertices.getValue(edge));
	}

	/**
	 * Retrieve the set of vertices in this graph.
	 * 
	 * @return a set of vertices.
	 */
	// public IncidenceList<VertexIncidence> getVertices()
	@Override
	public IntSet getVertices()
	{
		return new UnmodifiableIntSet(vertexSet);
	}

	/**
	 * Retrieve the set of edges in this graph.
	 * 
	 * @return a set of edges.
	 */
	// public IncidenceList<EdgeIncidence> getEdges()
	public IntSet getEdges()
	{
		return new UnmodifiableIntSet(edgeSet);
	}

	@Override
	public int getNextEdgeAvailable()
	{
		return edgeSet.getLowestIDAvailable();
	}

	@Override
	public int getNextVertexAvailable()
	{
		return vertexSet.getLowestIDAvailable();
	}

	@Override
	public IntSet getOutOnlyElements(int v)
	{
		assert vertexSet.contains(v) : "vertex does not exist: " + v;
		return ensureNotDangerous(v_out_only.getValue(v));
	}

	@Override
	public IntSet getInOnlyElements(int v)
	{
		assert vertexSet.contains(v) : "vertex does not exist: " + v;

		if (v_in_only == null)
			throw new IllegalStateException("'in' navigation is not enabled");

		return ensureNotDangerous(v_in_only.getValue(v));
	}

	@Override
	public IntSet getInOutOnlyElements(int v)
	{
		assert vertexSet.contains(v) : "vertex does not exist: " + v;

		if (v_in_out_only == null)
			throw new IllegalStateException("'out' navigation is not enabled");

		return ensureNotDangerous(v_in_out_only.getValue(v));
	}

	public static void main(String[] args)
	{
		testEdgeless();
	}

	private static void testEdgeless()
	{
		Grph g = new InMemoryGrph("some graph", false, DIRECTION.out);
		g.addUndirectedSimpleEdge(0, 5);
		g.addDirectedSimpleEdge(0, 3);
		UnitTests.ensure(g.getOutNeighbors(0).contains(5));
		UnitTests.ensure(g.isUndirectedSimpleEdge(0, 5));
		UnitTests.ensure(g.isDirectedSimpleEdge(0, 3));
		g.removeEdge(0, 3);
		UnitTests.ensure(g.getNumberOfEdges() == 1);
	}

	@Override
	public void removeEdge(int u, int v)
	{
		assert !storeEdges();

		if (isUndirectedSimpleEdge(u, v))
		{
			v_in_out_only.remove(u, v);
			v_in_out_only.remove(v, u);
			--numberOfUndirectedSimpleEdges;

			for (TopologyListener l : getTopologyListeners())
				l.undirectedSimpleEdgeRemoved(this, -1, u, v);
		}
		else
		{
			if (v_out_only != null)
			{
				v_out_only.remove(u, v);
			}

			if (v_in_only != null)
			{
				v_in_only.remove(v, u);
			}

			--numberOfDirectedSimpleEdges;

			for (TopologyListener l : getTopologyListeners())
				l.directedSimpleEdgeRemoved(this, -1, u, v);
		}

	}

	/**
	 * Checks if the given edge is an undirected simple edge.
	 * 
	 * @param edge
	 *            an edge
	 * @return true if the given edge is an undirected simple edge, false
	 *         otherwise
	 */
	@Override
	public boolean isUndirectedSimpleEdge(int edge)
	{
		assert edgeSet.contains(edge);
		return isSimpleEdge(edge) && simple_edge_directivity.getValue(edge) == 0;
	}

	/**
	 * Checks if the given edge is an undirected hyper edge.
	 * 
	 * @param edge
	 *            an edge
	 * @return true if the given edge is an undirected hyper edge, false
	 *         otherwise
	 */
	@Override
	public boolean isUndirectedHyperEdge(int edge)
	{
		assert edgeSet.contains(edge);
		return undirectedHyperEdgeVertices.hasValue(edge);
	}

	/**
	 * Adds the given edge connecting the two given vertices.
	 * 
	 * @param a
	 *            a vertex
	 * @param b
	 *            another vertex
	 * @param edge
	 *            the new edge
	 */
	@Override
	public void addUndirectedSimpleEdge(int edge, int a, int b)
	{
		assert a >= 0;
		assert b >= 0;

		if (!containsVertex(a))
		{
			addVertex(a);
		}

		if (!containsVertex(b))
		{
			addVertex(b);
		}

		if (edgeSet == null)
		{
			assert edge == -1;
			v_in_out_only.add(a, b);
			v_in_out_only.add(b, a);
		}
		else
		{
			assert edge >= 0;
			assert !edgeSet.contains(edge) : "edge already in graph " + edge;

			edgeSet.add(edge);
			simple_edge_directivity.setValue(edge, 0);

			if (se_a != null)
			{
				se_a.ensureCapacity(edge + 1);
				se_a.buffer[edge] = a;
			}

			if (se_b != null)
			{
				se_b.ensureCapacity(edge + 1);
				se_b.buffer[edge] = b;
			}

			v_in_out_only.add(a, edge);

			// if this is a loop
			if (a != b)
			{
				v_in_out_only.add(b, edge);
			}
		}

		++numberOfUndirectedSimpleEdges;

		for (TopologyListener l : getTopologyListeners())
			l.undirectedSimpleEdgeAdded(this, edge, a, b);
	}

	/**
	 * Adds the given arc, connecting the given source and destination vertices.
	 * 
	 * @param edge
	 *            the arc to be added
	 * @param tail
	 *            the source of the arc
	 * @param head
	 *            the destination of the arc
	 */
	@Override
	public void addDirectedSimpleEdge(int tail, int edge, int head)
	{
		assert tail >= 0;
		assert head >= 0;
		assert edge == -1 || !getEdges().contains(edge) : "edge already in graph: " + edge;

		if (!containsVertex(head))
		{
			addVertex(head);
		}

		if (!containsVertex(tail))
		{
			addVertex(tail);
		}

		if (storeEdges())
		{
			edgeSet.add(edge);
			se_a.ensureCapacity(edge + 1);
			se_a.buffer[edge] = tail;
			se_b.ensureCapacity(edge + 1);
			se_b.buffer[edge] = head;
			simple_edge_directivity.setValue(edge, 1);

			if (v_out_only != null)
			{
				v_out_only.add(tail, edge);
			}

			if (v_in_only != null)
			{
				v_in_only.add(head, edge);
			}
		}
		else
		{
			if (v_out_only != null)
			{
				v_out_only.add(tail, head);
			}

			if (v_in_only != null)
			{
				v_in_only.add(head, tail);
			}
		}

		++numberOfDirectedSimpleEdges;

		for (TopologyListener l : getTopologyListeners())
			l.directedSimpleEdgeAdded(this, edge, tail, head);
	}

	/**
	 * Adds the given directed hyper-edge. Initially connects the empty set to
	 * itself.
	 * 
	 * @param edge
	 *            the edge to be added
	 */
	@Override
	public void addDirectedHyperEdge(int edge)
	{
		assert !getEdges().contains(edge) : "edge already in graph: " + edge;

		edgeSet.add(edge);
		++numberOfDirectedHyperEdges;

		if (directedHyperEdgeTail != null)
		{
			directedHyperEdgeTail.setEmptySet(edge);
		}

		if (directedHyperEdgeHead != null)
		{
			directedHyperEdgeHead.setEmptySet(edge);
		}

		for (TopologyListener l : getTopologyListeners())
			l.directedHyperEdgeAdded(this, edge);
	}

	@Override
	public void addVertex(int v)
	{
		assert !vertexSet.contains(v);

		vertexSet.add(v);

		if (v_in_only != null)
		{
			v_in_only.setEmptySet(v);
		}

		if (v_out_only != null)
		{
			v_out_only.setEmptySet(v);
		}

		v_in_out_only.setEmptySet(v);

		for (TopologyListener l : getTopologyListeners())
			l.vertexAdded(this, v);
	}

	/**
	 * Adds the given undirected hyper-edge to this graph. This hyper-edge
	 * initially has no vertex incident to it.
	 * 
	 * @param edge
	 *            the edge to add
	 */
	@Override
	public void addUndirectedHyperEdge(int edge)
	{
		assert edge >= 0;
		edgeSet.add(edge);
		undirectedHyperEdgeVertices.setEmptySet(edge);

		++numberOfUndirectedHyperEdges;

		for (TopologyListener l : getTopologyListeners())
			l.undirectedHyperEdgeAdded(this, edge);
	}

	/**
	 * Adds the given vertex to the given hyper-edge.
	 * 
	 * @param edge
	 * @param vertex
	 */
	@Override
	public void addToUndirectedHyperEdge(int edge, int vertex)
	{
		assert edge >= 0;
		assert vertex >= 0;
		assert isUndirectedHyperEdge(edge) : "an hyperedge is expected";

		if (!containsVertex(vertex))
		{
			addVertex(vertex);
		}

		if (!containsEdge(edge))
		{
			addUndirectedHyperEdge(edge);
		}

		undirectedHyperEdgeVertices.add(edge, vertex);
		v_in_out_only.add(vertex, edge);

		for (TopologyListener l : getTopologyListeners())
			l.vertexAddedToUndirectedSimpleEdge(this, edge, vertex);
	}

	@Override
	public void addToDirectedHyperEdgeTail(int e, int v)
	{
		assert e >= 0;
		assert v >= 0;
		assert isDirectedHyperEdge(e) : "a directed hyperedge is expected";

		if (!containsVertex(v))
		{
			addVertex(v);
		}

		if (!containsEdge(e))
		{
			addDirectedHyperEdge(e);
		}

		directedHyperEdgeTail.add(e, v);
		v_out_only.add(v, e);

		for (TopologyListener l : getTopologyListeners())
			l.vertexAddedToDirectedHyperEdgeTail(this, e, v);
	}

	@Override
	public void addToDirectedHyperEdgeHead(int e, int v)
	{
		assert e >= 0;
		assert v >= 0;
		assert isDirectedHyperEdge(e) : "a directed hyperedge is expected";

		if (!containsVertex(v))
		{
			addVertex(v);
		}

		if (!getEdges().contains(e))
		{
			addDirectedHyperEdge(e);
		}

		directedHyperEdgeHead.add(e, v);
		v_in_only.add(v, e);

		for (TopologyListener l : getTopologyListeners())
			l.vertexAddedToDirectedHyperEdgeHead(this, e, v);
	}

	/**
	 * Removes the given vertex from the given hyper-edge.
	 * 
	 * @param edge
	 *            an edge
	 * @param vertex
	 *            a vertex incident to this edge
	 */
	@Override
	public final void removeFromHyperEdge(int edge, int vertex)
	{
		assert edge >= 0;
		assert vertex >= 0;

		if (!getVertices().contains(vertex))
			throw new IllegalArgumentException("vertex not in graph: " + vertex);

		if (!getEdges().contains(edge))
			throw new IllegalArgumentException("edge not in graph: " + edge);

		if (!isUndirectedHyperEdge(edge))
			throw new IllegalArgumentException("an hyperedge is expected");

		undirectedHyperEdgeVertices.remove(edge, vertex);
		v_in_out_only.remove(vertex, edge);
	}

	@Override
	public final void removeFromDirectedHyperEdgeTail(int edge, int vertex)
	{
		assert edge >= 0;
		assert vertex >= 0;

		if (!getVertices().contains(vertex))
			throw new IllegalArgumentException("vertex not in graph: " + vertex);

		if (!getEdges().contains(edge))
			throw new IllegalArgumentException("edge not in graph: " + edge);

		if (!isDirectedHyperEdge(edge))
			throw new IllegalArgumentException("an hyperedge is expected");

		directedHyperEdgeTail.remove(edge, vertex);
		v_out_only.remove(vertex, edge);
	}

	@Override
	public final void removeFromDirectedHyperEdgeHead(int edge, int vertex)
	{
		assert edge >= 0;
		assert vertex >= 0;

		if (!getVertices().contains(vertex))
			throw new IllegalArgumentException("vertex not in graph: " + vertex);

		if (!getEdges().contains(edge))
			throw new IllegalArgumentException("edge not in graph: " + edge);

		if (!isDirectedHyperEdge(edge))
			throw new IllegalArgumentException("an hyperedge is expected");

		directedHyperEdgeHead.remove(edge, vertex);
		v_in_only.remove(vertex, edge);
	}

	/**
	 * Removes the given vertex from the graph. All incident edges are removed
	 * first.
	 * 
	 * @param vertex
	 *            the vertex to be removed
	 */
	@Override
	public void removeVertex(int v)
	{
		assert vertexSet.contains(v) : "vertex not in graph " + v;

		disconnectVertex(v);

		if (!getInOnlyElements(v).isEmpty())
			throw new IllegalStateException("vertex cannot be removed because it is connected " + getInOnlyElements(v));

		if (!getOutOnlyElements(v).isEmpty())
			throw new IllegalStateException("vertex cannot be removed because it is connected ");

		if (!getInOutOnlyElements(v).isEmpty())
			throw new IllegalStateException("vertex " + v + " cannot be removed because it is connected " + getInOutOnlyElements(v));

		vertexSet.remove(v);

		if (v_in_only != null)
		{
			v_in_only.unsetValue(v);
		}

		if (v_out_only != null)
		{
			v_out_only.unsetValue(v);
		}

		v_in_out_only.unsetValue(v);

		for (TopologyListener l : getTopologyListeners())
			l.vertexRemoved(this, v);
	}

	/**
	 * Remove the given edge from the graph.
	 * 
	 * @param edge
	 *            the edge to be removed
	 */
	@Override
	public void removeEdge(int edge)
	{
		assert storeEdges;
		assert edgeSet.contains(edge) : "edge not in graph " + edge;

		if (isUndirectedSimpleEdge(edge))
		{
			int a = se_a.buffer[edge];
			v_in_out_only.remove(a, edge);
			int b = se_b.buffer[edge];

			// if this was a loop
			if (a != b)
			{
				v_in_out_only.remove(b, edge);
			}

			--numberOfUndirectedSimpleEdges;
			edgeSet.remove(edge);

			for (TopologyListener l : getTopologyListeners())
				l.undirectedSimpleEdgeRemoved(this, edge, a, b);
		}
		else if (isDirectedSimpleEdge(edge))
		{
			int tail = se_a.buffer[edge];

			if (v_out_only != null)
			{
				v_out_only.remove(tail, edge);
			}

			int head = se_b.buffer[edge];

			if (v_in_only != null)
			{
				v_in_only.remove(head, edge);
			}
			edgeSet.remove(edge);

			--numberOfDirectedSimpleEdges;

			for (TopologyListener l : getTopologyListeners())
				l.directedSimpleEdgeRemoved(this, edge, tail, head);
		}
		else if (isUndirectedHyperEdge(edge))
		{
			IntSet incidentVertices = undirectedHyperEdgeVertices.getValue(edge);

			for (int v : incidentVertices.toIntArray())
			{
				v_in_out_only.remove(v, edge);
			}
			edgeSet.remove(edge);

			--numberOfUndirectedHyperEdges;

			for (TopologyListener l : getTopologyListeners())
				l.undirectedHyperEdgeRemoved(this, edge, incidentVertices);
		}
		else
		{
			throw new NotYetImplementedException();
		}
	}

	/**
	 * Checks whether the given edge is a directed simple edge.
	 * 
	 * @param edge
	 *            an edge
	 * @return
	 */
	@Override
	public boolean isDirectedSimpleEdge(int edge)
	{
		assert edgeSet.contains(edge) : "edge not in graph " + edge;
		return isSimpleEdge(edge) && simple_edge_directivity.getValue(edge) == 1;
	}

	/**
	 * Checks whether the given edge is a directed hyper edge.
	 * 
	 * @param edge
	 *            an edge
	 * @return
	 */
	@Override
	public boolean isDirectedHyperEdge(int edge)
	{
		assert edgeSet.contains(edge);
		return (directedHyperEdgeHead != null && directedHyperEdgeHead.hasValue(edge))
				|| (directedHyperEdgeTail != null && directedHyperEdgeTail.hasValue(edge));
	}

	/**
	 * Assuming the given edge is a directed simple edge, retrieves its tail.
	 * 
	 * @param edge
	 *            an edge
	 * @return the tail of the edge.
	 */
	@Override
	public int getDirectedSimpleEdgeTail(int edge)
	{
		assert edgeSet.contains(edge) : "edge not in graph " + edge;
		assert isDirectedSimpleEdge(edge);
		assert getNavigation() == DIRECTION.in || getNavigation() == DIRECTION.in_out;
		return se_a.buffer[edge];
	}

	/**
	 * Assuming the given edge is a directed simple edge, retrieves its head.
	 * 
	 * @param edge
	 *            an edge
	 * @return the head of the edge.
	 */
	@Override
	public int getDirectedSimpleEdgeHead(int edge)
	{
		assert edgeSet.contains(edge) : "edge not in graph " + edge;
		assert isDirectedSimpleEdge(edge);
		assert getNavigation() == DIRECTION.out || getNavigation() == DIRECTION.in_out;
		return se_b.buffer[edge];
	}

	/**
	 * Assuming the given edge is an undirected simple edge, retrieves one of
	 * its incident vertices.
	 * 
	 * @param edge
	 *            an edge
	 * @return one of its incident vertices
	 */
	@Override
	public int getOneVertex(int edge)
	{
		assert edgeSet.contains(edge) : "edge not in graph " + edge;
		assert isSimpleEdge(edge);
		assert storeEdges;

		return se_a.buffer[edge];
	}

	/**
	 * Assuming the given edge is an undirected simple edge, retrieves the
	 * incident vertex other than thisVertex.
	 * 
	 * @param edge
	 *            an edge
	 * @return one of its incident vertices
	 * @param thisVertex
	 *            a vertex incident to edge
	 */
	@Override
	public int getTheOtherVertex(int edge, int thisVertex)
	{
		assert thisVertex >= 0;
		assert storeEdges;
		assert edge >= 0;
		assert edgeSet.contains(edge) : "edge not in graph " + edge + ", edges are " + edgeSet;
		assert vertexSet.contains(thisVertex) : "vertex not in graph " + thisVertex;
		assert isSimpleEdge(edge);
		int a = se_a.buffer[edge];
		return thisVertex != a ? a : se_b.buffer[edge];
	}

	/**
	 * Assuming the given edge is a directed hyper edge, retrieves its tail.
	 * 
	 * @param edge
	 *            an edge
	 * @return the tail of the edge.
	 */
	@Override
	public IntSet getDirectedHyperEdgeTail(int e)
	{
		assert edgeSet.contains(e) : "edge not in graph " + e;
		assert isDirectedHyperEdge(e);
		return ensureNotDangerous(directedHyperEdgeTail.getValue(e));
	}

	/**
	 * Assuming the given edge is a directed hyper edge, retrieves its head.
	 * 
	 * @param edge
	 *            an edge
	 * @return the head of the edge.
	 */
	@Override
	public IntSet getDirectedHyperEdgeHead(int e)
	{
		assert edgeSet.contains(e) : "edge not in graph " + e;
		assert isDirectedHyperEdge(e);
		return ensureNotDangerous(directedHyperEdgeHead.getValue(e));
	}

	/**
	 * 
	 * @return the number of undirected simple edges in this graph.
	 */
	@Override
	public int getNumberOfUndirectedSimpleEdges()
	{
		return numberOfUndirectedSimpleEdges;
	}

	/**
	 * 
	 * @return the number of directed simple edges in this graph.
	 */
	@Override
	public int getNumberOfDirectedSimpleEdges()
	{
		return numberOfDirectedSimpleEdges;
	}

	/**
	 * 
	 * @return the number of undirected hyper edges in this graph.
	 */
	@Override
	public int getNumberOfUndirectedHyperEdges()
	{
		return numberOfUndirectedHyperEdges;
	}

	/**
	 * 
	 * @return the number of directed hyper edges in this graph.
	 */
	@Override
	public int getNumberOfDirectedHyperEdges()
	{
		return numberOfDirectedHyperEdges;
	}

	private static void testClique()
	{
		InMemoryGrph g = new InMemoryGrph();
		g.ensureNVertices(5);
		g.clique();
		UnitTests.ensure(g.getDiameter() == 1);
		UnitTests.ensure(g.getEdges().size() == 10);
		UnitTests.ensure(g.isComplete());
	}

	private static void testEdgesConnecting()
	{
		Grph g = ClassicalGraphs.PetersenGraph();
		UnitTests.ensureEquals(g.getEdgesConnecting(8, 3).size(), 1);
		UnitTests.ensureEquals(g.getEdgesConnecting(1, 6).size(), 1);
		UnitTests.ensureEquals(g.getEdgesConnecting(4, 6).size(), 0);
		UnitTests.ensureEquals(g.getEdgesConnecting(7, 0).size(), 0);
	}

	private static void testDegree()
	{
		InMemoryGrph g = new InMemoryGrph();
		g.dgrid(3, 3);

		for (int v : g.getVertices().toIntArray())
		{
			int inEdgeDegree = g.getVertexDegree(5, Grph.TYPE.edge, Grph.DIRECTION.in);
			int outEdgeDegree = g.getVertexDegree(5, Grph.TYPE.edge, Grph.DIRECTION.out);
			int inOutEdgeDegree = g.getVertexDegree(5, Grph.TYPE.edge, Grph.DIRECTION.in_out);
			UnitTests.ensureEquals(inEdgeDegree + outEdgeDegree, inOutEdgeDegree);
		}
	}

	private static void testBasicThings()
	{
		InMemoryGrph g = new InMemoryGrph();
		UnitTests.ensureEquals(g.getVertices().isEmpty(), true);
		UnitTests.ensureEquals(g.getEdges().size(), 0);

		g.grid(2, 2);

		IntSet s = new IntHashSet();
		s.add(0);
		s.add(1);
		s.add(2);
		s.add(3);
		UnitTests.ensureEquals(g.getVertices(), s);

		g.clear();
		g.grid(5, 5);
		// g.display();
		UnitTests.ensure(g.getNeighbours(8).equals(3, 7, 9, 13));
		UnitTests.ensureEquals(g.getVertices().size(), 25);
		UnitTests.ensureEquals(g.getEdges().size(), 40);
		UnitTests.ensureEquals(g.getDiameter(), 8);
		// g.removeEdge(3);
		// Test.ensure(g.getEdges().size(), 39);

		UnitTests.ensureEquals(g.getConnectedComponents().size(), 1);
		g.disconnectVertex(7);
		UnitTests.ensure(g.getNeighbours(8).equals(3, 9, 13));
		UnitTests.ensureEquals(g.getEdgesIncidentTo(7).size(), 0);
		UnitTests.ensureEquals(g.getVertices().size(), 25);
		UnitTests.ensureEquals(g.getEdges().size(), 36);
		g.removeEdge(22);
		UnitTests.ensureEquals(g.getVertices().size(), 25);
		UnitTests.ensureEquals(g.getEdges().size(), 35);

		UnitTests.ensureEquals(g.getConnectedComponents().size(), 2);
		g.addSimpleEdge(0, 7, true);
		g.addSimpleEdge(12, 8, true);
		g.addSimpleEdge(8, 6, true);

		UnitTests.ensureEquals(g.getVertexDegree(8, Grph.TYPE.edge, Grph.DIRECTION.out), 4);
		UnitTests.ensureEquals(g.getVertexDegree(8, Grph.TYPE.edge, Grph.DIRECTION.in), 4);
		UnitTests.ensureEquals(g.getVertexDegree(8, Grph.TYPE.edge, Grph.DIRECTION.in_out), 5);

		UnitTests.ensureEquals(g.isMixed(), true);
		int e = g.addDirectedSimpleEdge(0, 1);
		UnitTests.ensureEquals(g.isMixed(), true);
		g.removeEdge(e);
		UnitTests.ensureEquals(g.isMixed(), true);
	}

	@Override
	public boolean containsVertex(int v)
	{
		return vertexSet.contains(v);
	}

	@Override
	public boolean containsEdge(int e)
	{
		return edgeSet.contains(e);
	}

}
