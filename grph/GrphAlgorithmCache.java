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

package grph;

import toools.set.IntSet;

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

	public E getCachedValue()
	{
		return cachedValue;
	}

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
			if (!graph.getTopologyListeners().contains(listener))
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
			throw new IllegalArgumentException("cannot call the same algo on 2 different graphs");

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
	private class L implements TopologyListener
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
		public void undirectedHyperEdgeRemoved(Grph graph, int edge, IntSet incidentVertices)
		{
			invalidateCachedValue();
		}

		@Override
		public void directedHyperEdgeRemoved(Grph graph, int edge, IntSet src, IntSet dest)
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
