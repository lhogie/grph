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

package grph.algo.topology;

import grph.Grph;
import grph.in_memory.InMemoryGrph;
import toools.set.DefaultIntSet;
import toools.set.IntSet;

public class RandomTreeTopologyGenerator extends RandomizedTopologyTransform
{
	private boolean directed = false;

	public boolean isDirected()
	{
		return directed;
	}

	public void setDirected(boolean directed)
	{
		this.directed = directed;
	}

	@Override
	public void compute(Grph g)
	{
		IntSet connectedV = new DefaultIntSet();
		IntSet unconnectedV = g.getVertices().clone();
		int first = unconnectedV.pickRandomElement(getPRNG());
		connectedV.add(first);
		unconnectedV.remove(first);

		while (!unconnectedV.isEmpty())
		{
			int someVertex = unconnectedV.pickRandomElement(getPRNG());
			int someOtherVertex = connectedV.pickRandomElement(getPRNG());
			g.addSimpleEdge(someVertex, someOtherVertex, isDirected());
			unconnectedV.remove(someVertex);
			connectedV.add(someVertex);
		}
	}

	public static void compute(Grph g, int numberOfVertices)
	{
		g.ensureNVertices(numberOfVertices);
		new RandomTreeTopologyGenerator().compute(g);
	}

	public static void main(String[] args)
	{
		Grph g = new InMemoryGrph();
		g.addNVertices(10);
		compute(g, 100);
		g.display();
	}

}
