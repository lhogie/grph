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

import grph.Grph;
import grph.routing.Message;
import grph.routing.RoutingDecision;
import grph.routing.RoutingProtocol;

public class BlindBroadcastingProtocol extends RoutingProtocol
{

    public BlindBroadcastingProtocol(Grph network, int networkNode)
    {
	super(network, networkNode);
    }

    /**
     * Sends the message through all links
     */
    @Override
    public RoutingDecision decide(Message p, int incomingNeighbor, int incomingEdges)
    {
	RoutingDecision r = new RoutingDecision();

	// if this routing node has not yet received this message
	if (!getReceivedMessages().contains(p))
	{
	    r.getTargetNodes().addAll(getNetwork().getOutNeighbours(getNetworkNode()));
	    r.getThroughLinks().addAll(getNetwork().getOutEdges(getNetworkNode()));
	}

	return r;
    }

}
