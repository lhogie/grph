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
package grph.algo.topology;

import java.util.Random;

/**
 *GLP topology (http://www.phil.uu.nl/preprints/preprints/PREPRINTS/preprint278.pdf
 * @author issam
 */
import grph.Grph;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import toools.collections.LucIntSets;

public class GLPIssamTopologyGenerator extends RandomizedTopologyTransform
{
	private Random prng = new Random();

	public Random getPRNG()
	{
		return prng;
	}

	public void setPRNG(Random prng)
	{
		if (prng == null)
			throw new NullPointerException();

		this.prng = prng;
	}

	// number of nodes initially in the backbone before the beginning of the
	// GLP generation
	private int nInitialNodes_ = 6;
	// number of edges added at each step. respects condition: nEdgesPerStep
	// < nInitialNodes
	private double nEdgesPerStep_ = 1.15;
	// the probability of choosing the case of a step with no new vertex is
	// connected.
	private double p_ = 0.4669;
	// a parameter in ]- inf ; 1] expressing to which extent the
	// preferential attachment is influent
	private double beta_ = 0.6753;

	public int getNInitialNodes()
	{
		return nInitialNodes_;
	}

	/**
	 * number of nodes initially in the backbone before the beginning of the GLP
	 * generation
	 * 
	 * @param initialNodes
	 */
	public void setNInitialNodes(int initialNodes)
	{
		if (initialNodes <= 0)
			throw new IllegalArgumentException("wrong nInitialNodes value");

		nInitialNodes_ = initialNodes;
	}

	public double getNEdgesPerStep()
	{
		return nEdgesPerStep_;
	}

	public void setNEdgesPerStep(double edgesPerStep)
	{
		if (edgesPerStep <= 0)
			throw new IllegalArgumentException("wrong nEdgesPerStep value");

		nEdgesPerStep_ = edgesPerStep;
	}

	public double getP()
	{
		return p_;
	}

	public void setP(double p)
	{
		if (0 > p || p > 1)
			throw new IllegalArgumentException("wrong p value");

		this.p_ = p;
	}

	public double getBeta()
	{
		return beta_;
	}

	public void setBeta(double beta)
	{
		if (beta > 1)
		{
			throw new IllegalArgumentException("wrong beta value");
		}

		this.beta_ = beta;
	}

	@Override
	public void compute(Grph graph)
	{
		if (graph.getVertices().isEmpty())
			throw new IllegalArgumentException("the graph must be not empty");

		if (nEdgesPerStep_ > nInitialNodes_)
			throw new IllegalStateException("nEdgesPerStep > nInitialNodes");

		// creation of the small initial backbone
		IntArrayList unconnectedV = new IntArrayList(graph.getVertices().toIntArray());
		int idx = getPRNG().nextInt(unconnectedV.size());
		int first = unconnectedV.get(idx);
		IntArrayList connectedV = new IntArrayList(graph.getVertices().size());
		connectedV.add(first);
		unconnectedV.remove(idx);
		int counter = 0;

		while ( ! unconnectedV.isEmpty() && counter < nInitialNodes_)
		{
			int someVertexIdx = getPRNG().nextInt(unconnectedV.size());
			int someVertex = unconnectedV.getInt(someVertexIdx);
			int someOtherVertex = LucIntSets.pickRandomInt(connectedV, getPRNG());

			if (someVertex != someOtherVertex)
			{
				counter++;
				graph.addUndirectedSimpleEdge(someVertex, someOtherVertex);
				unconnectedV.remove(someVertexIdx);
				connectedV.add(someVertex);
			}
		}

		IntArrayList degrees = new IntArrayList();

		// taking the degrees of the backbone vertices
		for (int i = 0; i < connectedV.size(); ++i)
		{
			degrees.add(i, graph.getNeighbours(connectedV.get(i)).size());
		}

		// connecting the other nodes according to the GLP model
		while ( ! unconnectedV.isEmpty())
		{
			double p2 = getPRNG().nextDouble();
			double step = nEdgesPerStep_;
			double rest = nEdgesPerStep_ - step;

			if (p2 < step)
			{
				++rest;
			}

			// addition of a edge
			if (getPRNG().nextDouble() < p_)
			{
				for (int i = 0; i < rest; ++i)
				{
					if ( ! unconnectedV.isEmpty())
					{
						int v1i = chooseVertexIdx(connectedV, beta_, degrees);
						int v2i = chooseVertexIdx(connectedV, beta_, degrees);
						int v1 = connectedV.get(v1i);
						int v2 = connectedV.get(v2i);

						// if both vertices are not the same and if they are
						// not already connected
						if (v1 != v2 && (graph.getEdgesConnecting(v1, v2).isEmpty()
								&& graph.getEdgesConnecting(v2, v1).isEmpty()))
						{
							graph.addUndirectedSimpleEdge(v1, v2);
							degrees.set(v1i, degrees.get(v1i) + 1);
							degrees.set(v2i, degrees.get(v2i) + 1);
						}
					}
				}
			}
			// addition of a vertex
			else
			{
				int v1i = getPRNG().nextInt(unconnectedV.size());
				int v1 = unconnectedV.get(v1i);
				int v1degree = 0;

				for (counter = 0; counter < rest; counter++)
				{
					if ( ! unconnectedV.isEmpty())
					{
						int v2i = chooseVertexIdx(connectedV, beta_, degrees);
						int v2 = connectedV.get(v2i);

						// if both vertices are not already connected
						if (v1 != v2 && graph.getEdgesConnecting(v1, v2).isEmpty())
						{
							graph.addUndirectedSimpleEdge(v1, v2);
							v1degree++;
							degrees.set(v2i, degrees.get(v2i) + 1);
						}
					}
				}

				unconnectedV.remove(v1i);
				connectedV.add(v1);
				degrees.add(v1degree);
			}
		}
	}

	private int chooseVertexIdx(IntArrayList verticesList, double beta,
			IntArrayList degrees)
	{
		double[] choosingProbality = new double[verticesList.size()];
		double sum = 0;

		for (int i = 0; i < choosingProbality.length; ++i)
		{
			int degree = degrees.get(i);
			choosingProbality[i] = degree - beta;
			sum += choosingProbality[i];
		}

		double choiceNumber = getPRNG().nextDouble() * sum;
		int i = 0;
		double som = choosingProbality[i];

		while (choiceNumber > som)
		{
			i++;
			som += choosingProbality[i];
		}

		return i;
	}

}
