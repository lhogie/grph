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

package grph.algo.k_shortest_paths;

import grph.Grph;
import grph.algo.search.GraphSearchListener;
import grph.algo.search.RandomSearch;
import grph.in_memory.InMemoryGrph;
import grph.path.ArrayListPath;
import grph.properties.NumericalProperty;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Random;

import toools.math.MathsUtilities;

import com.carrotsearch.hppc.IntArrayList;

public class RandomWalkBasedKShortestPaths extends KShortestPathsAlgorithm
{
	private Random random = new Random();

	public Random getRandom()
	{
		return random;
	}

	public void setRandom(Random random)
	{
		this.random = random;
	}

	@Override
	public List<ArrayListPath> compute(Grph g, final int source, final int destination, int numberOfPathsRequested, NumericalProperty weights)
	{
		final IntArrayList vertices = new IntArrayList();
		final PriorityQueue<ArrayListPath> pathQueue = new PriorityQueue<ArrayListPath>(10, new Comparator<ArrayListPath>() {

			@Override
			public int compare(ArrayListPath p1, ArrayListPath p2)
			{
				return MathsUtilities.compare(p1.getLength(), p2.getLength());
			}
		});

		while (pathQueue.size() < numberOfPathsRequested)
		{
			new RandomSearch(g, source, Grph.DIRECTION.out, random, g.getNumberOfVertices(), new GraphSearchListener() {

				@Override
				public DECISION vertexFound(int vertex)
				{
					if (vertex == destination)
					{
						ArrayListPath p = new ArrayListPath();

						for (int v : vertices.toArray())
						{
							p.extend(v);
						}

						p.extend(destination);

						if (!pathQueue.contains(p))
						{
							pathQueue.add(p);
						}

						vertices.clear();
						return DECISION.STOP;
					}
					else
					{
						int i = vertices.indexOf(vertex);

						// make sure the found path will be elementary
						if (i >= 0)
						{
							vertices.removeRange(i + 1, vertices.size());
						}
						else
						{
							vertices.add(vertex);
						}

						return DECISION.CONTINUE;
					}
				}

				@Override
				public void searchStarted()
				{
				}

				@Override
				public void searchCompleted()
				{
				}

			});
		}

		List<ArrayListPath> paths = new ArrayList<ArrayListPath>(pathQueue);
		return paths;
	}

	public static void main(String[] args)
	{
		Grph g = new InMemoryGrph();
		g.grid(5, 5);
		g.display();
		List<ArrayListPath> paths = new RandomWalkBasedKShortestPaths().compute(g, 0, 20, 5, null);

		for (ArrayListPath p : paths)
		{
			System.out.println(p.isElementary() + " length: " + p.getLength() + " vertices " + p);
			p.setColor(g, 7);
		}
	}
}
