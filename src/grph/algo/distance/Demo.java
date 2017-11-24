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
 
 package grph.algo.distance;

import java.util.Random;

import grph.Grph;
import grph.GrphAlgorithm;
import grph.in_memory.InMemoryGrph;


public abstract class Demo extends GrphAlgorithm<PageRank>
{
    public static void main(String[] args)
    {
	Grph g = new InMemoryGrph();
	g.grid(1000, 1000);
	PageRank rank = new PageRank(g, new Random());
//	g.display();

//	while (true)
	{
	    System.out.println("ok");
//	    rank.iterate(1, g.getNumberOfVertices());
	    rank.iterate(1, 100);
	    System.out.println("ok");
//	    rank.render();
//	    Threads.sleep(500);
//	    g.performRandomTopologicalChange(new Random());
	}
	
//	rank.getDistribution().display();
	
	
    }
}
