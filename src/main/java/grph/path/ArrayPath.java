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
