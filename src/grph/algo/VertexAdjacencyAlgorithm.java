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

import toools.set.IntSet;
import grph.Grph;
import grph.GrphAlgorithm;

import com.carrotsearch.hppc.cursors.IntCursor;

/**
 * Computes a list of adjacency lists.
 * 
 * @author lhogie
 * 
 */
public abstract class VertexAdjacencyAlgorithm extends GrphAlgorithm<int[][]>
{
	@Override
	public int[][] compute(Grph g)
	{
		IntSet vertices = g.getVertices();

		if (vertices.isEmpty())
		{
			return new int[0][];
		}
		else
		{
			int n = vertices.getGreatest() + 1;
			int[][] v = new int[n][];

			for (IntCursor c : g.getVertices())
			{
				v[c.value] = g.getNeighbours(c.value, getDirection()).toIntArray();
			}

			return v;
		}
	}

	public abstract Grph.DIRECTION getDirection();

	public static class Out extends VertexAdjacencyAlgorithm
	{
		@Override
		public Grph.DIRECTION getDirection()
		{
			return Grph.DIRECTION.out;
		}
	}

	public static class In extends VertexAdjacencyAlgorithm
	{
		@Override
		public Grph.DIRECTION getDirection()
		{
			return Grph.DIRECTION.in;
		}
	}

	public static class InOut extends VertexAdjacencyAlgorithm
	{
		@Override
		public Grph.DIRECTION getDirection()
		{
			return Grph.DIRECTION.in_out;
		}
	}
}
