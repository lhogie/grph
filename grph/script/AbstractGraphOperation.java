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
import grph.io.AbstractGraphReader;

import java.util.Collection;
import java4unix.CommandLine;
import java4unix.OptionSpecification;

import toools.StopWatch;
import toools.io.file.RegularFile;


public abstract class AbstractGraphOperation extends AbstractGrphScript
{

    @Override
    protected void declareOptions(Collection<OptionSpecification> optionSpecifications)
    {
	optionSpecifications.add(new OptionSpecification("--disable-parallelism", null, null, null,
		"Activates the use of multi-threading/automatic load-balancing on cores"));

	optionSpecifications.add(new OptionSpecification("--disable-caching", null, null, null,
		"Activates the use of caching"));
    }

    @Override
    public final int runScript(CommandLine cmdLine) throws Throwable
    {
	Grph.useCache = !isOptionSpecified(cmdLine, "--disable-caching");

	printMessage("Caching: " + (Grph.useCache ? "yes" : "no"));
	printMessage("");

	RegularFile inputFile = new RegularFile(cmdLine.findArguments().get(0));
	StopWatch stopWatch = new StopWatch();
	Grph graph = AbstractGraphReader.findReader(inputFile.getExtension()).readGraph(inputFile);
	printMessage("Graph loaded in " + stopWatch.getElapsedTime() + "ms (" + graph.getVertices().size()
		+ " vertices and " + graph.getEdges().size() + " edges)");
	printMessage("");
	return runScript(cmdLine, graph);
    }

    public abstract int runScript(CommandLine cmdLine, Grph graph) throws Throwable;
}
