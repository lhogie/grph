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
 
 

package grph.algo.topology;

import java.util.Random;

import grph.Grph;
import grph.in_memory.InMemoryGrph;

/**
 * The {@link Class} {@code ClassicalGraphs} contains methods for easily
 * obtaining classical graphs (paths, cycles, cliques, Petersen graph...)
 * 
 * @author Gregory Morel
 */
public class ClassicalGraphs
{

	// Don't instanciate this class
	private ClassicalGraphs()
	{

	}

	public Grph clique(int n)
	{
		Grph g = new InMemoryGrph();
		g.addNVertices(n);
		g.clique();
		return g;
	}

	// Ajouter des IllegalArgumentException

	/**
	 * Constructs the path graph on <i>n</i> vertices, P<sub>n</sub>.
	 * 
	 * @param n
	 *            the number of vertices of the path
	 * @return the path graph on <i>n</i> vertices, or the null graph (the graph
	 *         of order zero) if {@code n} is non-positive
	 */
	public static Grph path(int n)
	{
		if (n < 1)
			throw new IllegalArgumentException();

		Grph g = new InMemoryGrph();

		for (int i = 1; i < n; i++)
		{
			g.addUndirectedSimpleEdge(i - 1, i);
		}

		return g;
	}

	/**
	 * Constructs the cycle graph on <i>n</i> vertices, C<sub>n</sub>.
	 * 
	 * @param n
	 *            the number of vertices of the cycle
	 * @return the cycle graph on <i>n</i> vertices, or the null graph (the
	 *         graph of order zero) if {@code n} is non-positive
	 */
	public static Grph cycle(int n)
	{
		Grph g = path(n);
		if (n > 1)
			g.addUndirectedSimpleEdge(0, n - 1);

		return g;
	}

	/**
	 * Constructs the complete graph (also known as clique) on <i>n</i>
	 * vertices, K<sub>n</sub>.
	 * 
	 * @param n
	 *            the number of vertices of the complete graph
	 * @return the complete graph on <i>n</i> vertices, or the null graph (the
	 *         graph of order zero) if {@code n} is non-positive
	 */
	public static Grph completeGraph(int n)
	{
		Grph g = new InMemoryGrph();

		if (n == 1)
			// If absent, the complete graph of order 1 contains no vertex.
			g.addVertex();
		else
			for (int i = 0; i < n; i++)
				for (int j = i + 1; j < n; j++)
					g.addUndirectedSimpleEdge(i, j);

		return g;
	}

	/**
	 * Constructs the complete bipartite graph K<sub>m,n</sub>.
	 * 
	 * @param m
	 *            the number of vertices in the left side
	 * @param n
	 *            the number of vertices in the right side
	 * @return the complete bipartite graph on K<sub>m,n</sub>, or the null
	 *         graph (the graph of order zero) if {@code m} or {@code n} is
	 *         negative
	 */
	public static Grph completeBipartiteGraph(int m, int n)
	{
		Grph g = new InMemoryGrph();

		g.addNVertices(m + n);
		for (int i = 0; i < m; i++)
			for (int j = m; j < m + n; j++)
				g.addUndirectedSimpleEdge(i, j);

		return g;
	}

	/**
	 * Constructs a random bipartite graph. The probability of the existence of
	 * an edge is given by the parameter {@code p}
	 * 
	 * @param m
	 *            the number of vertices in the left side
	 * @param n
	 *            the number of vertices in the right side
	 * @param p
	 *            the probability for an edge to exist
	 * @return
	 */
	public static Grph randomBipartiteGraph(int m, int n, double p)
	{
		Random r = new Random();

		Grph g = new InMemoryGrph();
		g.addNVertices(m + n);

		for (int i = 0; i < m; i++)
			for (int j = m; j < m + n; j++)
				if (r.nextDouble() <= p)
					g.addUndirectedSimpleEdge(i, j);

		return g;
	}

	/**
	 * Constructs the <a href="http://en.wikipedia.org/wiki/Petersen_graph"
	 * target="_blank">Petersen graph</a>.
	 * 
	 * @return the Petersen graph.
	 */
	public static Grph PetersenGraph()
	{
		Grph g = cycle(5);
		g.addNVertices(5);
		for (int i = 5; i <= 9; i++)
		{
			g.addUndirectedSimpleEdge(i, i - 5);
			g.addUndirectedSimpleEdge(i, (i + 2) % 5 + 5);
		}

		return g;
	}

	public static Grph grid(int i, int j)
	{
		Grph g = new InMemoryGrph();
		g.grid(i, j);
		return g;
	}
}
