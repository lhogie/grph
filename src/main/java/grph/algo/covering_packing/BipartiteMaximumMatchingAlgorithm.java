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

package grph.algo.covering_packing;

import grph.DefaultIntSet;
import grph.Grph;
import grph.GrphAlgorithm;
import grph.algo.MaxFlowAlgorithm;
import grph.algo.MaxFlowAlgorithmResult;
import grph.algo.coloring.BipartitenessAlgorithm;
import grph.algo.coloring.GraphColoring;
import it.unimi.dsi.fastutil.ints.Int2DoubleMap;
import it.unimi.dsi.fastutil.ints.Int2DoubleMap.Entry;
import it.unimi.dsi.fastutil.ints.IntSet;
import toools.collections.LucIntSets;
import toools.collections.primitive.SelfAdaptiveIntSet;

/**
 * An algorithm to solve the maximum bipartite matching problem. It is based on
 * the classical reduction to a network flow problem, described by a linear
 * program and then solved by a solver.
 * 
 * @author Gregory Morel, Vincent Levorato, Jean-Francois Lalande
 */

@SuppressWarnings("serial")
public class BipartiteMaximumMatchingAlgorithm extends GrphAlgorithm<IntSet>
{

	/**
	 * Implementation of the algorithm described above.
	 * 
	 * @param g
	 *            a bipartite graph.
	 * @return an IntSet that consists of the set of edges in a maximum
	 *         bipartite matching, or null if g is not bipartite.
	 */
	@Override
	public IntSet compute(Grph g)
	{
		if ( ! g.isBipartite())
			return null;

		Grph flow = buildBipartiteFlow(g);

		int source = flow.getNumberOfVertices() - 2;
		int sink = flow.getNumberOfVertices() - 1;

		MaxFlowAlgorithmResult mfar = new MaxFlowAlgorithm().compute(flow, source, sink,
				null);
		Int2DoubleMap idm = mfar.getAssigments();
		IntSet is = new SelfAdaptiveIntSet(0);

		for (Entry i : idm.int2DoubleEntrySet())
		{
			if (i.getDoubleValue() > 0)
			{
				is.add(i.getIntKey());
			}
		}

		return LucIntSets.intersectionToTarget(DefaultIntSet.class, is, g.getEdges());
	}

	/**
	 * This method transforms the specified bipartite graph into a network.
	 * 
	 * @param g
	 *            a bipartite graph
	 * @return the corresponding network
	 */
	private Grph buildBipartiteFlow(Grph g)
	{
		// Maybe to be changed later in an other form
		BipartitenessAlgorithm ba = new BipartitenessAlgorithm();
		GraphColoring c = ba.compute(g);

		assert c != null;

		Grph flow = g.clone();

		int n = flow.getNumberOfVertices();
		int source = n;
		int sink = n + 1;

		flow.addVertex(source);
		flow.addVertex(sink);

		for (int v : c.getColorClass(0).toIntArray())
		{
			for (int w : flow.getNeighbours(v).toIntArray())
			{
				flow.disconnect(v, w);
				flow.addDirectedSimpleEdge(v, w);
			}

			// Add an edge from the source to each vertex in the left side of
			// the bipartition
			flow.addDirectedSimpleEdge(source, v);
		}

		for (int w : c.getColorClass(1).toIntArray())
		{
			// Add an edge from each vertex in the right side of the bipartition
			// to the sink
			flow.addDirectedSimpleEdge(w, sink);
		}

		return flow;
	}
}
