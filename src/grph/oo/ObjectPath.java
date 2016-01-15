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
 
 package grph.oo;

import grph.path.Path;

import java.util.ArrayList;
import java.util.List;

public class ObjectPath<V, E>
{
    private final ObjectGrph<V, E> g;
    private final Path p;

    public ObjectPath(Path p, ObjectGrph<V, E> g)
    {
	this.p = p;
	this.g = g;
    }

    public List<V> getVertices()
    {
	List<V> r = new ArrayList<V>();

	for (int v : p.toVertexArray())
	{
	    r.add(g.i2v(v));
	}

	return r;
    }

    public int getLength()
    {
	return p.getLength();
    }

    public List<E> getEdges()
    {
	List<E> r = new ArrayList<E>();

	if (p.getNumberOfVertices() > 1)
	{
	    for (int i = 1; i < p.getNumberOfVertices(); ++i)
	    {
		int e = p.getEdgeHeadingToVertexAt(i);

		if (e >= 0)
		{
		    r.add(g.i2e(e));
		}
	    }
	}

	return r;
    }

    @Override
    public String toString()
    {
	List<V> vv = getVertices();
	String s = g.getVertexLabel(vv.get(0));
	List<E> ee = getEdges();

	if (ee.size() != vv.size() - 1)
	    throw new IllegalStateException(" esize=" + ee.size() + " vsize=" + vv.size());

	for (int i = 0; i < ee.size(); ++i)
	{
	    s += " >" + g.getEdgeLabel(ee.get(i)) + "> " + g.getVertexLabel(vv.get(i + 1));
	}

	return s;
    }

    public static void main(String[] args)
    {
	ObjectGrph<String, String> g = new ObjectGrph();
	g.addUndirectedSimpleEdge("luc", "kicks", "jeremy");
	g.addUndirectedSimpleEdge("jvm", "knows", "julien");
	System.out.println();
    }
}
