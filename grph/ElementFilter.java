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

package grph;

import toools.set.IntSetFilter;

/**
 * A filter for selecting (or not) vertices or edges.
 * 
 * @author lhogie
 * 
 */
public class ElementFilter
{

	public static class DegreeFilter extends IntSetFilter
	{
		private final Grph g;
		private final Grph.TYPE degreeType;
		private final Grph.DIRECTION dir;
		private final int degree;

		public DegreeFilter(Grph g, int degree, Grph.TYPE degreeType,
				Grph.DIRECTION dir)
		{
			this.degree = degree;
			this.g = g;
			this.degreeType = degreeType;
			this.dir = dir;
		}

		@Override
		public boolean accept(int v)
		{
			return g.getVertexDegree(v, degreeType, dir) == degree;
		}
	}

	public static class LeafFilter extends DegreeFilter
	{
		public LeafFilter(Grph g)
		{
			super(g, 1, Grph.TYPE.vertex, Grph.DIRECTION.in_out);
		}
	}

	public static class SimplicialVertexFilter extends IntSetFilter
	{
		private final Grph g;

		public SimplicialVertexFilter(Grph g)
		{
			this.g = g;
		}

		@Override
		public boolean accept(int v)
		{
			return g.isSimplicial(v);
		}
	}

	public static class LoopFilter extends IntSetFilter
	{
		private final Grph g;

		public LoopFilter(Grph g)
		{
			this.g = g;
		}


		@Override
		public boolean accept(int e)
		{
			return g.isLoop(e);
		}
	}

}
