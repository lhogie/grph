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

package grph.algo.lad;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import grph.Grph;
import grph.util.Matching;
import toools.extern.ExternalProgram;
import toools.extern.Proces;
import toools.io.file.Directory;
import toools.io.file.RegularFile;
import toools.log.Logger;
import toools.log.StdOutLogger;
import toools.text.TextUtilities;

/**
 * LAD is a program (in C) for solving the subgraph isomorphism problem, the
 * goal of which is to decide if there exists a copy of a pattern graph in a
 * target graph. It can be used to solve induced or partial subgraph isomorphism
 * problems. The user may specify additional compatibility relationships among
 * the nodes. LAD is distributed under the CeCILL-B FREE SOFTWARE LICENSE.
 * 
 * @author lhogie
 * 
 */

public abstract class LAD
{
	public enum MODE
	{
		PARTIAL, INDUCED
	};

	private final RegularFile cmd = new RegularFile(Grph.COMPILATION_DIRECTORY,
			getName() + "/main");

	public void ensureCompiled(Logger log) throws IOException
	{
		if ( ! cmd.exists())
		{
			String url = "http://liris.cnrs.fr/csolnon/LAD.tgz";
			ExternalProgram.compileTarball(cmd.getParent().getParent(), url, getName(),
					"LAD/main", log);
		}
	}

	protected abstract String getName();

	public List<Matching> lad(Grph target, Grph pattern, MODE mode,
			boolean findAllSolutions)
	{
		if ( ! target.isUndirectedSimpleGraph() || ! pattern.isUndirectedSimpleGraph())
			throw new IllegalArgumentException();

		try
		{
			ensureCompiled(StdOutLogger.SYNCHRONIZED_INSTANCE);

			RegularFile patternFile = RegularFile.createTempFile(new Directory("/tmp/"),
					"grph-lad-pattern-", "");
			patternFile.setContent(new LADWriter().writeGraph(pattern));
			RegularFile targetFile = RegularFile.createTempFile(new Directory("/tmp/"),
					"grph-lad-target-", "");
			targetFile.setContent(new LADWriter().writeGraph(target));

			String stdout = new String(Proces.exec(cmd.getPath(),
					(findAllSolutions ? "-v" : "-fv") + (mode == MODE.INDUCED ? "i" : ""),
					"-p", patternFile.getPath(), "-t", targetFile.getPath()));
			System.out.println("dfsidsflkshkhj" + stdout);

			List<Matching> matchings = new ArrayList<Matching>();

			for (String l : TextUtilities.splitInLines(stdout))
			{
				if (l.startsWith("Solution "))
				{
					Matching m = new Matching();

					for (String relabel : l.substring(l.indexOf(": ") + 2).split(" "))
					{
						// System.out.println(relabel);
						int pos = relabel.indexOf('=');
						int vertexInPattern = Integer.valueOf(relabel.substring(0, pos));
						int vertexInGraph = Integer.valueOf(relabel.substring(pos + 1));
						m.graph2pattern(vertexInGraph, vertexInPattern);
						m.pattern2graph(vertexInPattern, vertexInGraph);
					}

					matchings.add(m);
				}
			}

			return matchings;
		}
		catch (IOException e)
		{
			throw new IllegalStateException(e);
		}
	}
}
