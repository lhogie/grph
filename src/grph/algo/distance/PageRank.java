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
 
 

package grph.algo.distance;

import java.util.Random;

import grph.Grph;
import grph.algo.MultiThreadProcessing;
import toools.collections.primitive.IntCursor;
import toools.math.Distribution;
import toools.math.MathsUtilities;

/**
 * The page rank value for every vertex in the graph. The page rank is computed
 * iteratively, by performing random walks from random sources.
 * 
 * @author lhogie
 * 
 */

public class PageRank
{
	private final int[] occurences;
	private long sum;
	final private Random prng;
	private final Grph g;

	/**
	 * Creates an empty page rank for the given graph, using the given random
	 * number generator.
	 * 
	 * @param g
	 *            a graph
	 * @param prng
	 *            a pseudo-random number generator
	 */
	public PageRank(Grph g, Random prng)
	{
		this.g = g;
		this.occurences = new int[g.getNumberOfVertices() + 1];
		this.prng = prng;
	}

	private void addOccurence(int v)
	{
		++occurences[v];
		++sum;
	}

	/**
	 * Retrieves the number of times the given vertex was encountered along the
	 * random walks
	 * 
	 * @param v
	 * @return
	 */
	public int getNumberOfOccurences(int v)
	{
		return occurences[v];
	}

	/**
	 * Computes the rank for the given vertex.
	 * 
	 * @param v
	 *            a vertex in the graph
	 * @return the rank of v, which is a ratio between 0 and 1. The sum of the
	 *         ranks for all vertices in the graph is 1.
	 */
	public double getRank(int v)
	{
		return ((double) occurences[v]) / sum;
	}

	/**
	 * Graphically renders this page rank on the graph.
	 */
	public void render()
	{
		for (IntCursor c : IntCursor.fromFastUtil(g.getVertices()))
		{
			int v = c.value;
			g.getVertexLabelProperty().setValue(v, "" + MathsUtilities.round(getRank(v), 2));
		}
	}

	/**
	 * Computes the distribution for this page rank.
	 * 
	 * @return the distribution object.
	 */
	public Distribution<Double> getDistribution()
	{
		Distribution<Double> d = new Distribution("page rank distribution");

		for (IntCursor c : IntCursor.fromFastUtil(g.getVertices()))
		{
			int v = c.value;
			System.out.println(MathsUtilities.round(getRank(v), 2));
			d.addOccurence(MathsUtilities.round(getRank(v), 2));
		}

		return d;
	}

	/**
	 * Performs the given number of random walks of the given length, starting
	 * from every vertex in the graph.
	 * 
	 * @param numberOfIterations
	 *            the number of "all source random walks" to do.
	 * @param walksLength
	 *            the length of each random walk
	 */
	public void iterate(int numberOfIterations, int walksLength)
	{
		for (int i = 0; i < numberOfIterations; ++i)
		{
			allSourceRandomWalks(walksLength, false);
		}
	}

	/**
	 * Performs random walks of the given length, starting from every vertex in
	 * the graph.
	 * 
	 * @param walksLength
	 *            the length of each random walk
	 * @param multithreaded
	 *            whether the process will be be multi-threaded.
	 */
	public void allSourceRandomWalks(final int walksLength, boolean multithreaded)
	{
		if (multithreaded)
		{
			new MultiThreadProcessing(g.getVertices()) {

				@Override
				protected void run(int threadID, int source)
				{
					iterate(source, walksLength);
				}
			};
		}
		else
		{
			for (IntCursor c : IntCursor.fromFastUtil(g.getVertices()))
			{
				randomWalk(c.value, walksLength);
			}
		}
	}

	/**
	 * Performs one single random walk of the given length, starting from the
	 * given source.
	 * 
	 * @param source
	 *            a vertex in the graph
	 * @param length
	 *            the length of the walk.
	 */
	public void randomWalk(int source, int length)
	{
		addOccurence(source);
		int[][] adjacencies = g.getOutNeighborhoods();

		for (int j = 0; j < length; ++j)
		{
			int v = source;
			int[] outNeighbors = adjacencies[v];

			if (outNeighbors.length == 0)
			{
				break;
			}
			else
			{
				v = outNeighbors[prng.nextInt(outNeighbors.length)];
				addOccurence(v);
			}
		}
	}

	/**
	 * Computes the page rank value for every vertex in the graph. If the graph
	 * has less than 1000 vertices, this runs n "all source random walks" of
	 * length n (complexity is O(n2)), other this runs sqrt(n)
	 * "all source random walks" of length sqrt(n) (complexity is O(n)).
	 */
	public void compute()
	{
		int n = g.getNumberOfVertices();

		if (n < 1000)
		{
			iterate(n, n);
		}
		else
		{
			int sqr = (int) Math.sqrt(n);
			iterate(sqr, sqr);
		}

	}

}
