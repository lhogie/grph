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
 
 package grph.demo;

import grph.Grph;
import grph.algo.distance.PageRank;

import java.util.Random;

import toools.io.Utilities;
import toools.set.DefaultIntSet;
import toools.set.IntSet;


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

	IntSet maxClique = new DefaultIntSet();

	while (true)
	{
	    g.highlightVertices(maxClique, 0);

	    int e = g.getEdges().pickRandomElement(r);
	    g.removeEdge(e);
	    int v1 = g.getVertices().pickRandomElement(r);
	    int v2 = g.getVertices().pickRandomElement(r, g.getNeighbours(v1), false);
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
