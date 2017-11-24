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

package grph.algo;

import java.io.IOException;
import java.util.List;

import grph.Grph;
import grph.in_memory.InMemoryGrph;
import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import toools.UnitTests;
import toools.collections.primitive.IntCursor;
import toools.extern.ExternalProgram;
import toools.extern.Proces;
import toools.io.JavaResource;
import toools.io.file.RegularFile;
import toools.text.TextUtilities;

public class DreadnautAlgorithm
{
	private static RegularFile ccode = new RegularFile(Grph.COMPILATION_DIRECTORY,
			"nauty24r2/dreadnaut");

	public static void ensureCompiled()
	{
		if ( ! ccode.exists())
		{
			try
			{
				JavaResource res = new JavaResource(DreadnautAlgorithm.class,
						"nauty24r2.tar.gz");
				ccode = ExternalProgram.compileTarball(ccode.getParent().getParent(), res,
						"nauty24r2", "dreadnaut", Grph.logger);
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

	public static Int2IntMap getMatching(Grph g, Grph h)
	{
		if (g.getNumberOfHyperEdges() > 0 || h.getNumberOfHyperEdges() > 0)
			throw new IllegalArgumentException("does not support hypergraphs");

		if (g.getNumberOfDirectedEdges() > 0 && g.getNumberOfUndirectedEdges() > 0)
			throw new IllegalArgumentException(
					"g has both directed and undirected edges");

		if (h.getNumberOfDirectedEdges() > 0 && h.getNumberOfUndirectedEdges() > 0)
			throw new IllegalArgumentException(
					"h has both directed and undirected edges");

		boolean digraph = g.getNumberOfDirectedEdges() > 0;
		return getMatching(g, h, digraph);
	}

	public static Int2IntMap getMatching(Grph g, Grph h, boolean treatAsDigraphs)
	{
		ensureCompiled();

		if ( ! g.isSimple())
			throw new IllegalArgumentException();

		if (g.isNull() && h.isNull())
		{
			return new Int2IntOpenHashMap();
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
				Int2IntMap pairs = new Int2IntOpenHashMap();

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

	private static byte[] createInputText(Grph g, StringBuilder b,
			boolean treatAsDigraphs)
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
		for (IntCursor c : IntCursor.fromFastUtil(g.getVertices()))
		{
			int v = c.value;

			for (IntCursor ec : IntCursor.fromFastUtil(g.getOutNeighbors(v)))
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
		UnitTests.ensure( ! areIsomorphic(g, h, true));

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
		UnitTests.ensure( ! areIsomorphic(g, h, true));

	}

}
