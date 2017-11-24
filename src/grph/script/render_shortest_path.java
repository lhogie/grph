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

import grph.Grph;
import grph.path.Path;
import java4unix.CommandLine;
import toools.collections.primitive.IntCursor;
import toools.io.file.RegularFile;
import toools.thread.Threads;

public class render_shortest_path extends AbstractGraphOperation
{

	public render_shortest_path(RegularFile launcher)
	{
		super(launcher);
		// TODO Auto-generated constructor stub
	}

	@Override
	public int runScript(CommandLine cmdLine, final Grph graph) throws Throwable
	{
		int source = Integer.valueOf(cmdLine.findParameters().get(1));
		int dest = Integer.valueOf(cmdLine.findParameters().get(2));

		for (IntCursor v : IntCursor.fromFastUtil(graph.getVertices()))
		{
			graph.getVertexColorProperty().setValue(v.value, 0);
		}

		Path p = graph.getShortestPath(source, dest);

		for (int v : p.toVertexArray())
		{
			graph.getVertexColorProperty().setValue(v, 2);
		}

		// graph.setVertexColor(source, 50);
		// graph.setVertexColor(dest, 43);

		graph.display();

		Threads.sleepForever();
		return 0;
	}

	@Override
	public String getShortDescription()
	{
		return "Render the given graph via graphstream";
	}

	public static void main(String[] args) throws Throwable
	{
		new render_shortest_path(null).run("/Users/lhogie/tmp/test.dc", "1", "65");
	}
}
