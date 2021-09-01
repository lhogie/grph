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

package grph.algo.clustering;

import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

import grph.Grph;
import grph.algo.topology.gsm.WirelessBackhaul;
import grph.in_memory.InMemoryGrph;
import grph.io.GraphvizImageWriter.COMMAND;
import grph.io.GraphvizImageWriter.OUTPUT_FORMAT;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntIterator;
import it.unimi.dsi.fastutil.ints.IntSet;
import toools.StopWatch;
import toools.collections.primitive.IntCursor;
import toools.collections.primitive.SelfAdaptiveIntSet;
import toools.gui.ColorPalette;
import toools.gui.RandomPalette;

public class CONID
{
	private class Pair
	{
		final IntSet kneighbors;
		final int id;

		public Pair(int id, IntSet neighbors)
		{
			this.kneighbors = neighbors;
			this.id = id;
		}

		public boolean isGreaterThan(Pair p)
		{
			int con = kneighbors.size();
			int pcon = p.kneighbors.size();
			return con > pcon || (con == pcon && id < p.id);
		}
	}

	public Clustering computeClusters(Grph g, int k)
	{
		// initialize data structures
		Pair[] pairs = createPairs(g, k);

		IntSet clusterHeads = new SelfAdaptiveIntSet(0);
		IntSet[] node_heads = new SelfAdaptiveIntSet[g.getVertices().getGreatest() + 1];

		for (int v : g.getVertices().toIntArray())
		{
			node_heads[v] = new SelfAdaptiveIntSet(0);
		}

		// ask node to self-identity as cluster heads or not
		for (int v : g.getVertices().toIntArray())
		{
			// if the node sees it has the greatest pair around
			if (hasGreatestPairInNeighborhood(v, pairs[v].kneighbors, pairs))
			{
				// self-identify as a cluster head
				clusterHeads.add(v);
				node_heads[v].add(v);
				// System.out.println("nodes self-identifying as cluster heads:
				// "
				// + v);
				// System.out.println("notifying: " + pairs[v].neighbors);

				// and notify its k-neighbors
				for (IntCursor c : IntCursor.fromFastUtil(pairs[v].kneighbors))
				{
					node_heads[c.value].add(v);
				}
			}
		}

		// ask nodes that have not receive cluster head request to create
		// clusters
		for (int v : g.getVertices().toIntArray())
		{
			if (node_heads[v].isEmpty())
			{
				// creates its own cluster
				clusterHeads.add(v);
				node_heads[v].add(v);

				// and notify its k-neighbors
				for (IntCursor c : IntCursor.fromFastUtil(pairs[v].kneighbors))
				{
					node_heads[c.value].add(v);
				}
			}
		}

		// now each node must choose to which cluster it'll belong to
		int[] node_head = new int[g.getVertices().getGreatest() + 1];
		Arrays.fill(node_head, - 1);

		for (int v : g.getVertices().toIntArray())
		{
			if (node_heads[v].isEmpty())
			{
				throw new IllegalStateException();
			}
			else if (node_heads[v].size() == 1)
			{
				node_head[v] = node_heads[v].iterator().nextInt();
			}
			else
			{
				node_head[v] = getGreatestPair(node_heads[v], pairs).id;
			}
		}

		return createClusters(g.getVertices(), node_head);
	}

	/**
	 * Create the initial pairs.
	 * 
	 * @param g
	 * @param k
	 * @return
	 */
	private Pair[] createPairs(Grph g, int k)
	{
		Pair[] pairs = new Pair[g.getVertices().getGreatest() + 1];

		for (int v : g.getVertices().toIntArray())
		{
			pairs[v] = new Pair(v, g.getNeighboursAtMostKHops(k, v));
		}

		return pairs;
	}

	/**
	 * Finds the greatest pairs in the given set of nodes.
	 * 
	 * @param vertices
	 * @param pairs
	 * @return
	 */
	private Pair getGreatestPair(IntSet vertices, Pair[] pairs)
	{
		if (vertices.isEmpty())
			throw new IllegalArgumentException("no node to iterate over");

		IntIterator i = vertices.iterator();
		Pair maxPair = pairs[i.next()];

		while (i.hasNext())
		{
			Pair p = pairs[i.nextInt()];

			if (p.isGreaterThan(maxPair))
			{
				maxPair = p;
			}
		}

		return maxPair;
	}

	/**
	 * Checks if the given node has the given pair in the given set of nodes.
	 * 
	 * @param v
	 * @param set
	 * @param pairs
	 * @return
	 */
	private boolean hasGreatestPairInNeighborhood(int v, IntSet set, Pair[] pairs)
	{
		Pair pv = pairs[v];

		for (IntCursor c : IntCursor.fromFastUtil(set))
		{
			// if we have found a pair that is greater that v's one
			if (pairs[c.value].isGreaterThan(pv))
			{
				return false;
			}
		}

		return true;
	}

	/**
	 * Builds the Grph clusters out of the info computed by the CONID algo
	 * 
	 * @param nodes
	 * @param node_head
	 * @return
	 */
	private Clustering createClusters(IntSet nodes, int[] node_head)
	{
		Int2ObjectMap<Cluster> head_cluster = new Int2ObjectOpenHashMap<Cluster>();

		for (IntCursor cv : IntCursor.fromFastUtil(nodes))
		{
			int v = cv.value;
			int h = node_head[v];

			// if node v belong to a cluster
			if (h != - 1)
			{
				Cluster c = head_cluster.get(h);

				if (c == null)
				{
					c = new Cluster(0);
					c.add(h);
					c.setHead(h);
					head_cluster.put(h, c);
				}

				c.add(v);
			}
		}

		Clustering clustering = new Clustering();
		clustering.getClusters().addAll(head_cluster.values());
		return clustering;
	}

	public static void main(String[] args) throws IOException
	{
		// enable errors checking
		ClassLoader.getSystemClassLoader().setDefaultAssertionStatus(true);

		// creates a random palette with enough colors
		ColorPalette palette = new RandomPalette(new Random(), 128);

		// creates an empty graph
		Grph g = new InMemoryGrph();
		WirelessBackhaul.degenerate(g, 100, 0.7);

		StopWatch sw = new StopWatch();
		// computes the clusters with k=4
		Clustering clusters = new CONID().computeClusters(g, 4);
		System.out.println(sw.getElapsedTime());

		// display the clusters on the graph
		g.highlight(clusters.getClusters());

		// ask Graphviz to creates an image and open it
		g.toGraphviz(COMMAND.fdp, OUTPUT_FORMAT.png);

	}
}
