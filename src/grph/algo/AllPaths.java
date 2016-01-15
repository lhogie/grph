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

package grph.algo;

import grph.Grph;
import grph.GrphAlgorithm;
import grph.algo.topology.ClassicalGraphs;
import grph.in_memory.InMemoryGrph;
import grph.path.Path;
import grph.path.PathExtender;
import grph.path.SingletonPath;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import toools.StopWatch;

import com.carrotsearch.hppc.IntObjectMap;
import com.carrotsearch.hppc.IntObjectOpenHashMap;

/*
 * We use Collection instead of Set because this allows to use ArrayList instead of HashSet for storing paths.
 * Anyway, by construction, the algorithm cannot find twice the same path. So the resulting collections cannot have duplicates.
 */

public class AllPaths extends GrphAlgorithm<Collection<Path>>
{
	@Override
	public Collection<Path> compute(Grph g)
	{
		return flatten(compute(g, Integer.MAX_VALUE, Integer.MAX_VALUE, true));
	}

	public static Collection<Path> computeAllPaths(Grph g)
	{
		return flatten(compute(g, Integer.MAX_VALUE, Integer.MAX_VALUE, true));
	}

	public static Collection<Path> flatten(IntObjectMap<List<Collection<Path>>> r)
	{
		Collection<Path> s = new ArrayList<Path>();

		// for every source vertex
		for (int v : r.keys().toArray())
		{
			// for every path length
			for (Collection<Path> ss : r.get(v))
			{
				s.addAll(ss);
			}
		}

		return s;
	}

	private static int numberOfPathsAlreadyFound;

	public static IntObjectMap<List<Collection<Path>>> compute(final Grph g, final int maxPathLength, final int maxNumberOfPaths, final boolean allowLoops)
	{
		final IntObjectOpenHashMap<List<Collection<Path>>> map = new IntObjectOpenHashMap<List<Collection<Path>>>();
		numberOfPathsAlreadyFound = 0;

		new MultiThreadProcessing(g.getVertices()) {

			@Override
			protected void run(int threadID, int v)
			{
				List<Collection<Path>> s = compute(v, g, maxPathLength, Math.max(0, maxNumberOfPaths - numberOfPathsAlreadyFound), allowLoops);

				synchronized(map)
				{
					map.put(v, s);
				}

				numberOfPathsAlreadyFound += count(s);

				if (numberOfPathsAlreadyFound >= maxNumberOfPaths)
				{
					return;
				}
			}
		};

		return map;
	}

	public static List<Collection<Path>> compute(int srcVertex, Grph g, int maxPathLength, final int maxNumberOfPaths, boolean allowLoops)
	{
		List<Collection<Path>> pathsByLength = new ArrayList();
		int numberOfPathsFound = 0;

		if (maxNumberOfPaths == 0)
			return pathsByLength;

		// one 0-long path
		Path initialPath = new SingletonPath(srcVertex);
		pathsByLength.add(0, Collections.singleton(initialPath));
		++numberOfPathsFound;

		if (maxNumberOfPaths == numberOfPathsFound)
			return pathsByLength;

		while (pathsByLength.size() - 1 < maxPathLength)
		{
			List<Path> extensions = new ArrayList<Path>();

			// for each path to be extended (of length n-1)
			for (Path p : pathsByLength.get(pathsByLength.size() - 1))
			{
				int d = p.getDestination();

				for (int e : g.getOutEdges(d).toIntArray())
				{
					int successor = g.getTheOtherVertex(e, d);

					// if this is a loop
					if (!p.containsVertex(successor))
					{
						extensions.add(new PathExtender(p, e, successor));

						// if we have found enough paths
						if (++numberOfPathsFound + extensions.size() == maxNumberOfPaths)
						{
							pathsByLength.add(extensions);
							return pathsByLength;
						}
					}
				}
			}

			if (extensions.isEmpty())
			{
				return pathsByLength;
			}
			else
			{
				pathsByLength.add(extensions);
			}
		}

		return pathsByLength;
	}

	private static int count(List<Collection<Path>> pathByLength)
	{
		int n = 0;

		for (Collection s : pathByLength)
		{
			n += s.size();
		}

		return n;
	}

	public static void main(String[] args)
	{
		Grph g = new InMemoryGrph();
		g.dgrid(12, 12);
		StopWatch sw = new StopWatch();
		Collection<Path> res = AllPaths.computeAllPaths(g);
		System.out.println(sw);
		System.out.println(res.size() + " paths found in ");
//		System.out.println(res);
	}

	private static void test()
	{
		Grph g = ClassicalGraphs.path(2);

		for (Path p : new AllPaths().compute(g))
		{
			if (p.getLength() == 2)
			{
				System.out.println(p);
			}
		}

	}
}
