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
 
 package grph.algo.ogdf;

import grph.Grph;
import grph.in_memory.InMemoryGrph;
import grph.io.AbstractGraphTextWriter;

import java.io.IOException;
import java.io.PrintStream;

public class OGDFWriter extends AbstractGraphTextWriter
{

    @Override
    public void printGraph(Grph g, PrintStream printStream) throws IOException
    {
	printStream.println(g.getVertices().size());
	
	for (int v : g.getVertices().toIntArray())
	{
	    printStream.println(v);
	}

	printStream.println(g.getEdges().size());
	
	for (int e : g.getEdges().toIntArray())
	{
	    int v1 = g.getOneVertex(e);
	    printStream.println(v1);
	    int v2 = g.getTheOtherVertex(e, v1);
	    printStream.println(v2);
	}
    }
    
    public static void main(String[] args) throws IOException
    {
	Grph g = new InMemoryGrph();
	g.grid(3, 3);
	new OGDFWriter().printGraph(g, System.out);
    }
}
