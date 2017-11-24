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
import grph.GrphAlgorithm;
import it.unimi.dsi.fastutil.ints.IntSet;
import toools.Clazz;
import toools.collections.primitive.IntCursor;
import toools.collections.primitive.SelfAdaptiveIntSet;

/**
 * Computes the complement (or inverse) of the given graph.
 * 
 * @author lhogie
 *
 */
@SuppressWarnings("serial")
public class ComplementAlgorithm extends GrphAlgorithm<Grph>
{
	@Override
	public Grph compute(Grph g)
	{
		if (g.isMixed())
			throw new IllegalArgumentException("cannot get the inverse of a mixed graph");

		if (g.getNumberOfHyperEdges() > 0)
			throw new IllegalArgumentException("cannot get the inverse of an hypergraph");

		// if this is a digraph
		if (g.getNumberOfDirectedSimpleEdges() > 0)
		{
			Grph complement = Clazz.makeInstance(g.getClass());

			for (IntCursor c : IntCursor.fromFastUtil(g.getEdges()))
			{
				int e = c.value;
				int src = g.getDirectedSimpleEdgeTail(e);
				int dest = g.getDirectedSimpleEdgeHead(e);
				complement.addDirectedSimpleEdge(dest, e, src);
			}

			return complement;
		}
		else
		{
			Grph complement = Clazz.makeInstance(g.getClass());
			IntSet vg = g.getVertices();
			complement.addVertices(vg);
			int[] vertices = vg.toIntArray();

			IntSet visited = new SelfAdaptiveIntSet();
			for (int a : vertices)
			{
				for (int b : vertices)
				{
					if (a != b && ! visited.contains(b))
					{
						// if the two edges are not connected
						if ( ! g.areVerticesAdjacent(a, b))
						{
							complement.addUndirectedSimpleEdge(a, b);
						}
					}
				}

				visited.add(a);
			}

			return complement;
		}
	}
}
