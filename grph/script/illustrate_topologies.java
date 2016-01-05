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
 
 package grph.script;


import grph.Grph;
import grph.algo.topology.TopologyGenerator;
import grph.io.GraphvizImageWriter;

import java.util.Collection;
import java4unix.CommandLine;
import java4unix.OptionSpecification;

import toools.Clazz;
import toools.io.file.RegularFile;



public class illustrate_topologies extends AbstractGrphScript
{

	@Override
	protected void declareOptions(Collection<OptionSpecification> optionSpecifications)
	{
		optionSpecifications.add(new OptionSpecification("--vertices", "-v", "[0-9]+", "10", "The number of vertices to be instantiated."));
	}

	@Override
	public int runScript(CommandLine cmdLine) throws Throwable
	{
		int nv = Integer.valueOf(getOptionValue(cmdLine, "--vertices"));
		
		for (Class<TopologyGenerator> clazz : Clazz.findImplementations(TopologyGenerator.class))
		{
			try
			{
				printMessage("Illustrating " + clazz.getName());
				TopologyGenerator tg = Clazz.makeInstance(clazz);
				Grph graph = new grph.in_memory.InMemoryGrph();
				graph.addNVertices(nv);
				tg.compute(graph);
				RegularFile outputFile = new RegularFile(tg.getClass().getName() + "-" + nv + "-example.pdf");
				printMessage("writing " + outputFile.getPath());
				new GraphvizImageWriter().writeGraph(graph, outputFile);
			}
			catch (Throwable t)
			{
				printNonFatalError("Generator " + clazz.getName() + " failed");
				t.printStackTrace();
			}
		}

		return 0;
	}

	@Override
	public String getShortDescription()
	{
		return "Generate a PDF file for all topologies available";
	}

}
