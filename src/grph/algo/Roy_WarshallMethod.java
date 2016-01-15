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

package grph.algo;

import grph.Grph;
import grph.in_memory.InMemoryGrph;

public class Roy_WarshallMethod
{
	/**
	 * Create the transitive closure of the given graph.
	 * 
	 * @param source
	 *            The graph to transform.
	 * @return A new graph that is the transformed using the transitive closure
	 *         from the given parameter.
	 */
	public static Grph createTransitiveClosure(final Grph source)
	{
		if (source == null)
			return null;

		int[] verticesList = source.getVertices().toIntArray();

		Grph result = source.clone();

		if (verticesList.length < 3)
			return result;

		boolean changed = false;

		do
		{
			for (int x : verticesList)
			{
				for (int neighbor1 : result.getOutNeighbors(x).toIntArray())
				{
					for (int neighbor2 : result.getOutNeighbors(neighbor1).toIntArray())
					{
						if (result.getEdgesConnecting(x, neighbor2).size() == 0)
						{
							result.addDirectedSimpleEdge(x, neighbor2);
							changed = true;
						}
					}
				}
			}
		} while (changed);

		return result;
	}

	public static void main(String[] args)
	{
		Grph g = null;
		g = new InMemoryGrph();

		g.grid(3, 3, true, false, false);
		g.display();

		Grph g2;
		g2 = Roy_WarshallMethod.createTransitiveClosure(g);
		g2.display();
	}

}
