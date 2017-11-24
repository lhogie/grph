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
 
 

package grph.io;

import java.io.PrintStream;

import grph.Grph;
import grph.TopologyListener;
import grph.properties.Property;
import grph.properties.PropertyListener;
import it.unimi.dsi.fastutil.ints.IntSet;
import toools.collections.primitive.LucIntSet;

public class DynamicsLogger
{
	private PrintStream out;
	private boolean enabled = true;

	private final Grph g;

	public DynamicsLogger(Grph g)
	{
		this(g, System.out);
	}

	public DynamicsLogger(Grph g, PrintStream out)
	{
		g.getTopologyListeners().add(TopoL);
		g.getVertexColorProperty().getListeners().add(VCL);
		g.getVertexLabelProperty().getListeners().add(VLL);
		g.getVertexSizeProperty().getListeners().add(VSL);
		g.getVertexShapeProperty().getListeners().add(VSHL);
		g.getEdgeColorProperty().getListeners().add(ECL);
		g.getEdgeLabelProperty().getListeners().add(ELL);
		g.getEdgeWidthProperty().getListeners().add(ESL);
		g.getEdgeStyleProperty().getListeners().add(ESHL);
		this.out = out;
		this.g = g;
	}

	public PrintStream getOut()
	{
		return out;
	}

	public void setOut(PrintStream out)
	{
		this.out = out;
	}

	public boolean isEnabled()
	{
		return enabled;
	}

	public void setEnabled(boolean enabled)
	{
		this.enabled = enabled;
	}

	public void detach()
	{
		g.getTopologyListeners().remove(TopoL);
		g.getVertexColorProperty().getListeners().remove(VCL);
		g.getVertexLabelProperty().getListeners().remove(VLL);
		g.getVertexSizeProperty().getListeners().remove(VSL);
		g.getVertexShapeProperty().getListeners().remove(VSHL);
		g.getEdgeColorProperty().getListeners().remove(ECL);
		g.getEdgeLabelProperty().getListeners().remove(ELL);
		g.getEdgeWidthProperty().getListeners().remove(ESL);
		g.getEdgeStyleProperty().getListeners().remove(ESHL);
	}

	private TopologyListener TopoL = new TopologyListener() {
		@Override
		public void vertexAdded(Grph graph, int vertex)
		{
			print(out, "Vertex " + vertex + " added");
		}

		@Override
		public void vertexRemoved(Grph graph, int vertex)
		{
			print(out, "Vertex " + vertex + " removed");
		}

		@Override
		public void directedSimpleEdgeAdded(Grph g, int edge, int src,
				int dest)
		{
			print(out, "Arc " + edge + " added from " + src + " to " + dest);
		}

		@Override
		public void undirectedSimpleEdgeAdded(Grph g, int edge, int a,
				int b)
		{
			print(out, "Undirected edge " + edge + " added between " + a
					+ " and " + b);

		}

		@Override
		public void undirectedHyperEdgeAdded(Grph g, int edge)
		{
			print(out, "Hyper-edge " + edge + " added associating vertices: "
					+ g.getUndirectedHyperEdgeVertices(edge));
		}

		@Override
		public void directedHyperEdgeAdded(Grph g, int edge)
		{
			print(out, "direct hyper-edge " + edge + " added");
		}

		@Override
		public void directedSimpleEdgeRemoved(Grph g, int edge, int a,
				int b)
		{
			print(out, "Edge " + edge + " between vertices " + a + " and " + b
					+ " was removed");

		}

		@Override
		public void undirectedSimpleEdgeRemoved(Grph g, int edge,
				int a, int b)
		{
			print(out, "Edge " + edge + " connecting vertices " + a + " and "
					+ b + " was removed");

		}

		@Override
		public void undirectedHyperEdgeRemoved(Grph g, int edge,
				IntSet incidentVertices)
		{
			print(out, "undirected hyper-edge " + edge
					+ " removed, it was connecting vertices "
					+ incidentVertices);
		}

		@Override
		public void directedHyperEdgeRemoved(Grph g, int edge,
				IntSet src, IntSet dest)
		{
			print(out, "directed hyper-edge " + edge
					+ " removed, it was connecting vertices " + src
					+ " to vertices " + dest);
		}

		@Override
		public void vertexAddedToDirectedHyperEdgeTail(Grph g, int e,
				int v)
		{
			print(out, "vertex " + v + " added to directed hyper-edge " + e
					+ " tail");
		}

		@Override
		public void vertexAddedToDirectedHyperEdgeHead(Grph g, int e,
				int v)
		{
			print(out, "vertex " + v + " added to directed hyper-edge " + e
					+ " head");
		}

		@Override
		public void vertexAddedToUndirectedSimpleEdge(Grph g, int e,
				int v)
		{
			print(out, "vertex " + v + " added to undirected hyper-edge " + e
					+ " head");
		}

		@Override
		public void vertexRemovedFromUndirectedHyperEdge(Grph g,
				int e, int v)
		{
			print(out, "vertex " + v + " removed from undirected hyper-edge "
					+ e);
		}

		@Override
		public void vertexRemovedFromDirectedHyperEdgeTail(Grph g,
				int e, int v)
		{
			print(out, "vertex " + v + " removed from directed hyper-edge " + e
					+ " tail");
		}

		@Override
		public void vertexRemovedFromDirectedHyperEdgeHead(Grph g,
				int e, int v)
		{
			print(out, "vertex " + v + " removed from directed hyper-edge " + e
					+ " head");
		}
	};

	private PropertyListener VCL = new PropertyListener() {
		@Override
		public void valueChanged(Property p, int vertex)
		{
			print(out, "setting color " + p.getValueAsString(vertex)
					+ " for vertex " + vertex);
		}
	};

	private PropertyListener ECL = new PropertyListener() {
		@Override
		public void valueChanged(Property p, int e)
		{
			print(out, "setting color " + p.getValueAsString(e) + " for edge " + e);

		}
	};

	private PropertyListener VLL = new PropertyListener() {

		@Override
		public void valueChanged(Property p, int vertex)
		{
			print(out, "setting label " + p.getValueAsString(vertex) + " for vertex " + vertex);
		}
	};

	private PropertyListener ELL = new PropertyListener() {

		@Override
		public void valueChanged(Property p, int edge)
		{
			print(out, "setting label " + p.getValueAsString(edge) + " for edge " + edge);
		}
	};

	private PropertyListener ESL = new PropertyListener() {

		@Override
		public void valueChanged(Property p, int e)
		{
			print(out, "setting width " + p.getValueAsString(e) + " for edge " + e);
		}
	};

	private PropertyListener VSL = new PropertyListener() {

		@Override
		public void valueChanged(Property p, int v)
		{
			print(out, "setting width " + p.getValueAsString(v) + " for vertex " + v);
		}
	};

	private PropertyListener ESHL = new PropertyListener() {

		@Override
		public void valueChanged(Property p, int e)
		{
			print(out, "setting style " + p.getValueAsString(e) + " for edge " + e);
		}
	};

	private PropertyListener VSHL = new PropertyListener() {

		@Override
		public void valueChanged(Property p, int v)
		{
			print(out, "setting style " + p.getValueAsString(v) + " for vertex " + v);
		}
	};

	protected void print(PrintStream out, String s)
	{
		if (enabled)
		{
			out.println(s);
		}
	}
}
