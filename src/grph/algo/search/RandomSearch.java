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

package grph.algo.search;

import java.util.Random;

import grph.Grph;
import grph.Grph.DIRECTION;
import grph.algo.search.GraphSearchListener.DECISION;
import grph.algo.topology.ClassicalGraphs;
import it.unimi.dsi.fastutil.ints.IntSet;
import toools.collections.primitive.LucIntSet;

public class RandomSearch
{
	public RandomSearch(Grph graph, int source, Grph.DIRECTION direction, Random prng,
			int numberOfIterations, GraphSearchListener l)
	{
		assert graph != null;
		assert source >= 0;
		assert numberOfIterations >= 0;

		l.searchStarted();

		for (int i = 0; i < numberOfIterations; ++i)
		{
			DECISION d = l.vertexFound(source);

			if (d == null)
			{
				throw new IllegalStateException(
						"you need to decide something. Stop or continue?");
			}
			else if (d == DECISION.STOP)
			{
				return;
			}

			LucIntSet out = graph.getNeighbours(source, direction);

			if (out.isEmpty())
			{
				break;
			}
			else
			{
				source = out.pickRandomElement(prng);
			}
		}

		l.searchStarted();

	}

	public static void main(String[] args)
	{
		Grph g = ClassicalGraphs.grid(5, 5);
		new RandomSearch(g, 0, DIRECTION.out, new Random(), 10000,
				new GraphSearchListener()
				{

					@Override
					public void searchStarted()
					{
						// TODO Auto-generated method stub

					}

					@Override
					public DECISION vertexFound(int v)
					{
						System.out.println(v);

						if (v == 6)
						{
							return DECISION.STOP;
						}

						return DECISION.CONTINUE;
					}

					@Override
					public void searchCompleted()
					{
						// TODO Auto-generated method stub

					}
				});
	}

}
