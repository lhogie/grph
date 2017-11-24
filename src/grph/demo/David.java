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
 
 package grph.demo;

import java.util.Random;

import grph.Grph;
import grph.algo.distance.PageRank;
import it.unimi.dsi.fastutil.ints.IntSet;
import toools.collections.primitive.SelfAdaptiveIntSet;
import toools.collections.LucIntSets;
import toools.collections.primitive.LucIntSet;
import toools.io.Utilities;


public class David
{
    public static void main(String[] args)
    {
	ClassLoader.getSystemClassLoader().setDefaultAssertionStatus(true);
	Random r = new Random();

	Grph g = new grph.in_memory.InMemoryGrph();
	// step("creation of an empty graph");

	// System.exit(0);
	g.display();

	// step("generation d'un graphe");
	g.addNVertices(30);
	g.glp();

	IntSet maxClique = new SelfAdaptiveIntSet();

	while (true)
	{
	    g.highlightVertices(maxClique, 0);

	    int e = g.getEdges().pickRandomElement(r);
	    g.removeEdge(e);
	    int v1 = g.getVertices().pickRandomElement(r);
	    int v2 = LucIntSets.pickRandomInt(g.getVertices(), r, g.getNeighbours(v1), false);
	    g.addUndirectedSimpleEdge(v1, v2);
	    
	    maxClique = g.getMaximumClique();
	    g.highlightVertices(maxClique);

	    PageRank pr = g.getPageRanking(r);
	    pr.compute();
	    pr.render();

//	    Threads.sleep(500);
	}

    }

    private static void step(String s)
    {
	Utilities.readUserInput("\n# " + s, ".*");

    }
}
