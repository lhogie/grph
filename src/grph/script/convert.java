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
 
 package grph.script;


import grph.Grph;
import grph.io.AbstractGraphWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java4unix.CommandLine;

import toools.io.file.RegularFile;



public class convert extends AbstractGraphOperation
{


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
		if (cmdLine.findArguments().size() == 1)
		{
			RegularFile inputFile = new RegularFile(cmdLine.findArguments().get(0));

			for (String extension : AbstractGraphWriter.getExtensions())
			{
				files.add(new RegularFile(inputFile.getNameWithoutExtension() + "." + extension));
			}
		}
		else
		{
			files.add(new RegularFile(cmdLine.findArguments().get(1)));
		}
		
		return files;
	}

	public static void main(String[] args) throws Throwable
	{
		new convert().run("/Users/lhogie/tmp/test.dc", "/Users/lhogie/tmp/test.dot");
	}
}
