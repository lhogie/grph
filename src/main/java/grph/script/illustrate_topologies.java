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

import grph.Grph;
import grph.algo.topology.TopologyGenerator;
import grph.io.GraphvizImageWriter;
import j4u.CommandLine;
import toools.io.file.RegularFile;
import toools.reflect.Clazz;

public class illustrate_topologies extends AbstractGrphScript
{

	public illustrate_topologies(RegularFile launcher)
	{
		super(launcher);
		addOption("--vertices", "-v", "[0-9]+", "10",
				"The number of vertices to be instantiated.");
	}

	@Override
	public int runScript(CommandLine cmdLine) throws Throwable
	{
		int nv = Integer.valueOf(getOptionValue(cmdLine, "--vertices"));

		for (Class<TopologyGenerator> clazz : Clazz
				.findImplementations(TopologyGenerator.class))
		{
			try
			{
				printMessage("Illustrating " + clazz.getName());
				TopologyGenerator tg = Clazz.makeInstance(clazz);
				Grph graph = new grph.in_memory.InMemoryGrph();
				graph.addNVertices(nv);
				tg.compute(graph);
				RegularFile outputFile = new RegularFile(
						tg.getClass().getName() + "-" + nv + "-example.pdf");
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
