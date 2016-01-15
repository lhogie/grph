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

package grph.algo;

import grph.Grph;
import grph.in_memory.InMemoryGrph;

import java.io.IOException;
import java.util.List;

import toools.UnitTests;
import toools.extern.ExternalProgram;
import toools.extern.Proces;
import toools.io.JavaResource;
import toools.io.file.RegularFile;
import toools.text.TextUtilities;

import com.carrotsearch.hppc.IntIntMap;
import com.carrotsearch.hppc.IntIntOpenHashMap;
import com.carrotsearch.hppc.cursors.IntCursor;

public class DreadnautAlgorithm
{
	private static RegularFile ccode = new RegularFile(Grph.COMPILATION_DIRECTORY, "nauty24r2/dreadnaut");

	public static void ensureCompiled()
	{
		if (!ccode.exists())
		{
			try
			{
				JavaResource res = new JavaResource(DreadnautAlgorithm.class, "nauty24r2.tar.gz");
				ccode = ExternalProgram.compileTarball(ccode.getParent().getParent(), res, "nauty24r2", "dreadnaut", Grph.logger);
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}

	public static boolean areIsomorphic(Grph g, Grph h)
	{
		return getMatching(g, h) != null;
	}

	public static boolean areIsomorphic(Grph g, Grph h, boolean treatAsDigraphs)
	{
		return getMatching(g, h, treatAsDigraphs) != null;
	}

	public static IntIntMap getMatching(Grph g, Grph h)
	{
		if (g.getNumberOfHyperEdges() > 0 || h.getNumberOfHyperEdges() > 0)
			throw new IllegalArgumentException("does not support hypergraphs");

		if (g.getNumberOfDirectedEdges() > 0 && g.getNumberOfUndirectedEdges() > 0)
			throw new IllegalArgumentException("g has both directed and undirected edges");

		if (h.getNumberOfDirectedEdges() > 0 && h.getNumberOfUndirectedEdges() > 0)
			throw new IllegalArgumentException("h has both directed and undirected edges");

		boolean digraph = g.getNumberOfDirectedEdges() > 0;
		return getMatching(g, h, digraph);
	}

	public static IntIntMap getMatching(Grph g, Grph h, boolean treatAsDigraphs)
	{
		ensureCompiled();

		if (!g.isSimple())
			throw new IllegalArgumentException();

		if (g.isNull() && h.isNull())
		{
			return new IntIntOpenHashMap();
		}
		else if (g.isNull() || h.isNull())
		{
			return null;
		}
		else
		{
			// building the input text
			byte[] inputText = createInputText(g, h, treatAsDigraphs);
			// calling the external program
			String stdout = new String(Proces.exec(ccode.getPath(), inputText));

			// remove text before dreadnaut conclusion
			stdout = stdout.substring(stdout.indexOf("h and h' are "));
			// System.err.println(stdout);

			if (stdout.startsWith("h and h' are identical."))
			{
				List<String> outputLines = TextUtilities.splitInLines(stdout);
				IntIntMap pairs = new IntIntOpenHashMap();

				for (String m : outputLines.get(1).trim().split(" +"))
				{
					String[] vertices = m.split("-");
					int v1 = Integer.valueOf(vertices[0]);
					int v2 = Integer.valueOf(vertices[1]);
					pairs.put(v1, v2);
				}

				return pairs;
			}
			else
			{
				return null;
			}
		}
	}

	private static byte[] createInputText(Grph g, Grph h, boolean treatAsDigraphs)
	{
		StringBuilder b = new StringBuilder();
		b.append("c -a -m\n");
		createInputText(g, b, treatAsDigraphs);
		b.append("x @\n");
		createInputText(h, b, treatAsDigraphs);
		b.append("x\n");
		b.append("##\n");
		b.append("q\n");
		return b.toString().getBytes();
	}

	private static byte[] createInputText(Grph g, StringBuilder b, boolean treatAsDigraphs)
	{
		b.append("n=");
		b.append(g.getVertices().size());
		b.append('\n');

		if (treatAsDigraphs)
		{
			b.append("+digraph\n");
		}

		b.append('g');
		b.append('\n');

		// write degrees
		for (IntCursor c : g.getVertices())
		{
			int v = c.value;

			for (IntCursor ec : g.getOutNeighbors(v))
			{
				b.append(' ');
				b.append(ec.value);
			}

			b.append(';');
			b.append('\n');
		}

		return b.toString().getBytes();
	}


	private static void testDigraph()
	{
		Grph g = new InMemoryGrph();
		g.addDirectedSimpleEdge(0, 1);
		g.addDirectedSimpleEdge(1, 2);

		Grph h = new InMemoryGrph();
		h.addDirectedSimpleEdge(0, 1);
		h.addDirectedSimpleEdge(1, 2);

		UnitTests.ensure(areIsomorphic(g, h, true));

		h.addDirectedSimpleEdge(0, 2);
		UnitTests.ensure(!areIsomorphic(g, h, true));

	}

	private static void testUndigraph()
	{
		Grph g = new InMemoryGrph();
		g.addUndirectedSimpleEdge(0, 1);
		g.addUndirectedSimpleEdge(1, 2);

		Grph h = new InMemoryGrph();
		h.addUndirectedSimpleEdge(0, 1);
		h.addUndirectedSimpleEdge(2, 1);

		UnitTests.ensure(areIsomorphic(g, h, true));

		h.addDirectedSimpleEdge(0, 2);
		UnitTests.ensure(!areIsomorphic(g, h, true));

	}

}
