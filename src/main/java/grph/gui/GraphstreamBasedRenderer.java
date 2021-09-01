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
Julien Deantoni (I3S, Université Cote D'Azur, Saclay) 

*/
 
 
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

import java.awt.GridLayout;
import java.io.IOException;

import javax.swing.JPanel;

import org.miv.graphstream.graph.Edge;
import org.miv.graphstream.graph.Node;
import org.miv.graphstream.graph.implementations.MultiGraph;
import org.miv.graphstream.ui.GraphViewerRemote;
import org.miv.graphstream.ui2.swing.SwingGraphViewer;

import grph.Grph;
import grph.TopologyListener;
import grph.properties.NumericalProperty;
import grph.properties.Property;
import grph.properties.PropertyListener;
import it.unimi.dsi.fastutil.ints.IntSet;
import toools.collections.primitive.IntCursor;
import toools.exceptions.NotYetImplementedException;
import toools.io.JavaResource;

public class GraphstreamBasedRenderer extends JPanel
{
	private final Grph sourceGraph_;
	private final MultiGraph graphstreamGraph = new MultiGraph();

	// static
	// {
	// System.setProperty("gs.ui.renderer",
	// "org.graphstream.ui.j2dviewer.J2DGraphRenderer");

	// }

	public MultiGraph getGraphstreamGraph()
	{
		return graphstreamGraph;
	}

	private final TopoListener topologyListener = new TopoListener();
	private final PropertyListener vertexColorListener = new VColorL();
	private final PropertyListener vertexShapeListener = new VShapeL();
	private final PropertyListener vertexSizeListener = new VSizeL();
	private final PropertyListener vertexLabelListener = new VLabelL();
	private final PropertyListener edgeColorListener = new EColorL();
	private final PropertyListener edgeStyleListener = new EShapeL();
	private final PropertyListener edgeWidthListener = new ESizeL();
	private final PropertyListener edgeLabelListener = new ELabelL();

	public GraphstreamBasedRenderer(Grph g)
	{

			String css = new String(new JavaResource(GraphstreamBasedRenderer.class,
					"graphstream-0.4.2.css").getByteArray());
			graphstreamGraph.addAttribute("ui.stylesheet", css);

		sourceGraph_ = g;

		for (IntCursor v : IntCursor.fromFastUtil(g.getVertices()))
		{
			this.topologyListener.vertexAdded(g, v.value);
		}

		if (g.storeEdges())
		{
			for (int e : g.getEdges().toIntArray())
			{
				if (g.isUndirectedSimpleEdge(e))
				{
					int a = g.getOneVertex(e);
					int b = g.getTheOtherVertex(e, a);
					this.topologyListener.undirectedSimpleEdgeAdded(g, e, a, b);
				}
				else if (g.isDirectedSimpleEdge(e))
				{
					int a = g.getDirectedSimpleEdgeTail(e);
					int b = g.getDirectedSimpleEdgeHead(e);
					this.topologyListener.directedSimpleEdgeAdded(g, e, a, b);
				}
				else
				{
					throw new NotYetImplementedException();
				}
			}
		}
		else
		{
			for (int u : g.getVertices().toIntArray())
			{
				for (int v : g.getInOutOnlyElements(u).toIntArray())
				{
					if (v < u)
					{
						this.topologyListener.undirectedSimpleEdgeAdded(g, - 1, u, v);
					}
				}

				for (int v : g.getOutOnlyElements(u).toIntArray())
				{
					this.topologyListener.directedSimpleEdgeAdded(g, - 1, u, v);
				}

			}
		}

		g.getTopologyListeners().add(this.topologyListener);
		g.getVertexColorProperty().getListeners().add(vertexColorListener);
		g.getVertexLabelProperty().getListeners().add(vertexLabelListener);
		g.getVertexSizeProperty().getListeners().add(vertexSizeListener);
		g.getVertexShapeProperty().getListeners().add(vertexShapeListener);

		if (g.storeEdges())
		{
			g.getEdgeColorProperty().getListeners().add(edgeColorListener);
			g.getEdgeLabelProperty().getListeners().add(edgeLabelListener);
			g.getEdgeWidthProperty().getListeners().add(edgeWidthListener);
			g.getEdgeStyleProperty().getListeners().add(edgeStyleListener);
		}

		setLayout(new GridLayout(1, 1));

		SwingGraphViewer w = new SwingGraphViewer(graphstreamGraph, true);
		org.miv.graphstream.ui2.GraphViewerRemote r = w.newViewerRemote();
		// w.setQuality(4);
		add(w.getSwingComponent());
	}

	public GraphViewerRemote display()
	{
		graphstreamGraph.addAttribute("layout.force", 0.0002);
		return graphstreamGraph.display();
	}

	public Node getGraphstreamNode(int v)
	{
		return graphstreamGraph.getNode(String.valueOf(v));
	}

	public Edge getGraphstreamEdge(int e)
	{
		return graphstreamGraph.getEdge(String.valueOf(e));
	}

	private class TopoListener implements TopologyListener
	{
		private void addEdge(Grph ds, int e)
		{
			if (e != - 1)
			{
				edgeLabelListener.valueChanged(sourceGraph_.getEdgeLabelProperty(), e);
				edgeColorListener.valueChanged(sourceGraph_.getEdgeColorProperty(), e);
				edgeStyleListener.valueChanged(sourceGraph_.getEdgeStyleProperty(), e);
				edgeWidthListener.valueChanged(sourceGraph_.getEdgeWidthProperty(), e);
			}
		}

		private void addVertex(Grph ds, int vertex)
		{
			vertexLabelListener.valueChanged(sourceGraph_.getVertexLabelProperty(),
					vertex);
			vertexColorListener.valueChanged(sourceGraph_.getVertexColorProperty(),
					vertex);
			vertexShapeListener.valueChanged(sourceGraph_.getVertexShapeProperty(),
					vertex);
			vertexSizeListener.valueChanged(sourceGraph_.getVertexSizeProperty(), vertex);
		}

		@Override
		public void vertexAdded(Grph g, int vertex)
		{
			// gets the ID of the graphpstream node
			String newId = String.valueOf(vertex);

			// tells graphstream the create of vertex with the given ID
			graphstreamGraph.addNode(newId);
			addVertex(g, vertex);
		}

		@Override
		public void vertexRemoved(Grph graph, int vertex)
		{
			if (graph != sourceGraph_)
				throw new IllegalStateException();

			graphstreamGraph.removeNode(String.valueOf(vertex));
		}

		@Override
		public void directedSimpleEdgeAdded(Grph g, int edge, int t, int h)
		{
			String edgeID = edge == - 1 ? t + "=>" + h : String.valueOf(edge);
			String srcID = String.valueOf(t);
			String destID = String.valueOf(h);
			graphstreamGraph.addEdge(edgeID, srcID, destID, true);
			addEdge(g, edge);
		}

		@Override
		public void undirectedSimpleEdgeAdded(Grph g, int edge, int a, int b)
		{
			String edgeID = edge == - 1 ? Math.min(a, b) + "-" + Math.max(a, b)
					: String.valueOf(edge);
			String srcID = String.valueOf(a);
			String destID = String.valueOf(b);
			graphstreamGraph.addEdge(edgeID, srcID, destID, false);
			addEdge(g, edge);
		}

		@Override
		public void undirectedHyperEdgeAdded(Grph g, int edge)
		{
			throw new NotYetImplementedException();

		}

		@Override
		public void directedHyperEdgeAdded(Grph g, int edge)
		{
			throw new NotYetImplementedException();

		}

		@Override
		public void directedSimpleEdgeRemoved(Grph g, int edge, int src, int dest)
		{
			String edgeID = edge == - 1 ? src + "=>" + dest : String.valueOf(edge);
			graphstreamGraph.removeEdge(edgeID);
		}

		@Override
		public void undirectedSimpleEdgeRemoved(Grph Grph, int edge, int a, int b)
		{
			String edgeID = edge == - 1 ? Math.min(a, b) + "-" + Math.max(a, b)
					: String.valueOf(edge);
			graphstreamGraph.removeEdge(edgeID);
		}

		@Override
		public void undirectedHyperEdgeRemoved(Grph graph, int edge,
				IntSet incidentVertices)
		{
			throw new NotYetImplementedException();
		}

		@Override
		public void directedHyperEdgeRemoved(Grph graph, int edge, IntSet src,
				IntSet dest)
		{
			throw new NotYetImplementedException();
		}

		@Override
		public void vertexAddedToDirectedHyperEdgeTail(Grph Grph, int e, int v)
		{
			throw new NotYetImplementedException();
		}

		@Override
		public void vertexAddedToDirectedHyperEdgeHead(Grph Grph, int e, int v)
		{
			throw new NotYetImplementedException();
		}

		@Override
		public void vertexAddedToUndirectedSimpleEdge(Grph Grph, int edge, int vertex)
		{
			throw new NotYetImplementedException();
		}

		@Override
		public void vertexRemovedFromUndirectedHyperEdge(Grph g, int edge, int vertex)
		{
			throw new NotYetImplementedException();
		}

		@Override
		public void vertexRemovedFromDirectedHyperEdgeTail(Grph g, int e, int v)
		{
			throw new NotYetImplementedException();
		}

		@Override
		public void vertexRemovedFromDirectedHyperEdgeHead(Grph g, int e, int v)
		{
			throw new NotYetImplementedException();
		}

	}

	private class ELabelL implements PropertyListener
	{
		@Override
		public void valueChanged(Property p, int e)
		{
			if (e != - 1)
			{
				String l = p.getValueAsString(e);
				getGraphstreamEdge(e).setAttribute("label", l == null ? "e" + e : l);
			}
		}
	}

	private class VLabelL implements PropertyListener
	{
		@Override
		public void valueChanged(Property p, int v)
		{
			String label = p.getValueAsString(v);
			getGraphstreamNode(v).setAttribute("label", label == null ? "" + v : label);
		}
	}

	private class VShapeL implements PropertyListener
	{
		@Override
		public void valueChanged(Property p, int e)
		{
			getGraphstreamNode(e).setAttribute("node-shape",
					((NumericalProperty) p).getValueAsInt(e) == 0 ? "circle" : "square");
		}
	}

	private class EShapeL implements PropertyListener
	{
		@Override
		public void valueChanged(Property p, int e)
		{
			if (e != - 1)
			{
				getGraphstreamEdge(e).setAttribute("edge-style",
						((NumericalProperty) p).getValueAsInt(e) == 0 ? "plain"
								: "dashes");
			}
		}
	}

	private class VSizeL implements PropertyListener
	{
		@Override
		public void valueChanged(Property p, int v)
		{
			getGraphstreamNode(v).setAttribute("width",
					2 * ((NumericalProperty) p).getValueAsInt(v));
		}
	}

	private class ESizeL implements PropertyListener
	{
		@Override
		public void valueChanged(Property p, int e)
		{
			if (e != - 1)
			{
				getGraphstreamEdge(e).setAttribute("width",
						((NumericalProperty) p).getValueAsInt(e) * 2);
			}
		}
	}

	private class EColorL implements PropertyListener
	{
		@Override
		public void valueChanged(Property p, int e)
		{
			if (e != - 1)
			{
				getGraphstreamEdge(e).setAttribute("color",
						sourceGraph_.getEdgeColorProperty().getValueAsColor(e));
			}
		}
	}

	private class VColorL implements PropertyListener
	{
		@Override
		public void valueChanged(Property p, int v)
		{
			getGraphstreamNode(v).setAttribute("color",
					sourceGraph_.getVertexColorProperty().getValueAsColor(v));
		}
	}

	public GraphViewerRemote displayInAFrame()
	{
		return graphstreamGraph.display();
	}

	public static void main(String[] args)
	{
		Grph g = new grph.in_memory.InMemoryGrph();
		g.grid(3, 3);

		g.getVertexColorProperty().setValue(0, 4);
		g.getEdgeColorProperty().setValue(0, 4);
		g.getVertexLabelProperty().setValue(1, "v");
		g.getEdgeLabelProperty().setValue(1, "e");
		g.getVertexShapeProperty().setValue(1, 1);
		g.getVertexSizeProperty().setValue(1, 30);
		g.getEdgeStyleProperty().setValue(1, 1);
		g.getEdgeWidthProperty().setValue(1, 1);

		g.displayGraphstream_0_4_2();
	}

}
