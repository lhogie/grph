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
import grph.io.graphml.GraphMLWriter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import toools.Clazz;
import toools.ExceptionUtilities;
import toools.io.file.RegularFile;

public abstract class AbstractGraphWriter
{
	public abstract void writeGraph(Grph g, OutputStream os) throws IOException;

	public final void writeGraph(Grph graph, RegularFile outFile) throws IOException
	{
		OutputStream os = outFile.createWritingStream();
		writeGraph(graph, os);
		os.close();
	}

	public final byte[] writeGraph(Grph graph)
	{
		try
		{
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			writeGraph(graph, os);
			return os.toByteArray();
		}
		catch (IOException ex)
		{
			throw new IllegalStateException(ExceptionUtilities.toString(ex));
		}
	}

	public static Map<String, Class> extension_writer = new HashMap<String, Class>();

	static
	{
		extension_writer.put("dot", DotWriter.class);
		extension_writer.put("grphtext", GrphTextWriter.class);
		extension_writer.put("grph", GrphBinaryWriter.class);
		extension_writer.put("gml", GMLWriter.class);
		extension_writer.put("graphml", GraphMLWriter.class);
		extension_writer.put("inet", EdgeListWriter.class);
	}

	public static AbstractGraphWriter findWriter(String ext)
	{
		return (AbstractGraphWriter) Clazz.makeInstance(extension_writer.get(ext));
	}

	public static Set<String> getExtensions()
	{
		return extension_writer.keySet();
	}

}
