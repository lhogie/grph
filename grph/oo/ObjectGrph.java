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

/**
 * Grph
 * Initial Software by Luc HOGIE, Issam TAHIRI, Aurélien LANCIN, Nathann COHEN, David COUDERT.
 * Copyright © INRIA/CNRS/UNS, All Rights Reserved, 2011, v0.9
 *
 * The Grph license grants any use or destribution of both binaries and source code, if
 * a prior notification was made to the Grph development team.
 * Modification of the source code is not permitted. 
 * 
 *
 */

package grph.oo;

import grph.Grph;
import grph.algo.AllPaths;
import grph.algo.subgraph_isomorphism.own.EdgeLabelBasedSubgraphMatcher;
import grph.algo.subgraph_isomorphism.own.FindAllCycles;
import grph.path.Path;
import grph.properties.StringProperty;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import toools.UnitTests;
import toools.collections.AutoGrowingArrayList;
import toools.set.IntHashSet;
import toools.set.IntSet;

import com.carrotsearch.hppc.ObjectIntMap;
import com.carrotsearch.hppc.ObjectIntOpenHashMap;
import com.carrotsearch.hppc.cursors.IntCursor;

/**
 * This class provides an object-oriented interface to the functionality
 * provided by the Grph library. This is particularly useful when Grph is to be
 * integrated to software projects that implies graph of objects.
 * 
 * @author lhogie
 * 
 * @param <V>
 * @param <E>
 */
public class ObjectGrph<V, E>
{
	private final ObjectIntMap<V> vertex_int = new ObjectIntOpenHashMap<V>();
	private final ObjectIntMap<E> edge_int = new ObjectIntOpenHashMap<E>();
	private final AutoGrowingArrayList<V> vertices = new AutoGrowingArrayList();
	private final AutoGrowingArrayList<E> edges = new AutoGrowingArrayList();
	protected final Grph backingGrph = new grph.in_memory.InMemoryGrph();

	public ObjectGrph()
	{
		backingGrph.setVerticesLabel(new StringProperty("vertex label"));
		backingGrph.setEdgesLabel(new StringProperty("edge label"));
	}
	
	public ObjectGrph<V, E> getSubGraph(Grph g)
	{
		ObjectGrph<V, E> og = new ObjectGrph<V, E>();

		for (int v : g.getVertices().toIntArray())
		{
			og.addVertex(i2v(v));
		}

		for (int e : g.getEdges().toIntArray())
		{
			if (g.isDirectedSimpleEdge(e))
			{
				V src = i2v(g.getDirectedSimpleEdgeTail(e));
				V dest = i2v(g.getDirectedSimpleEdgeHead(e));
				og.addDirectedSimpleEdge(src, i2e(e), dest);
			}
			else if (g.isDirectedSimpleEdge(e))
			{
				int a = g.getOneVertex(e);
				V src = i2v(a);
				V dest = i2v(g.getTheOtherVertex(e, a));
				og.addDirectedSimpleEdge(src, i2e(e), dest);
			}
			else
			{
				throw new IllegalStateException("unsupported edge type");
			}
		}

		return og;
	}

	public boolean containsVertex(V v)
	{
		return vertex_int.containsKey(v);
	}

	public boolean containsEdge(E e)
	{
		return edge_int.containsKey(e);
	}

	public Set<E> getEdges(V src, V dest)
	{
		return i2e(backingGrph.getEdgesConnecting(v2i(src), v2i(dest)));
	}

	public int addVertex(V v)
	{
		if (getVertices().contains(v))
			throw new IllegalArgumentException("vertex already in graph");

		int vi = backingGrph.addVertex();
		vertex_int.put(v, vi);
		vertices.add(vi, v);
		backingGrph.getVertexLabelProperty().setValue(vi, v.toString());
		return vi;
	}

	public void removeVertex(V v)
	{
		int vi = vertex_int.remove(v);
		vertices.set(vi, null);
		backingGrph.removeVertex(vi);
	}

	public void removeEdge(E e)
	{
		int ie = edge_int.remove(e);
		edges.set(ie, null);
		backingGrph.removeEdge(ie);
	}

	public int addSimpleEdge(V src, E e, V dest, boolean directed)
	{
		assert !getEdges().contains(e) : "edge " + e + " already in graph";

		int a = getVertices().contains(src) ? v2i(src) : addVertex(src);
		int b = getVertices().contains(dest) ? v2i(dest) : addVertex(dest);
		int ei;

		if (directed)
		{
			ei = backingGrph.addDirectedSimpleEdge(a, b);
		}
		else
		{
			ei = backingGrph.addUndirectedSimpleEdge(a, b);
		}

		edge_int.put(e, ei);
		edges.set(ei, e);
		backingGrph.getEdgeLabelProperty().setValue(ei, e.toString());
		return ei;
	}

	public Set<V> getIncidentVertices(E e)
	{
		return i2v(backingGrph.getVerticesIncidentToEdge(e2i(e)));
	}

	public int addUndirectedSimpleEdge(V src, E e, V dest)
	{
		return addSimpleEdge(src, e, dest, false);
	}

	
	public int addDirectedSimpleEdge(V src, E e, V dest)
	{
		return addSimpleEdge(src, e, dest, true);
	}

	public Collection<V> getOutNeighbors(V v)
	{
		assert getVertices().contains(v) : "vertex not in graph";
		return i2v(backingGrph.getOutNeighbors(v2i(v)));
	}

