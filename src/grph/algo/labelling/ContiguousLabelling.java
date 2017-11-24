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
 
 package grph.algo.labelling;

import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
/**
 * Contiguous vertex/edge labelling.
 * @author lhogie
 *
 */
public class ContiguousLabelling extends Relabelling
{
    private Int2IntMap vertex_label = new Int2IntOpenHashMap();
    private Int2IntMap edge_label = new Int2IntOpenHashMap();

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
