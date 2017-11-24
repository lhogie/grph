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

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import grph.Grph;
import toools.collections.primitive.IntCursor;
import toools.gui.Utilities;

public class DotWriter extends AbstractGraphTextWriter
{

	@Override
	public void printGraph(Grph g, PrintStream ps) throws IOException
	{
		printGraph(g, true, ps);
	}

	public void printGraph(Grph g, boolean writeEdgeLabels, OutputStream os)
			throws IOException
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

		for (IntCursor c : IntCursor.fromFastUtil(graph.getVertices()))
		{
			int v = c.value;
			text.append('\t');
			text.append(v);
			text.append(' ');
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("fillcolor", "#" + Utilities
					.toRGBHexa(graph.getVertexColorProperty().getValueAsColor(v)));
			map.put("fontcolor", "black");
			map.put("label", graph.getVertexLabelProperty().getValueAsString(v));

			map.put("size", graph.getVertexSizeProperty().getValue(v));
			map.put("shape", graph.getVertexShapeProperty().getValue(v) == 1 ? "square"
					: "circle");
			text.append(to(map));
			text.append(';');
			text.append('\n');
		}

		text.append('\n');

		for (IntCursor c : IntCursor.fromFastUtil(graph.getEdges()))
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
			map.put("color", "#" + Utilities
					.toRGBHexa(graph.getEdgeColorProperty().getValueAsColor(c.value)));

			if (writeEdgeLabels)
			{
				map.put("label", graph.getEdgeLabelProperty().getValueAsString(e));
			}

			if (graph.isUndirectedSimpleEdge(e))
			{
				map.put("arrowhead", "none");
			}

			map.put("penwidth", graph.getEdgeWidthProperty().getValue(e));
			map.put("style",
					graph.getEdgeStyleProperty().getValue(e) == 1 ? "dotted" : "solid");

			text.append(to(map));
			text.append(';');
			text.append('\n');
		}

		text.append('}');
		// System.out.println(text);
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
