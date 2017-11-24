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

package grph.algo.structural.cliquer;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;

import grph.Grph;
import grph.GrphAlgorithm;
import grph.algo.topology.ClassicalGraphs;
import grph.io.DimacsWriter;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import it.unimi.dsi.fastutil.ints.IntSets;
import toools.collections.Collections;
import toools.collections.LucIntSets;
import toools.collections.primitive.SelfAdaptiveIntSet;
import toools.extern.ExternalProgram;
import toools.extern.Proces;
import toools.io.JavaResource;
import toools.io.file.Directory;
import toools.io.file.RegularFile;
import toools.text.TextUtilities;

public class Cliquer extends GrphAlgorithm<Collection<IntSet>>
{
	public static final Directory d = new Directory(Grph.COMPILATION_DIRECTORY,
			"cliquer-1.21");
	public static final RegularFile cmd = new RegularFile(d, "cl");

	public static void ensureCompiled()
	{
		if ( ! cmd.exists())
		{
			JavaResource res = JavaResource.getAttachedResource(Cliquer.class,
					"cliquer-1.21.tar.gz");

			try
			{
				ExternalProgram.compileTarball(Grph.COMPILATION_DIRECTORY, res,
						"cliquer-1.21", "cl", Grph.logger);
			}
			catch (IOException e)
			{
				throw new IllegalStateException();
			}
		}
	}

	public static Collection<IntSet> compute(Grph g, int min, int max, boolean all)
	{
		if (min <= 0)
			throw new IllegalArgumentException("min must be >0");

		Collection<IntSet> r = new HashSet<IntSet>();

		if (g.getNumberOfVertices() == 0)
		{
			// there will be no clique
		}
		else if (g.getNumberOfVertices() == 1)
		{
			r.add(IntSets.singleton(g.getVertices().getGreatest()));
		}
		else
		{
			ensureCompiled();
			RegularFile inputFile = RegularFile.createTempFile();

			try
			{
				inputFile.setContent(new DimacsWriter().writeGraph(g));
				byte[] out = Proces.exec(cmd.getPath(), all ? "--all" : "--single",
						"--min", "" + min, "--max", "" + max, inputFile.getPath());

				String s = new String(out);

				for (String l : TextUtilities
						.splitInLines(s.replaceAll("size=[0-9]+, weight=[0-9]+:", "")))
				{
					// remove -1 to every vertex found in order to use Grph 0-N
					// ID space
					IntSet ss = new SelfAdaptiveIntSet();

					for (int v : LucIntSets.from(IntOpenHashSet.class, l))
					{
						ss.add(v - 1);
					}

					r.add(ss);
				}
			}
			catch (IOException e)
			{
				throw new IllegalStateException();
			}
			finally
			{
				inputFile.delete();
			}
		}

		return r;
	}

	public static void main(String[] args)
	{
		Grph g = ClassicalGraphs.PetersenGraph();
		g.addUndirectedSimpleEdge(2, 5);
		g.display();
		Collection<IntSet> cliques = compute(g, 1, 5, true);
		System.out.println(cliques);
	}

	@Override
	public Collection<IntSet> compute(Grph g)
	{
		return compute(g, 1, Integer.MAX_VALUE, true);
	}
}
