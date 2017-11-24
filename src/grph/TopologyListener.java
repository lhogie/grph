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

package grph;

import it.unimi.dsi.fastutil.ints.IntSet;

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
