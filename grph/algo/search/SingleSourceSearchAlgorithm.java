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

package grph.algo.search;

import grph.Grph;
import grph.GrphAlgorithm;
import grph.algo.MultiThreadProcessing;
import grph.algo.distance.DistanceMatrix;
import grph.algo.distance.PredecessorMatrix;
import toools.set.IntSet;

public abstract class SingleSourceSearchAlgorithm<R extends SearchResult>
		extends GrphAlgorithm<R[]>
{
	@Override
	public R[] compute(final Grph g)
	{
		// computes and cache
		// we need to do that otherwise multiple threads will call it
		// simultaneously
		g.getOutNeighborhoods();

		return compute(g, g.getVertices());
	}

	public R[] compute(final Grph g, IntSet sources)
	{
		return compute(g, Grph.DIRECTION.out, sources);
	}

	public R[] compute(final Grph g, final Grph.DIRECTION d, IntSet sources)
	{
		final R[] r = createArray(sources.getGreatest() + 1);

		new MultiThreadProcessing(g.getVertices()) {

			@Override
			protected void run(int threadID, int source)
			{
				r[source] = compute(g, source, d, null);
			}

		};

		return r;
	}

	protected abstract R[] createArray(int i);

	public R compute(Grph g, int source)
	{
		return compute(g, source, Grph.DIRECTION.out, null);
	}

	public R compute(Grph g, int source, GraphSearchListener listener)
	{
		return compute(g, source, Grph.DIRECTION.out, listener);
	}

	public abstract R compute(Grph g, int source, Grph.DIRECTION d,
			GraphSearchListener listener);

	public PredecessorMatrix computePredecessorMatrix(Grph g)
	{
		return new PredecessorMatrix(compute(g));
	}

	public DistanceMatrix computeDistanceMatrix(Grph g)
	{
		return new DistanceMatrix(compute(g));
	}
}
