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
 
 package grph.algo.topology.gsm;

import java.io.PrintStream;
import java.util.Collection;
import java.util.Random;

import grph.io.GraphvizImageWriter.COMMAND;
import grph.io.GraphvizImageWriter.OUTPUT_FORMAT;
import grph.script.AbstractGrphScript;
import java4unix.CommandLine;
import java4unix.OptionSpecification;
import toools.collections.ElementPrinter;
import toools.io.file.Directory;
import toools.io.file.RegularFile;

public class WirelessBackhaulScript extends AbstractGrphScript
{

	public WirelessBackhaulScript(RegularFile launcher)
	{
		super(launcher);
	}

	@Override
	protected void declareOptions(Collection<OptionSpecification> optionSpecifications)
	{
		optionSpecifications.add(new OptionSpecification("--numberOfBTSs", null, "[0-9]+", "30", "the number of BTSs"));
		optionSpecifications.add(new OptionSpecification("--numberOfGTWs", null, "[0-9]+", "3", "the number of BSCs"));
		optionSpecifications.add(new OptionSpecification("--density", null, ".*", "0.8", "the number of BSCs"));
	}

	@Override
	public int runScript(CommandLine cmdLine) throws Throwable
	{
		ClassLoader.getSystemClassLoader().setDefaultAssertionStatus(true);
		int bts = Integer.valueOf(getOptionValue(cmdLine, "--numberOfBTSs"));
		int bsc = Integer.valueOf(getOptionValue(cmdLine, "--numberOfGTWs"));
		double density = Double.valueOf(getOptionValue(cmdLine, "--density"));
		
		if (density < 0 || density > 1)
			throw new IllegalArgumentException("invalid density " + density +  ". Please give a value between 0 and 1");

		final WirelessBackhaul b = new WirelessBackhaul(bts, bsc, new Random(), density);
		RegularFile imageFile = b.g.toGraphviz(COMMAND.neato, OUTPUT_FORMAT.png, Directory.getCurrentDirectory());
		System.out.println("Generating file " + imageFile.getName());

		RegularFile datFile = new RegularFile(imageFile.getPath().replaceAll("\\.png$", ".dat"));
		PrintStream p = new PrintStream(datFile.createWritingStream());
		System.out.println("Generating file " + datFile.getName());

		p.println("NumNodes = " + b.g.getNumberOfVertices() + ";");
		p.println("Nodes = " + b.g.getVertices() + ";");
		p.println("NumEdges = " + b.g.getNumberOfEdges() + ";");
		p.println("Edges = " + b.g.getEdges().toString(new ElementPrinter() {

			@Override
			public String toString(int e)
			{
				int v1 = b.g.getOneVertex(e);
				int v2 = b.g.getTheOtherVertex(e, v1);
				int c = b.edgeCapacities.getValueAsInt(e);
				return "<" + v1 + ", " + v2 + ", " + c + ">";
			}
		}) + ";");
		p.println("Gateways = " + b.bscIDs() + ";");
		p.close();

		return 0;
	}

	@Override
	public String getShortDescription()
	{
		return "Computes a wireless backhaul network for Alvinice's use.";
	}

	public static void main(String[] args) throws Throwable
	{
		new WirelessBackhaulScript(null).run("--numberOfBTSs=30", "--numberOfGTWs=5", "--density=0.8");
	}
}
