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
 
 package grph.algo.search;
import grph.Grph;
import grph.GrphAlgorithm;
import grph.algo.MultiThreadProcessing;
import grph.algo.distance.DistanceMatrixBasedDiameterAlgorithm;
import grph.in_memory.InMemoryGrph;
import toools.StopWatch;
import toools.math.MathsUtilities;

/**
 * compute the diameter via n*bfs without storing the matrices
 * 
 * @author lhogie
 * 
 */
public class DistanceListsBasedDiameterAlgorithm extends GrphAlgorithm<Integer>
{
    class A
    {
	int n = -1;
    }

    @Override
    public Integer compute(final Grph graph)
    {
	assert graph != null;
	final A a = new A();

	new MultiThreadProcessing(graph.getVertices()) {

	    @Override
	    protected void run(int threadID, int source)
	    {
	    	SearchResult r = new BFSAlgorithm().compute(graph, source);
		int max = MathsUtilities.max(r.distances);

		if (max > a.n)
		{
		    a.n = max;
		}
	    }
	};

	return a.n;
    }

    public static void main(String[] args)
    {
	Grph g = new InMemoryGrph();
	g.getOutNeighborhoods();
	int n = 50;
	g.grid(n, n);
	StopWatch sw = new StopWatch();
	System.out.println(new DistanceMatrixBasedDiameterAlgorithm().compute(g));
	System.out.println(sw.getElapsedTime() + "ms");
	sw.reset();
	System.out.println(new DistanceListsBasedDiameterAlgorithm().compute(g));
	System.out.println(sw.getElapsedTime() + "ms");
    }
}
