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

import grph.Grph;
import cnrs.grph.set.IntSet;
import cnrs.oodes.DES;

public class NewMessageInQueueEvent extends MessageRelatedEvent
{
    private final int node;
    private final int sendingNode;
    private final int incomingLink;

    public NewMessageInQueueEvent(DES<Grph> simulation, double date, Message message, int sendingNode, int link,
	    int receivingNode)
    {
	super(simulation, date, message);

	if (receivingNode < 0)
	    throw new IllegalArgumentException();

	this.node = receivingNode;
	this.sendingNode = sendingNode;
	this.incomingLink = link;
    }

    public int getNode()
    {
	return node;
    }

    @Override
    protected void execute()
    {
	DES<Grph> s = getSimulation();

	RoutingProtocol routingProtocol = (RoutingProtocol) s.getSystem().getObjectVertexProperty().getValue(node);

	// if the message has reached a recipient node
	if (getMessage().getHeader().getTargetNodes().contains(node))
	{
	    getMessage().getReachedRecipients().add(node);
	}

	// if ALL recipients have been reached
	if (getMessage().isDelivered())
	{
	    getSimulation().stop();
	}
	else
	{
	    // if the message was not received in the past or if the
	    // protocol allows multiple processing
	    if (!routingProtocol.getReceivedMessages().contains(getMessage())
		    || routingProtocol.isReconsiderMessageUponMultipleReceptions())
	    {
		// asks the router what to do with the message
		RoutingDecision routingDecision = routingProtocol.decide(getMessage(), sendingNode, incomingLink);

		// retry later if asked
		if (routingDecision.getTargetNodes().contains(node) && routingProtocol.allowsPostponing())
		{
		    double postoneDate = getOccurenceDate() + routingDecision.getDelay();
		    getSimulation().getEventQueue().add(
			    new NewMessageInQueueEvent(s, postoneDate, getMessage(), node, -1, node));

		    routingDecision.getTargetNodes().remove(node);
		}
		else
		{
		    throw new IllegalStateException("this routing protocol does not allows postponing");
		}

		// forward to neighbors
		for (int link : routingDecision.getThroughLinks().toIntArray())
		{
		    IntSet neighbors = s.getSystem().getVerticesAccessibleThrough(node, link);

		    for (int neighbor : neighbors.toIntArray())
		    {
			// picks up a delay between 0 and 1s
			double trafficDelay = s.getPRNG().nextDouble();
			double date = s.getTime() + routingDecision.getDelay() + trafficDelay;

			// schedule the future remote reception
			getSimulation().getEventQueue().add(
				new NewMessageInQueueEvent(s, date, getMessage(), node, link, neighbor));
		    }
		}

		// marks this message has "received"
		routingProtocol.getReceivedMessages().add(getMessage());
	    }
	}
    }

    @Override
    public String toString()
    {
	StringBuilder b = new StringBuilder();
	b.append("new msg in queue: ");

	if (sendingNode >= 0)
	{
	    b.append(sendingNode);
	    b.append(" > ");
	}

	b.append("m" + getMessage().getID());
	b.append(" > ");
	b.append(node);

	if (incomingLink >= 0)
	{
	    b.append("(via link ");
	    b.append(incomingLink);
	    b.append(')');
	}

	return "message " + getMessage().getID() + " arrived in queue of router " + node;
    }

}
