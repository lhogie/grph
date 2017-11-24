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
 
 package grph.oo;

import java.util.ArrayList;
import java.util.List;

import grph.path.Path;

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
