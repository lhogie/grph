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
 
 

package grph.algo.topology;

import java.util.Random;

import grph.Grph;
import grph.in_memory.InMemoryGrph;
import grph.io.EdgeListReader;
import grph.io.GraphBuildException;
import grph.io.ParseException;
import toools.extern.Proces;

/**
 * Provides a bridge to the INET topology generator
 * (http://topology.eecs.umich.edu/inet/).
 * 
 * @author lhogie
 * 
 */
public class InetTopologyGenerator extends RandomizedTopologyTransform
{
	private double fraction_of_degree_one_nodes = 0.36;
	private double plane_dimension = 1;

	public double getFractionOfDegreeOneNodes()
	{
		return fraction_of_degree_one_nodes;
	}

	public void setFractionOfDegreeOneNodes(double fraction_of_degree_one_nodes)
	{
		if (!(0 <= fraction_of_degree_one_nodes) && (fraction_of_degree_one_nodes <= 1))
			throw new IllegalArgumentException();

		this.fraction_of_degree_one_nodes = fraction_of_degree_one_nodes;
	}

	public double getPlaneDimension()
	{
		return plane_dimension;
	}

	public void setPlaneDimension(double plane_dimension)
	{
		if (plane_dimension <= 0)
			throw new IllegalArgumentException();

		this.plane_dimension = plane_dimension;
	}

	@Override
	public void compute(Grph graph)
	{
		if (graph.getVertices().size() < 3037)
			throw new IllegalArgumentException("The number of nodes must be no less than 3037 to use inet.");

		String output = new String(
				Proces.exec("inet", "-n", "" + graph.getVertices().size() + "-p" + getPlaneDimension(), "-d" + getFractionOfDegreeOneNodes()));

		try
		{
			new EdgeListReader().readGraph(output);
		}
		catch (ParseException e)
		{
			throw new IllegalStateException(e);
		}
		catch (GraphBuildException e)
		{
			throw new IllegalStateException(e);
		}
	}

	public static Grph inet(int n, double p, int d, long seed)
	{
		Grph graph = new InMemoryGrph();
		graph.addNVertices(n);

		Random r = new Random(seed);
		InetTopologyGenerator g = new InetTopologyGenerator();
		g.setPRNG(r);
		g.setFractionOfDegreeOneNodes(p);
		g.setPlaneDimension(d);
		g.compute(graph);

		return graph;
	}

}
