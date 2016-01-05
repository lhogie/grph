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
import grph.GrphAlgorithmCache;
import grph.properties.Property;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;

import toools.Clazz;
import toools.NotYetImplementedException;
import toools.io.file.RegularFile;

import com.carrotsearch.hppc.IntArrayList;
import com.carrotsearch.hppc.cursors.IntCursor;

public class GrphBinaryReader extends AbstractGraphReader
{
	@Override
	public Grph readGraph(InputStream is) throws IOException, ParseException
	{
		DataInputStream dis = new DataInputStream(is);

		String classname = dis.readUTF();
		System.out.println(classname);
		Class<Grph> graphClass = (Class<Grph>) Clazz.findClass(classname);
		Grph graph = Clazz.makeInstance(graphClass);
		boolean hasProperties = dis.readBoolean();
		boolean hasCache = dis.readBoolean();

		readTopology(graph, dis);

		if (hasProperties)
		{
			readProperties(graph, dis);
		}

		if (hasCache)
		{
			readCachedValues(graph, new ObjectInputStream(dis));
		}

		return graph;
	}

	private void readCachedValues(Grph graph, ObjectInputStream dis) throws IOException
	{
		for (;;)
		{
			String algorithmName = dis.readUTF();

			if (algorithmName.equals(""))
			{
				break;
			}
			else
			{
				GrphAlgorithmCache algorithmCache = (GrphAlgorithmCache) graph.findAlgorithm(algorithmName);

				try
				{
					Object value = dis.readObject();
					algorithmCache.setCachedValue(value);
				}
				catch (ClassNotFoundException e)
				{
					e.printStackTrace();
					throw new IllegalStateException();
				}
			}
		}
	}

	private void readProperties(Grph graph, DataInputStream dis) throws IOException
	{
		for (int i = dis.readInt(); i > 0; --i)
		{
			String name = dis.readUTF();
			System.out.println(name);
			Property p = graph.findPropertyByName(name);
			p.fromGrphBinary(dis);
		}
	}

	private void readTopology(Grph g, DataInputStream dis) throws IOException
	{
		int numberOfVertices = dis.readInt();

		if (numberOfVertices > 0)
		{
			int greatestVertexID = dis.readInt();

			// read isolated vertices
			for (int i = dis.readInt(); i > 0; --i)
			{
				g.addVertex(readInteger(dis, greatestVertexID));
			}

			int numberOfEdges = dis.readInt();

			if (numberOfEdges > 0)
			{
				int greatestEdgeID = dis.readInt();

				// read undirected simple edges
				for (int i = dis.readInt(); i > 0; --i)
				{
					int edge = readInteger(dis, greatestEdgeID);
					int a = readInteger(dis, greatestVertexID);
					int b = readInteger(dis, greatestVertexID);
					g.addUndirectedSimpleEdge(edge, a, b);
				}

				// read directed simple edges
				for (int i = dis.readInt(); i > 0; --i)
				{
					int edge = readInteger(dis, greatestEdgeID);
					int t = readInteger(dis, greatestVertexID);
					int h = readInteger(dis, greatestVertexID);
					g.addDirectedSimpleEdge(t, edge, h);
				}

				// read undirected hyper edges
				for (int i = dis.readInt(); i > 0; --i)
				{
					int edge = readInteger(dis, greatestEdgeID);
					g.addUndirectedHyperEdge(edge);

					for (IntCursor v : parseVerticeList(dis, g, greatestVertexID))
					{
						g.addToUndirectedHyperEdge(edge, v.value);
					}
				}

				// read directed hyper edges
				for (int i = dis.readInt(); i > 0; --i)
				{
					throw new NotYetImplementedException("directed hyperedges are not yet supported");
					/*
					 * int edge = readInteger(dis, greatestEdgeID);
					 * graph.addDirectedHyperEdge(edge);
					 * 
					 * for (IntCursor v : parseVerticeList(dis, graph,
					 * greatestVertexID)) { // graph.getDi(edge, v.value); }
					 * 
					 * for (IntCursor v : parseVerticeList(dis, graph,
					 * greatestVertexID)) { //
					 * graph.addToDirectedHyperEdge(edge, v.value); }
					 */
				}
			}
		}
	}

	public static int readInteger(DataInputStream dis, int greatestValue) throws IOException
	{
		if (greatestValue < 256)
		{
			return (int) dis.readByte() & 0xFF;
		}
		else if (greatestValue < 65536)
		{
			return dis.readChar();
		}
		else
		{
			return dis.readInt();
		}
	}

	private IntArrayList parseVerticeList(DataInputStream dis, Grph graph, int numberOfVertices) throws IOException
	{
		IntArrayList set = new IntArrayList();

		for (int i = dis.readInt(); i > 0; --i)
		{
			set.add(readInteger(dis, numberOfVertices));
		}

		return set;
	}

	public static void main(String[] args) throws IOException, ParseException, GraphBuildException
	{
		Grph g = new grph.in_memory.InMemoryGrph();
		g.grid(20, 7);
		g.getDiameter();
		g.getVertexColorProperty().setValue(1, 15);
		RegularFile f = RegularFile.createTempFile();
		f.setContent(g.toGrphBinary());
		System.out.println(f.getPath());

		Grph h = Grph.fromGrphBinary(f.getContent());

		System.out.println(g.equals(h));
		h.display();
	}

}
