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

package grph.algo.structural.cliquer;

import grph.Grph;
import grph.GrphAlgorithm;
import grph.algo.topology.ClassicalGraphs;

import java.util.Collection;

import toools.set.IntSet;

public class FindAllCliques extends GrphAlgorithm<Collection<IntSet>>
{

	@Override
	public Collection<IntSet> compute(Grph g)
	{
		return Cliquer.compute(g, 1, Integer.MAX_VALUE, true);
	}

	public static void main(String[] args)
	{
		Grph g = ClassicalGraphs.grid(3, 3);
		Collection<IntSet> r = new FindAllCliques().compute(g);

		for (IntSet s : r)
		{
			System.out.println(s);

		}
	}
}
