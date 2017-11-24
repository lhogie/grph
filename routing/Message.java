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

import toools.MemoryObject;
import cnrs.grph.set.SelfAdaptiveIntSet;
import cnrs.grph.set.IntSet;

public class Message implements MemoryObject
{
    private static int numberOfInstances = 0;


    private final int id = numberOfInstances++;
    private final MessageHeader header;
    private MemoryObject content;

    // the set of recipient nodes that have already been reached
    private final IntSet reachedRecipients = new SelfAdaptiveIntSet();

    

    public Message(int source, int... recipients)
    {
	this(new MessageHeader(source, recipients));
    }

    public IntSet getReachedRecipients()
    {
	return reachedRecipients;
    }

    
    public MemoryObject getContent()
    {
	return content;
    }

    public void setContent(MemoryObject content)
    {
	this.content = content;
    }

    public int getId()
    {
	return id;
    }

    public MessageHeader getHeader()
    {
	return header;
    }

    public Message(MessageHeader header)
    {
	this.header = header;
    }

    public int getID()
    {
	return id;
    }

    @Override
    public int getMemoryFootprintInBits()
    {
	int sz = 0;

	// adds the size of the header, if any
	sz += header.getMemoryFootprintInBits();

	// adds the size of the content, if any
	sz += content == null ? 0 : content.getMemoryFootprintInBits();

	return sz;
    }

    @Override
    public String toString()
    {
	return "Message " + id + ":\n  Header: \n" + getHeader() + "\n  Content: " + content;
    }

    public boolean isDelivered()
    {
	return getReachedRecipients().equals(getHeader().getTargetNodes());
    }
}
