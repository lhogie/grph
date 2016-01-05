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

public class PathExtender extends AbstractPath
{
	private final Path basePath;
	private final int extensionEdge;
	private final int extensionVertex;

	public PathExtender(Path p, int extensionEdge, int extensionVertex)
	{
		this.basePath = p;
		this.extensionEdge = extensionEdge;
		this.extensionVertex = extensionVertex;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj instanceof PathExtender)
		{
			PathExtender e = (PathExtender) obj;
			return basePath.equals(e.basePath)
					&& extensionEdge == e.extensionEdge
					&& extensionVertex == e.extensionVertex;
		}
		else if (obj instanceof Path)
		{
			Path p = (Path) obj;
			int n = p.getNumberOfVertices();

			if (n != p.getNumberOfVertices())
			{
				return false;
			}
			else
			{
				for (int i = 0; i < n; ++i)
				{
					int v = getVertexAt(i);
					int pv = p.getVertexAt(i);

					if (v != pv)
					{
						return false;
					}
					else
					{
						int e = getEdgeHeadingToVertexAt(i);
						int pe = p.getEdgeHeadingToVertexAt(i);

						if (e >= 0 && pe >= 0 && e != pe)
						{
							return false;
						}
					}
				}
			}

			return true;
		}
		else
		{
			return false;
		}
	}

	@Override
	public PathExtender clone()
	{
		return clone(false);
	}

	public PathExtender clone(boolean deep)
	{
		return new PathExtender(deep ? (Path) basePath.clone() : basePath,
				extensionEdge, extensionVertex);
	}

	@Override
	public int[] toVertexArray()
	{
		int[] s = basePath.toVertexArray();
		s = Arrays.copyOf(s, s.length + 1);
		s[s.length - 1] = extensionVertex;
		return s;
	}

	@Override
	public String toString()
	{
		if (extensionEdge < 0)
		{
			return basePath.toString() + " >" + extensionVertex;
		}
		else
		{
			return basePath.toString() + " >" + extensionEdge + "> "
					+ extensionVertex;
		}
	}

	@Override
	public int getDestination()
	{
		return extensionVertex;
	}

	@Override
	public void extend(int e, int v)
	{
		throw new PathNotModifiableException();
	}

	@Override
	public int getSource()
	{
		return basePath.getSource();
	}

	@Override
	public void setSource(int v)
	{
		basePath.setSource(v);
	}

	@Override
	public int getVertexAt(int i)
	{
		if (i < basePath.getNumberOfVertices())
		{
			return basePath.getVertexAt(i);
		}
		else if (i == basePath.getNumberOfVertices())
		{
			return extensionVertex;
		}
		else
		{
			throw new IndexOutOfBoundsException("numberOfVertices="
					+ getNumberOfVertices() + ", i=" + i);
		}
	}

	@Override
	public boolean containsVertex(int v)
	{
		return extensionVertex == v || basePath.containsVertex(v);
	}

	@Override
	public int indexOfVertex(int v)
	{
		return v == extensionVertex ? getLength() : basePath.indexOfVertex(v);
	}

	@Override
	public void reverse()
	{
		throw new PathNotModifiableException();
	}

	@Override
	public int getEdgeHeadingToVertexAt(int i)
	{
		if (i == getNumberOfVertices() - 1)
		{
			return extensionEdge;
		}
		else
		{
			return basePath.getEdgeHeadingToVertexAt(i);
		}
	}

	@Override
	public int getNumberOfVertices()
	{
		return basePath.getNumberOfVertices() + 1;
	}

	public static void main(String[] args)
	{
		Path p = new ArrayListPath();
		p.setSource(5);
		p.extend(1, 3);
		p.extend(5, 2);
		p.extend(3, 6);
		System.out.println(p);

		PathExtender e = new PathExtender(p, 6, 12);

		System.out.println(e);

		for (int i = 0; i < e.getNumberOfVertices(); ++i)
		{
			System.out.println(e.getVertexAt(i));
		}

		System.out.println(e.getVertexSet());
	}
}
