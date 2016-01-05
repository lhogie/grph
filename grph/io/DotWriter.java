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

package grph.io;

import grph.Grph;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import toools.gui.Utilities;

import com.carrotsearch.hppc.cursors.IntCursor;

public class DotWriter extends AbstractGraphTextWriter
{

	@Override
	public void printGraph(Grph g, PrintStream ps) throws IOException
	{
		printGraph(g, true, ps);
	}

	public void printGraph(Grph g, boolean writeEdgeLabels, OutputStream os) throws IOException
	{
		os.write(createDotText(g, writeEdgeLabels).getBytes());
	}

	public String createDotText(Grph graph, boolean writeEdgeLabels)
	{
		StringBuilder text = new StringBuilder();
		text.append("digraph {\n");
		text.append("\trankdir=LR;\n");
		text.append("\tstart=0;\n");
		text.append("\tnode [style=\"filled\"]\n\n");
		

		for (IntCursor c : graph.getVertices())
		{
			int v = c.value;
			text.append('\t');
			text.append(v);
			text.append(' ');
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("fillcolor", "#" + Utilities.toRGBHexa(graph.getVertexColorProperty().getValueAsColor(v)));
			map.put("fontcolor", "black");
			map.put("label", graph.getVertexLabelProperty().getValueAsString(v));
			
			map.put("size", graph.getVertexSizeProperty().getValue(v));
			map.put("shape", graph.getVertexShapeProperty().getValue(v) == 1 ? "square" : "circle");
			text.append(to(map));
			text.append(';');
			text.append('\n');
		}

		text.append('\n');

		for (IntCursor c : graph.getEdges())
		{
			int e = c.value;

			if (graph.isSimpleEdge(e))
			{
				text.append('\t');
				int a = graph.getOneVertex(e);
				text.append(a);
				text.append(" -> ");
				text.append(graph.getTheOtherVertex(e, a));
			}

			text.append(' ');

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("color", "#" + Utilities.toRGBHexa(graph.getEdgeColorProperty().getValueAsColor(c.value)));

			if (writeEdgeLabels)
			{
				map.put("label", graph.getEdgeLabelProperty().getValueAsString(e));
			}

			if (graph.isUndirectedSimpleEdge(e))
			{
				map.put("arrowhead", "none");
			}

			map.put("penwidth", graph.getEdgeWidthProperty().getValue(e));
			map.put("style", graph.getEdgeStyleProperty().getValue(e) == 1 ? "dotted" : "solid");

			text.append(to(map));
			text.append(';');
			text.append('\n');
		}

		text.append('}');
//		System.out.println(text);
		return text.toString();
	}

	public String to(Map<String, Object> map)
	{
		StringBuilder s = new StringBuilder();
		s.append('[');
		Iterator<String> iterator = map.keySet().iterator();

		while (iterator.hasNext())
		{
			String propertyName = iterator.next();
			Object value = map.get(propertyName);

			{
				s.append(propertyName.toString());
				s.append('=');
				s.append('"');
				s.append(value != null ? value.toString() : "");
				s.append('"');

				if (iterator.hasNext())
				{
					s.append(", ");
				}
			}
		}

		s.append(']');
		return s.toString();
	}

	public static void main(String[] args)
	{
		Grph g = new grph.in_memory.InMemoryGrph();
		g.display();
		g.dgrid(2, 2);
		g.addUndirectedSimpleEdge(0, 2);
		g.getVertexColorProperty().setValue(0, 5);
		System.out.println(g.toDot());
	}

}
