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
 */package grph.routing.protocol;

import cnrs.grph.set.IntSet;
import toools.math.IntIterator;
import grph.Grph;
import grph.routing.Message;
import grph.routing.RoutingDecision;
import grph.routing.RoutingProtocol;

public abstract class DistanceBasedRoutingProtocol extends RoutingProtocol
{

    public DistanceBasedRoutingProtocol(Grph network, int networkNode)
    {
	super(network, networkNode);
    }

    /**
     * Sends the message through all links
     */
    @Override
    public RoutingDecision decide(Message m, int incomingNeighbor, int incomingEdges)
    {
	RoutingDecision r = new RoutingDecision();

	for (int recipient : m.getHeader().getTargetNodes().toIntArray())
	{
	    int n = findNeighborClosestTo(recipient);
	    IntSet links = getNetwork().getEdgesConnecting(getNetworkNode(), n);
	    r.getTargetNodes().addAll(n);
	    r.getThroughLinks().addAll(links);
	}

	return r;
    }

    protected int findNeighborClosestTo(int recipient)
    {
	int closestNode = getNetworkNode();
	int minDistance = Integer.MAX_VALUE;
	IntIterator i = getNetwork().getNeighbours(getNetworkNode()).iteratorPrimitive();

	while (i.hasNext())
	{
	    int n = i.next();
	    int distance = computeDistance(recipient, n);

	    // if we have a valid distance, that is lower that the the minimum
	    // one we have found so far
	    if (distance >= 0 && distance < minDistance)
	    {
		minDistance = distance;
		closestNode = n;
	    }
	}

	return closestNode;
    }

    protected abstract int computeDistance(int n1, int n2);

}
