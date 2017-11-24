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

import java.util.List;

public abstract class RoutingProtocol
{
    // the list of messages that were already received by this node
//    private final List<Message> receivedMessages = new ArrayList<Message>();
    
    private boolean reconsiderMessageUponMultipleReceptions = false;
    private boolean allowsPostponing = false;

    
    public boolean isReconsiderMessageUponMultipleReceptions()
    {
	return reconsiderMessageUponMultipleReceptions;
    }

    public void setReconsiderMessageUponMultipleReceptions(boolean reconsiderMessageUponMultipleReceptions)
    {
	this.reconsiderMessageUponMultipleReceptions = reconsiderMessageUponMultipleReceptions;
    }

 /*

    public List<Message> getReceivedMessages()
    {
	return receivedMessages;
    }
*/

    public boolean allowsPostponing()
    {
	return allowsPostponing;
    }


    public abstract RoutingDecision decide(int node, Message p, int incomingNeighbor, int incomingEdge);
}
