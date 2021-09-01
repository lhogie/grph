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
 
 

package grph.algo.search;

import grph.Grph;
import grph.GrphAlgorithm;
import grph.algo.MultiThreadProcessing;
import grph.algo.distance.DistanceMatrix;
import grph.algo.distance.PredecessorMatrix;
import toools.collections.primitive.LucIntSet;

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

	public R[] compute(final Grph g, LucIntSet sources)
	{
		return compute(g, Grph.DIRECTION.out, sources);
	}

	public R[] compute(final Grph g, final Grph.DIRECTION d, LucIntSet sources)
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
