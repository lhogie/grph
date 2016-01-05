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

package grph.algo.subgraph_isomorphism.own;

import grph.Grph;
import grph.path.Path;
import grph.properties.Property;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import toools.math.MathsUtilities;
import toools.set.DefaultIntSet;
import toools.set.IntSet;

public abstract class LabelBasedSubgraphMatcher
{
	public Set<Grph> findAllMatches(Grph g, Collection<String> patterns, Property vertexLabels, Property edgeLabels)
	{
		List<List<Path>> ss = new ArrayList();
		Map<String, Set<Path>> paths = findAllPathsMatching(g, patterns, vertexLabels, edgeLabels);

		for (Set<Path> s : paths.values())
		{
			ss.add(new ArrayList(s));
		}

		int[] indexes = new int[ss.size()];
		int[] sizes = new int[ss.size()];

		for (int i = 0; i < sizes.length; ++i)
		{
			sizes[i] = ss.get(i).size();
		}

		Set<Grph> gs = new HashSet();

		while (MathsUtilities.next(indexes, sizes))
		{
			Set<Path> combination = new HashSet<Path>();

			for (int i = 0; i < indexes.length; ++i)
			{
				combination.add(ss.get(i).get(indexes[i]));
			}

			gs.add(paths2subgraph(combination, g));
		}

		return gs;
	}

	private static Grph paths2subgraph(Set<Path> paths, Grph g)
	{
		IntSet vertices = new DefaultIntSet();

		for (Path p : paths)
		{
			vertices.addAll(p.getVertexSet());
		}

		return g.getSubgraphInducedByVertices(vertices);
	}

	public Map<String, Set<Path>> findAllPathsMatching(Grph g,
			Collection<String> patterns, Property vertexLabels, Property edgeLabels)
	{
		Map<String, Set<Path>> paths = new HashMap<String, Set<Path>>();
		Collection<Path> allPaths = g.getAllPaths();

		for (String re : patterns)
		{
			paths.put(re, findPathsMatching(allPaths, re, vertexLabels, edgeLabels));
		}

		return paths;
	}

	public Set<Path> findPathsMatching(Collection<Path> paths, String pattern, Property vertexLabels, Property edgeLabels)
	{
		Set<Path> r = new HashSet<Path>();

		for (Path p : paths)
		{
			if (pathToString(p, vertexLabels, edgeLabels).matches(pattern))
			{
				r.add(p);
			}
		}

		return r;
	}

	public abstract String pathToString(Path p, Property vertexLabels, Property edgeLabels);

}
