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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import grph.Grph;
import grph.algo.lad.LADReader;
import toools.Clazz;
import toools.io.file.RegularFile;

public abstract class AbstractGraphReader
{
	public abstract Grph readGraph(InputStream is) throws ParseException, IOException, GraphBuildException;

	public final Grph readGraph(RegularFile inFile) throws IOException, ParseException, GraphBuildException
	{
		assert inFile != null;
		assert inFile.exists();
		assert inFile.canRead();

		InputStream is = inFile.createReadingStream();
		Grph g = readGraph(is);
		is.close();
		return g;
	}

	public final Grph readGraph(String text) throws ParseException, GraphBuildException
	{
		return readGraph(text.getBytes());
	}

	public final Grph readGraph(byte[] bytes) throws ParseException, GraphBuildException
	{
		assert bytes != null;

		try
		{
			ByteArrayInputStream os = new ByteArrayInputStream(bytes);
			return readGraph(os);
		}
		catch (IOException ex)
		{
			throw new IllegalStateException(ex);
		}
	}

	public static Map<String, Class<? extends AbstractGraphReader>> extension_readerClass = new HashMap();

	static
	{
		// map.put("dot", DotReader.class);
		extension_readerClass.put("grpht", GrphTextReader.class);
		extension_readerClass.put("grphb", GrphBinaryReader.class);
		extension_readerClass.put("edgelist", EdgeListReader.class);
		extension_readerClass.put("inet", EdgeListReader.class);
		extension_readerClass.put("caida", EdgeListReader.class);
		extension_readerClass.put("lad", LADReader.class);
		extension_readerClass.put("dimacs", DimacsReader.class);
	}

	public static AbstractGraphReader findReader(String extension)
	{
		Class<? extends AbstractGraphReader> clazz = extension_readerClass.get(extension);

		if (clazz == null)
		{
			throw new IllegalArgumentException("cannot find a reader for extension: " + extension);
		}
		else
		{
			return Clazz.makeInstance(clazz);

		}
	}

	protected static int parseNumber(String s, int lineNumber) throws ParseException
	{
		assert s != null;
		assert lineNumber > 0;

		try
		{
			return Integer.valueOf(s.trim());
		}
		catch (NumberFormatException ex)
		{
			throw new ParseException("invalid number: " + s, lineNumber);
		}
	}
}
