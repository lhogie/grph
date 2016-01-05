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
 
 package grph.algo.lad;

import grph.Grph;
import grph.in_memory.InMemoryGrph;
import grph.io.AbstractGraphReader;
import grph.io.GraphBuildException;
import grph.io.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

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
