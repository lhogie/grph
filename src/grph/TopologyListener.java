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
 
 /**
 *	This file is part of Grph.
 *	
 *  Grph is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Grph is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Grph.  If not, see <http://www.gnu.org/licenses/>. *
 */

package grph;

import toools.set.IntSet;

/**
 * Objects that listen to graph topology changes need to implement this
 * interface.
 * 
 * @author lhogie
 * 
 */

public interface TopologyListener
{
    // vertex addition and removal
    void vertexAdded(Grph g, int v);

    void vertexRemoved(Grph g, int v);

    // undirected simple edge
    void undirectedSimpleEdgeAdded(Grph g, int edge, int a, int b);

    void undirectedSimpleEdgeRemoved(Grph g, int edge, int a, int b);

    // directed simple edge
    void directedSimpleEdgeAdded(Grph g, int edge, int src, int dest);

    void directedSimpleEdgeRemoved(Grph g, int edge, int a, int b);

    // undirected hyper edge

    void undirectedHyperEdgeAdded(Grph g, int edge);

    void undirectedHyperEdgeRemoved(Grph g, int edge, IntSet incidentVertices);

    void vertexAddedToUndirectedSimpleEdge(Grph g, int edge, int vertex);

    void vertexRemovedFromUndirectedHyperEdge(Grph g, int edge, int vertex);

    // directed hyper edge
    void directedHyperEdgeAdded(Grph g, int edge);

    void directedHyperEdgeRemoved(Grph g, int edge, IntSet src, IntSet dest);

    void vertexAddedToDirectedHyperEdgeTail(Grph g, int e, int v);

    void vertexAddedToDirectedHyperEdgeHead(Grph g, int e, int v);

    void vertexRemovedFromDirectedHyperEdgeTail(Grph g, int e, int v);

    void vertexRemovedFromDirectedHyperEdgeHead(Grph g, int e, int v);

}
