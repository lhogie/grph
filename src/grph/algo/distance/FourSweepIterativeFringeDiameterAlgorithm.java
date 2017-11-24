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
import grph.algo.search.BFSAlgorithm;
import grph.algo.search.SearchResult;
import grph.in_memory.InMemoryGrph;

/**
 * Computes the diameter of the graph using the heuristic algorithm proposed by
 * Pierluigi Crescenzi, Roberto Grossi, Michel Habib, Leonardo Lanzi and Andrea
 * Marino in &ldquo;On computing the diameter of real-world undirected
 * graphs&rdquo;. The result is always exact. This heuristic appears to take
 * linear time on many real world graphs. Its worst case time complexity is
 * O(nm).
 * 
 * Assumes that the graph is undirected. It picks a random node and returns the
 * diameter of its connected component (probably the giant connected component
 * if there is one).
 * 
 */
public class FourSweepIterativeFringeDiameterAlgorithm extends GrphAlgorithm<Integer>
{

	@Override
	public Integer compute(Grph g)
	{
		int r = g.getVertices().pickRandomElement(new Random());

		// Sweep 1
		BFSSweep sweep = new BFSSweep(g, r);
		BFSSweep minEcc = sweep;

		// Sweep 2
		sweep = new BFSSweep(g, sweep.farthestVertex);
		sweep.checkSameComponentSize(minEcc);
		// eccentricity >= sweep1.eccentricity
		BFSSweep maxEcc = sweep;

		// Sweep 3
		sweep = new BFSSweep(g, sweep.centerVertex);
		sweep.checkSameComponentSize(minEcc);

		if (sweep.eccentricity < minEcc.eccentricity)
		{
			minEcc = sweep;
		}
		else if (sweep.eccentricity > maxEcc.eccentricity)
		{ // intuitively no, but could be possible
			maxEcc = sweep;
		}

		// Sweep 4
		sweep = new BFSSweep(g, sweep.farthestVertex);
		sweep.checkSameComponentSize(minEcc);
		// eccentricity >= sweep3.eccentricity
		if (sweep.eccentricity > maxEcc.eccentricity)
		{
			maxEcc = sweep;
		}

		// Explore the fringe
		int diamInf = maxEcc.eccentricity;
		int diamSup = 2 * minEcc.eccentricity; // TODO: compute the diameter of
		// minEcc BFS tree, can be
		// smaller
		assert minEcc != maxEcc;
		int nbSweeps = 4;

		while (diamInf < diamSup)
		{
			int iLast = minEcc.bfsResult.visitOrder.size() - 1;
			int iFringe = iLast;

			while (iFringe > 0
					&& 2 * minEcc.bfsResult.distances[minEcc.bfsResult.visitOrder
							.getInt(iFringe)] > diamInf)
			{
				iFringe--;
			}

			// System.err.print("At most " + (iLast - iFringe) + " remaining
			// sweeps. diameter is in [" + diamInf + ", "
			// + diamSup + "]\r");

			int v = minEcc.bfsResult.visitOrder.getInt(iLast);
			//before was "HPPC resize()"
			minEcc.bfsResult.visitOrder.ensureCapacity(iLast);
			sweep = new BFSSweep(g, v);
			nbSweeps++;
			if (sweep.eccentricity > maxEcc.eccentricity)
			{
				maxEcc = sweep;
				diamInf = maxEcc.eccentricity;
			}
			int distAboveFringe = minEcc.bfsResult.distances[minEcc.bfsResult.visitOrder
					.getInt(iLast - 1)];
			diamSup = Math.max(maxEcc.eccentricity, 2 * distAboveFringe);
		}

		// System.err.println("\nNumber of sweeps : " + nbSweeps);
		return diamInf; // == diamSup
	}

	public static void main(String[] args)
	{
		int n = 100;
		if (args.length >= 1)
		{
			n = Integer.parseInt(args[0]);
		}

		Grph g;
		int diam;

		g = new InMemoryGrph();
		g.addNVertices(n * 100);
		new grph.algo.topology.RandomNewmanWattsStrogatzTopologyGenerator().compute(g, 5,
				.2);
		System.out.println("diameter of NewmanWattsStrogatz(" + (n * 100) + ",5,.2)");
		diam = new FourSweepIterativeFringeDiameterAlgorithm().compute(g);
		System.out.println(diam);

		g = new InMemoryGrph();
		g.addNVertices(n * 10);
		new grph.algo.topology.GNPTopologyGenerator().compute(g, .01);
		System.out.println("diameter of GNP(" + (n * 10) + ",.01)");
		diam = new FourSweepIterativeFringeDiameterAlgorithm().compute(g);
		System.out.println(diam);

		g = new InMemoryGrph();
		g.grid(n, n);
		System.out.println("diameter of grid " + n + "x" + n + ":");
		diam = new FourSweepIterativeFringeDiameterAlgorithm().compute(g);
		System.out.println(diam);
		assert diam == 2 * (n - 1);

		g = g.getLineGraph();
		System.out.println("diameter of line graph of grid :");
		diam = new FourSweepIterativeFringeDiameterAlgorithm().compute(g);
		System.out.println(diam);
		assert diam == 2 * (n - 1) - 1;

		g = new InMemoryGrph();
		g.addNVertices(n * n);
		new grph.algo.topology.RingTopologyGenerator().compute(g);
		System.out.println("diameter of Ring(" + (n * n) + ")");
		diam = new FourSweepIterativeFringeDiameterAlgorithm().compute(g);
		System.out.println(diam);
		assert diam == (n * n + 1) / 2;
	}

	class BFSSweep
	{
		int source;
		SearchResult bfsResult;
		int componentSize;
		int eccentricity;
		int farthestVertex;
		int centerVertex;

		BFSSweep(Grph g, int s)
		{
			source = s;
			bfsResult = new BFSAlgorithm().compute(g, source);
			componentSize = bfsResult.visitOrder.size();
			farthestVertex = bfsResult.visitOrder.get(componentSize - 1);
			eccentricity = bfsResult.distances[farthestVertex];
			// The possible center is at mid-distance between the source and the
			// farthest vertex.
			centerVertex = farthestVertex;
			for (int d = eccentricity / 2; d > 0; d--)
			{
				centerVertex = bfsResult.predecessors[centerVertex];
			}
		}

		void checkSameComponentSize(BFSSweep b)
		{
			if (b.componentSize != componentSize)
			{
				throw new IllegalStateException("Component size have changed : "
						+ componentSize + " != " + b.componentSize
						+ ". May be the graph is not symmetric.");
			}
		}
	}
}
