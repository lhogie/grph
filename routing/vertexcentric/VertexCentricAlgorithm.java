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
 */package grph.routing.vertexcentric;

import java.util.LinkedList;
import java.util.List;

import toools.collections.AutoGrowingArrayList;
import grph.Grph;

public abstract class VertexCentricAlgorithm<M extends Message>
{
    private int time = 0;
    private AutoGrowingArrayList<List<M>> messageBoxes = new AutoGrowingArrayList<List<M>>();

    public int getTime()
    {
	return time;
    }


    public void compute(Grph g)
    {
	while (terminated())
	{
	    for (int v : g.getVertices().toIntArray())
	    {
		compute(v, g);
		time++;
	    }
	    
	    iterationOver();
	}
    }

    protected void iterationOver()
    {
    }


    public abstract void compute(int vertex, Grph g);

    protected abstract boolean terminated();

    protected M getNextMessageFor(int v)
    {
	List<M> box = messageBoxes.get(v);

	if (box == null || box.isEmpty())
	{
	    return null;
	}
	else
	{
	    return box.remove(box.size() - 1);
	}
    }

    public void postMessage(M m, int recipient)
    {
	List<M> box = messageBoxes.get(recipient);

	if (box == null)
	{
	    messageBoxes.add(recipient, box = new LinkedList<M>());
	}

	box.add(m);
    }

}
