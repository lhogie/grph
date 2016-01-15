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

package grph.algo.topology.gsm;

import grph.Grph;
import grph.algo.topology.RandomTreeTopologyGenerator;
import grph.in_memory.InMemoryGrph;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import toools.math.MathsUtilities;
import toools.set.IntSet;
import toools.set.IntSets;

import com.carrotsearch.hppc.IntObjectMap;
import com.carrotsearch.hppc.IntObjectOpenHashMap;
import com.carrotsearch.hppc.cursors.ObjectCursor;

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
//		g.toGraphviz(COMMAND.fdp, OUTPUT_FORMAT.pdf).open();
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
	public static IntObjectMap<int[]> createBackhaulWithNVertices(Grph g, int n, Random random, boolean highlight)
	{
		int numberOfVerticesInTheBackbone = n / 2;
		int numberOfVerticesInTrees = n - numberOfVerticesInTheBackbone;

		int numberOfVerticesInARing = Math.min(20, numberOfVerticesInTheBackbone);
		int numberOfRings = numberOfVerticesInTheBackbone / numberOfVerticesInARing;

		int numberOfVerticesInATree = Math.min(5, numberOfVerticesInTrees);
		int numberOfTrees = numberOfVerticesInTrees / numberOfVerticesInATree;

		return createRingsAndTrees(g, numberOfRings, numberOfVerticesInARing, numberOfVerticesInARing, numberOfTrees, numberOfVerticesInATree,
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
	public static IntObjectMap<int[]> createRingsAndTrees(Grph g, int numberOfRings, int minNumberOfVerticesInARing, int maxNumberOfVerticesInARing,
			int numberOfTrees, int minNumberOfVerticesInATree, int maxNumberOfVerticesInATree, Random random, boolean highlight)
	{
		List<IntSet> rings = createRingsOnly(g, numberOfRings, minNumberOfVerticesInARing, maxNumberOfVerticesInARing, random);

		if (highlight)
		{
			g.highlightVertices(g.getVertices());
		}

		IntObjectMap<int[]> trees = addTrees(g, numberOfTrees, minNumberOfVerticesInATree, maxNumberOfVerticesInATree, random);

		if (highlight)
		{
			for (ObjectCursor<int[]> t : trees.values())
			{
				g.highlightVertices(t.value);
			}
		}

		return trees;
	}

	private static IntSet createRing(Grph g, int minNumberOfVerticesPerRing, int maxNumberOfVerticesPerRing, Random random)
	{
		int n = MathsUtilities.pickRandomBetween(minNumberOfVerticesPerRing, maxNumberOfVerticesPerRing + 1, random);
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
	public static List<IntSet> createRingsOnly(Grph g, int numberOfRings, int minNumberOfVerticesInARing, int maxNumberOfVerticesInARing, Random random)
	{
		List<IntSet> rings = new ArrayList();
		rings.add(createRing(g, minNumberOfVerticesInARing, maxNumberOfVerticesInARing, random));

		while (rings.size() < numberOfRings)
		{
			// create a new ring for the tree
			IntSet ring = createRing(g, minNumberOfVerticesInARing, maxNumberOfVerticesInARing, random);

			// select a random ring in the tree as the parent for the new
			// ring
			IntSet parentRing = rings.get(random.nextInt(rings.size()));
			int someVertexInRing = ring.pickRandomElement(random);
			int itsNeighborInRing = IntSets.intersection(ring, g.getNeighbours(someVertexInRing)).pickRandomElement(random);

			int someVertexInParentRing = parentRing.pickRandomElement(random);
			int distance = Math.max(parentRing.size() / 3, 1);
			IntSet fringe = g.getFringes(distance, someVertexInParentRing).get(distance);
			int itsNeighborInParentRing = IntSets.intersection(parentRing, fringe).pickRandomElement(random);

			g.addUndirectedSimpleEdge(someVertexInParentRing, someVertexInRing);
			g.addUndirectedSimpleEdge(itsNeighborInParentRing, itsNeighborInRing);
			g.disconnect(someVertexInRing, itsNeighborInRing);

			rings.add(ring);
		}

		return rings;
	}

	public static IntObjectMap<int[]> addTrees(Grph g, int numberOfTrees, int minNumberOfVerticesInATree, int maxNumberOfVerticesInATree, Random random)
	{
		int[] attachementPoints = g.getVertices().pickRandomSubset(random, numberOfTrees, false).toIntArray();
		IntObjectMap<int[]> trees = new IntObjectOpenHashMap<int[]>();

		for (int attachementVertexInBackbone : attachementPoints)
		{
			// creates a new tree
			Grph tree = new InMemoryGrph();
			RandomTreeTopologyGenerator.compute(tree, MathsUtilities.pickRandomBetween(minNumberOfVerticesInATree, maxNumberOfVerticesInATree + 1, random));

			// plunge it into the graph
			int[] relabeledTreeVertices = g.addGraph(tree).values().toIntArray();

			// randomly select a root
			int root = relabeledTreeVertices[random.nextInt(relabeledTreeVertices.length)];
			trees.put(root, relabeledTreeVertices);

			// connect the tree in to the backbone
			g.contractVertices(attachementVertexInBackbone, root);
		}

		return trees;
	}
}
