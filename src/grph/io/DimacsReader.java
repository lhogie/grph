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
 
 package grph.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import grph.Grph;

public class DimacsReader extends AbstractGraphReader
{

    @Override
    public Grph readGraph(InputStream is) throws ParseException, IOException
    {
	Grph g = new grph.in_memory.InMemoryGrph();
	BufferedReader br = new BufferedReader(new InputStreamReader(is));

	String line = br.readLine();
	int l = 1;

	while (line != null)
	{
	    line = line.trim();

	    if (line.charAt(0) != 'c' && line.charAt(0) != 'p')
	    {
		String[] tokens = line.split("[ \\ ]+");

		if (tokens.length != 3)
		{
		    throw new ParseException("malformed line", l);
		}
		else
		{
		    int v1 = Integer.valueOf(tokens[1]);
		    int v2 = Integer.valueOf(tokens[2]);

		    if (!g.getVertices().contains(v1))
		    {
			g.addVertex(v1);
		    }

		    if (!g.getVertices().contains(v2))
		    {
			g.addVertex(v2);
		    }

		    if (g.getEdgesConnecting(v1, v2) != null && g.getEdgesConnecting(v1, v2).isEmpty())
		    {
			g.addUndirectedSimpleEdge(v1, v2);
		    }

		}
	    }
	    line = br.readLine();
	    l++;
	}
	
	return g;

    }
}
