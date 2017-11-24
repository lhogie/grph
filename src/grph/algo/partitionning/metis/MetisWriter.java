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

package grph.algo.partitionning.metis;

import java.io.IOException;
import java.io.PrintStream;

import grph.Grph;
import grph.algo.labelling.Incrementlabelling;
import grph.algo.labelling.Relabelling;
import grph.in_memory.InMemoryGrph;
import grph.io.AbstractGraphTextWriter;
import grph.io.GraphBuildException;
import grph.io.ParseException;

public class MetisWriter extends AbstractGraphTextWriter
{
	@Override
	public void printGraph(Grph g, PrintStream os)
	{
		if ( ! g.isUndirectedSimpleGraph())
			throw new IllegalArgumentException();

		os.print(g.getVertices().size());
		os.print(' ');
		os.print(g.getEdges().size());
		os.print('\n');

		if (g.getVertices().contains(0))
			throw new IllegalArgumentException(
					"graph has vertex 0 which is not supported by metis");

		for (int v = 1; v <= g.getVertices().getGreatest(); ++v)
		{
			if (g.getVertices().contains(v))
			{
				os.print(g.getNeighbours(v).toString_numbers_only());
			}

			os.print('\n');
		}
	}

	public static void main(String[] args)
			throws IOException, ParseException, GraphBuildException
	{
		Grph g = new InMemoryGrph();
		g.grid(3, 3);
		Relabelling rl = new Incrementlabelling(1);
		Grph gg = rl.compute(g);
		String s = new MetisWriter().printGraph(gg);
		System.out.println(s);
		// gg.display();

	}
}
