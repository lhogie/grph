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
import grph.algo.topology.ClassicalGraphs;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import toools.extern.Proces;
import toools.gui.PDFRenderingAWTComponent;

/**
 * Th default behavior is to generate PDF data via the dot command.
 * 
 * @author lhogie
 * 
 */
public class GraphvizImageWriter extends AbstractGraphWriter
{
	//public static String pathToGraphvizCommands = "";
	
	public static enum COMMAND {
		dot, neato, fdp, twopi, circo
	};

	public static enum OUTPUT_FORMAT {
		pdf, png, fig, svg
	};

	public static OUTPUT_FORMAT defaultOutputFormat = OUTPUT_FORMAT.fig;
	public static boolean defaultEdgeLabelling = false;
	
	/**
	 * Write SVG data to the stream.
	 */
	@Override
	public void writeGraph(Grph g, OutputStream os) throws IOException
	{
		writeGraph(g, computeMostAppropriateDrawingCommand(g), defaultOutputFormat, defaultEdgeLabelling, os);
	}

	public static COMMAND computeMostAppropriateDrawingCommand(Grph g)
	{
//		return COMMAND.neato;
		return g.isDirected() ? COMMAND.dot : COMMAND.neato;
	}

	public byte[] writeGraph(Grph graph, OUTPUT_FORMAT of)
	{
		return writeGraph(graph, computeMostAppropriateDrawingCommand(graph), of, defaultEdgeLabelling);
	}

	
	public void writeGraph(Grph graph, COMMAND cmd, OUTPUT_FORMAT of, boolean writeEdgeLabels, OutputStream os) throws IOException
	{
		String dotText = new DotWriter().createDotText(graph, writeEdgeLabels);
//		String path = pathToGraphvizCommands.ens() ? cmd.name() : pathToGraphvizCommands + cmd.n
		byte[] bytes = Proces.exec( cmd.name(), dotText.getBytes(), "-T" + of.name());
		os.write(bytes);
	}

	public static void main(String[] args)
	{
		Grph g = new grph.in_memory.InMemoryGrph();
		g.grid(4, 4);
		PDFRenderingAWTComponent c = new PDFRenderingAWTComponent();
		toools.gui.Utilities.displayInJFrame(c, "test");
		c.setPDFData(new GraphvizImageWriter().writeGraph(g), 0);
	}

	public byte[] writeGraph(Grph g, COMMAND cmd, OUTPUT_FORMAT of, boolean writeEdgeLabels)
	{
		try
		{
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			writeGraph(g, cmd, of, writeEdgeLabels, bos);
			return bos.toByteArray();
		}
		catch (IOException e)
		{
			throw new IllegalStateException(e);
		}

	}
	
	
	private static void test()
	{
		ClassicalGraphs.cycle(5).toGraphviz(COMMAND.neato, OUTPUT_FORMAT.fig);
	}
}
