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
import grph.GrphAlgorithm;
import grph.in_memory.InMemoryGrph;
import grph.io.DimacsWriter;

import java.io.IOException;
import java.util.List;

import toools.UnitTests;
import toools.extern.ExternalProgram;
import toools.extern.Proces;
import toools.io.JavaResource;
import toools.io.file.Directory;
import toools.io.file.RegularFile;
import toools.log.Logger;
import toools.log.StdOutLogger;
import toools.set.DefaultIntSet;
import toools.set.IntSet;
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
			IntSet result = new DefaultIntSet();

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
