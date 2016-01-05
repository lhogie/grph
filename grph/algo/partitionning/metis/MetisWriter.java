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
 
 package grph.algo.partitionning.metis;

import grph.Grph;
import grph.algo.labelling.Incrementlabelling;
import grph.algo.labelling.Relabelling;
import grph.in_memory.InMemoryGrph;
import grph.io.AbstractGraphTextWriter;
import grph.io.GraphBuildException;
import grph.io.ParseException;

import java.io.IOException;
import java.io.PrintStream;

public class MetisWriter extends AbstractGraphTextWriter
{
    @Override
    public void printGraph(Grph g, PrintStream os)
    {
	if (!g.isUndirectedSimpleGraph())
	    throw new IllegalArgumentException();

	os.print(g.getVertices().size());
	os.print(' ');
	os.print(g.getEdges().size());
	os.print('\n');

	if (g.getVertices().contains(0))
	    throw new IllegalArgumentException("graph has vertex 0 which is not supported by metis");

	for (int v = 1; v <= g.getVertices().getGreatest(); ++v)
	{
	    if (g.getVertices().contains(v))
	    {
		os.print(g.getNeighbours(v).toString_numbers_only());
	    }

	    os.print('\n');
	}
    }

    public static void main(String[] args) throws IOException, ParseException, GraphBuildException
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
