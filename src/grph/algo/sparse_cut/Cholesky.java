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
 
 package grph.algo.sparse_cut;

/*************************************************************************
 * Compilation: javac Cholesky.java Execution: java Cholesky
 * 
 * Compute Cholesky decomposition of symmetric positive definite matrix A =
 * LL^T.
 * 
 * % java Cholesky 2.00000 0.00000 0.00000 0.50000 2.17945 0.00000 0.50000
 * 1.26179 3.62738
 * 
 * 
 * Copyright © 2007, Robert Sedgewick and Kevin Wayne. Last updated: Tue Sep 29
 * 16:17:41 EDT 2009.
 *************************************************************************/

public class Cholesky
{
    private static final double EPSILON = 1e-10;

    // is symmetric
    public static boolean isSymmetric(double[][] A)
    {
	int N = A.length;
	for (int i = 0; i < N; i++)
	{
	    for (int j = 0; j < i; j++)
	    {
		if (A[i][j] != A[j][i])
		    return false;
	    }
	}
	return true;
    }

    // is symmetric
    public static boolean isSquare(double[][] A)
    {
	int N = A.length;
	for (int i = 0; i < N; i++)
	{
	    if (A[i].length != N)
		return false;
	}
	return true;
    }

    // return Cholesky factor L of psd matrix A = L L^T
    public static double[][] cholesky(double[][] A)
    {
	if (!isSquare(A))
	{
	    throw new RuntimeException("Matrix is not square");
	}
	if (!isSymmetric(A))
	{
	    throw new RuntimeException("Matrix is not symmetric");
	}

	int N = A.length;
	double[][] L = new double[N][N];

	for (int i = 0; i < N; i++)
	{
	    for (int j = 0; j <= i; j++)
	    {
		double sum = 0.0;

		for (int k = 0; k < j; k++)
		{
		    sum += L[i][k] * L[j][k];
		}

		if (i == j)
		    L[i][i] = Math.sqrt(A[i][i] - sum);
		else
		    L[i][j] = 1.0 / L[j][j] * (A[i][j] - sum);
	    }

	    if (L[i][i] <= 0)
	    {
		throw new RuntimeException("Matrix not positive definite");
	    }
	}
	return L;
    }

    // sample client
    public static void main(String[] args)
    {
	int N = 3;
	double[][] A = { { 1, -0.5, -0.5 }, { -0.5, 1, -0.5 }, { -0.5, -0.5, 1 } };
	double[][] L = cholesky(A);
	for (int i = 0; i < N; i++)
	{
	    for (int j = 0; j < N; j++)
	    {
		System.out.printf("%8.5f ", L[i][j]);
	    }
	    System.out.println();
	}

	int i;
	int j;

	// !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	L[2][2] = 0;

	for (i = 0; i < 3; i++)
	{
	    System.out.println("v" + i + " norm squared: "
		    + (L[i][0] * L[i][0] + L[i][1] * L[i][1] + L[i][2] * L[i][2]));
	}

	i = 0;
	j = 1;
	System.out
		.println("v"
			+ i
			+ " - v"
			+ j
			+ " norm squared: "
			+ ((L[i][0] - L[j][0]) * (L[i][0] - L[j][0]) + (L[i][1] - L[j][1]) * (L[i][1] - L[j][1]) + (L[i][2] - L[j][2])
				* (L[i][2] - L[j][2])));
	i = 0;
	j = 2;
	System.out
		.println("v"
			+ i
			+ " - v"
			+ j
			+ " norm squared: "
			+ ((L[i][0] - L[j][0]) * (L[i][0] - L[j][0]) + (L[i][1] - L[j][1]) * (L[i][1] - L[j][1]) + (L[i][2] - L[j][2])
				* (L[i][2] - L[j][2])));
	i = 1;
	j = 2;
	System.out
		.println("v"
			+ i
			+ " - v"
			+ j
			+ " norm squared: "
			+ ((L[i][0] - L[j][0]) * (L[i][0] - L[j][0]) + (L[i][1] - L[j][1]) * (L[i][1] - L[j][1]) + (L[i][2] - L[j][2])
				* (L[i][2] - L[j][2])));

	System.out.println("*************************" + 9.871756306943412218e-01);
    }

}
