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
import grph.in_memory.InMemoryGrph;
import toools.collections.primitive.IntCursor;
import toools.math.IntMatrix;

/**
 * Compute the incidence matrix for the given graph. Lines are vertices. Columns
 * are edges. Each cell indicates if the corresponding vertex is incident to the
 * given edge. In the case of directed edges, 1 indicates that the vertices is
 * head, -1 that it is tail. In the case of undirected edges, 1 indicates that
 * the vertex is incident.
 * 
 * 
 * @author lhogie
 * 
 */

public class IncidenceMatrixAlgorithm extends GrphAlgorithm<IntMatrix>
{

	@Override
	public IntMatrix compute(Grph g)
	{
		int n = g.getVertices().size() + 1;
		IntMatrix matrix = new IntMatrix(n, g.getEdges().size());

		for (IntCursor c : IntCursor.fromFastUtil(g.getEdges()))
		{
			int e = c.value;

			if (g.isUndirectedSimpleEdge(e))
			{
				int v1 = g.getOneVertex(e);
				int v2 = g.getTheOtherVertex(e, v1);
				matrix.set(v1, e, 1);
				matrix.set(v2, e, 1);
			}
			else if (g.isDirectedSimpleEdge(e))
			{
				int h = g.getDirectedSimpleEdgeHead(e);
				int t = g.getDirectedSimpleEdgeTail(e);
				matrix.set(h, e, 1);
				matrix.set(t, e, - 1);
			}
			else if (g.isUndirectedHyperEdge(e))
			{
				for (IntCursor cv : IntCursor
						.fromFastUtil(g.getVerticesIncidentToEdge(e)))
				{
					matrix.set(cv.value, e, 1);
				}
			}
			else if (g.isDirectedHyperEdge(e))
			{
				for (IntCursor cv :IntCursor.fromFastUtil(g.getDirectedHyperEdgeTail(e)))
				{
					matrix.set(cv.value, e, - 1);
				}
				for (IntCursor cv : IntCursor.fromFastUtil(g.getDirectedHyperEdgeHead(e)))
				{
					matrix.set(cv.value, e, 1);
				}
			}
		}

		return matrix;
	}

	public static void main(String[] args)
	{
		Grph g = new InMemoryGrph();
		g.dgrid(3, 3);
		g.display();
		System.out.println(g.getIncidenceMatrix());
	}

}
