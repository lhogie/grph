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

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package grph.algo.sparse_cut;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.StringTokenizer;

import grph.Grph;
import grph.GrphAlgorithm;
import grph.in_memory.InMemoryGrph;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import toools.collections.LucIntSets;
import toools.extern.Proces;
import toools.io.JavaResource;
import toools.io.file.RegularFile;
import toools.os.Linux;
import toools.os.MacOSX;
import toools.os.OperatingSystem;

/**
 * 
 * @author Issam Tahiri, Luc Hogie
 * 
 *         Approximation log(n) du sparsest cut de
 * 
 *         Expander Flows, Geometric Embeddings and Graph Partitioning
 * 
 */
public class SparseCutAlgorithm extends GrphAlgorithm<Collection<IntSet>>
{
	/*
	 * public Collection<IntSet> compute(Grph g, int n) { if (n < 2) throw new
	 * IllegalArgumentException();
	 * 
	 * else if (n == 2) { return compute(g); } else {
	 * 
	 * } }
	 */
	@Override
	public Collection<IntSet> compute(Grph g)
	{
		RegularFile inputFile = RegularFile.createTempFile();

		inputFile.setContent(convertInputGraph(g).getBytes());
		RegularFile outputFile = RegularFile.createTempFile();
		// Proces.TRACE_CALLS = true;
		Proces.exec(getExecutableFile().getPath(), inputFile.getPath(),
				outputFile.getPath());

		String outText = new String(outputFile.getContent());

		inputFile.delete();
		outputFile.delete();

		IntSet part = new IntOpenHashSet(
				parseOutputData(g.getNumberOfVertices(), outText));
		Collection<IntSet> sets = new ArrayList<IntSet>();
		sets.add(part);
		sets.add(LucIntSets.difference(g.getVertices(), part));
		return sets;
	}

	private RegularFile getExecutableFile()
	{
		RegularFile exeFile = new RegularFile(Grph.COMPILATION_DIRECTORY, "csdp");

		if ( ! exeFile.exists())
		{
			try
			{
				getResource().exportToFile(exeFile);
				exeFile.setExecutable(true);
				return exeFile;
			}
			catch (IOException e)
			{
				throw new IllegalStateException(e);
			}
		}

		return exeFile;
	}

	private JavaResource getResource()
	{
		if (OperatingSystem.getLocalOperatingSystem() instanceof MacOSX)
		{
			return new JavaResource(SparseCutAlgorithm.class,
					"csdp6.0.1maccore/bin/csdp");
		}
		else if (OperatingSystem.getLocalOperatingSystem() instanceof Linux)
		{
			return new JavaResource(SparseCutAlgorithm.class,
					"csdp6.1.0linuxp4/bin/csdp");
		}
		else
		{
			throw new IllegalStateException("unsupported OS: "
					+ OperatingSystem.getLocalOperatingSystem().getName());
		}
	}

