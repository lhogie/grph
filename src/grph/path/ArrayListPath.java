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

package grph.path;

import grph.Grph;
import grph.algo.topology.ClassicalGraphs;
import toools.collections.Collections;

import com.carrotsearch.hppc.IntArrayList;

public class ArrayListPath extends AbstractPath
{
	private final IntArrayList vertexList = new IntArrayList();
	private final IntArrayList edgeList = new IntArrayList();

	@Override
	public int getSource()
	{
		if (vertexList.isEmpty())
			throw new IllegalStateException("path is empty");

		return vertexList.get(0);
	}

	@Override
	public int getDestination()
	{
		if (vertexList.isEmpty())
			throw new IllegalStateException("path is empty");

		return vertexList.get(vertexList.size() - 1);
	}

	@Override
	public int getNumberOfVertices()
	{
		return vertexList.size();
	}

	@Override
	public int[] toVertexArray()
	{
		return vertexList.toArray();
	}

	@Override
	public void reverse()
	{
		Collections.reverse(vertexList.buffer, 0, vertexList.size());
		Collections.reverse(edgeList.buffer, 0, edgeList.size());
	}

	@Override
	public boolean containsVertex(int someVertex)
	{
		return vertexList.contains(someVertex);
	}

	@Override
	public void setSource(int v)
	{
		if (vertexList.isEmpty())
		{
			vertexList.add(v);
		}
		else
		{
			vertexList.set(0, v);
		}
	}

	@Override
	public int getVertexAt(int i)
	{
		return vertexList.get(i);
	}

	@Override
	public int indexOfVertex(int v)
	{
		return vertexList.indexOf(v);
	}

	@Override
	public void extend(int thoughLink, int v)
	{
		if (thoughLink >= 0 && getNumberOfVertices() == 0)
			throw new IllegalStateException("a path cannot start with an edge");

		edgeList.add(thoughLink);
		vertexList.add(v);
	}

	@Override
	public boolean equals(Object o)
	{
		if (o instanceof ArrayListPath)
		{
			return equals((ArrayListPath) o);
		}
		else
		{
			return super.equals(o);
		}
	}

	public boolean equals(ArrayListPath p)
	{
		return vertexList.equals(p.vertexList) && edgeList.equals(p.edgeList);
	}

	@Override
	public ArrayListPath clone()
	{
		ArrayListPath c = new ArrayListPath();
		c.setSource(getSource());

		for (int i = 0; i < edgeList.size(); ++i)
		{
			c.extend(edgeList.get(i), getVertexAt(i + 1));
		}

		return c;
	}

	@Override
	public int getEdgeHeadingToVertexAt(int i)
	{
		if (i == 0)
			throw new IllegalArgumentException(
					"the source of a path has no edge heading to it");

		return edgeList.get(i - 1);
	}

	public static void main(String[] args)
	{
		Path p = new ArrayListPath();
		p.setSource(0);
		p.extend(4);
		p.extend(3, 5);

		for (int i = 1; i < p.getNumberOfVertices(); ++i)
		{
			System.out.println(p.getEdgeHeadingToVertexAt(i));
		}

		System.out.println(p);

		Grph g = ClassicalGraphs.grid(4, 4);
		p.setColor(g, 6);
		g.display();
	}
}
