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

import java.util.Random;

import grph.Grph;
import grph.Grph.DIRECTION;
import j4u.CommandLine;
import toools.StopWatch;
import toools.collections.LucIntSets;
import toools.io.file.RegularFile;

public class biggest_graph_possible extends AbstractGrphScript
{

	public biggest_graph_possible(RegularFile launcher)
	{
		super(launcher);
		addOption("--degree", "-d", "[0-9]+", "3", "Set degree of the desired graph");
		addOption("--seed", "-s", "[0-9]+", String.valueOf(System.currentTimeMillis()),
				"Set the seed for the PRNG");
		addOption("--step", null, "[0-9]+", "10000",
				"Set the number of new vertices for which a message will be given to the user");
		addOption("--verticesOnly", "-V", null, null, "do not store edges");
	}

	@Override
	public int runScript(CommandLine cmdLine) throws Throwable
	{
		Random r = new Random(Long.valueOf(getOptionValue(cmdLine, "--seed")));
		int degree = Integer.valueOf(getOptionValue(cmdLine, "-d"));
		boolean verticesOnly = isOptionSpecified(cmdLine, "--verticesOnly");
		int step = Integer.valueOf(getOptionValue(cmdLine, "--step"));
		printMessage("Generating graph of degree " + degree);

		Grph g = verticesOnly
				? new grph.in_memory.InMemoryGrph("", false, DIRECTION.in_out)
				: new grph.in_memory.InMemoryGrph();

		StopWatch sw = new StopWatch();

		try
		{
			while (true)
			{
				int v = g.addVertex();

				for (int i = 0; i < degree; ++i)
				{
					int w = LucIntSets.pickRandomInt(g.getVertices(), r);
					g.addUndirectedSimpleEdge(v, w);
				}

				if (g.getNumberOfVertices() % step == 0)
				{
					printMessage(g + " (+" + sw.getElapsedTime() + "ms)");
				}
			}
		}
		catch (OutOfMemoryError e)
		{
			printMessage("Completed. Could generate " + g);
		}

		return 0;
	}

	@Override
	public String getShortDescription()
	{
		return "Creates the biggest graph possible";
	}

	public static void main(String[] args) throws Throwable
	{
		new biggest_graph_possible(null).run("--degree=3");
		// new biggest_graph_possible().run("--degree=3", "--verticesOnly");
	}

}
