/* (C) Copyright 2009-2013 CNRS (Centre National de la Recherche Scientifique).

Licensed to the CNRS under one
or more contributor license agreements.  See the NOTICE file
distributed with this work for additional information
regarding copyright ownership.  The CNRS licenses this file
to you under the Apache License, Version 2.0 (the
"License"); you may not use this file except in compliance
with the License.  You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing,
software distributed under the License is distributed on an
"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
KIND, either express or implied.  See the License for the
specific language governing permissions and limitations
under the License.

*/

/* Contributors:

Luc Hogie (CNRS, I3S laboratory, University of Nice-Sophia Antipolis) 
Aurelien Lancin (Coati research team, Inria)
Christian Glacet (LaBRi, Bordeaux)
David Coudert (Coati research team, Inria)
Fabien Crequis (Coati research team, Inria)
Grégory Morel (Coati research team, Inria)
Issam Tahiri (Coati research team, Inria)
Julien Fighiera (Aoste research team, Inria)
Laurent Viennot (Gang research-team, Inria)
Michel Syska (I3S, Université Cote D'Azur)
Nathann Cohen (LRI, Saclay) 
Julien Deantoin (I3S, Université Cote D'Azur, Saclay) 

*/
 
 

package grph.algo.topology.manet;

import cnrs.minides.DES;
import cnrs.minides.StdOutLogger;
import grph.algo.mobility.InSquareRandomMobility;

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
