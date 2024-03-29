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
 
 

package grph.script;

import java.util.Collection;
import java.util.Random;

import grph.Grph;
import j4u.CommandLine;
import j4u.OptionSpecification;
import toools.StopWatch;
import toools.io.file.RegularFile;
import toools.math.MathsUtilities;

public class heavy_grph_process extends AbstractGrphScript
{

	public heavy_grph_process(RegularFile launcher)
	{
		super(launcher);
		// TODO Auto-generated constructor stub
	}

	@Override
	public int runScript(CommandLine cmdLine) throws Throwable
	{
//		ClassLoader.getSystemClassLoader().setDefaultAssertionStatus(true);
		Random r = new Random();
		boolean storeEdges = true;

		Grph g = new grph.in_memory.InMemoryGrph();
		int n = 100000;
		int degree = 100;
		printMessage("Generating graph of degree " + degree);

		StopWatch sw = new StopWatch();

		for (int v = 0; v < n; ++v)
		{
			g.addVertex(v);
		}

		for (int v = 0; v < n; ++v)
		{
			for (int i = 0; i < degree; ++i)
			{
				int w = MathsUtilities.pickRandomBetween(0, n, r);
				g.addUndirectedSimpleEdge(v, w);
			}
		}

		System.out.println(g + " created in " + sw.getElapsedTime() + "ms");

		for (int i = 0;; ++i)
		{
			g.performRandomTopologicalChange(r);

			if (i % 100 == 0)
				System.out.println("100 changes in " + sw.getElapsedTime() + "ms");
		}
	}

	@Override
	public String getShortDescription()
	{
		return "Runs a heavy Grph process, suited to profiling";
	}

	public static void main(String[] args) throws Throwable
	{
		new heavy_grph_process(null).run();
	}

	
}
