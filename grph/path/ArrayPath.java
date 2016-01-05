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

import java.util.Arrays;

public class ArrayPath extends AbstractPath
{
	private final int[] vertices;

	public ArrayPath(int... vertices)
	{
		this.vertices = vertices;
	}

	@Override
	public int getSource()
	{
		return vertices[0];
	}

	@Override
	public void setSource(int v)
	{
		vertices[0] = v;
	}

	@Override
	public int getVertexAt(int i)
	{
		return vertices[i];
	}

	@Override
	public AbstractPath clone()
	{
		return new ArrayPath(Arrays.copyOf(vertices, vertices.length));
	}

	@Override
	public int getDestination()
	{
		return vertices[vertices.length - 1];
	}

	@Override
	public int[] toVertexArray()
	{
		return vertices;
	}

	@Override
	public boolean containsVertex(int someVertex)
	{
		return toools.collections.Arrays.contains(vertices, someVertex);
	}

	@Override
	public int indexOfVertex(int v)
	{
		return toools.collections.Arrays.indexOf(vertices, v);
	}

	@Override
	public void extend(int e, int v)
	{
		throw new PathNotModifiableException();
	}

	@Override
	public void reverse()
	{
		throw new IllegalStateException();
	}

	@Override
	public int getEdgeHeadingToVertexAt(int i)
	{
		return -1;
	}

	@Override
	public int getNumberOfVertices()
	{
		return vertices.length;
	}

	public static void main(String[] args)
	{
		Path p = new ArrayPath(4, 5, 7);
		System.out.println(p);
		System.out.println(p.getLength());
	}
}
