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
 
 package grph.algo.lad;

import grph.Grph;
import grph.in_memory.InMemoryGrph;
import grph.util.Matching;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import toools.UnitTests;
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

public class DirectedLAD
{
    private final static RegularFile cmd = new RegularFile(Grph.COMPILATION_DIRECTORY, "directedLAD/main");

    public static void ensureCompiled(Logger log) throws IOException
    {
	if (!cmd.exists())
	{
	    String url = "http://liris.cnrs.fr/csolnon/directedLAD.tgz";
	    ExternalProgram.compileTarball(cmd.getParent().getParent(), url, "directedLAD", "LAD/main", log);
	}
    }

    public enum MODE
    {
	PARTIAL, INDUCED
    };

    public static Collection<Matching> lad(Grph target, Grph pattern, MODE mode,
	    boolean findAllSolutions)
    {
	try
	{
	    ensureCompiled(StdOutLogger.SYNCHRONIZED_INSTANCE);

	    RegularFile patternFile = RegularFile.createTempFile(new Directory("/tmp/"), "grph-lad-pattern-", "");
	    patternFile.setContent(new LADWriter().writeGraph(pattern));
	    RegularFile targetFile = RegularFile.createTempFile(new Directory("/tmp/"), "grph-lad-target-", "");
	    targetFile.setContent(new LADWriter().writeGraph(target));

	    String stdout = new String(Proces.exec(cmd.getPath(), (findAllSolutions ? "-v" : "-fv")
		    + (mode == MODE.INDUCED ? "i" : ""), "-p", patternFile.getPath(), "-t", targetFile.getPath()));

	    Collection<Matching> matchings = new ArrayList<Matching>();

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

    public static void main(String[] args)
    {
	Grph target = new InMemoryGrph();
	target.grid(5, 5);

	Grph pattern = new InMemoryGrph();
	pattern.addNVertices(16);
	pattern.chain(false);
	Collection<Matching> r = DirectedLAD.lad(target, pattern, MODE.INDUCED, true);
	
	for (Matching m : r)
	{
	    System.out.println(m.pattern2graph());
	}

	target.highlightVertices(r.iterator().next().graph2pattern().keys().toIntArray());
	target.display();
    }

    private static void tst() throws IOException
    {
	Grph target = new InMemoryGrph();
	target.grid(3, 3);

	Grph pattern = new InMemoryGrph();
	pattern.grid(2, 2);

	Collection<Matching> s = target.getInducedSubgraphIsomorphism(pattern, false);
	UnitTests.ensure(s.size() == 4);
    }
}
