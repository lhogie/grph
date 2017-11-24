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

package grph.algo.subgraph_isomorphism.own;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import grph.Grph;
import grph.path.Path;
import grph.properties.Property;
import it.unimi.dsi.fastutil.ints.IntSet;
import toools.collections.primitive.SelfAdaptiveIntSet;
import toools.math.MathsUtilities;

public abstract class LabelBasedSubgraphMatcher
{
	public Set<Grph> findAllMatches(Grph g, Collection<String> patterns,
			Property vertexLabels, Property edgeLabels)
	{
		List<List<Path>> ss = new ArrayList();
		Map<String, Set<Path>> paths = findAllPathsMatching(g, patterns, vertexLabels,
				edgeLabels);

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
		IntSet vertices = new SelfAdaptiveIntSet();

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

	public Set<Path> findPathsMatching(Collection<Path> paths, String pattern,
			Property vertexLabels, Property edgeLabels)
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

	public abstract String pathToString(Path p, Property vertexLabels,
			Property edgeLabels);

}
