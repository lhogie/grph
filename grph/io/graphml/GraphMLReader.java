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
import grph.io.AbstractGraphReader;
import grph.io.ParseException;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.xml.sax.SAXException;

import toools.CodeShouldNotHaveBeenReachedException;
import toools.io.Utilities;
import toools.text.xml.XMLNode;
import toools.text.xml.XMLUtilities;

public class GraphMLReader extends AbstractGraphReader
{
    @Override
    public Grph readGraph(InputStream is) throws ParseException, IOException
    {
	try
	{
	    return readGraph(is, null, null);
	}
	catch (SAXException e)
	{
	    e.printStackTrace();
	    throw new ParseException(e.getMessage(), -1);
	}
    }

    public Grph readGraph(InputStream is, Class vPropertyClass, Class ePropertyClass) throws ParseException,
	    IOException, SAXException
    {
	XMLNode root = XMLUtilities.xml2node(new String(Utilities.readUntilEOF(is)), false);
	Grph g = new grph.in_memory.InMemoryGrph();
	Map<String, Key> key_attributeName = new HashMap<String, Key>();

	if (root.getName().equals("graphml"))
	{
	    for (XMLNode gn : root.getChildren())
	    {
		if (gn.getName().equals("key"))
		{
		    String id = gn.getAttributes().get("id");
		    String target = gn.getAttributes().get("for");
		    String name = gn.getAttributes().get("attr.name");
		    String type = gn.getAttributes().get("attr.type");
		    key_attributeName.put(id, new Key(target, name, id, type));
		}
		else if (gn.getName().equals("graph"))
		{
		    boolean edgesAreDirectedByDefault = gn.getAttributes().get("edgedefault").equals("directed");

		    for (XMLNode elementNode : gn.getChildren())
		    {
			int id = Integer.valueOf(gn.getAttributes().get("id"));

			if (elementNode.getName().equals("node"))
			{
			    g.addVertex(id);
			    // data(elementNode, true, key_attributeName, g, id,
			    // vPropertyClass, ePropertyClass);
			}
			else if (elementNode.getName().equals("edge"))
			{
			    int s = Integer.valueOf(gn.getAttributes().get("source"));
			    int d = Integer.valueOf(gn.getAttributes().get("target"));
			    g.addSimpleEdge(s, id, d, edgesAreDirectedByDefault);
			    // data(elementNode, false, key_attributeName, g,
			    // id, vPropertyClass, ePropertyClass);
			}
			else
			{
			    throw new ParseException("node is neither a node or an edge", 0);
			}
		    }
		}
		else
		{
		    throw new ParseException("child node is not graph", 0);
		}
	    }

	    throw new CodeShouldNotHaveBeenReachedException();
	}
	else
	{
	    throw new ParseException("root node is not graphml", 0);
	}

    }

    /*
     * private void data(XMLNode xmlNode, boolean targetIsNode, Map<String, Key>
     * key_attributeName, Grph g, int targetId, Class vPropertyClass, Class
     * ePropertyClass) throws ParseException { for (XMLNode c :
     * xmlNode.getChildren()) { if (c.getName().equals("data")) { String k =
     * c.getAttributes().get("key"); Key key = key_attributeName.get(k); String
     * data = c.getTextBuffer().toString(); set(targetIsNode, key.name,
     * key.type, data, g, targetId, vPropertyClass, ePropertyClass); } else {
     * throw new ParseException("node is not a data node", 0); } } } private
     * void set(boolean targetIsNode, String name, String type, String data,
     * Grph g, int id, Class vPropertyClass, Class ePropertyClass) throws
     * ParseException {
     * 
     * try { Serializable prop = targetIsNode ? g.getVertexProperties(id) :
     * g.getEdgeProperties(id);
     * 
     * if (prop == null) { if (targetIsNode) { g.setVertexProperties(id, prop =
     * Clazz.makeInstance(vPropertyClass)); } else { g.setEdgeProperties(id,
     * prop = Clazz.makeInstance(ePropertyClass)); } }
     * 
     * Field field = (targetIsNode ? vPropertyClass :
     * ePropertyClass).getDeclaredField(name); Class fieldType =
     * field.getType(); field.setAccessible(true);
     * 
     * if (fieldType == String.class) { field.set(prop, data); } else if
     * (fieldType == double.class) { field.set(prop, Double.valueOf(data)); }
     * else if (fieldType == int.class) { field.set(prop,
     * Integer.valueOf(data)); } else if (fieldType == boolean.class) {
     * field.set(prop, Boolean.valueOf(data)); } else { throw new
     * ParseException("cannot set a property of type " + fieldType.getName(),
     * 0); } } catch (NoSuchFieldException e) { throw new
     * ParseException("no such field : " + name, 0); } catch
     * (IllegalArgumentException e) { throw new
     * ParseException("illegal argument : " + data, 0); } catch
     * (IllegalAccessException e) { e.printStackTrace(); throw new
     * IllegalStateException(); } }
     */
}