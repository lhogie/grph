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

import java.util.Collection;
import java.util.Random;
import java4unix.CommandLine;
import java4unix.OptionSpecification;

import toools.set.IntSet;
import toools.thread.Threads;

import com.carrotsearch.hppc.cursors.IntCursor;

public class demo_lifegame extends AbstractGrphScript
{

	@Override
	public int runScript(CommandLine cmdLine) throws Throwable
	{
		Random r = new Random(Long.valueOf(getOptionValue(cmdLine, "--seed")));
		Grph g = createGraph(Integer.valueOf(getOptionValue(cmdLine, "--edge")), r);
		g.display();

		long delay = Long.valueOf(getOptionValue(cmdLine, "--delay"));

		for (;;)
		{
			int v = g.getVertices().pickRandomElement(r);
			int numberOfLivingNeighbours = computeNumberOfLivingNeighbors(g.getNeighbours(v), g);

			if (g.getVertexColorProperty().getValue(v) == 0) // if the cell is
			// dead
			{
				if (numberOfLivingNeighbours == 3)
				{
					g.getVertexColorProperty().setValue(v, 1); // make it birth
				}
			}
			else
			// the cell is alive
			{
				if (numberOfLivingNeighbours != 3 && numberOfLivingNeighbours != 2)
				{
					g.getVertexColorProperty().setValue(v, 0); // kill it
				}
			}

			Threads.sleepMs(delay);
		}
	}

	private Grph createGraph(int edge, Random r)
	{
		Grph g = new grph.in_memory.InMemoryGrph();
		g.addNVertices(edge * edge);
		g.grid(edge, edge, false, true, true);

		for (IntCursor v : g.getVertices())
		{
			g.getVertexColorProperty().setValue(v.value, (r.nextBoolean() ? 4 : 5));
		}

		return g;
	}

	private int computeNumberOfLivingNeighbors(IntSet vertices, Grph g)
	{
		int n = 0;

		for (IntCursor v : vertices)
		{
			if (g.getVertexColorProperty().getValue(v.value) == 1)
			{
				++n;
			}
		}

		return n;
	}

	@Override
	public String getShortDescription()
	{
		return "Lifegame";
	}

	public static void main(String[] args) throws Throwable
	{
		new demo_lifegame().run("/Users/lhogie/tmp/test.dc");
	}

	@Override
	protected void declareOptions(Collection<OptionSpecification> optionSpecifications)
	{
		optionSpecifications.add(new OptionSpecification("--edge", "-e", "[0-9]+", "10", "Set the edge size for the grid"));
		optionSpecifications.add(new OptionSpecification("--seed", "-s", "[0-9]+", String.valueOf(System.currentTimeMillis()), "Set the seed for the PRNG"));
		optionSpecifications.add(new OptionSpecification("--delay", "-d", "[0-9]+", "10", "Set the number of millisecond between two iterations"));
	}
}
