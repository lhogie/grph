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
import grph.properties.NumericalProperty;

/**
 * Enables reading graph data in which each line represents an edges. Lines
 * starting with '#' are treated as comment. Number separators
 */

public class EdgeListReader extends AbstractGraphReader
{
	private boolean createDirectedEdges = false;
	private boolean allowsMultipleEdges = false;

	public boolean allowsMultipleEdges()
	{
		return allowsMultipleEdges;
	}

	public void setAllowsMultipleEdges(boolean allowsMultipleEdges)
	{
		this.allowsMultipleEdges = allowsMultipleEdges;
	}

	public boolean isCreateDirectedEdges()
	{
		return createDirectedEdges;
	}

	public void setCreateDirectedEdges(boolean createDirectedEdges)
	{
		this.createDirectedEdges = createDirectedEdges;
	}

	@Override
	public Grph readGraph(InputStream is) throws ParseException, IOException
	{
		Grph g = new grph.in_memory.InMemoryGrph();
		alterGraph(g, is, true, false, null);
		return g;
	}

	public static void alterGraph(Grph g, InputStream is,
			boolean allowsMultipleEdges, boolean createDirectedEdges,
			NumericalProperty edgeWeightProperty) throws ParseException,
			IOException
	{
		BufferedReader br = new BufferedReader(new InputStreamReader(is));

		for (int lineNumber = 1;; ++lineNumber)
		{
			String line = br.readLine();

			if (line == null)
			{
				break;
			}
			else
			{
				line = line.trim();

				// if this line is not empty and it is not a comment
				if (line.length() > 0 && line.charAt(0) != '#')
				{
					// old CAIDA maps use | as the delimiter
					String[] numbers = line.split("[ \\t|]+");

					if (numbers.length >= 2)
					{
						int v1 = parseNumber(numbers[0], lineNumber);
						int v2 = parseNumber(numbers[1], lineNumber);
						int e = -1;

						if (!g.getVertices().contains(v1))
						{
							g.addVertex(v1);
						}

						if (!g.getVertices().contains(v2))
						{
							g.addVertex(v2);
						}

						if (allowsMultipleEdges
								|| !g.areVerticesAdjacent(v1, v2))
						{
							if (createDirectedEdges)
							{
								e = g.addDirectedSimpleEdge(v1, v2);
							}
							else
							{

								e = g.addUndirectedSimpleEdge(v1, v2);
							}
						}

						// if a third number is available
						if (e != -1 && numbers.length >= 3 && edgeWeightProperty != null)
						{
							int n = parseNumber(numbers[2], lineNumber);

							if (n >= 0)
							{
								edgeWeightProperty.setValue(e, n);
							}
						}
					}
					else
					{
						throw new ParseException("malformed line ", lineNumber);
					}
				}
			}
		}
	}
}
