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

package grph.gui;

import grph.Grph;
import grph.TopologyListener;
import grph.properties.Property;
import grph.properties.PropertyListener;

import javax.swing.JComponent;

import toools.set.IntSet;

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
