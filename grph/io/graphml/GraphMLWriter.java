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
 
 package grph.io.graphml;

import grph.Grph;
import grph.io.AbstractGraphTextWriter;

import java.io.PrintStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import toools.text.xml.XMLNode;

import com.carrotsearch.hppc.cursors.IntCursor;

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

	XMLNode graphmlNode = new XMLNode("graphml");
	graphmlNode.getAttributes().put("xmlns", "http://graphml.graphdrawing.org/xmlns");
	Map<String, Key> attributeName_key = new HashMap<String, Key>();

	XMLNode graphNode = new XMLNode("graph");
	graphNode.setParent(graphmlNode);
	graphNode.getAttributes().put("id", "" + g.hashCode());
	graphNode.getAttributes().put("edgedefault", "directed");

	for (IntCursor c : g.getVertices())
	{
	    int v = c.value;
	    XMLNode vertexNode = new XMLNode("node");
	    vertexNode.setParent(graphNode);
	    vertexNode.getAttributes().put("id", "" + v);
//	    data(true, v, g, vertexNode, attributeName_key);
	}

	for (IntCursor c : g.getEdges())
	{
	    int e = c.value;
	    int v1 = g.getOneVertex(e);
	    XMLNode edgeNode = new XMLNode("edge");
	    edgeNode.setParent(graphNode);
	    edgeNode.getAttributes().put("id", "" + e);
	    edgeNode.getAttributes().put("source", "" + v1);
	    edgeNode.getAttributes().put("destination", "" + g.getTheOtherVertex(e, v1));
//	    data(false, e, g, edgeNode, attributeName_key);
	}

	for (Key k : attributeName_key.values())
	{
	    XMLNode keyNode = new XMLNode("key");
	    keyNode.setParent(graphmlNode, 0);
	    keyNode.getAttributes().put("id", "" + k.id);
	    keyNode.getAttributes().put("for", "" + k.target);
	    keyNode.getAttributes().put("attr.name", "" + k.name);
	    keyNode.getAttributes().put("attr.type", "" + k.type);
	}

	ps.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
	ps.println(graphmlNode.toString('\t'));
    }
/*
    private void data(boolean targetIsNode, int v, Grph g, XMLNode parentNode, Map<String, Key> attributeName_key)
    {
	Serializable p = g.getVertexProperties(v);

	if (p != null)
	{
	    for (Field field : p.getClass().getDeclaredFields())
	    {
		XMLNode dataNode = new XMLNode("data");
		dataNode.setParent(parentNode);
		dataNode.getAttributes().put("key", field.getName());
		attributeName_key.put(field.getName(), new Key(targetIsNode ? "node" : "edge", field.getName(), ""
			+ attributeName_key.size(), field.getType().getName()));

		try
		{
		    field.setAccessible(true);
		    TextNode textNode = new TextNode(field.get(p).toString());
		    textNode.setParent(dataNode);
		}
		catch (IllegalAccessException e)
		{
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		}
	    }
	}
    }
*/
    public static class P implements Serializable
    {
	String coucou = "ok";
    }

    public static void main(String[] args)
    {

	Grph g = new grph.in_memory.InMemoryGrph();
	g.grid(3, 3);
//	g.setVertexProperties(4, new P());
	System.out.println(g.toGraphML());
    }
}