	private String convertInputGraph(Grph g)
	{
		int n = g.getNumberOfVertices();

		StringWriter sw = new StringWriter();
		PrintWriter out = new PrintWriter(sw);

		// nombre de contraintes
		out.println(n * (n - 1) * (n - 2) / 2 + n + 1);

		// nombre de blocs dans les matrices
		out.println(2);

		// taille des blocs. Le deuxieme bloc est diagonal sert juste a
		// transformer les inegalités en égalités
		out.println(n + " -" + (1 + n * (n - 1) * (n - 2) / 2));
		int equalityN = 1;

		// des valeurs réelles (coté droit des contraintes)
		for (int i = 0; i < n; i++)
		{
			out.print(1 + " ");
		}
		out.print(n * (n - 1) + " ");
		for (int i = 0; i < n * (n - 1) * (n - 2) / 2; i++)
		{
			out.print(0 + " ");
		}
		out.println();

		// fonction objective à !!!!minimiser !!!!!
		double[][] C = new double[n + 1][n + 1];

		for (int e : g.getEdges().toIntArray())
		{
			int i = g.getOneVertex(e);
			int j = g.getTheOtherVertex(e, i);
			C[i][i] += 1;
			C[j][j] += 1;
			int min = Math.min(i, j);
			int max = Math.max(i, j);
			C[min][max] -= 1;

		}
		for (int i = 1; i <= n; i++)
		{
			for (int j = 1; j <= n; j++)
			{
				if (C[i][j] != 0)
				{
					// fonction objective(0). 1er bloc. ieme ligne. jeme
					// colonne. la valeur C[i][j]. ( le - car minimisation)
					out.println(0 + " 1 " + i + " " + j + " " + ( - C[i][j]));
				}
			}
		}

		// n premieres contraintes: les n vecteurs sont unitaires.
		for (int i = 1; i <= n; i++)
		{
			// ieme contrainte. 1er bloc. ieme ligne. ieme colonne. la valeur 1.
			out.println(i + " 1 " + i + " " + i + " 1");
		}

		// (n+1)eme contrainte: la moyenne des distance est au min une
		// constante.
		int[][] A = new int[n + 1][n + 1];
		for (int i = 1; i <= n; i++)
		{
			for (int j = i + 1; j <= n; j++)
			{
				A[i][i]++;
				A[j][j]++;
				A[i][j] = A[i][j] - 1;
			}
		}
		for (int i = 1; i <= n; i++)
		{
			for (int j = 1; j <= n; j++)
			{
				if (A[i][j] != 0)
				{
					// (n+1)eme contrainte. 1er bloc. ieme ligne. jeme colonne.
					// la valeur A[i][j].
					out.println((n + 1) + " 1 " + i + " " + j + " " + A[i][j]);
				}
			}
		}
		out.println((n + 1) + " 2 " + equalityN + " " + equalityN + " -1");
		equalityN++;

		// contraintes d'inégalité triangulaire.
		int contrainteN = n + 2;
		for (int i = 1; i <= n; i++)
		{
			for (int k = i + 1; k <= n; k++)
			{
				for (int j = 1; j <= n; j++)
				{
					if (j != i && j != k)
					{
						// contrainteN eme contrainte. 1er bloc. jeme ligne.
						// jeme colonne. la valeur 2.
						out.println(contrainteN + " 1 " + j + " " + j + " " + 2);
						// contrainteN eme contrainte. 1er bloc. ieme ligne.
						// keme colonne. la valeur 2.
						out.println(contrainteN + " 1 " + Math.min(i, k) + " "
								+ Math.max(i, k) + " " + 1);
						// contrainteN eme contrainte. 1er bloc. ieme ligne.
						// jeme colonne. la valeur -2.
						out.println(contrainteN + " 1 " + Math.min(i, j) + " "
								+ Math.max(i, j) + " " + - 1);
						// contrainteN eme contrainte. 1er bloc. keme ligne.
						// jeme colonne. la valeur -2.
						out.println(contrainteN + " 1 " + Math.min(k, j) + " "
								+ Math.max(k, j) + " " + - 1);
						// contrainteN eme contrainte. 2eme bloc. 1ere ligne.
						// 1ere colonne. la valeur 1.
						out.println(contrainteN + " 2 " + equalityN + " " + equalityN
								+ " -1");
						equalityN++;

						contrainteN++;
					}
				}
			}
		}

		out.close();
		return sw.toString();
	}

	private static double[] difference(double[] v1, double[] v2)
	{

		double[] ret = new double[v1.length];

		if (v1.length != v2.length)
		{
			throw new IllegalStateException();
		}
		else
		{
			for (int i = 0; i < v1.length; i++)
			{
				ret[i] = v1[i] - v2[i];
			}
		}

		return ret;
	}

	private static double squareNorm(double[] v1)
	{

		double ret = 0;

		for (int i = 0; i < v1.length; i++)
		{
			ret += v1[i] * v1[i];
		}

		return ret;
	}

