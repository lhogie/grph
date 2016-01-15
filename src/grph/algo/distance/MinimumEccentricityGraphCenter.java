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

package grph.algo.distance;

import grph.Grph;
import grph.GrphAlgorithm;
import grph.in_memory.InMemoryGrph;
import grph.properties.NumericalProperty;
import toools.math.IntIterator;

import com.carrotsearch.hppc.cursors.IntCursor;

public class MinimumEccentricityGraphCenter extends GrphAlgorithm<Integer>
{
	@Override
	public Integer compute(Grph g)
	{
		IntIterator i = g.getVertices().iteratorPrimitive();
		int v = i.next();
		int ecc = g.getEccentricity(v, null);

		while (i.hasNext())
		{
			int vv = i.next();
			int cc = g.getEccentricity(vv, null);

			if (cc < ecc)
			{
				v = vv;
				ecc = cc;
			}
		}

		return v;
	}

	public static void main(String[] args)
	{
		Grph g = new InMemoryGrph();
		// g.rnws(50, 3, 0.1);
		g.grid(10, 10);
		int center = new MinimumEccentricityGraphCenter().compute(g);
		g.highlightVertex(center, 5);
		NumericalProperty minEccProperty = new NumericalProperty(
				"minium vertex eccentricity", 16, 0);
		for (IntCursor c : g.getVertices())
		{
			minEccProperty.setValue(c.value, g.getEccentricity(c.value, null));
		}

		g.setVerticesLabel(minEccProperty);
		g.display();

	}
}
