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
 
 package grph.topology.random.genetic;

import grph.Grph;

public class Demo
{
    public static void main(String[] args)
    {
	System.out.println("starting");

	GraphPopulation p = new GraphPopulation(new grph.in_memory.InMemoryGrph()) {

	    public double computeFitness(Grph g)
	    {
		if (g.isNull() || !g.isConnected() || g.hasMultipleEdges() || g.hasLoops()
			|| g.getNumberOfUndirectedEdges() != g.getNumberOfEdges())
		{
		    return Double.NEGATIVE_INFINITY;
		}
		else
		{
		    int ddiff = Math.abs(g.getTwoSweepBFSDiameterApproximatedDiameter() - 10);
		    int ndiff = Math.abs(g.getVertices().size() - 10);
		    double avgD = Math.abs(g.getAverageDegree() - 10);
		    return -(ddiff + ndiff + avgD);
		}
	    }
	};

	p.expansion(30);
	p.setAllowsAsynchronousUpdates(true);
	// p.monitor();

	while (true)
	{
	    if (p.makeNewGeneration())
	    {
		System.out.println(p);

		if (p.get(0).fitness[0] == 0)
		{
		    break;
		}
	    }
	}

	System.out.println("completed");
	p.get(0).object.display();
    }

}