	public static Collection<Integer> parseOutputData(int n, String text)
	{

		Collection<Integer> ret = new ArrayList<Integer>();

		double[][] A = new double[n][n];

		Scanner in = new Scanner(text);
		while (in.hasNextLine())
		{
			StringTokenizer s = new StringTokenizer(in.nextLine());
			if (s.nextToken().equals("2") && s.nextToken().equals("1"))
			{
				int lineN = Integer.parseInt(s.nextToken()) - 1;
				int columnN = Integer.parseInt(s.nextToken()) - 1;
				double value = Double.parseDouble(s.nextToken());

				if (lineN == columnN)
				{
					A[lineN][columnN] = value;
				}
				else
				{
					A[lineN][columnN] = value;
					A[columnN][lineN] = value;
				}
			}
		}

		double[][] L = Cholesky.cholesky(A);

		int d = L[0].length;

		List<double[]> P = new ArrayList<double[]>();
		List<double[]> N = new ArrayList<double[]>();

		while (true)
		{

			P.clear();
			N.clear();

			// FINDING A GOOD CUT: STEP1 (choosing a random line u)
			// get a random vector u
			double[] u = new double[d];
			for (int i = 0; i < d; i++)
			{
				u[i] = Math.random();
			}

			// compute the norm of u
			double uNorm = Math.sqrt(squareNorm(u));

			// normalize u
			for (int i = 0; i < d; i++)
			{
				u[i] = u[i] / uNorm;
			}

			double avgDiff = 0;
			int number = 0;
			for (int k = 0; k < n; k++)
			{
				for (int l = k + 1; l < n; l++)
				{
					double diff = squareNorm(difference(L[k], L[l]));
					avgDiff += diff;
					number++;
				}
			}
			avgDiff = avgDiff / number;

			// FINDING A GOOD CUT: STEP2 (filling P and N)

			for (int i = 0; i < n; i++)
			{
				double sp = scalarProduct(u, L[i]);
				if (sp >= 1 / Math.sqrt(d))
				{
					P.add(L[i]);
				}
				else
				{
					N.add(L[i]);
				}
			}

			if (P.size() == 0)
			{
				continue;
			}

			// FINDING A GOOD CUT: STEP3 (discarding bad edges of points)
			double delta = 1 / Math.sqrt(Math.log(n));

			for (int a = 0; a < P.size(); a++)
			{
				double[] x = P.get(a);

				for (int b = 0; b < N.size(); b++)
				{
					double[] y = N.get(b);

					double diffNormSquare = squareNorm(difference(x, y));

					if (diffNormSquare <= delta)
					{
						P.remove(x);
						N.remove(y);
					}
				}
			}

			if (P.size() == 0)
			{
				continue;
			}

			// FINDING A GOOD CUT: STEP4 (constructing partitions)
			double r = delta; // Math.random() * delta;
			java.util.Collections.shuffle(P);
			double[] randomVector = P.iterator().next();

			Set<double[]> partition = new HashSet<double[]>();
			for (double[] x : P)
			{

				double diffNormSquare = squareNorm(difference(x, randomVector));

				if (diffNormSquare <= r)
				{
					partition.add(x);
				}
			}

			// FINDING A GOOD CUT: STEP5 (outputing solution)

			for (double[] x : partition)
			{
				for (int k = 0; k < n; k++)
				{
					double[] y = L[k];
					double diffNormSquare = squareNorm(difference(x, y));

					if (diffNormSquare == 0)
					{
						ret.add(k + 1);
					}
				}
			}

			break;
		}

		in.close();
		return ret;

	}

	public static double scalarProduct(double[] v1, double[] v2)
	{
		double ret = 0;

		if (v1.length != v2.length)
		{
			throw new IllegalStateException();
		}
		else
		{
			for (int i = 0; i < v1.length; i++)
			{
				ret += v1[i] * v2[i];
			}
		}

		return ret;
	}

	public static void main(String[] args) throws FileNotFoundException, IOException
	{
		// build a graph
		Grph g = new InMemoryGrph();
		g.addUndirectedSimpleEdge(1, 2);
		g.addUndirectedSimpleEdge(1, 4);
		g.addUndirectedSimpleEdge(1, 5);
		g.addUndirectedSimpleEdge(2, 3);
		g.addUndirectedSimpleEdge(2, 4);
		g.addUndirectedSimpleEdge(2, 6);
		g.addUndirectedSimpleEdge(3, 7);
		g.addUndirectedSimpleEdge(3, 8);
		g.addUndirectedSimpleEdge(4, 6);
		g.addUndirectedSimpleEdge(5, 6);
		g.addUndirectedSimpleEdge(6, 7);
		g.addUndirectedSimpleEdge(7, 8);

		// display the graph using attraction/repulsion-based layout
		g.display();

		// compute the cut
		SparseCutAlgorithm algo = new SparseCutAlgorithm();
		Collection<IntSet> r = algo.compute(g);

		// show it clearly on the displayed graph
		g.highlightVertices(r);
	}
}
