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
 
 package grph.algo.degree;


import grph.Grph;
import grph.Grph.DIRECTION;
import grph.GrphAlgorithm;

import java.util.Iterator;

import toools.math.IntIterator;
import toools.math.MathsUtilities;

import com.carrotsearch.hppc.cursors.IntCursor;

public abstract class AbstractDegreeAlgorithm extends GrphAlgorithm<Integer>
{
	enum STAT
	{
		minimum, maximum
	}

	protected abstract Grph.TYPE getType();

	protected abstract STAT getStat();

	protected abstract DIRECTION getDirection();

	@Override
	public final Integer compute(Grph graph)
	{
		if (getStat() == STAT.minimum)
		{
			return getMinDegree(graph, getType(), getDirection());
		}
		else
		{
			return getMaxDegree(graph, getType(), getDirection());
		}
	}



	public static  int getMaxDegree(final Grph graph, final Grph.TYPE type, final Grph.DIRECTION dir)
	{
		return MathsUtilities.computeMaximum(new IntIterator() {
			Iterator<IntCursor> i = graph.getVertices().iterator();

			@Override
			public int next()
			{
				return graph.getVertexDegree(i.next().value, type, dir);
			}

			@Override
			public boolean hasNext()
			{
				return i.hasNext();
			}
		});
	}

	public static  int getMinDegree(final Grph graph, final Grph.TYPE type, final Grph.DIRECTION dir)
	{
		return MathsUtilities.computeMinimum(new IntIterator() {
			Iterator<IntCursor> i = graph.getVertices().iterator();

			@Override
			public int next()
			{
				return graph.getVertexDegree(i.next().value, type, dir);
			}

			@Override
			public boolean hasNext()
			{
				return i.hasNext();
			}
		});

	}
}
