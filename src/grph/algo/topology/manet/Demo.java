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
 */

package grph.algo.topology.manet;

import grph.algo.mobility.InSquareRandomMobility;
import cnrs.oodes.DES;
import cnrs.oodes.StdOutLogger;

public class Demo
{

	public static void main(String[] args)
	{
		// a graph whose the topology reacts upon the mobility of the nodes
		Manet manet = new Manet();
		manet.addNVertices(100);

		// communication range is set to 50m
		manet.setRange(50);

		// the object that manages time
		DES<Manet> des = new DES<Manet>(manet);
		des.getListeners().add(new StdOutLogger());

		// nodes move in a 1000x1000m square
		manet.setMobilityModel(new InSquareRandomMobility<Manet>(des, 1000));

		// start the simulation
		des.run();
	}
}
