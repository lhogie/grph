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

package grph;

import java.io.Serializable;

import it.unimi.dsi.fastutil.ints.IntSet;

/**
 * A graph algorithm wrapper that cache the result computed by its underlying
 * algorithm.
 * 
 * @author lhogie
 * 
 * @param <E>
 *            the result type of the algorithm, which is the same as the result
 *            type of the underfyling algorithm
 */

public class GrphAlgorithmCache<E> extends GrphAlgorithm<E>
{
	private E cachedValue;


	private final GrphAlgorithm<E> algo;
	private final Grph graph;
	private boolean localCacheEnabled = Grph.useCache;
	private boolean cachedValueValid;
	private long timeSpentComputingThis = 0;
	private long numberOfTimesTheCacheWasUsed = 0;
	private long numberOfTimesAComputationWasPerformed = 0;
	private transient final TopologyListener listener = new L();

	public GrphAlgorithmCache(Grph graph, GrphAlgorithm<E> algo)
	{
		this.algo = algo;
		this.graph = graph;
		graph.getTopologyListeners().add(listener);
	}

	public E getCachedValue()
	{
		return cachedValue;
	}

	
	/**
	 * Returns the underlying algorithm.
	 * 
	 * @return the underlying algorithm
	 */
	public GrphAlgorithm<E> getCachedAlgorithm()
	{
		return algo;
	}

	@Override
	public String getSourceCode()
	{
		return algo.getSourceCode();
	}

	public void invalidateCachedValue()
	{
		cachedValueValid = false;
		cachedValue = null;
	}

	public final boolean isCachedValueValid()
	{
		return isCacheEnabled() && cachedValueValid;
	}

	public final long getTimeSpentComputing()
	{
		return timeSpentComputingThis;
	}

	public final long getNumberOfTimesTheCacheWasUsed()
	{
		return numberOfTimesTheCacheWasUsed;
	}

	public final long getAvgTimeRequiredForComputing()
	{
		return timeSpentComputingThis / numberOfTimesAComputationWasPerformed;
	}

	public final long getTotalNumberOfCallsFor()
	{
		return numberOfTimesAComputationWasPerformed + numberOfTimesTheCacheWasUsed;
	}

	public final long getTimeRequiredForComputingIfCacheWereNotUsed()
	{
		return getTotalNumberOfCallsFor() * getAvgTimeRequiredForComputing();
	}

	public final long getNumberOfTimesAComputationWasPerformed()
	{
		return numberOfTimesAComputationWasPerformed;
	}

	public boolean isCacheEnabled()
	{
		return Grph.useCache && localCacheEnabled;
	}

	public void setCacheEnabled(boolean enabled)
	{
		this.localCacheEnabled = enabled;

		if (enabled)
		{
			if ( ! graph.getTopologyListeners().contains(listener))
			{
				graph.getTopologyListeners().add(listener);
			}
		}
		else
		{
			graph.getTopologyListeners().remove(listener);
		}
	}

	@Override
	public E compute(Grph g)
	{
		if (this.graph != g)
			throw new IllegalArgumentException(
					"cannot call the same algo on 2 different graphs");

		return compute();
	}

	private E compute()
	{
		if (isCachedValueValid())
		{
			++numberOfTimesTheCacheWasUsed;
		}
		else
		{
			// updates the value in the cache by recomputing it
			long startDate = System.currentTimeMillis();
			this.cachedValue = algo.compute(graph);
			timeSpentComputingThis += System.currentTimeMillis() - startDate;
			++numberOfTimesAComputationWasPerformed;
			cachedValueValid = true;
		}

		return cachedValue;
	}

	/*
	 * @Override public boolean equals(Object obj) { return obj.getClass() ==
	 * getClass(); }
	 * 
	 * @Override public int hashCode() { return getClass().hashCode(); }
	 * 
	 * @Override public String toString() { return getName(); }
	 */
	private class L implements TopologyListener, Serializable
	{
		@Override
		public void vertexAdded(Grph graph, int vertex)
		{
			invalidateCachedValue();
		}

		@Override
		public void vertexRemoved(Grph graph, int vertex)
		{
			invalidateCachedValue();
		}

		@Override
		public void directedSimpleEdgeAdded(Grph Grph, int edge, int src, int dest)
		{
			invalidateCachedValue();
		}

		@Override
		public void undirectedSimpleEdgeAdded(Grph Grph, int edge, int a, int b)
		{
			invalidateCachedValue();
		}

		@Override
		public void undirectedHyperEdgeAdded(Grph graph, int edge)
		{
			invalidateCachedValue();
		}

		@Override
		public void directedHyperEdgeAdded(Grph graph, int edge)
		{
			invalidateCachedValue();
		}

		@Override
		public void directedSimpleEdgeRemoved(Grph Grph, int edge, int a, int b)
		{
			invalidateCachedValue();
		}

		@Override
		public void undirectedSimpleEdgeRemoved(Grph Grph, int edge, int a, int b)
		{
			invalidateCachedValue();
		}

		@Override
		public void undirectedHyperEdgeRemoved(Grph graph, int edge,
				IntSet incidentVertices)
		{
			invalidateCachedValue();
		}

		@Override
		public void directedHyperEdgeRemoved(Grph graph, int edge, IntSet src,
				IntSet dest)
		{
			invalidateCachedValue();
		}

		@Override
		public void vertexAddedToDirectedHyperEdgeTail(Grph Grph, int e, int v)
		{
			invalidateCachedValue();
		}

		@Override
		public void vertexAddedToDirectedHyperEdgeHead(Grph Grph, int e, int v)
		{
			invalidateCachedValue();
		}

		@Override
		public void vertexAddedToUndirectedSimpleEdge(Grph Grph, int edge, int vertex)
		{
			invalidateCachedValue();
		}

		@Override
		public void vertexRemovedFromUndirectedHyperEdge(Grph g, int edge, int vertex)
		{
			invalidateCachedValue();
		}

		@Override
		public void vertexRemovedFromDirectedHyperEdgeTail(Grph g, int e, int v)
		{
			invalidateCachedValue();
		}

		@Override
		public void vertexRemovedFromDirectedHyperEdgeHead(Grph g, int e, int v)
		{
			invalidateCachedValue();
		}
	}

	public void setCachedValue(E v)
	{
		this.cachedValue = v;
		cachedValueValid = true;
	}

}
