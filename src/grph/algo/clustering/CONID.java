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
 
 package grph.algo.clustering;

import grph.Grph;
import grph.algo.topology.gsm.WirelessBackhaul;
import grph.in_memory.InMemoryGrph;
import grph.io.GraphvizImageWriter.COMMAND;
import grph.io.GraphvizImageWriter.OUTPUT_FORMAT;

import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

import toools.StopWatch;
import toools.gui.ColorPalette;
import toools.gui.RandomPalette;
import toools.math.IntIterator;
import toools.set.DefaultIntSet;
import toools.set.IntSet;

import com.carrotsearch.hppc.IntObjectMap;
import com.carrotsearch.hppc.IntObjectOpenHashMap;
import com.carrotsearch.hppc.cursors.IntCursor;
import com.carrotsearch.hppc.cursors.IntObjectCursor;

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

	IntSet clusterHeads = new DefaultIntSet();
	IntSet[] node_heads = new DefaultIntSet[g.getVertices().getGreatest() + 1];

	for (int v : g.getVertices().toIntArray())
	{
	    node_heads[v] = new DefaultIntSet();
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
		// System.out.println("nodes self-identifying as cluster heads: "
		// + v);
		// System.out.println("notifying: " + pairs[v].neighbors);

		// and notify its k-neighbors
		for (IntCursor c : pairs[v].kneighbors)
		{
		    node_heads[c.value].add(v);
		}
	    }
	}

	// ask nodes that have not receive cluster head request to create clusters
	for (int v : g.getVertices().toIntArray())
	{
	    if (node_heads[v].isEmpty())
	    {
		// creates its own cluster
		clusterHeads.add(v);
		node_heads[v].add(v);

		// and notify its k-neighbors
		for (IntCursor c : pairs[v].kneighbors)
		{
		    node_heads[c.value].add(v);
		}
	    }
	}

	// now each node must choose to which cluster it'll belong to
	int[] node_head = new int[g.getVertices().getGreatest() + 1];
	Arrays.fill(node_head, -1);

	for (int v : g.getVertices().toIntArray())
	{
	    if (node_heads[v].isEmpty())
	    {
		throw new IllegalStateException();
	    }
	    else if (node_heads[v].size() == 1)
	    {
		node_head[v] = node_heads[v].iterator().next().value;
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

	IntIterator i = vertices.iteratorPrimitive();
	Pair maxPair = pairs[i.next()];

	while (i.hasNext())
	{
	    Pair p = pairs[i.next()];

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

	for (IntCursor c : set)
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
	IntObjectMap<Cluster> head_cluster = new IntObjectOpenHashMap<Cluster>();

	for (IntCursor cv : nodes)
	{
	    int v = cv.value;
	    int h = node_head[v];

	    // if node v belong to a cluster
	    if (h != -1)
	    {
		Cluster c = head_cluster.get(h);

		if (c == null)
		{
		    c = new Cluster();
		    c.add(h);
		    c.setHead(h);
		    head_cluster.put(h, c);
		}

		c.add(v);
	    }
	}

	Clustering clustering = new Clustering();

	for (IntObjectCursor<Cluster> c : head_cluster)
	{
	    clustering.getClusters().add(c.value);
	}

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
	g.toGraphviz(COMMAND.fdp, OUTPUT_FORMAT.png).open();
	
    }
}
