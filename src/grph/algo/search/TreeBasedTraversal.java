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
 
 

/**
 * Grph
 * Initial Software by Luc HOGIE, Issam TAHIRI, Aurélien LANCIN, Nathann COHEN, David COUDERT.
 * Copyright © INRIA/CNRS/UNS, All Rights Reserved, 2011, v0.9
 *
 * The Grph license grants any use or destribution of both binaries and source code, if
 * a prior notification was made to the Grph development team.
 * Modification of the source code is not permitted. 
 * 
 *
 */
package grph.algo.search;

import grph.Grph;
import grph.algo.search.GraphSearchListener.DECISION;
import toools.collections.primitive.IntQueue;
import toools.collections.primitive.IntQueue.ACCESS_MODE;

public abstract class TreeBasedTraversal extends SingleSourceSearchAlgorithm<SearchResult>
{
	@Override
	public SearchResult compute(Grph graph, int source, Grph.DIRECTION direction, GraphSearchListener listener)
	{
		assert graph != null;
		assert graph.getVertices().contains(source);
		int[][] adj = graph.getNeighbors(direction);
		int n = graph.getVertices().getGreatest() + 1;
		SearchResult r = new SearchResult(n);
//		r.source = source;
		r.distances[source] = 0;
		r.visitOrder.add(source);

		if (listener != null)
		{
			listener.searchStarted();
		}

		IntQueue queue = new IntQueue();
		queue.add(source);

		while (queue.getSize() > 0)
		{
			int v = queue.extract(getAccessMode());
			int d = r.distances[v];

			if (listener != null)
			{
				if (listener.vertexFound(v) == DECISION.STOP)
				{
					break;
				}
			}

			for (int neighbor : adj[v])
			{
				// if this vertex was not yet visited
				if (r.distances[neighbor] == -1)
				{
					r.predecessors[neighbor] = v;
					r.distances[neighbor] = d + 1;
					queue.add(neighbor);
					r.visitOrder.add(neighbor);
				}
			}
		}

		if (listener != null)
		{
			listener.searchCompleted();
		}

		return r;
	}

	protected abstract ACCESS_MODE getAccessMode();

	@Override
	protected SearchResult[] createArray(int n)
	{
		return new SearchResult[n];
	}
}
