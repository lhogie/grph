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

package grph.util;

import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.ints.Int2IntMaps;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;

public class Matching
{
	private final Int2IntMap graph2pattern = new Int2IntOpenHashMap();
	private final Int2IntMap pattern2graph = new Int2IntOpenHashMap();

	@Override
	public boolean equals(Object o)
	{
		return o instanceof Matching && equals((Matching) o);
	}

	public boolean equals(Matching o)
	{
		return o.graph2pattern.equals(pattern2graph);
	}

	@Override
	public String toString()
	{
		return pattern2graph.toString();
	}

	public Int2IntMap pattern2graph()
	{
		return Int2IntMaps.unmodifiable(pattern2graph);
	}

	public void graph2pattern(int vertexInGraph, int vertexInPattern)
	{
		graph2pattern.put(vertexInGraph, vertexInPattern);
		pattern2graph.put(vertexInPattern, vertexInGraph);

	}

	public void pattern2graph(int vertexInPattern, int vertexInGraph)
	{
		graph2pattern.put(vertexInGraph, vertexInPattern);
		pattern2graph.put(vertexInPattern, vertexInGraph);
	}

	public Int2IntMap graph2pattern()
	{
		return Int2IntMaps.unmodifiable(graph2pattern);
	}
}
