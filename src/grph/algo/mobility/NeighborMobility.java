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
 
 package grph.algo.mobility;

import grph.Grph;
import toools.set.IntSet;
import cnrs.oodes.DES;

public class NeighborMobility extends MobilityModel
{
    public NeighborMobility(DES<Grph> des)
    {
	super(des);
    }

    @Override
    public double move(int v)
    {
	Grph g = getGrph();
	IntSet neighbors = g.getNeighbours(v);

	if (neighbors.size() >= 2)
	{
	    int n = neighbors.pickRandomElement(getPRNG());
	    g.disconnect(v, n);

	    neighbors.remove(n);
	    int m = neighbors.pickRandomElement(getPRNG());
	    g.addUndirectedSimpleEdge(m, n);
	}

	return getPRNG().nextDouble();
    }

    @Override
    public String toString(int v)
    {
	return "changing neighbors of v" + v + ". New neighhors are " + getGrph().getNeighbours(v);
    }

}
