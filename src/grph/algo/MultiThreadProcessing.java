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

package grph.algo;

import grph.Grph;
import grph.algo.search.BFSAlgorithm;
import grph.in_memory.InMemoryGrph;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import toools.StopWatch;
import toools.set.IntSet;

import com.carrotsearch.hppc.cursors.IntCursor;

public abstract class MultiThreadProcessing
{
	private int finishedThreads = 0;

	public static int defaultNumberOfThreads = Runtime.getRuntime().availableProcessors();

	public MultiThreadProcessing(IntSet ids)
	{
		this(ids, defaultNumberOfThreads);
	}

	public MultiThreadProcessing(IntSet ids, final int numberOfThreads)
	{
		assert ids.size() > 0;
		assert numberOfThreads >= 0;

		if (numberOfThreads == 0)
		{
			for (int id : ids.toIntArray())
			{
				MultiThreadProcessing.this.run(0, id);
			}
		}
		else
		{
			final Iterator<IntCursor> iDIterator;
			iDIterator = ids.iterator();

			final List<Thread> threads = new ArrayList<Thread>();

			for (int i = 0; i < numberOfThreads; ++i)
			{
				final int threadID = i;

				threads.add(new Thread() {
					public void run()
					{
						while (true)
						{
							int id = getNextID(iDIterator);

							if (id == -1)
								break;

							MultiThreadProcessing.this.run(threadID, id);
						}

						synchronized (iDIterator)
						{
							if (++finishedThreads == numberOfThreads && waiting)
							{
								iDIterator.notify();
							}
						}
					}
				});
			}

			// now starts all threads
			for (Thread thisThread : threads)
			{
				thisThread.start();
			}

			synchronized (iDIterator)
			{
				if (finishedThreads < numberOfThreads)
				{
					try
					{
						waiting = true;
						iDIterator.wait();
					}
					catch (InterruptedException e)
					{
						e.printStackTrace();
					}
				}
			}
		}

	}

	private synchronized int getNextID(Iterator<IntCursor> iDIterator)
	{
		if (iDIterator.hasNext())
		{
			return iDIterator.next().value;
		}
		else
		{
			return -1;
		}
	}

	private boolean waiting = false;

	protected abstract void run(int threadID, int id);

	public static void main(String[] args)
	{

		final Grph g = new InMemoryGrph();
		g.useCache = true;
		g.grid(70, 70);

		for (int numberOfThreads = 10; numberOfThreads > 0; --numberOfThreads)
		{
			StopWatch sw = new StopWatch();

			new MultiThreadProcessing(g.getVertices(), numberOfThreads) {

				@Override
				protected void run(int t, int id)
				{
					new BFSAlgorithm().compute(g, id);
				}
			};

			System.out.println("number of threads = " + numberOfThreads + "\t duration of the process " + sw.getElapsedTime() + "ms");
		}
	}
}
