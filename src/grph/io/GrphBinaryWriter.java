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
import grph.algo.topology.ClassicalGraphs;
import grph.properties.Property;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;

import toools.UnitTests;
import toools.set.IntSet;

import com.carrotsearch.hppc.cursors.IntCursor;

public class GrphBinaryWriter extends AbstractGraphWriter
{

	private boolean writeProperties = true;
	private boolean writeCache = true;

	@Override
	public void writeGraph(Grph graph, OutputStream os) throws IOException
	{
		assert graph != null;
		assert os != null;
		DataOutputStream dos = new DataOutputStream(os);
		dos.writeUTF(graph.getClass().getName());
		dos.writeBoolean(writeProperties);
		dos.writeBoolean(writeCache);
		writeTopology(graph, dos);

		if (writeProperties)
		{
			writeProperties(graph, dos);
		}

		if (writeCache)
		{
			writeCachedValues(graph, new ObjectOutputStream(dos));
		}
	}

	private void writeCachedValues(Grph graph, ObjectOutputStream dos) throws IOException
	{
		Collection<GrphAlgorithmCache> caches = graph.listCachingGraphAlgorithms();

		for (GrphAlgorithmCache cache : caches)
		{
			Object cachedValue = cache.getCachedValue();

			if (cachedValue != null && cachedValue instanceof Serializable)
			{
				dos.writeUTF(cache.getCachedAlgorithm().getClass().getName());
				dos.writeObject(cachedValue);
			}
		}

		dos.writeUTF("");
		dos.flush();
	}

	private void writeProperties(Grph g, DataOutputStream dos) throws IOException
	{
		Collection<Property> properties = g.getProperties();
		Iterator<Property> i = properties.iterator();

		while (i.hasNext())
		{
			if (i.next().getName() == null)
			{
				i.remove();
			}
		}

		dos.writeInt(properties.size());

		for (Property p : properties)
		{
			if (p.getName() != null)
			{
				dos.writeUTF(p.getName());
				p.toGrphBinary(dos);
			}
		}
	}

	private void writeTopology(Grph g, DataOutputStream dos) throws IOException
	{
		int numberOfVertices = g.getVertices().size();
		dos.writeInt(numberOfVertices);

		if (numberOfVertices > 0)
		{
			int greatestVertexID = g.getVertices().getGreatest();
			dos.writeInt(greatestVertexID);
			writeVertices(dos, g.getIsolatedVertices(), g);

			int numberOfEdges = g.getEdges().size();
			dos.writeInt(numberOfEdges);

			if (numberOfEdges > 0)
			{
				int greatestEdgeID = g.getEdges().getGreatest();
				dos.writeInt(greatestEdgeID);
				dos.writeInt(g.getNumberOfUndirectedSimpleEdges());

				for (int e : g.getEdges().toIntArray())
				{
					if (g.isUndirectedSimpleEdge(e))
					{
						writeEdge(dos, e, g);
						int a = g.getOneVertex(e);
						writeVertex(dos, a, g);
						writeVertex(dos, g.getTheOtherVertex(e, a), g);
					}
				}

				dos.writeInt(g.getNumberOfDirectedSimpleEdges());

				for (int e : g.getEdges().toIntArray())
				{
					if (g.isDirectedSimpleEdge(e))
					{
						writeEdge(dos, e, g);
						writeVertex(dos, g.getDirectedSimpleEdgeTail(e), g);
						writeVertex(dos, g.getDirectedSimpleEdgeHead(e), g);
					}
				}

				dos.writeInt(g.getNumberOfUndirectedHyperEdges());

				for (int e : g.getEdges().toIntArray())
				{
					if (g.isUndirectedHyperEdge(e))
					{
						writeEdge(dos, e, g);
						writeVertices(dos, g.getUndirectedHyperEdgeVertices(e), g);
					}
				}

				dos.writeInt(g.getNumberOfDirectedHyperEdges());

				for (int e : g.getEdges().toIntArray())
				{
					if (g.isDirectedHyperEdge(e))
					{
						writeEdge(dos, e, g);
						writeVertices(dos, g.getDirectedHyperEdgeTail(e), g);
						writeVertices(dos, g.getDirectedHyperEdgeHead(e), g);
					}
				}
			}
		}
	}

	public static void writeVertices(DataOutputStream dos, IntSet set, Grph g) throws IOException
	{
		dos.writeInt(set.size());

		for (IntCursor v : set)
		{
			writeVertex(dos, v.value, g);
		}
	}

	public static void writeEdge(DataOutputStream dos, int e, Grph g) throws IOException
	{
		writeInteger(e, dos, g.getEdges().getGreatest());
	}

	public static void writeVertex(DataOutputStream dos, int v, Grph g) throws IOException
	{
		writeInteger(v, dos, g.getVertices().getGreatest());
	}

	public static void writeInteger(int n, DataOutputStream dos, int greatestItem) throws IOException
	{
		if (greatestItem < 256)
		{
			dos.writeByte(n);
		}
		else if (greatestItem < 65536)
		{
			dos.writeChar(n);
		}
		else
		{
			dos.writeInt(n);
		}
	}

	private static void tdestBinaryEncoding() throws ParseException, GraphBuildException
	{
		Grph g = ClassicalGraphs.completeBipartiteGraph(10, 10);
		byte[] bytes = g.toGrphBinary();
		Grph h = Grph.fromGrphBinary(bytes);
		UnitTests.ensureEquals(h, g);
	}
}
