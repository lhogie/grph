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

import java.util.Random;

import toools.set.DefaultIntSet;
import toools.set.IntHashSet;
import toools.set.IntSet;

public abstract class RandomTraversalButVisitOnlyOnce implements GraphSearchListener
{

    public RandomTraversalButVisitOnlyOnce(Grph graph, int v, Grph.DIRECTION direction, Random prng, int numberOfVisits)
    {
	assert graph != null;
	assert v > 0;
	assert numberOfVisits >= 0 : "negative number of visits";

	searchStarted();
	IntSet visited = new DefaultIntSet();

	for (int i = 0;; ++i)
	{
	    if (vertexFound(v) == DECISION.STOP)
	    {
		break;
	    }

	    visited.add(v);
	    IntSet neighbors = new IntHashSet();
	    neighbors.addAll(graph.getNeighbours(v, direction));
	    neighbors.removeAll(visited);

	    if (neighbors.isEmpty())
	    {
		return;
	    }

	    v = neighbors.pickRandomElement(prng);
	}
	
	searchCompleted();
    }

}
