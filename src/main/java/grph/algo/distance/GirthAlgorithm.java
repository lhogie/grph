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
Julien Deantoni (I3S, Université Cote D'Azur, Saclay) 

*/
 
 
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

package grph.algo.distance;

import grph.Grph;
import grph.GrphAlgorithm;
import grph.algo.topology.ClassicalGraphs;
import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntSet;
import toools.collections.primitive.IntCursor;
import toools.collections.primitive.LucIntSet;
import toools.collections.primitive.SelfAdaptiveIntSet;

/**
 * @author Gregory Morel
 */
@SuppressWarnings("serial")
public class GirthAlgorithm extends GrphAlgorithm<IntSet>
{

	/**
	 * @return an IntSet that contains vertices of a shortest cycle of
	 *         <code>g</code>
	 */
	@Override
	public IntSet compute(Grph g)
	{
		int girth = Integer.MAX_VALUE;
		IntSet cycle = new SelfAdaptiveIntSet(0);

		// We perform a BFS for each vertex
		for (IntCursor c : IntCursor.fromFastUtil(g.getVertices()))
		{
			int v = c.value;

			IntSet visited = new SelfAdaptiveIntSet(g.getNumberOfVertices());

			LucIntSet queue = new SelfAdaptiveIntSet(g.getNumberOfVertices());
			queue.add(v);

			Int2IntMap parent = new Int2IntOpenHashMap();
			parent.put(v, - 1);

			Int2IntMap distance = new Int2IntOpenHashMap();
			distance.put(v, 0);

			while ( ! queue.isEmpty())
			{
				int x = queue.getGreatest(); // No matter what element we pick
				// from R
				visited.add(x);
				queue.remove(x);
				for (int y : g.getNeighbours(x).toIntArray())
				{
					if (y != parent.get(x))
					{ // Do not merge the if
						if ( ! visited.contains(y))
						{
							parent.put(y, x);
							distance.put(y, distance.get(x) + 1);
							queue.add(y);
						}
						else
						{
							int d = distance.get(x) + distance.get(y) + 1;
							if (d < girth)
							{
								girth = d;
								cycle = new SelfAdaptiveIntSet(0);

								int u = x;

								while (u != - 1)
								{
									cycle.add(u);
									u = parent.get(u);
								}
							}
						}
					}
				}
			}
		}

		return cycle;
	}

	public static void main(String[] args)
	{
		Grph g = ClassicalGraphs.path(4).getComplement();
		g.display();
		IntSet shortestCycle = g.getShortestCycle();
		System.out.println("A shortest cycle of g is induced by " + shortestCycle
				+ " so the girth is " + shortestCycle.size());
	}
}
