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
 
 package grph.io;

import grph.Grph;
import grph.TopologyListener;

import java.io.IOException;
import java.io.PrintStream;

import toools.set.IntSet;

public class DGSWriter extends AbstractGraphTextWriter
{
    private final TopologyListener listener = new MG();
    private PrintStream printStream;
    private Grph g;

    @Override
    public void printGraph(Grph graph, PrintStream printStream) throws IOException
    {
	if (this.g != null)
	    throw new IllegalStateException("busy");

	this.g = graph;
	graph.getTopologyListeners().add(listener);
	this.printStream = printStream;
	printStream.append("DGS002\n");
	printStream.append("graph 0 0\n");
    }

    private class MG implements TopologyListener
    {
	private int step = 0;

	@Override
	public void vertexAdded(Grph graph, int vertex)
	{
	    step();
	    printStream.append("an " + vertex + "\n");
	}

	private void step()
	{
	    printStream.append("st " + this.step++ + "\n");	    
	}

	@Override
	public void vertexRemoved(Grph graph, int vertex)
	{
	    step();
	    printStream.append("dn " + vertex + "\n");
	}

	@Override
	public void directedSimpleEdgeAdded(Grph graph, int edge, int src, int dest)
	{
	    step();
	    int a = graph.getOneVertex(edge);
	    int b = graph.getTheOtherVertex(edge, a);
	    printStream.append("ae " + edge + " " + a + " " + b + " 1\n");
	}

	@Override
	public void undirectedSimpleEdgeAdded(Grph graph, int edge, int a, int b)
	{
	    step();
	    printStream.append("ae " + edge + " " + a + " " + b + " 1\n");
	}

	@Override
	public void undirectedHyperEdgeAdded(Grph g, int edge)
	{
	    step();
	    printStream.append("ae " + edge + "\n");
	}

	@Override
	public void directedHyperEdgeAdded(Grph g, int edge)
	{
	    step();
	    printStream.append("ae " + edge + "\n");
	}

	@Override
	public void directedSimpleEdgeRemoved(Grph g, int edge, int a, int b)
	{
	    step();
	    printStream.append("de " + edge + "\n");
	}

	@Override
	public void undirectedSimpleEdgeRemoved(Grph g, int edge, int a, int b)
	{
	    step();
	    printStream.append("de " + edge + "\n");
	}

	@Override
	public void undirectedHyperEdgeRemoved(Grph g, int edge, IntSet incidentVertices)
	{
	    step();
	    printStream.append("de " + edge + "\n");
	}

	@Override
	public void directedHyperEdgeRemoved(Grph g, int edge, IntSet src, IntSet dest)
	{
	    step();
	    printStream.append("de " + edge + "\n");
	}

	@Override
	public void vertexAddedToDirectedHyperEdgeTail(Grph g, int e, int v)
	{
	    step();
	    System.err.println("unsupported");
	}

	@Override
	public void vertexAddedToDirectedHyperEdgeHead(Grph g, int e, int v)
	{
	    step();
	    System.err.println("unsupported");
	}

	@Override
	public void vertexAddedToUndirectedSimpleEdge(Grph g, int edge, int vertex)
	{
	    step();
	    System.err.println("unsupported");
	}

	@Override
	public void vertexRemovedFromUndirectedHyperEdge(Grph g, int edge, int vertex)
	{
	    step();
	    System.err.println("unsupported");
	}

	@Override
	public void vertexRemovedFromDirectedHyperEdgeTail(Grph g, int e, int v)
	{
	    step();
	    System.err.println("unsupported");
	}

	@Override
	public void vertexRemovedFromDirectedHyperEdgeHead(Grph g, int e, int v)
	{
	    step();
	    System.err.println("unsupported");
	}
    }
}
