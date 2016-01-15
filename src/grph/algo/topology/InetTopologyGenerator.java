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

package grph.algo.topology;

import grph.Grph;
import grph.in_memory.InMemoryGrph;
import grph.io.EdgeListReader;
import grph.io.GraphBuildException;
import grph.io.ParseException;

import java.util.Random;

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
