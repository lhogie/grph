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
import grph.algo.topology.ClassicalGraphs;

import java.io.IOException;
import java.io.PrintStream;

import toools.UnitTests;
import toools.set.IntSet;

import com.carrotsearch.hppc.cursors.IntCursor;

public class GrphTextWriter extends AbstractGraphTextWriter
{
    @Override
    public void printGraph(Grph g, PrintStream os)
    {
	printGraph(g, os, true, true);
    }

    public void printGraph(Grph g, PrintStream os, boolean printEdgesID, boolean whitespaces)
    {
	if (!g.getClass().getName().equals(Grph.class.getName()))
	{
	    os.append("graph class=");
	    os.append(g.getClass().getName());
	    os.append('\n');
	}

	IntSet isolatedV = g.getIsolatedVertices();

	if (!isolatedV.isEmpty())
	{
	    os.append("# isolated vertices\n");

	    for (IntCursor v : isolatedV)
	    {
		os.append(v.value + "\n");
	    }
	}

	os.append("# edges (e = simple edge, a = simple arc, E = hyperedge, A = directed hyperedge)\n");

	for (IntCursor c : g.getEdges())
	{
	    int e = c.value;

	    if (g.isUndirectedSimpleEdge(e)) // 1 2
	    {
		os.append("e" + e + " ");
		g.getVerticesIncidentToEdge(e).writeTo(os);
	    }
	    else if (g.isDirectedSimpleEdge(e)) // 1 > 2
	    {
		os.append("a" + e + " ");
		os.append(String.valueOf(g.getDirectedSimpleEdgeTail(e)));
		os.append(" > ");
		os.append(String.valueOf(g.getDirectedSimpleEdgeHead(e)));
	    }
	    else if (g.isUndirectedHyperEdge(e)) // 1 2 3 4
	    {
		os.append("E" + e + " ");
		g.getUndirectedHyperEdgeVertices(e).writeTo(os);
	    }
	    else
	    {
		os.append("A" + e + " ");
		g.getDirectedHyperEdgeTail(e).writeTo(os);
		os.append(" > ");
		g.getDirectedHyperEdgeHead(e).writeTo(os);
	    }

	    os.append('\n');
	}
    }

    public static void main(String[] args) throws IOException, ParseException, GraphBuildException
    {
	Grph g = new grph.in_memory.InMemoryGrph();
	g.addUndirectedSimpleEdge(1, 2);
	g.addDirectedSimpleEdge(1, 2);
	g.addUndirectedHyperEdge(9);
	g.addToUndirectedHyperEdge(9, 1);
	g.addToUndirectedHyperEdge(9, 4);
	g.addDirectedHyperEdge(34);
	g.addToDirectedHyperEdgeTail(34, 4);
	g.addToDirectedHyperEdgeTail(34, 6);
	g.addToDirectedHyperEdgeTail(34, 8);
	g.addToDirectedHyperEdgeHead(34, 1);
	g.addToDirectedHyperEdgeHead(34, 8);
	String s = new GrphTextWriter().printGraph(g);
	System.out.println(s);
	
	Grph h = new GrphTextReader().readGraph(s);
	
	System.out.println(new GrphTextWriter().printGraph(h));
    }

    private static void testTextEncoding() throws ParseException, GraphBuildException
    {
	Grph g = ClassicalGraphs.completeBipartiteGraph(10, 10);
	String s = g.toGrphText();
	// System.out.println(s);
	Grph h = Grph.fromGrphText(s);
	UnitTests.ensureEquals(h, g);
    }
}
