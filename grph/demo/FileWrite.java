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
 
 package grph.demo;

import grph.Grph;
import grph.io.GraphBuildException;
import grph.io.GrphBinaryReader;
import grph.io.GrphBinaryWriter;
import grph.io.ParseException;

import java.io.IOException;

import toools.io.file.RegularFile;


public class FileWrite
{
    public static void main(String[] args)
    {
	System.out.println(new Object().toString());

	ClassLoader.getSystemClassLoader().setDefaultAssertionStatus(true);
	// the file the graph will be written to and read from
	RegularFile f = new RegularFile("example.dhdf");

	{
	    // creates an 10x10 grid
	    System.out.println("Creating graph");
	    Grph g = new grph.in_memory.InMemoryGrph();
	    g.addNVertices(40);
	    g.ring();
	    System.out.println(g);

	    try
	    {
		// serialize it to the file
		System.out.println("Writing graph to file");
		new GrphBinaryWriter().writeGraph(g, f);
	    }
	    catch (IOException e1)
	    {
		System.err.println("output error while writing the file");
	    }
	}

	System.out.println("File size is " + f.getSize() + " bytes");

	{
	    try
	    {
		// read the graph from the file
		System.out.println("Reading graph from file");
		Grph g = new GrphBinaryReader().readGraph(f);

		// display it
		g.display();
	    }
	    catch (ParseException e)
	    {
		System.err.println("invalid file data");
	    }
	    catch (IOException e)
	    {
		System.err.println("input error while reading the file: " + e.getMessage());
	    }
	    catch (GraphBuildException e)
	    {
		System.err.println("cannot build graph: " + e.getMessage());
	    }
	}

	f.delete();
    }
}
