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
 
 package grph.algo.structural.cliquer;

import grph.Grph;
import grph.GrphAlgorithm;
import grph.algo.topology.ClassicalGraphs;
import grph.io.DimacsWriter;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;

import toools.extern.ExternalProgram;
import toools.extern.Proces;
import toools.io.JavaResource;
import toools.io.file.Directory;
import toools.io.file.RegularFile;
import toools.set.DefaultIntSet;
import toools.set.IntSet;
import toools.set.IntSets;
import toools.set.IntSingletonSet;
import toools.text.TextUtilities;

public class Cliquer extends GrphAlgorithm<Collection<IntSet>>
{
    public static final Directory d = new Directory(Grph.COMPILATION_DIRECTORY, "cliquer-1.21");
    public static final RegularFile cmd = new RegularFile(d, "cl");

    public static void ensureCompiled()
    {
	if (!cmd.exists())
	{
	    JavaResource res = JavaResource.getAttachedResource(Cliquer.class, "cliquer-1.21.tar.gz");

	    try
	    {
		ExternalProgram.compileTarball(Grph.COMPILATION_DIRECTORY, res, "cliquer-1.21", "cl", Grph.logger);
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
	    r.add(new IntSingletonSet(g.getVertices().getGreatest()));
	}
	else
	{
	    ensureCompiled();
	    RegularFile inputFile = RegularFile.createTempFile();

	    try
	    {
		inputFile.setContent(new DimacsWriter().writeGraph(g));
		byte[] out = Proces.exec(cmd.getPath(), all ? "--all" : "--single", "--min", "" + min, "--max", ""
			+ max, inputFile.getPath());

		String s = new String(out);

		for (String l : TextUtilities.splitInLines(s.replaceAll("size=[0-9]+, weight=[0-9]+:", "")))
		{
		    // remove -1 to every vertex found in order to use Grph 0-N ID space
		    IntSet ss = new DefaultIntSet();

		    for (int v : IntSets.from(l).toIntArray())
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
