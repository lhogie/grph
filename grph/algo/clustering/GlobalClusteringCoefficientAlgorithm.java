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
