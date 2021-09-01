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

import grph.Grph;
import grph.GrphAlgorithm;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import toools.collections.primitive.SelfAdaptiveIntSet;

@SuppressWarnings("serial")
public class BranchingMinimumVertexCoverAlgorithm extends GrphAlgorithm<IntSet>
{

	@Override
	public IntSet compute(Grph g)
	{
		return simpleBranching(g.clone(), new SelfAdaptiveIntSet(0));
	}

	private IntSet simpleBranching(Grph g, IntSet vc)
	{
		if (g.getNumberOfVertices() == 0)
			return vc;
		else
		{
			// Pick the first vertex
			int v = g.getVertices().toIntArray()[0];

			// Branching: either we add v in the cover, or its neighbors
			// So we build two graphs: g1 := g \ {v}, and g2 := g \ N[v]
			Grph g1 = g.clone();
			IntSet vc1 = new IntOpenHashSet(vc);
			g1.removeVertex(v);
			vc1.add(v);

			Grph g2 = g.clone();
			IntSet vc2 = new IntOpenHashSet(vc);
			IntSet n = g2.getNeighbours(v);
			g2.removeVertices(n);
			g2.removeVertex(v);
			vc2.addAll(n);

			// We compute the two Vertex Covers
			IntSet sol1 = simpleBranching(g1, vc1);
			IntSet sol2 = simpleBranching(g2, vc2);

			// And return the best one
			if (sol1.size() < sol2.size())
				return sol1;
			else
				return sol2;
		}
	}
}
