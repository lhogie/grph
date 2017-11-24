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
 */package grph.routing;

import cnrs.grph.set.SelfAdaptiveIntSet;
import cnrs.grph.set.IntSet;

public class RoutingDecision
{
    private final IntSet neighbors = new SelfAdaptiveIntSet();
    private final IntSet throughLinks = new SelfAdaptiveIntSet();
    private int delay = 0;

    public int getDelay()
    {
	return delay;
    }

    public void setDelay(int delay)
    {
	if (delay < 0)
	    throw new IllegalArgumentException("invalid delay: " + delay);

	this.delay = delay;
    }

    public IntSet getTargetNodes()
    {
	return neighbors;
    }

    public IntSet getThroughLinks()
    {
	return throughLinks;
    }

    @Override
    public String toString()
    {
	return "targetNodes = " + neighbors + ", throughLinks = " + throughLinks;
    }
}
