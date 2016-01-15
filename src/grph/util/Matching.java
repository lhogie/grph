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
 
 package grph.util;

import toools.collections.HPPCIntMap;
import toools.collections.IntMap;
import toools.collections.ReadOnlyIntMap;

public class Matching
{
    private final IntMap graph2pattern = new HPPCIntMap();
    private final IntMap pattern2graph = new HPPCIntMap();

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

    public IntMap pattern2graph()
    {
	return new ReadOnlyIntMap(pattern2graph);
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

    public IntMap graph2pattern()
    {
	return new ReadOnlyIntMap(graph2pattern);
    }
}
