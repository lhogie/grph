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
import grph.algo.search.SearchResult;
import grph.algo.topology.ClassicalGraphs;
import grph.properties.NumericalProperty;
import toools.collections.Arrays;

import com.carrotsearch.hppc.IntArrayList;

public class SearchResultWrappedPath extends AbstractPath
{
	private int source;
	private int destination;
	private int[] vertexArray;
	private final SearchResult predecessorMatrix;

	public SearchResultWrappedPath(SearchResult predecessorMatrix, int source, int destination)
	{
		this.predecessorMatrix = predecessorMatrix;
		this.source = source;
		this.destination = destination;
	}

	@Override
	public int getSource()
	{
		return source;
	}

	@Override
	public int getDestination()
	{
		return destination;
	}

	@Override
	public int getNumberOfVertices()
	{
		return toVertexArray().length;
	}

	@Override
	public int[] toVertexArray()
	{
		// maybe it was already computed
		if (vertexArray == null)
		{
			IntArrayList s = new IntArrayList();

			int pred = destination;

			while (pred != source)
			{
				s.add(pred);
				pred = predecessorMatrix.predecessors[pred];

				if (pred == -1)
				{
					throw new IllegalStateException("path is not applicable");
				}
			}

			s.add(source);
			vertexArray = s.toArray();
			Arrays.reverse(vertexArray);
		}

		return vertexArray;
	}

	@Override
	public boolean isShortestPath(Grph g, NumericalProperty weights)
	{
		throw new IllegalStateException();
	}

	@Override
	public boolean containsVertex(int someVertex)
	{
		return Arrays.contains(toVertexArray(), someVertex);
	}

	@Override
	public void setSource(int v)
	{
		this.source = v;
		vertexArray = null;
	}

	@Override
	public int getVertexAt(int i)
	{
		return toVertexArray()[i];
	}

	@Override
	public AbstractPath clone()
	{
		return new ArrayPath(toVertexArray());
	}

	@Override
	public int indexOfVertex(int v)
	{
		return Arrays.indexOf(toVertexArray(), v);
	}

	@Override
	public void extend(int e, int v)
	{
		throw new PathNotModifiableException();
	}

	@Override
	public void reverse()
	{
		throw new PathNotModifiableException();
	}

	@Override
	public int getEdgeHeadingToVertexAt(int i)
	{
		return -1;
	}

	public static void main(String[] args)
	{
		Grph g = ClassicalGraphs.grid(5, 5);

		Path p = new SearchResultWrappedPath(g.bfs(0), 0, 16);
		System.out.println(p);
	}

}
