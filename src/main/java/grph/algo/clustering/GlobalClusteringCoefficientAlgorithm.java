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
 
 package grph.algo.clustering;

import grph.Grph;
import grph.GrphAlgorithm;
import grph.algo.AdjacencyMatrix;

public class GlobalClusteringCoefficientAlgorithm extends GrphAlgorithm<Double>
{
	/**
	 * Computes the global clustering coefficient of this graph, defined as the
	 * number of triangles over the number of triplets (i.e. triangles and
	 * triangles minus an edge).
	 * 
	 * @return
	 */
	@Override
	public Double compute(Grph g)
	{
		AdjacencyMatrix A = g.getAdjacencyMatrix();
		AdjacencyMatrix A2 = AdjacencyMatrix.power(A, 2);

		// Computes the number of connected open triplets
		int nbOfOpentriplets = 0;
		for (int i = 0; i < A2.getSize() - 1; i++)
			for (int j = i + 1; j < A2.getSize(); j++)
				if (!g.areVerticesAdjacent(A2.getVertexFromMatrixIndex(i),
						A2.getVertexFromMatrixIndex(j)))
					nbOfOpentriplets += A2.get(i, j);

		System.out.println("Triplets : " + nbOfOpentriplets);

		int nbOfTriangles = g.getNumberOfTriangles();

		return (double) nbOfTriangles / (nbOfTriangles + nbOfOpentriplets);
	}


}
