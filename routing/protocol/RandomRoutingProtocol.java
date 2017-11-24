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

import java.util.Random;

import cnrs.grph.set.IntSet;

public class RandomRoutingProtocol extends RoutingProtocol
{

    private Random prng = new Random();

    public RandomRoutingProtocol(Grph network, int networkNode, Random prng)
    {
	super(network, networkNode);
	this.prng = prng;
    }

    /**
     * Sends the message through a random outgoing link.
     */
    @Override
    public RoutingDecision decide(Message p, int incomingNeighbor, int incomingEdge)
    {
	IntSet outEdges = getNetwork().getOutNeighbours(getNetworkNode());
	RoutingDecision rd = new RoutingDecision();
	int outLink = outEdges.pickRandomElement(prng);
	rd.getThroughLinks().add(outLink);
	rd.getTargetNodes().add(getNetwork().getTheOtherVertex(outLink, getNetworkNode()));
	return rd;
    }

}
