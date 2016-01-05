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
 
 package grph.io;

import grph.Grph;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

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
