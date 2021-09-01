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
Julien Deantoni (I3S, Université Cote D'Azur, Saclay) 

*/
 
 

package grph.demo;

import java.io.FileNotFoundException;

import grph.Grph;
import grph.path.ArrayListPath;

public class CreatePath
{
	public static void main(String[] args) throws FileNotFoundException
	{
		ClassLoader.getSystemClassLoader().setDefaultAssertionStatus(true);
		Grph g = new grph.in_memory.InMemoryGrph();
		g.grid(3, 3);
		System.out.println(g);
		System.out.println(g.toGrphText());
		System.out.println(g.getNeighbours(0));
		ArrayListPath p = new ArrayListPath();
		p.extend(1);
		p.extend(2);
		p.extend(3);
		p.extend(4);
		p.extend(9);
		p.extend(14);
		p.setColor(g, 3);
		g.display();

		System.out.println("is applicable: " + p.isApplicable(g));
		System.out.println("is shortest: " + p.isShortestPath(g, null));
	}
}
