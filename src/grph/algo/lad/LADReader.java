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
 
 package grph.algo.lad;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import grph.Grph;
import grph.in_memory.InMemoryGrph;
import grph.io.AbstractGraphReader;
import grph.io.GraphBuildException;
import grph.io.ParseException;

public class LADReader extends AbstractGraphReader
{

    @Override
    public Grph readGraph(InputStream is) throws ParseException, IOException, GraphBuildException
    {
	Grph g = new InMemoryGrph();
	BufferedReader br = new BufferedReader(new InputStreamReader(is));

	int numberOfVertices = Integer.valueOf(br.readLine());

	for (int v1 = 0; v1 < numberOfVertices; ++v1)
	{
	    String line = br.readLine();

	    if (line == null)
	    {
		break;
	    }
	    else
	    {
		line = line.trim();

		if (line.charAt(0) != '#')
		{
		    String[] tokens = line.split("[ \\t]+");
		    int degree = parseNumber(tokens[0], v1 + 2);

		    for (int i = 1; i < tokens.length; ++i)
		    {
			int v2 = parseNumber(tokens[i], v1 + 2);
			g.addUndirectedSimpleEdge(v1, v2);
		    }
		}
	    }
	}

	return g;
    }

}
