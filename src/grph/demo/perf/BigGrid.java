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

package grph.demo.perf;

import grph.Grph;
import toools.StopWatch;

import com.carrotsearch.hppc.cursors.IntCursor;

public class BigGrid
{
	public static void main(String[] args)
	{
		int n = 1000;
		Grph g = new grph.in_memory.InMemoryGrph();
		StopWatch sw = new StopWatch();
		g.grid(n, n);
		System.out.println(g);
		System.out.println("*** " + sw.getElapsedTime() + "ms");
		sw.reset();

		int sum = 0;

		for (IntCursor c : g.getVertices())
		{
			sum += c.value;
		}

		System.out.println(sum);
		System.out.println("*** " + sw.getElapsedTime() + "ms");
		sw.reset();
		g.bfs(45);
		System.out.println("bfs ok");
		System.out.println("*** " + sw.getElapsedTime() + "ms");
	}
}
