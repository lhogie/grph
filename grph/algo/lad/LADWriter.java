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
 
 package grph.algo.lad;

import grph.Grph;
import grph.in_memory.InMemoryGrph;
import grph.io.AbstractGraphTextWriter;
import grph.io.GraphBuildException;
import grph.io.ParseException;

import java.io.IOException;
import java.io.PrintStream;

import com.carrotsearch.hppc.cursors.IntCursor;

public class LADWriter extends AbstractGraphTextWriter
{
    @Override
    public void printGraph(Grph g, PrintStream os)
    {
	if (!g.isUndirectedSimpleGraph())
	    throw new IllegalArgumentException();
	
	os.println(g.getVertices().size());

	for (int v : g.getVertices().toIntArray())
	{
	    os.print(g.getVertexDegree(v, Grph.TYPE.edge, Grph.DIRECTION.in_out));

	    for (IntCursor nc : g.getNeighbours(v))
	    {
		os.print(' ');
		os.print(nc.value);
	    }

	    os.print('\n');
	}
    }

    public static void main(String[] args) throws IOException, ParseException, GraphBuildException
    {
	Grph g = new InMemoryGrph();
	g.grid(3, 3);
	String s = new LADWriter().printGraph(g);
	System.out.println(s);
    }
}
