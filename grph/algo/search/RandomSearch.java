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
 
 package grph.algo.search;

import grph.Grph;
import grph.Grph.DIRECTION;
import grph.algo.search.GraphSearchListener.DECISION;
import grph.algo.topology.ClassicalGraphs;

import java.util.Random;

import toools.set.IntSet;

public class RandomSearch
{
    public RandomSearch(Grph graph, int source, Grph.DIRECTION direction, Random prng, int numberOfIterations,
	    GraphSearchListener l)
    {
	assert graph != null;
	assert source >= 0;
	assert numberOfIterations >= 0;

	l.searchStarted();

	for (int i = 0; i < numberOfIterations; ++i)
	{
	    DECISION d = l.vertexFound(source);

	    if (d == null)
	    {
		throw new IllegalStateException("you need to decide something. Stop or continue?");
	    }
	    else if (d == DECISION.STOP)
	    {
		return;
	    }

	    IntSet out = graph.getNeighbours(source, direction);

	    if (out.isEmpty())
	    {
		break;
	    }
	    else
	    {
		source = out.pickRandomElement(prng);
	    }
	}

	l.searchStarted();

    }

    public static void main(String[] args)
    {
	Grph g = ClassicalGraphs.grid(5, 5);
	new RandomSearch(g, 0, DIRECTION.out, new Random(), 10000, new GraphSearchListener() {

	    @Override
	    public void searchStarted()
	    {
		// TODO Auto-generated method stub

	    }

	    @Override
	    public DECISION vertexFound(int v)
	    {
		System.out.println(v);

		if (v == 6)
		{
		    return DECISION.STOP;
		}

		return DECISION.CONTINUE;
	    }

	    @Override
	    public void searchCompleted()
	    {
		// TODO Auto-generated method stub

	    }
	});
    }

}
