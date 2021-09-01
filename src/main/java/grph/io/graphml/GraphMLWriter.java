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

package grph.io.graphml;

import java.io.PrintStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import grph.Grph;
import grph.io.AbstractGraphTextWriter;
import toools.collections.primitive.IntCursor;
import toools.text.xml.DNode;

/**
 * http://graphml.graphdrawing.org/primer/graphml-primer.html#BCfile
 * 
 * @author lhogie
 * 
 */

public class GraphMLWriter extends AbstractGraphTextWriter
{

	@Override
	public void printGraph(Grph g, PrintStream ps)
	{
		if (g.isHypergraph())
			throw new IllegalArgumentException("Cannot export an hypergraph to GraphML");

		DNode graphmlNode = new DNode("graphml");
		graphmlNode.getAttributes().put("xmlns", "http://graphml.graphdrawing.org/xmlns");
		Map<String, Key> attributeName_key = new HashMap<String, Key>();

		DNode graphNode = new DNode("graph");
		graphNode.setParent(graphmlNode);
		graphNode.getAttributes().put("id", "" + g.hashCode());
		graphNode.getAttributes().put("edgedefault", "directed");

		for (IntCursor c : IntCursor.fromFastUtil(g.getVertices()))
		{
			int v = c.value;
			DNode vertexNode = new DNode("node");
			vertexNode.setParent(graphNode);
			vertexNode.getAttributes().put("id", "" + v);
			// data(true, v, g, vertexNode, attributeName_key);
		}

		for (IntCursor c : IntCursor.fromFastUtil(g.getEdges()))
		{
			int e = c.value;
			int v1 = g.getOneVertex(e);
			DNode edgeNode = new DNode("edge");
			edgeNode.setParent(graphNode);
			edgeNode.getAttributes().put("id", "" + e);
			edgeNode.getAttributes().put("source", "" + v1);
			edgeNode.getAttributes().put("destination", "" + g.getTheOtherVertex(e, v1));
			// data(false, e, g, edgeNode, attributeName_key);
		}

		for (Key k : attributeName_key.values())
		{
			DNode keyNode = new DNode("key");
			keyNode.setParent(graphmlNode, 0);
			keyNode.getAttributes().put("id", "" + k.id);
			keyNode.getAttributes().put("for", "" + k.target);
			keyNode.getAttributes().put("attr.name", "" + k.name);
			keyNode.getAttributes().put("attr.type", "" + k.type);
		}

		ps.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		ps.println(graphmlNode.toXML('\t'));
	}

	/*
	 * private void data(boolean targetIsNode, int v, Grph g, XMLNode
	 * parentNode, Map<String, Key> attributeName_key) { Serializable p =
	 * g.getVertexProperties(v);
	 * 
	 * if (p != null) { for (Field field : p.getClass().getDeclaredFields()) {
	 * XMLNode dataNode = new XMLNode("data"); dataNode.setParent(parentNode);
	 * dataNode.getAttributes().put("key", field.getName());
	 * attributeName_key.put(field.getName(), new Key(targetIsNode ? "node" :
	 * "edge", field.getName(), "" + attributeName_key.size(),
	 * field.getType().getName()));
	 * 
	 * try { field.setAccessible(true); TextNode textNode = new
	 * TextNode(field.get(p).toString()); textNode.setParent(dataNode); } catch
	 * (IllegalAccessException e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); } } } }
	 */
	public static class P implements Serializable
	{
		String coucou = "ok";
	}

	public static void main(String[] args)
	{

		Grph g = new grph.in_memory.InMemoryGrph();
		g.grid(3, 3);
		// g.setVertexProperties(4, new P());
		System.out.println(g.toGraphML());
	}
}
