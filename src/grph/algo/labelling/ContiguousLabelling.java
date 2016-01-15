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
 
 package grph.algo.labelling;

import com.carrotsearch.hppc.IntIntMap;
import com.carrotsearch.hppc.IntIntOpenHashMap;
/**
 * Contiguous vertex/edge labelling.
 * @author lhogie
 *
 */
public class ContiguousLabelling extends Relabelling
{
    private IntIntMap vertex_label = new IntIntOpenHashMap();
    private IntIntMap edge_label = new IntIntOpenHashMap();

    @Override
    public int getVertexLabel(int v)
    {
	// if this vertex has already been labelled
	if (vertex_label.containsKey(v))
	{
	    return vertex_label.get(v);
	}
	else
	{
	    // computes the new label
	    int label = vertex_label.size();

	    // assign it
	    vertex_label.put(v, label);
	    return label;
	}

    }

    @Override
    public int getEdgeLabel(int e)
    {
	if (edge_label.containsKey(e))
	{
	    return edge_label.get(e);
	}
	else
	{
	    int label = edge_label.size();
	    edge_label.put(e, label);
	    return label;
	}
    }

}
