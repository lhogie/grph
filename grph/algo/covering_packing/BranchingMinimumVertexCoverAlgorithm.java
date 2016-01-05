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
 
 package grph.algo.covering_packing;

import grph.Grph;
import grph.GrphAlgorithm;
import toools.set.DefaultIntSet;
import toools.set.IntSet;

@SuppressWarnings("serial")
public class BranchingMinimumVertexCoverAlgorithm extends GrphAlgorithm<IntSet> {

	@Override
	public IntSet compute(Grph g) {
		return simpleBranching(g.clone(), new DefaultIntSet());
	}

	private IntSet simpleBranching(Grph g, IntSet vc) {
		if(g.getNumberOfVertices() == 0)
			return vc;
		else {
			// Pick the first vertex
			int v = g.getVertices().toIntArray()[0];

			// Branching: either we add v in the cover, or its neighbors
			// So we build two graphs: g1 := g \ {v}, and g2 := g \ N[v]
			Grph g1 = g.clone();
			IntSet vc1 = vc.clone();
			g1.removeVertex(v);
			vc1.add(v);

			Grph g2 = g.clone();
			IntSet vc2 = vc.clone();
			IntSet n = g2.getNeighbours(v);
			g2.removeVertices(n);
			g2.removeVertex(v);
			vc2.addAll(n);

			// We compute the two Vertex Covers
			IntSet sol1 = simpleBranching(g1, vc1);
			IntSet sol2 = simpleBranching(g2, vc2);

			// And return the best one
			if(sol1.size() < sol2.size())
				return sol1;
			else
				return sol2;
		}
	}
}
