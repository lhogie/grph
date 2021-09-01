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
import it.unimi.dsi.fastutil.ints.IntSet;
import j4u.CommandLine;
import toools.collections.primitive.IntCursor;
import toools.io.file.RegularFile;
import toools.thread.Threads;

public class demo_lifegame extends AbstractGrphScript
{

	public demo_lifegame(RegularFile launcher)
	{
		super(launcher);
		addOption("--edge", "-e", "[0-9]+", "10", "Set the edge size for the grid");
		addOption("--seed", "-s", "[0-9]+", String.valueOf(System.currentTimeMillis()),
				"Set the seed for the PRNG");
		addOption("--delay", "-d", "[0-9]+", "10",
				"Set the number of millisecond between two iterations");
	}

	@Override
	public int runScript(CommandLine cmdLine) throws Throwable
	{
		Random r = new Random(Long.valueOf(getOptionValue(cmdLine, "--seed")));
		Grph g = createGraph(Integer.valueOf(getOptionValue(cmdLine, "--edge")), r);
		g.display();

		long delay = Long.valueOf(getOptionValue(cmdLine, "--delay"));

		for (;;)
		{
			int v = g.getVertices().pickRandomElement(r);
			int numberOfLivingNeighbours = computeNumberOfLivingNeighbors(
					g.getNeighbours(v), g);

			if (g.getVertexColorProperty().getValue(v) == 0) // if the cell is
			// dead
			{
				if (numberOfLivingNeighbours == 3)
				{
					g.getVertexColorProperty().setValue(v, 1); // make it birth
				}
			}
			else
			// the cell is alive
			{
				if (numberOfLivingNeighbours != 3 && numberOfLivingNeighbours != 2)
				{
					g.getVertexColorProperty().setValue(v, 0); // kill it
				}
			}

			Threads.sleepMs(delay);
		}
	}

	private Grph createGraph(int edge, Random r)
	{
		Grph g = new grph.in_memory.InMemoryGrph();
		g.addNVertices(edge * edge);
		g.grid(edge, edge, false, true, true);

		for (IntCursor v : IntCursor.fromFastUtil(g.getVertices()))
		{
			g.getVertexColorProperty().setValue(v.value, (r.nextBoolean() ? 4 : 5));
		}

		return g;
	}

	private int computeNumberOfLivingNeighbors(IntSet vertices, Grph g)
	{
		int n = 0;

		for (IntCursor v : IntCursor.fromFastUtil(vertices))
		{
			if (g.getVertexColorProperty().getValue(v.value) == 1)
			{
				++n;
			}
		}

		return n;
	}

	@Override
	public String getShortDescription()
	{
		return "Lifegame";
	}

	public static void main(String[] args) throws Throwable
	{
		new demo_lifegame(null).run("/Users/lhogie/tmp/test.dc");
	}
}
