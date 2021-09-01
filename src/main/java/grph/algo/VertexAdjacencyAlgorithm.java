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
Julien Deantoni (I3S, Université Cote D'Azur, Saclay) 

*/
 
 
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

import grph.Grph;
import grph.GrphAlgorithm;
import it.unimi.dsi.fastutil.ints.IntSet;
import toools.collections.primitive.IntCursor;
import toools.collections.primitive.LucIntSet;

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
		LucIntSet vertices = g.getVertices();

		if (vertices.isEmpty())
		{
			return new int[0][];
		}
		else
		{
			int n = vertices.getGreatest() + 1;
			int[][] v = new int[n][];

			for (IntCursor c : IntCursor.fromFastUtil(g.getVertices()))
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
