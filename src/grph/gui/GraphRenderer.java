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
 
 

package grph.gui;

import javax.swing.JComponent;

import grph.Grph;
import grph.TopologyListener;
import grph.properties.Property;
import grph.properties.PropertyListener;
import it.unimi.dsi.fastutil.ints.IntSet;
import toools.collections.primitive.LucIntSet;

public abstract class GraphRenderer implements
		TopologyListener, PropertyListener
{
	private final Grph g;

	public GraphRenderer(Grph g)
	{
		g.getTopologyListeners().add(this);

		for (Property p : g.getProperties())
		{
			p.getListeners().add(this);
		}

		this.g = g;
		update();
	}

	public abstract JComponent getComponent();
	
	@Override
	public void vertexAdded(Grph graph, int vertex)
	{
		update();
	}

	protected abstract void update();

	@Override
	public void vertexRemoved(Grph graph, int vertex)
	{
		update();
	}



	@Override
	public void valueChanged(Property g, int id)
	{
		update();
	}

	@Override
	public void directedSimpleEdgeAdded(Grph Grph, int edge,
			int src, int dest)
	{
		update();
	}

	@Override
	public void undirectedSimpleEdgeAdded(Grph Grph,
			int edge, int a, int b)
	{
		update();
	}

	@Override
	public void undirectedHyperEdgeAdded(Grph graph, int edge)
	{
		update();
	}

	@Override
	public void directedHyperEdgeAdded(Grph graph, int edge)
	{
		update();
	}

	@Override
	public void directedSimpleEdgeRemoved(Grph Grph,
			int edge, int a, int b)
	{
		update();
	}

	@Override
	public void undirectedSimpleEdgeRemoved(Grph Grph,
			int edge, int a, int b)
	{
		update();
	}

	@Override
	public void undirectedHyperEdgeRemoved(Grph graph, int edge,
			IntSet incidentVertices)
	{
		update();
	}

	@Override
	public void directedHyperEdgeRemoved(Grph graph, int edge,
			IntSet src, IntSet dest)
	{
		update();
	}

	@Override
	public void vertexAddedToDirectedHyperEdgeTail(Grph Grph,
			int e, int v)
	{
		update();
	}

	@Override
	public void vertexAddedToDirectedHyperEdgeHead(Grph Grph,
			int e, int v)
	{
		update();
	}

	@Override
	public void vertexAddedToUndirectedSimpleEdge(Grph Grph,
			int edge, int vertex)
	{
		update();
	}

	@Override
	public void vertexRemovedFromUndirectedHyperEdge(Grph g, int edge,
			int vertex)
	{
		update();
	}

	@Override
	public void vertexRemovedFromDirectedHyperEdgeTail(Grph g, int e,
			int v)
	{
		update();
	}

	@Override
	public void vertexRemovedFromDirectedHyperEdgeHead(Grph g, int e,
			int v)
	{
		update();
	}

	public Grph getG()
	{
		return g;
	}

}
