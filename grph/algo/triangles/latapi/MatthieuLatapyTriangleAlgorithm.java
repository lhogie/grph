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

package grph.algo.triangles.latapi;

import grph.Grph;
import grph.GrphAlgorithm;
import grph.in_memory.InMemoryGrph;

import java.io.IOException;
import java.util.List;

import toools.UnitTests;
import toools.extern.ExternalProgram;
import toools.extern.Proces;
import toools.io.JavaResource;
import toools.io.file.RegularFile;
import toools.text.TextUtilities;

import com.carrotsearch.hppc.cursors.IntCursor;

/**
 * Fast and Compact Algorithms for Triangle Problems in Very Large (Sparse
 * (Power-Law)) Graphs. See http://www-rp.lip6.fr/~latapy/Triangles/
 * 
 * @author lhogie
 * 
 */
public class MatthieuLatapyTriangleAlgorithm extends GrphAlgorithm<Result>
{
	public enum ALGORITHM {
		EDGE_ITERATOR, FORWARD, COMPACT_FORWARD
	};

	private ALGORITHM algorithm = ALGORITHM.EDGE_ITERATOR;

	public ALGORITHM getAlgorithm()
	{
		return algorithm;
	}

	public void setAlgorithm(ALGORITHM algorithm)
	{
		this.algorithm = algorithm;
	}

	public static RegularFile executable = new RegularFile(Grph.COMPILATION_DIRECTORY, "tr");

	@Override
	public Result compute(Grph g)
	{
		ensureCompiled();

		if (!g.isSimple())
			throw new IllegalArgumentException();

		if (g.getEdges().size() < 3)
		{
			Result r = new Result();
			r.numberOfTriangles = 0;
			r.avgClusteringCoefficient = 0;
			r.globalClusteringCoefficient = 0;
			return r;
		}
		else
		{
			String algo = new String[] { "e", "f", "cf" }[algorithm.ordinal()];

			// building the input text
			byte[] inputText = createInputText(g);

			// calling the external program
			String output = new String(Proces.exec(executable.getPath(), inputText, "-c", "-cc", '-' + algo));

			// parsing the output text
			List<String> outputLines = TextUtilities.splitInLines(output);
			Result r = new Result();
			r.numberOfTriangles = Integer.valueOf(outputLines.get(0).substring(6, outputLines.get(0).indexOf("triangles...") - 1));
			r.avgClusteringCoefficient = Double.valueOf(outputLines.get(1).substring(12));
			r.globalClusteringCoefficient = Double.valueOf(outputLines.get(2).substring(11));
			return r;
		}

	}

	private void ensureCompiled()
	{
		if (!executable.exists())
		{
			JavaResource res = new JavaResource(getClass(), "tr.c");

			try
			{
				ExternalProgram.compileCSource(res, Grph.COMPILATION_DIRECTORY, Grph.logger);
			}
			catch (IOException e)
			{
				throw new IllegalStateException("cannot compile " + res.getPath());
			}
		}
	}

	public static byte[] createInputText(Grph g)
	{
		StringBuilder b = new StringBuilder();
		b.append(g.getVertices().size());
		b.append('\n');

		// write degrees
		for (IntCursor c : g.getVertices())
		{
			int v = c.value;
			b.append(v);
			b.append('\t');
			b.append(g.getVertexDegree(v, Grph.TYPE.edge, Grph.DIRECTION.in_out));
			b.append('\n');
		}

		// write adjacencies
		for (IntCursor c : g.getEdges())
		{
			int e = c.value;
			int a = g.getOneVertex(e);
			b.append(a);
			b.append('\t');
			b.append(g.getTheOtherVertex(e, a));
			b.append('\n');
		}

		return b.toString().getBytes();
	}

	public static void main(String[] args)
	{
		Grph g = new InMemoryGrph();
		g.addNVertices(50);
		g.glp();
		System.out.println(g.getNumberOfTriangles());

	}

	private static void test()
	{
		Grph g = new InMemoryGrph();
		g.grid(3, 3);
		MatthieuLatapyTriangleAlgorithm a = new MatthieuLatapyTriangleAlgorithm();
		UnitTests.ensure(a.compute(g).numberOfTriangles == 0);

		g = new InMemoryGrph();
		g.addNVertices(3);
		g.ring();
		UnitTests.ensure(a.compute(g).numberOfTriangles == 1);

	}

}
