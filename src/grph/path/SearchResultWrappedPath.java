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

import it.unimi.dsi.fastutil.ints.IntArrayList;

import grph.Grph;
import grph.algo.search.SearchResult;
import grph.algo.topology.ChordalTopologyGenerator;
import grph.algo.topology.TopologyGenerator;
import grph.in_memory.InMemoryGrph;
import grph.properties.NumericalProperty;
import toools.collections.Arrays;

public class SearchResultWrappedPath extends AbstractPath
{
	private int source;
	private int destination;
	private int[] vertexArray;
	private final SearchResult predecessorMatrix;

	public SearchResultWrappedPath(SearchResult predecessorMatrix, int source,
			int destination)
	{
		if (predecessorMatrix == null)
			throw new IllegalArgumentException();

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
		if (this.vertexArray == null)
		{
			IntArrayList s = new IntArrayList();

			int pred = destination;

			while (pred != source)
			{
				s.add(pred);
				pred = predecessorMatrix.predecessors[pred];

				if (pred == - 1)
				{
					throw new IllegalStateException("path is not applicable");
				}
			}

			s.add(source);
			this.vertexArray = s.elements();
			Arrays.reverse(this.vertexArray);
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
		return - 1;
	}

	public static void main(String[] args)
	{
		Grph g = new InMemoryGrph();
		g.addNVertices(1000);
		
		TopologyGenerator tg = new ChordalTopologyGenerator();
		tg.compute(g);
		System.out.println(g);
		
		Path p = new SearchResultWrappedPath(g.bfs(0), 0, 486);
		System.out.println(p.isApplicable(g));
		System.out.println(p);
	}

}
