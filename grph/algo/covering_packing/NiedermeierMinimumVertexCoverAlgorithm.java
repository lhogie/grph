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

/**
 * @author Gregory Morel, Vincent Levorato, Jean-Francois Lalande

 */

import grph.Grph;
import grph.GrphAlgorithm;
import grph.in_memory.InMemoryGrph;
import toools.set.DefaultIntSet;
import toools.set.IntSet;
import toools.set.IntSets;

@SuppressWarnings("serial")
public class NiedermeierMinimumVertexCoverAlgorithm extends GrphAlgorithm<IntSet>
{

	@Override
	public IntSet compute(Grph g)
	{
		return branching(g.clone(), new DefaultIntSet());
	}

	/*
	 * This is the O(1.33^k) Bounded Search Tree algorithm, as explained in
	 * Niedermeier, "Invitation to Fixed Parameter Algorithms", 2006, pp.
	 * 98-101.
	 * 
	 * Note: Some small improvements have been added. Moreover, because of the
	 * recursive nature of this algorithm, we can - and it makes the code more
	 * readable - remove the nested 'else' statements.
	 */
	private IntSet branching(Grph g, IntSet vc)
	{
		// If the considered graph has no vertices or no edges, just return the
		// current cover
		if (g.getNumberOfVertices() == 0 || g.getNumberOfUndirectedSimpleEdges() == 0)
			return vc;

		// Isolated vertices are useless in a Vertex Cover
		g.removeVertices(g.getVerticesOfDegree(0));

		// If there is a vertex x with degree one, then choose its neighborhood
		// to be in the vertex cover
		// (for avoiding useless recursive calls, we remove all such vertices at
		// the same time.)
		IntSet deg1 = g.getVerticesOfDegree(1);

		if (deg1.size() > 0)
		{
			for (int v : deg1.toIntArray())
			{
				// We have to check if v is still in the graph, as it may have
				// been removed as the neighbor of
				// an other vertex of degree 1
				if (g.getVertices().contains(v))
				{
					IntSet nv = g.getNeighbours(v);
					vc.addAll(nv);
					g.removeVertices(nv);
					g.removeVertex(v);
				}
			}

			return branching(g, vc);
		}

		// Now branch on vertices on degree at least 5
		IntSet degSup5 = g.getVerticesOfDegreeAtLeast(5);
		if (degSup5.size() > 0)
		{
			// Pick one vertex
			int v = degSup5.toIntArray()[0];

			// Build two graphs: g1 := g \ {v}, and g2 := g \ N[v] and branch
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

			IntSet sol1 = branching(g1, vc1);
			IntSet sol2 = branching(g2, vc2);

			if (sol1.size() < sol2.size())
				return sol1;
			else
				return sol2;
		}

		// Note: a graph with 0 vertices is not considered regular
		if (g.isRegular())
		{
			// Pick one vertex
			int v = g.getVertices().toIntArray()[0];

			// Build two graphs: g1 := g \ {v}, and g2 := g \ N[v] and branch
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

			IntSet sol1 = branching(g1, vc1);
			IntSet sol2 = branching(g2, vc2);

			if (sol1.size() < sol2.size())
				return sol1;
			else
				return sol2;
		}

		// Branch on vertices on degree 2
		IntSet deg2 = g.getVerticesOfDegree(2);
		if (deg2.size() > 0)
		{
			// Pick a vertex x with degree 2
			int x = deg2.toIntArray()[0];

			// Let a and b its neighbors
			int[] Nx = g.getNeighbours(x).toIntArray();
			int a = Nx[0];
			int b = Nx[1];

			// If a and b are adjacent, add them to the vertex cover
			if (g.areVerticesAdjacent(a, b))
			{
				vc.add(a);
				vc.add(b);
				g.removeVertices(a, b, x);
				return branching(g, vc);
			}
			else
			{
				// Else if a and b are degree 2 and have an other common vertex
				// c, add x and c to the cover
				IntSet Na = g.getNeighbours(a);
				IntSet Nb = g.getNeighbours(b);
				Na.remove(x); // Because we look for a common neighbor of a and
				// b that is different of x
				Nb.remove(x); // Useless to restore x after

				if (g.getVertexDegree(a) == 2 && g.getVertexDegree(b) == 2 && !Na.isEmpty() && Na.equals(Nb))
				{
					int c = Na.toIntArray()[0];
					vc.add(x);
					vc.add(c);
					g.removeVertices(x, c);
					return branching(g, vc);
				}
				// Else branch according to N(x):={a,b} and N(a) U N(b)
				else
				{
					Grph g1 = g.clone();
					IntSet vc1 = vc.clone();
					g1.removeVertices(a, b, x); // We can remove x too
					vc1.addAll(a, b);

					Grph g2 = g.clone();
					IntSet vc2 = vc.clone();
					IntSet nab = IntSets.union(g2.getNeighbours(a), g2.getNeighbours(b));
					g2.removeVertices(nab);
					g2.removeVertices(a, b); // Now a and b are isolated, so we
					// can remove them
					vc2.addAll(nab);

					IntSet sol1 = branching(g1, vc1);
					IntSet sol2 = branching(g2, vc2);

					return sol1.size() < sol2.size() ? sol1 : sol2;
				}
			}
		}

		// Finally, branch on vertices of degree 3
		IntSet deg3 = g.getVerticesOfDegree(3);
		if (deg3.size() > 0)
		{
			// Pick a vertex x with degree 3
			int x = deg3.toIntArray()[0];

			// a, b and c are the neighbors of x
			int[] nx = g.getNeighbours(x).toIntArray();
			int a = nx[0];
			int b = nx[1];
			int c = nx[2];

			// Test if x is part of a triangle; if it is, variable t is
			// different of initial value -1
			int t = -1;
			if (g.areVerticesAdjacent(a, b))
			{
				t = c;
			}
			else if (g.areVerticesAdjacent(a, c))
			{
				t = b;
			}
			else if (g.areVerticesAdjacent(b, c))
			{
				t = a;
			}

			// If such a triangle exists, branch according to N(x) and N(t)
			if (t != -1)
			{
				Grph g1 = g.clone();
				IntSet vc1 = vc.clone();
				g1.removeVertices(nx);
				g1.removeVertex(x);
				vc1.addAll(nx);

				Grph g2 = g.clone();
				IntSet vc2 = vc.clone();
				IntSet nt = g2.getNeighbours(t);
				g2.removeVertices(nt);
				g2.removeVertex(t);
				vc2.addAll(nt);

				IntSet sol1 = branching(g1, vc1);
				IntSet sol2 = branching(g2, vc2);

				if (sol1.size() < sol2.size())
					return sol1;
				else
					return sol2;
			}

			// Test if x is part of a 4-cycle
			IntSet na = g.getNeighbours(a);
			IntSet nb = g.getNeighbours(b);
			IntSet nc = g.getNeighbours(c);

			IntSet n = null;

			// Temporarily removes x from na, nb, and nc to compute their other
			// common neighbors
			na.remove(x);
			nb.remove(x);
			nc.remove(x);

			if (!IntSets.intersection(na, nb).isEmpty())
				n = IntSets.intersection(na, nb);
			else if (!IntSets.intersection(na, nc).isEmpty())
				n = IntSets.intersection(na, nc);
			else if (!IntSets.intersection(nb, nc).isEmpty())
				n = IntSets.intersection(nb, nc);

			if (n != null)
			{
				// d is the 4th vertex of a 4-cycle containing x
				int d = n.toIntArray()[0];

				// We branch on N(x) and {x,d}
				Grph g1 = g.clone();
				IntSet vc1 = vc.clone();
				g1.removeVertices(nx);
				g1.removeVertex(x);
				vc1.addAll(nx);

				Grph g2 = g.clone();
				IntSet vc2 = vc.clone();
				g2.removeVertices(x, d); // we can remove a, b and c since they
				// are now isolated
				vc2.addAll(x, d);

				IntSet sol1 = branching(g1, vc1);
				IntSet sol2 = branching(g2, vc2);

				if (sol1.size() < sol2.size())
					return sol1;
				else
					return sol2;
			}

			// Last possible case: branching on N(x), N(a) and {a} U N(b) U N(c)
			Grph g1 = g.clone();
			IntSet vc1 = vc.clone();
			g1.removeVertices(nx);
			g1.removeVertex(x);
			vc1.addAll(nx);

			// Restore x in na, nb, and nc (previously removed)
			na.add(x);
			nb.add(x);
			nc.add(x);

			Grph g2 = g.clone();
			IntSet vc2 = vc.clone();
			g2.removeVertices(na);
			g2.removeVertex(a);
			vc2.addAll(na);

			Grph g3 = g.clone();
			IntSet vc3 = vc.clone();

			// In the following instruction, we have to compute the union,
			// because removing nb first, and
			// nc then may cause an error if a vertex of nc has been already
			// removed.
			g3.removeVertices(IntSets.union(nb, nc));

			vc3.addAll(a);
			vc3.addAll(nb);
			vc3.addAll(nc);

			IntSet sol1 = branching(g1, vc1);
			IntSet sol2 = branching(g2, vc2);
			IntSet sol3 = branching(g3, vc3);

			if (sol1.size() <= sol2.size() && sol1.size() <= sol3.size())
				return sol1;
			else if (sol2.size() < sol1.size() && sol2.size() < sol3.size())
				return sol2;
			else
				return sol3;
		}

		// It is possible that we only removed vertices of degree 0 or 1, and
		// thus never did a recursive
		// call. So the resulting graph is not necessarily empty, and we have to
		// restart on it
		return branching(g, vc);
	}

	public static void main(String[] args)
	{
		Grph g = new InMemoryGrph();
		g.grid(10, 10);
		IntSet r = new NiedermeierMinimumVertexCoverAlgorithm().compute(g);
		System.out.println(r);
	}
}
