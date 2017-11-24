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

package grph.algo.topology.gsm;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import grph.Grph;
import grph.algo.topology.RandomTreeTopologyGenerator;
import grph.in_memory.InMemoryGrph;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntSet;
import toools.collections.LucIntSets;
import toools.collections.primitive.LucIntSet;
import toools.math.MathsUtilities;

/**
 * This topology generators creates a tree of rings. Incident rings have at
 * least two vertices in common.
 * 
 * It first creates an initial ring. Then, it creates several other rings. For
 * each of them: It select on the rings already in the graph as the parent of
 * the new ring. Then it selects 2 adjacent vertices in the new ring, and
 * disconnect them, turning the ring into a path. Each of the two ends of the
 * path is connected to a random vertex in the parent ring. These random
 * vertices are chosen so that their distance is at most 3.
 * 
 */

public class TreeOfRingsWirelessBackhaul
{
	public static void main(String[] args) throws IOException
	{
		Grph g = new InMemoryGrph();
		// compute(g, 10, 10, 20, 15, 4, 10, new Random(), true);
		createBackhaulWithNVertices(g, 40, new Random(), true);
		g.display();
		// g.toGraphviz(COMMAND.fdp, OUTPUT_FORMAT.pdf).open();
	}

	/**
	 * 
	 * @param g
	 * @param n
	 * @param random
	 * @param highlight
	 * @return a set of vertex sets, each containing the vertices implied in a
	 *         given tree.
	 */
	public static Int2ObjectMap<int[]> createBackhaulWithNVertices(Grph g, int n,
			Random random, boolean highlight)
	{
		int numberOfVerticesInTheBackbone = n / 2;
		int numberOfVerticesInTrees = n - numberOfVerticesInTheBackbone;

		int numberOfVerticesInARing = Math.min(20, numberOfVerticesInTheBackbone);
		int numberOfRings = numberOfVerticesInTheBackbone / numberOfVerticesInARing;

		int numberOfVerticesInATree = Math.min(5, numberOfVerticesInTrees);
		int numberOfTrees = numberOfVerticesInTrees / numberOfVerticesInATree;

		return createRingsAndTrees(g, numberOfRings, numberOfVerticesInARing,
				numberOfVerticesInARing, numberOfTrees, numberOfVerticesInATree,
				numberOfVerticesInATree, random, highlight);
	}

	/**
	 * Creates both the tree of rings and the trees attached to it.
	 * 
	 * @param g
	 * @param numberOfRings
	 * @param minNumberOfVerticesInARing
	 * @param maxNumberOfVerticesInARing
	 * @param numberOfTrees
	 * @param minNumberOfVerticesInATree
	 * @param maxNumberOfVerticesInATree
	 * @param random
	 * @param highlight
	 * @return a set of vertex sets, each containing the vertices implied in a
	 *         given tree.
	 */
	public static Int2ObjectMap<int[]> createRingsAndTrees(Grph g, int numberOfRings,
			int minNumberOfVerticesInARing, int maxNumberOfVerticesInARing,
			int numberOfTrees, int minNumberOfVerticesInATree,
			int maxNumberOfVerticesInATree, Random random, boolean highlight)
	{
		List<IntSet> rings = createRingsOnly(g, numberOfRings, minNumberOfVerticesInARing,
				maxNumberOfVerticesInARing, random);

		if (highlight)
		{
			g.highlightVertices(g.getVertices());
		}

		Int2ObjectMap<int[]> trees = addTrees(g, numberOfTrees,
				minNumberOfVerticesInATree, maxNumberOfVerticesInATree, random);

		if (highlight)
		{
			for (int[] t : trees.values())
			{
				g.highlightVertices(t);
			}
		}

		return trees;
	}

	private static IntSet createRing(Grph g, int minNumberOfVerticesPerRing,
			int maxNumberOfVerticesPerRing, Random random)
	{
		int n = MathsUtilities.pickRandomBetween(minNumberOfVerticesPerRing,
				maxNumberOfVerticesPerRing + 1, random);
		IntSet s = g.addNVertices(n);
		g.ring(s, false);
		return s;
	}

	/**
	 * 
	 * @param g
	 * @param numberOfRings
	 * @param minNumberOfVerticesInARing
	 * @param maxNumberOfVerticesInARing
	 * @param random
	 * @return the list of rings.
	 */
	public static List<IntSet> createRingsOnly(Grph g, int numberOfRings,
			int minNumberOfVerticesInARing, int maxNumberOfVerticesInARing, Random random)
	{
		List<IntSet> rings = new ArrayList();
		rings.add(createRing(g, minNumberOfVerticesInARing, maxNumberOfVerticesInARing,
				random));

		while (rings.size() < numberOfRings)
		{
			// create a new ring for the tree
			IntSet ring = createRing(g, minNumberOfVerticesInARing,
					maxNumberOfVerticesInARing, random);

			// select a random ring in the tree as the parent for the new
			// ring
			IntSet parentRing = rings.get(random.nextInt(rings.size()));
			int someVertexInRing = LucIntSets.pickRandomInt(ring, random);
			int itsNeighborInRing = LucIntSets.intersectionToTarget(LucIntSet.class,
					ring, g.getNeighbours(someVertexInRing)).pickRandomElement(random);

			int someVertexInParentRing = LucIntSets.pickRandomInt(parentRing, random);
			int distance = Math.max(parentRing.size() / 3, 1);
			IntSet fringe = g.getFringes(distance, someVertexInParentRing).get(distance);
			int itsNeighborInParentRing = LucIntSets
					.intersectionToTarget(LucIntSet.class, parentRing, fringe)
					.pickRandomElement(random);

			g.addUndirectedSimpleEdge(someVertexInParentRing, someVertexInRing);
			g.addUndirectedSimpleEdge(itsNeighborInParentRing, itsNeighborInRing);
			g.disconnect(someVertexInRing, itsNeighborInRing);

			rings.add(ring);
		}

		return rings;
	}

	public static Int2ObjectMap<int[]> addTrees(Grph g, int numberOfTrees,
			int minNumberOfVerticesInATree, int maxNumberOfVerticesInATree, Random random)
	{
		int[] attachementPoints = LucIntSets
				.pickRandomSubIntset(g.getVertices(), random, numberOfTrees, false)
				.toIntArray();
		Int2ObjectMap<int[]> trees = new Int2ObjectOpenHashMap<int[]>();

		for (int attachementVertexInBackbone : attachementPoints)
		{
			// creates a new tree
			Grph tree = new InMemoryGrph();
			RandomTreeTopologyGenerator.compute(tree, MathsUtilities.pickRandomBetween(
					minNumberOfVerticesInATree, maxNumberOfVerticesInATree + 1, random));

			// plunge it into the graph
			int[] relabeledTreeVertices = g.addGraph(tree).values().toIntArray();

			// randomly select a root
			int root = relabeledTreeVertices[random
					.nextInt(relabeledTreeVertices.length)];
			trees.put(root, relabeledTreeVertices);

			// connect the tree in to the backbone
			g.contractVertices(attachementVertexInBackbone, root);
		}

		return trees;
	}
}