	public boolean hasIncidentEdges(V v)
	{
		return !backingGrph.getEdgesIncidentTo(v2i(v)).isEmpty();
	}

	public Set<E> getIncidentEdges(V v)
	{
		return i2e(backingGrph.getEdgesIncidentTo(v2i(v)));
	}

	protected int v2i(V v)
	{
		if (!getVertices().contains(v))
			throw new IllegalArgumentException("unknow vertex " + v);

		return vertex_int.get(v);
	}

	public Collection<V> getVertices()
	{
		// converts the HPPC collection to Java collection
		return new HPPCCollection2JavaCollection<V>(vertex_int.keys());
	}

	public Collection<E> getEdges()
	{
		return new HPPCCollection2JavaCollection<E>(edge_int.keys());
	}

	protected int e2i(E e)
	{
		if (!getEdges().contains(e))
			throw new IllegalArgumentException("unknow edge " + e);

		return edge_int.get(e);
	}

	protected V i2v(int v)
	{
		return vertices.get(v);
	}

	protected E i2e(int e)
	{
		return edges.get(e);
	}

	protected Set<V> i2v(IntSet set)
	{
		Set<V> c = new HashSet();

		for (IntCursor n : set)
		{
			c.add(i2v(n.value));
		}

		return c;
	}

	protected IntSet v2i(Collection<V> set)
	{
		IntSet c = new IntHashSet();

		for (V v : set)
		{
			c.add(v2i(v));
		}

		return c;
	}

	protected Set<E> i2e(IntSet set)
	{
		Set<E> c = new HashSet();

		for (IntCursor n : set)
		{
			c.add(i2e(n.value));
		}

		return c;
	}

	protected IntSet e2i(Set<E> set)
	{
		IntSet c = new IntHashSet();

		for (E e : set)
		{
			c.add(e2i(e));
		}

		return c;
	}

	public void display()
	{
		backingGrph.display();
	}

	public V getDirectedSimpleEdgeTail(E e)
	{
		return i2v(backingGrph.getDirectedSimpleEdgeTail(e2i(e)));
	}

	public V getDirectedSimpleEdgeHead(E e)
	{
		return i2v(backingGrph.getDirectedSimpleEdgeHead(e2i(e)));
	}

	public String getEdgeLabel(E e)
	{
		return backingGrph.getEdgeLabelProperty().getValueAsString(e2i(e));
	}

	public void setEdgeLabel(E e, String s)
	{
		backingGrph.getEdgeLabelProperty().setValue(e2i(e), s);
	}

	public String getVertexLabel(V v)
	{
		return backingGrph.getVertexLabelProperty().getValueAsString(v2i(v));
	}

	public void setVertexLabel(V v, String s)
	{
		backingGrph.getVertexLabelProperty().setValue(v2i(v), s);
	}

	public Set<ObjectPath> getAllPaths()
	{
		Set<ObjectPath> r = new HashSet<ObjectPath>();

		for (Path p : AllPaths.computeAllPaths(backingGrph))
		{
			r.add(new ObjectPath(p, this));
		}

		return r;
	}

	public Set<ObjectPath> getAllCycles()
	{
		Set<ObjectPath> r = new HashSet<ObjectPath>();

		for (Path p : new FindAllCycles().compute(backingGrph))
		{
			r.add(new ObjectPath(p, this));
		}

		return r;
	}

	public Map<String, Set<ObjectPath>> findMatchingPaths(Set<String> patterns)
	{
		Set<ObjectGrph> res = new HashSet<ObjectGrph>();
		Map<String, Set<Path>> matches = new EdgeLabelBasedSubgraphMatcher().findAllPathsMatching(backingGrph, patterns,
				this.backingGrph.getVertexLabelProperty(), backingGrph.getEdgeLabelProperty());

		Map<String, Set<ObjectPath>> r = new HashMap<String, Set<ObjectPath>>();

		for (String p : matches.keySet())
		{
			Set<Path> s = matches.get(p);
			Set<ObjectPath> rr = new HashSet<ObjectPath>();

			for (Path pp : s)
			{
				rr.add(new ObjectPath(pp, this));
			}

			r.put(p, rr);
		}

		return r;
	}

	private static void test()
	{
		ObjectGrph<String, String> g = new ObjectGrph();
		g.addUndirectedSimpleEdge("luc", "kicks", "jeremy");
		g.addUndirectedSimpleEdge("jvm", "knows", "julien");

		UnitTests.ensure(g.getVertices().contains("luc"));
		UnitTests.ensure(g.getEdges().contains("knows"));

		UnitTests.ensure(g.getOutNeighbors("luc").contains("jeremy"));
	}

	public static void main(String[] args)
	{
		ObjectGrph<String, String> g = new ObjectGrph();
		g.addUndirectedSimpleEdge("luc", "kicks", "jeremy");
		g.addUndirectedSimpleEdge("luc", "knows", "jvm");
		System.out.println(g.getIncidentEdges("luc"));
		g.removeEdge("knows");
		System.out.println(g.getIncidentEdges("luc"));
	}

	public static void main2(String[] args)
	{
		ObjectGrph<String, String> g = new ObjectGrph();
		g.addUndirectedSimpleEdge("luc", "kicks", "jeremy");
		g.addUndirectedSimpleEdge("luc", "knows", "jvm");
		Set<String> patterns = new HashSet<String>();
		patterns.add(".*");
		patterns.add(".*knows.*");
		System.out.println(g.findMatchingPaths(patterns));
	}
}
