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


import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import grph.Grph;
import grph.io.AbstractGraphWriter;
import java4unix.CommandLine;
import toools.io.file.RegularFile;



public class convert extends AbstractGraphOperation
{


	public convert(RegularFile launcher)
	{
		super(launcher);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getShortDescription()
	{
		return "Convert the given graph description file to another file."
		+ "The output format is guessed by the extension of the output file, given as the second argument."
		+ "If no such 2nd argument is passed, then the conversion is done to all output format supported."
		+ "These include DHDL, DHDF, GML, GraphML, Inet, etc.";
	}

	@Override
	public int runScript(CommandLine cmdLine, Grph graph) throws IOException
	{
		for (RegularFile outputFile : getOutputFiles(cmdLine))
		{
			String ext = outputFile.getExtension();
			printMessage("Writing file " + outputFile.getName());
			AbstractGraphWriter.findWriter(ext).writeGraph(graph, outputFile);
		}

		return 0;
	}
	
	private Collection<RegularFile> getOutputFiles(CommandLine cmdLine)
	{
		Collection<RegularFile> files = new ArrayList<RegularFile>();
		
		// if no output file is specified
		if (cmdLine.findParameters().size() == 1)
		{
			RegularFile inputFile = new RegularFile(cmdLine.findParameters().get(0));

			for (String extension : AbstractGraphWriter.getExtensions())
			{
				files.add(new RegularFile(inputFile.getNameWithoutExtension() + "." + extension));
			}
		}
		else
		{
			files.add(new RegularFile(cmdLine.findParameters().get(1)));
		}
		
		return files;
	}

	public static void main(String[] args) throws Throwable
	{
		new convert(null).run("/Users/lhogie/tmp/test.dc", "/Users/lhogie/tmp/test.dot");
	}
}
