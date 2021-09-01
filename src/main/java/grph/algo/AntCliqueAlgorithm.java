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
 
 

package grph.algo;

import java.io.IOException;
import java.util.List;

import grph.Grph;
import grph.GrphAlgorithm;
import grph.in_memory.InMemoryGrph;
import grph.io.DimacsWriter;
import it.unimi.dsi.fastutil.ints.IntSet;
import toools.UnitTests;
import toools.collections.primitive.SelfAdaptiveIntSet;
import toools.extern.ExternalProgram;
import toools.extern.Proces;
import toools.io.JavaResource;
import toools.io.file.Directory;
import toools.io.file.RegularFile;
import toools.log.Logger;
import toools.log.StdOutLogger;
import toools.text.TextUtilities;

/**
 * Compute the maximum clique in the graph, using a ant-based heuristic. Call
 * the AntClique program by Christine Solnon.
 * 
 * @author lhogie
 * 
 */
public class AntCliqueAlgorithm extends GrphAlgorithm<IntSet>
{
	private final static RegularFile cmd = new RegularFile(Grph.COMPILATION_DIRECTORY, "AntClique/main");

	public void ensureCompiled(Logger log) throws IOException
	{
		if (!cmd.exists())
		{
			JavaResource res = new JavaResource(getClass(), "AntClique.tgz");
			ExternalProgram.compileTarball(cmd.getParent().getParent(), res, "AntClique", "main", log);
		}
	}

	public static void main(String[] args)
	{
		test();
	}

	private static void test()
	{
		Grph g = new InMemoryGrph();
		g.grid(5, 5);
		g.addUndirectedSimpleEdge(13, 7);
		g.addUndirectedSimpleEdge(8, 12);
		UnitTests.ensure(g.getMaximumClique().size() == 4);
	}

	@Override
	public IntSet compute(Grph g)
	{
		if (g.hasMultipleEdges())
			throw new IllegalArgumentException("AntClique does not support multigraphs");

		try
		{
			ensureCompiled(StdOutLogger.SYNCHRONIZED_INSTANCE);

			RegularFile f = RegularFile.createTempFile(new Directory("/tmp/"), "grph-AntClique-", "");
			f.setContent(new DimacsWriter().writeGraph(g));

			String stdout = new String(Proces.exec(cmd.getPath(), "-i", f.getPath()));

			List<String> outputLines = TextUtilities.splitInLines(stdout);
			IntSet result = new SelfAdaptiveIntSet(0);

			for (String v : outputLines.get(outputLines.size() - 2).substring("	- clique list: ".length()).split(" "))
			{
				result.add(Integer.valueOf(v) - 1);
			}

			return result;

		}
		catch (IOException e)
		{
			throw new IllegalStateException(e);
		}
	}

}
