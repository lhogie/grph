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

import grph.Grph;
import grph.algo.topology.ClassicalGraphs;
import grph.properties.ObjectProperty;

public class Demo
{
    public static void main(String[] args)
    {

	VertexCentricAlgorithm<DemoMessage> a = new VertexCentricAlgorithm<DemoMessage>() {

	    @Override
	    protected boolean terminated()
	    {
		return getTime() == 100;
	    }


	    
	    @Override
	    public void compute(int v, Grph g)
	    {
		while (true)
		{
		    DemoMessage msg = getNextMessageFor(v);

		    if (msg == null)
		    {
			break;
		    }

		    int a = msg.degree;
		}

		for (int n : g.getOutNeighbours(v).toIntArray())
		{
		    DemoMessage m = new DemoMessage();
		    m.degree = g.getNeighbours(v).size();
		    postMessage(m, n);
		}
	    }
	};

	Grph g = ClassicalGraphs.PetersenGraph();
	a.compute(g);
    }

    static class DemoMessage extends Message
    {
	public int degree;
    }
}
