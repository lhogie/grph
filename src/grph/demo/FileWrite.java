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
 
 package grph.demo;

import java.io.IOException;

import grph.Grph;
import grph.io.GraphBuildException;
import grph.io.GrphBinaryReader;
import grph.io.GrphBinaryWriter;
import grph.io.ParseException;
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
