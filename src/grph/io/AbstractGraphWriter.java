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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import grph.Grph;
import grph.io.graphml.GraphMLWriter;
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
