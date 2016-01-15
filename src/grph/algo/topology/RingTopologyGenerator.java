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
 
 package grph.algo.topology;

import grph.Grph;
import grph.in_memory.InMemoryGrph;

import java.util.Iterator;

import toools.set.DefaultIntSet;
import toools.set.IntSet;

import com.carrotsearch.hppc.cursors.IntCursor;

/**
 * Link the given nodes as a chain.
 * 
 * @author lhogie
 * 
 */
public class RingTopologyGenerator extends RandomizedTopologyTransform
{
    public boolean isDirected()
    {
	return directed;
    }

    public void setDirected(boolean directed)
    {
	this.directed = directed;
    }

    private boolean directed;

    @Override
    public void compute(Grph graph)
    {
	ring(graph, graph.getVertices(), isDirected());
    }

    public static IntSet ring(Grph graph, IntSet vertices, boolean directed)
    {
	IntSet edges = new DefaultIntSet();
	
	if (vertices.size() > 1)
	{
	    Iterator<IntCursor> i = vertices.iterator();
	    int predecessor = i.next().value;
	    int first = predecessor;

	    while (i.hasNext())
	    {
		int a = i.next().value;
		edges.add(graph.addSimpleEdge(predecessor, a, directed));
		predecessor = a;
	    }

	    edges.add(graph.addSimpleEdge(predecessor, first, directed));
	}

	return edges;
    }

    public static void main(String[] args)
    {
	Grph g = new InMemoryGrph();
	g.addNVertices(10);
	System.out.println("*** " + g.getVertices().size());
	g.ring();
	g.display();
    }

    public static IntSet ring(Grph g, boolean directed)
    {
	return ring(g, g.getVertices(), directed);
    }

}
