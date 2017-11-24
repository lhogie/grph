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

import grph.Grph;
import java4unix.CommandLine;
import toools.io.file.RegularFile;


public class has_cycles extends AbstractGraphOperation
{
	public has_cycles(RegularFile launcher)
	{
		super(launcher);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getShortDescription()
	{
		return "Prints if the given graph has cycles";
	}

	@Override
	public int runScript(CommandLine cmdLine, Grph graph) throws IOException
	{
		int src = Integer.valueOf(cmdLine.findParameters().get(1));
		printMessage(graph.hasCycles(src) ? "This graph has at least one cycle." : "This graph has no cycle.");
		graph.display();
		return 0;
	}
}
