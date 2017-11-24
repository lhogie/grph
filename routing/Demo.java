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
import grph.routing.protocol.RandomRoutingProtocol2;

import java.util.Random;

import cnrs.oodes.DES;

public class Demo
{

    public static void main(String[] args)
    {
	Grph g = new Grph();
	g.grid(5, 5);
	g.display();
	DES<Grph> s = new DES(g);


	new RandomRoutingProtocol2(g, n, new Random())
	for (int n : g.getVertices().toIntArray())
	{
//	    g.getObjectVertexProperty().setValue(n, new GeodesicDistanceBasedRoutingProtocol(g, n));
	    g.getObjectVertexProperty().setValue(n, );
	}

	// the message first starts from node 0 at time 0
	int src = 0;
	int dest = g.getVertices().getGreatest();
	s.getEventQueue().add(new NewMessageInQueueEvent(s, 0, new Message(src, dest), -1, -1, src));

	// starts the simulation
	s.run();
    }
}
