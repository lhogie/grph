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

package grph.script;

import grph.Grph;
import grph.Grph.DIRECTION;

import java.util.Collection;
import java.util.Random;
import java4unix.CommandLine;
import java4unix.OptionSpecification;

import toools.StopWatch;

public class biggest_graph_possible extends AbstractGrphScript
{

	@Override
	public int runScript(CommandLine cmdLine) throws Throwable
	{
		Random r = new Random(Long.valueOf(getOptionValue(cmdLine, "--seed")));
		int degree = Integer.valueOf(getOptionValue(cmdLine, "-d"));
		boolean verticesOnly = isOptionSpecified(cmdLine, "--verticesOnly");
		int step = Integer.valueOf(getOptionValue(cmdLine, "--step"));
		printMessage("Generating graph of degree " + degree);

		Grph g = verticesOnly ? new grph.in_memory.InMemoryGrph("", false, DIRECTION.in_out) : new grph.in_memory.InMemoryGrph();

		StopWatch sw = new StopWatch();

		try
		{
			while (true)
			{
				int v = g.addVertex();

				for (int i = 0; i < degree; ++i)
				{
					int w = g.getVertices().pickRandomElement(r);
					g.addUndirectedSimpleEdge(v, w);
				}

				if (g.getNumberOfVertices() % step == 0)
				{
					printMessage(g + " (+" + sw.getElapsedTime() + "ms)");
				}
			}
		}
		catch (OutOfMemoryError e)
		{
			printMessage("Completed. Could generate " + g);
		}

		return 0;
	}

	@Override
	public String getShortDescription()
	{
		return "Creates the biggest graph possible";
	}

	public static void main(String[] args) throws Throwable
	{
		new biggest_graph_possible().run("--degree=3");
		// new biggest_graph_possible().run("--degree=3", "--verticesOnly");
	}

	@Override
	protected void declareOptions(Collection<OptionSpecification> optionSpecifications)
	{

		optionSpecifications.add(new OptionSpecification("--degree", "-d", "[0-9]+", "3", "Set degree of the desired graph"));
		optionSpecifications.add(new OptionSpecification("--seed", "-s", "[0-9]+", String.valueOf(System.currentTimeMillis()),
				"Set the seed for the PRNG"));
		optionSpecifications.add(new OptionSpecification("--step", null, "[0-9]+", "10000",
				"Set the number of new vertices for which a message will be given to the user"));
		optionSpecifications.add(new OptionSpecification("--verticesOnly", "-V", "do not store edges"));
	}
}
