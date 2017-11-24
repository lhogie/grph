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

public class MessageHeader implements MemoryObject
{
    private final int source;
    private final IntSet targetNodes = new SelfAdaptiveIntSet();

    public MessageHeader(int source, int... recipients)
    {
	if (source < 0)
	    throw new IllegalArgumentException();

	this.source = source;
	targetNodes.addAll(recipients);
    }

    public int getSource()
    {
	return source;
    }

    public IntSet getTargetNodes()
    {
	return targetNodes;
    }

    @Override
    public int getMemoryFootprintInBits()
    {
	return 4 + targetNodes.size() * 4 + 4;
    }

    @Override
    public String toString()
    {
	return "   - source: " + source + "\n   - targetNodes: " + targetNodes;
    }
}
