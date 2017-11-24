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
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import toools.Clazz;
import toools.collections.Collections;
import toools.collections.LucIntSets;

/**
 * e0 2 1 a1 1 > 2 E9 1 4 A34 4 6 8 > 1 8
 * 
 * @author lhogie
 * 
 */

public class GrphTextReader extends AbstractGraphReader
{

	@Override
	public Grph readGraph(InputStream is) throws ParseException, IOException
	{
		Grph g = null;
		BufferedReader br = new BufferedReader(new InputStreamReader(is));

		for (int lineNumber = 0;; ++lineNumber)
		{
			String line = br.readLine();

			if (line == null)
			{
				break;
			}

			line = line.trim();
			int indexOfComment = line.indexOf('#');

			if (indexOfComment >= 0)
			{
				line = line.substring(0, indexOfComment).trim();
			}

			if ( ! line.isEmpty())
			{
				int equalPos = line.indexOf('=');

				// this is a property
				if (equalPos >= 0)
				{
					String name = line.substring(0, equalPos);
					String value = line.substring(equalPos + 1);

					if (name.equals("graph class"))
					{
						g = (Grph) Clazz.makeInstance(Clazz.findClass(value));
					}
					else
					{
						throw new ParseException("unknown property: " + name, lineNumber);
					}
				}
				else
				{
					if (g == null)
					{
						g = new grph.in_memory.InMemoryGrph();
					}

					line = line.trim();
					char edgeType = line.charAt(0);
					int indexOfSpace = line.indexOf(' ');

					// no incident vertices
					if (indexOfSpace == - 1)
					{
						int edgeID = Integer.valueOf(line.substring(1));

						if (edgeType == 'E')
						{
							g.addUndirectedHyperEdge(edgeID);
						}
						else if (edgeType == 'A')
						{
							g.addDirectedHyperEdge(edgeID);
						}
						else
						{
							throw new IllegalStateException(
									"incident vertices are missing");
						}
					}
					else
					{
						int e = Integer.valueOf(line.substring(1, indexOfSpace));

						// retain only incident vertices
						line = line.substring(indexOfSpace).trim();

						if (edgeType == 'e' || edgeType == 'a')
						{
							line.replace('>', ' ');
							IntArrayList incidentVertices = Collections.toArrayList(line);

							if (incidentVertices.size() == 2)
							{
								g.addSimpleEdge(incidentVertices.get(0),
										incidentVertices.get(1), edgeType == 'a');
							}
							else
							{
								throw new IllegalStateException(
										"only 2 incident vertices are allowed");
							}
						}
						else if (edgeType == 'E')
						{
							g.addUndirectedHyperEdge(e);

							for (int v : LucIntSets.from(IntOpenHashSet.class, line)
									.toIntArray())
							{
								g.addToUndirectedHyperEdge(e, v);
							}

						}
						else if (edgeType == 'A')
						{
							g.addDirectedHyperEdge(e);

							String[] sets = line.split(">");

							if (sets.length == 2)
							{
								for (int v : LucIntSets
										.from(IntOpenHashSet.class, sets[0]).toIntArray())
								{
									g.addToDirectedHyperEdgeTail(e, v);
								}

								for (int v : LucIntSets
										.from(IntOpenHashSet.class, sets[1]).toIntArray())
								{
									g.addToDirectedHyperEdgeHead(e, v);
								}
							}
							else
							{
								throw new IllegalStateException(
										"line does not match *>*");
							}
						}
					}
				}
			}
		}

		return g;
	}

}
