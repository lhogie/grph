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
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

import javax.swing.JPanel;

import grph.Grph;
import grph.TopologyListener;
import grph.io.GraphvizImageWriter;
import grph.io.GraphvizImageWriter.OUTPUT_FORMAT;
import grph.properties.Property;
import grph.properties.PropertyListener;
import it.unimi.dsi.fastutil.ints.IntSet;
import jfig.gui.JFigViewerBean;
import toools.gui.Utilities;
import toools.thread.Threads;

public class GraphvizComponent extends JPanel
		implements TopologyListener, PropertyListener
{
	private Grph g;
	private final JFigViewerBean b = new JFigViewerBean();

	public GraphvizComponent(Grph g)
	{
		setLayout(new GridLayout(1, 1));
		add(b);
		g.getTopologyListeners().add(this);

		for (Property p : g.getProperties())
		{
			p.getListeners().add(this);
		}
		// Utilities.displayInJFrame(this, "Grph Graphviz renderer");
		this.g = g;
		update();
	}

	@Override
	public void vertexAdded(Grph graph, int vertex)
	{
		update();
	}

	public void update()
	{
		byte[] figText = new GraphvizImageWriter().writeGraph(g, OUTPUT_FORMAT.fig);
		b.setAntiAlias(true);

		try
		{
			File tempFile = File.createTempFile("lmu", "fig");
			tempFile.deleteOnExit();
			FileOutputStream fos = new FileOutputStream(tempFile);
			fos.write(figText);
			fos.flush();
			fos.close();
			b.setURL(tempFile.toURI().toURL());
		}
		catch (IOException ex)
		{
			throw new IllegalStateException(ex);
		}
	}

	@Override
	public void repaint()
	{
		if (b != null)
		{
			b.doFullRedraw();
			b.doZoomFit();
		}
	}

	@Override
	public void vertexRemoved(Grph graph, int vertex)
	{
		update();
	}

	public static void main(String[] args)
	{
		Grph g = new grph.in_memory.InMemoryGrph();
		g.grid(4, 4);
		Utilities.displayInJFrame(new GraphvizComponent(g), "");
		Random r = new Random();

		while (true)
		{
			Threads.sleepMs(1000);

			if (Math.random() < 0.5)
			{
				int v1 = g.getVertices().pickRandomElement(r);
				int v2 = g.getVertices().pickRandomElement(r);

				if (Math.random() < 0.5)
				{
					g.addUndirectedSimpleEdge(v1, v2);
				}
				else
				{
					g.addDirectedSimpleEdge(v1, v2);
				}
			}
			else
			{
				g.removeEdge(g.getEdges().pickRandomElement(r));
			}
		}
	}

	@Override
	public void valueChanged(Property g, int id)
	{
		update();
	}

	@Override
	public void directedSimpleEdgeAdded(Grph Grph, int edge, int src, int dest)
	{
		update();
	}

	@Override
	public void undirectedSimpleEdgeAdded(Grph Grph, int edge, int a, int b)
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
	public void directedSimpleEdgeRemoved(Grph Grph, int edge, int a, int b)
	{
		update();
	}

	@Override
	public void undirectedSimpleEdgeRemoved(Grph Grph, int edge, int a, int b)
	{
		update();
	}

	@Override
	public void undirectedHyperEdgeRemoved(Grph graph, int edge, IntSet incidentVertices)
	{
		update();
	}

	@Override
	public void directedHyperEdgeRemoved(Grph graph, int edge, IntSet src, IntSet dest)
	{
		update();
	}

	@Override
	public void vertexAddedToDirectedHyperEdgeTail(Grph Grph, int e, int v)
	{
		update();
	}

	@Override
	public void vertexAddedToDirectedHyperEdgeHead(Grph Grph, int e, int v)
	{
		update();
	}

	@Override
	public void vertexAddedToUndirectedSimpleEdge(Grph Grph, int edge, int vertex)
	{
		update();
	}

	@Override
	public void vertexRemovedFromUndirectedHyperEdge(Grph g, int edge, int vertex)
	{
		update();
	}

	@Override
	public void vertexRemovedFromDirectedHyperEdgeTail(Grph g, int e, int v)
	{
		update();
	}

	@Override
	public void vertexRemovedFromDirectedHyperEdgeHead(Grph g, int e, int v)
	{
		update();
	}

}
