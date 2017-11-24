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
 
 package grph.script;

import java.util.Collection;

import grph.Grph;
import grph.io.AbstractGraphReader;
import java4unix.CommandLine;
import java4unix.OptionSpecification;
import toools.StopWatch;
import toools.io.file.RegularFile;


public abstract class AbstractGraphOperation extends AbstractGrphScript
{

    public AbstractGraphOperation(RegularFile launcher)
	{
		super(launcher);
	}

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

	RegularFile inputFile = new RegularFile(cmdLine.findParameters().get(0));
	StopWatch stopWatch = new StopWatch();
	Grph graph = AbstractGraphReader.findReader(inputFile.getExtension()).readGraph(inputFile);
	printMessage("Graph loaded in " + stopWatch.getElapsedTime() + "ms (" + graph.getVertices().size()
		+ " vertices and " + graph.getEdges().size() + " edges)");
	printMessage("");
	return runScript(cmdLine, graph);
    }

    public abstract int runScript(CommandLine cmdLine, Grph graph) throws Throwable;
}
