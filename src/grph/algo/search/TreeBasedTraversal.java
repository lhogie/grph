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
import toools.collections.IntQueue;
import toools.collections.IntQueue.ACCESS_MODE;

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
