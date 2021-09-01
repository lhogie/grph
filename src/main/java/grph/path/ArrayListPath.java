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

package grph.path;

import grph.Grph;
import grph.algo.topology.ClassicalGraphs;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntArrays;

public class ArrayListPath extends AbstractPath
{
	private final IntArrayList vertexList = new IntArrayList();
	private final IntArrayList edgeList = new IntArrayList();

	@Override
	public int getSource()
	{
		if (vertexList.isEmpty())
			throw new IllegalStateException("path is empty");

		return vertexList.getInt(0);
	}

	@Override
	public int getDestination()
	{
		if (vertexList.isEmpty())
			throw new IllegalStateException("path is empty");

		return vertexList.getInt(vertexList.size() - 1);
	}

	@Override
	public int getNumberOfVertices()
	{
		return vertexList.size();
	}

	@Override
	public int[] toVertexArray()
	{
		return vertexList.toIntArray();
	}

	@Override
	public void reverse()
	{
		new IntArrayList(IntArrays.reverse(vertexList.toIntArray()));
		new IntArrayList(IntArrays.reverse(edgeList.toIntArray()));
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
