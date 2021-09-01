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

package grph.algo.topology.gsm;

import java.io.IOException;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import grph.Grph;
import grph.algo.search.SearchResult;
import grph.algo.topology.AsymmetricTopologyGenerator;
import grph.in_memory.InMemoryGrph;
import grph.properties.NumericalProperty;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntSet;
import toools.collections.primitive.LucIntSet;
import toools.collections.primitive.SelfAdaptiveIntSet;
import toools.math.MathsUtilities;

public class WirelessBackhaul
{
	Grph g;
	Set<BSC> bscs = new HashSet<BSC>();
	NumericalProperty edgeCapacities = new NumericalProperty("edge capacities", 32, 1);

	public WirelessBackhaul(int numberOfBTSs, int numberOfBSCs, Random prng,
			double density)
	{
		g = new InMemoryGrph();
		int side = (int) Math.sqrt(numberOfBTSs);
		g.dgrid(side, side);
		new AsymmetricTopologyGenerator().compute(g);
		degenerate(g, g.getVertices().pickRandomElement(prng), density);
		bscs.addAll(addBSCs(g, numberOfBSCs, prng));

		for (int e : g.getEdges().toIntArray())
		{
			// int distanceOfBSC = findBSC(e);
			// System.out.println(distanceOfBSC);
			// edgeCapacities.setValue(e, 100+200-distanceOfBSC);
			edgeCapacities.setValue(e,
					(int) MathsUtilities.pickRandomBetween(100, 200, prng));
		}

		for (BSC bsc : bscs)
		{
			g.getVertexColorProperty().setValue(bsc.id, 12);
			g.getVertexSizeProperty().setValue(bsc.id, 20);

			for (Operator o : bsc.operators)
			{
				g.getVertexColorProperty().setValue(o.id, 5);
				g.getVertexShapeProperty().setValue(o.id, 0);
			}
		}
	}

	/**
	 * Degenerates the given graph. Any connected graph is accepted.
	 * 
	 * @param g
	 * @param ratioOfEdgesToBeDeleted
	 */
	public static void degenerate(Grph g, int source, double ratioOfEdgesToBeDeleted)
	{
		Random r = new Random();
		int attempts = 0;
		final int maxNumberOfAttempts = g.getNumberOfVertices() * 10;
		SearchResult bfs = g.bfs(source);
		int[] distances = bfs.distances;
		int maxDistance = bfs.maxDistance();
		int initialNbEdges = g.getEdges().size();
		int targetNbEdges = (int) Math.round(initialNbEdges * ratioOfEdgesToBeDeleted);

		// while we still have too many edges
		while (g.getEdges().size() > targetNbEdges)
		{
			int e = g.getEdges().pickRandomElement(r);
			int t = g.getDirectedSimpleEdgeTail(e);

			double distanceRelativeToMaxDistance = distances[t] / (double) maxDistance;

			// the farther is the node, the greater the probability to
			// degeneration
			if (r.nextDouble() < distanceRelativeToMaxDistance)
			{
				int h = g.getDirectedSimpleEdgeHead(e);
				LucIntSet oppositeEdges = g.getOppositeEdges(e);
				g.removeEdge(e);
				g.removeEdge(oppositeEdges.getGreatest());

				if (oppositeEdges.size() != 1)
					throw new IllegalStateException();

				if ( ! g.isStronglyConnected())
				{
					g.addDirectedSimpleEdge(t, h);
					g.addDirectedSimpleEdge(h, t);

					if (++attempts == maxNumberOfAttempts)
						throw new IllegalArgumentException(
								"cannot reduce that much the density while maintaining the network strongly connected");
				}
			}
		}
	}

	public static Set<BSC> addBSCs(Grph g, int n, Random prng)
	{
		Set<BSC> r = new HashSet<BSC>();
		IntSet BSCids = new SelfAdaptiveIntSet(n);

		for (int i = 0; i < n; ++i)
		{
			int gtw = g.addVertex();
			r.add(new BSC(gtw));

			int attachementBTS = BSCids.isEmpty() ? 0 : farther(g, BSCids);
			BSCids.add(gtw);

			g.addDirectedSimpleEdge(attachementBTS, gtw);
			g.addDirectedSimpleEdge(gtw, attachementBTS);
		}

		return r;
	}

	public static int farther(Grph g, IntSet s)
	{
		if (s.isEmpty())
			throw new IllegalArgumentException();

		Int2ObjectMap<int[]> m = new Int2ObjectOpenHashMap<int[]>();

		for (int v : s.toIntArray())
		{
			m.put(v, g.bfs(v).distances);
		}

		int maxComposedDistance = 0;
		int farthest = - 1;

		for (int v : g.getVertices().toIntArray())
		{
			int composedDistance = composedDistance(g, v, s, m);

			if (composedDistance > maxComposedDistance)
			{
				farthest = v;
				maxComposedDistance = composedDistance;
			}
		}

		return farthest;

	}

	private static int composedDistance(Grph g2, int v, IntSet s, Int2ObjectMap<int[]> m)
	{
		int sum = 0;

		for (int source : s.toIntArray())
		{
			sum += m.get(source)[v];
		}

		return sum;
	}

	public static void main(String[] args) throws IOException
	{

	}

	IntSet bscIDs()
	{
		IntSet r = new SelfAdaptiveIntSet(bscs.size());

		for (BSC b : bscs)
		{
			r.add(b.id);
		}

		return r;
	}
}
