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

package grph.io;

import java.io.IOException;
import java.io.PrintStream;

import grph.Grph;
import grph.algo.topology.ClassicalGraphs;
import it.unimi.dsi.fastutil.ints.IntSet;
import toools.UnitTests;
import toools.collections.primitive.IntCursor;
import toools.collections.primitive.LucIntSet;

public class GrphTextWriter extends AbstractGraphTextWriter
{
	@Override
	public void printGraph(Grph g, PrintStream os)
	{
		printGraph(g, os, true, true);
	}

	public void printGraph(Grph g, PrintStream os, boolean printEdgesID,
			boolean whitespaces)
	{
		if ( ! g.getClass().getName().equals(Grph.class.getName()))
		{
			os.append("graph class=");
			os.append(g.getClass().getName());
			os.append('\n');
		}

		IntSet isolatedV = g.getIsolatedVertices();

		if ( ! isolatedV.isEmpty())
		{
			os.append("# isolated vertices\n");

			for (IntCursor v : IntCursor.fromFastUtil(isolatedV))
			{
				os.append(v.value + "\n");
			}
		}

		os.append(
				"# edges (e = simple edge, a = simple arc, E = hyperedge, A = directed hyperedge)\n");

		for (IntCursor c : IntCursor.fromFastUtil(g.getEdges()))
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

	public static void main(String[] args)
			throws IOException, ParseException, GraphBuildException
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
		UnitTests.ensureEqual(h, g);
	}
}
