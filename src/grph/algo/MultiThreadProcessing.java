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

package grph.algo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import grph.Grph;
import grph.algo.search.BFSAlgorithm;
import grph.in_memory.InMemoryGrph;
import it.unimi.dsi.fastutil.ints.IntSet;
import toools.StopWatch;
import toools.collections.primitive.IntCursor;

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
			final Iterator<IntCursor> iDIterator = IntCursor.fromFastUtil(ids).iterator();

			final List<Thread> threads = new ArrayList<Thread>();

			for (int i = 0; i < numberOfThreads; ++i)
			{
				final int threadID = i;

				threads.add(new Thread()
				{
					public void run()
					{
						while (true)
						{
							int id = getNextID(iDIterator);

							if (id == - 1)
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
			return - 1;
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

			new MultiThreadProcessing(g.getVertices(), numberOfThreads)
			{

				@Override
				protected void run(int t, int id)
				{
					new BFSAlgorithm().compute(g, id);
				}
			};

			System.out.println("number of threads = " + numberOfThreads
					+ "\t duration of the process " + sw.getElapsedTime() + "ms");
		}
	}
}
