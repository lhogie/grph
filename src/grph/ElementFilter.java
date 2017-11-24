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

package grph;

import java.util.function.IntPredicate;

/**
 * A filter for selecting (or not) vertices or edges.
 * 
 * @author lhogie
 * 
 */
public class ElementFilter
{
	public static class DegreeFilter implements IntPredicate
	{
		private final Grph g;
		private final Grph.TYPE degreeType;
		private final Grph.DIRECTION dir;
		private final int degree;

		public DegreeFilter(Grph g, int degree, Grph.TYPE degreeType, Grph.DIRECTION dir)
		{
			this.degree = degree;
			this.g = g;
			this.degreeType = degreeType;
			this.dir = dir;
		}

		@Override
		public boolean test(int v)
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

	public static class SimplicialVertexFilter implements IntPredicate
	{
		private final Grph g;

		public SimplicialVertexFilter(Grph g)
		{
			this.g = g;
		}

		@Override
		public boolean test(int v)
		{
			return g.isSimplicial(v);
		}
	}

	public static class LoopFilter implements IntPredicate
	{
		private final Grph g;

		public LoopFilter(Grph g)
		{
			this.g = g;
		}

		@Override
		public boolean test(int e)
		{
			return g.isLoop(e);
		}
	}

}
