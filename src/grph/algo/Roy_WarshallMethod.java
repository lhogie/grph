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
 
 

package grph.algo;

import grph.Grph;
import grph.in_memory.InMemoryGrph;

public class Roy_WarshallMethod
{
	/**
	 * Create the transitive closure of the given graph.
	 * 
	 * @param source
	 *            The graph to transform.
	 * @return A new graph that is the transformed using the transitive closure
	 *         from the given parameter.
	 */
	public static Grph createTransitiveClosure(final Grph source)
	{
		if (source == null)
			return null;

		int[] verticesList = source.getVertices().toIntArray();

		Grph result = source.clone();

		if (verticesList.length < 3)
			return result;

		boolean changed = false;

		do
		{
			for (int x : verticesList)
			{
				for (int neighbor1 : result.getOutNeighbors(x).toIntArray())
				{
					for (int neighbor2 : result.getOutNeighbors(neighbor1).toIntArray())
					{
						if (result.getEdgesConnecting(x, neighbor2).size() == 0)
						{
							result.addDirectedSimpleEdge(x, neighbor2);
							changed = true;
						}
					}
				}
			}
		} while (changed);

		return result;
	}

	public static void main(String[] args)
	{
		Grph g = null;
		g = new InMemoryGrph();

		g.grid(3, 3, true, false, false);
		g.display();

		Grph g2;
		g2 = Roy_WarshallMethod.createTransitiveClosure(g);
		g2.display();
	}

}
