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
 
 package grph.topology.random.genetic;

import grph.Grph;

import java.util.Random;

import cnrs.i3s.papareto.CrossoverOperator;
import cnrs.i3s.papareto.Individual;
import cnrs.i3s.papareto.Population;



/**
 * It takes half of the vertices in g1 and the other half in g2.
 * It takes half of the edges in g1 and the other half in g2.
 * 
 * @author lhogie
 *
 */

public class GraphCrossover extends CrossoverOperator<Grph>
{
    @Override
    public String getFriendlyName()
    {
	return "Grph crossover";
    }

    @Override
    public Grph crossover(Individual<Grph> i1, Individual<Grph> i2, Population<Grph> p, Random r)
    {
	Grph g1 = i1.object;
	Grph g2 = i2.object;
	Grph h = new grph.in_memory.InMemoryGrph();

	for (int v : g1.getVertices().toIntArray())
	{
	    if (r.nextBoolean())
	    {
		h.addVertex(v);
	    }
	}

	for (int v : g2.getVertices().toIntArray())
	{
	    if (r.nextBoolean())
	    {
		if (!h.getVertices().contains(v))
		{
		    h.addVertex(v);
		}
	    }
	}

	for (int e : g1.getEdges().toIntArray())
	{
	    if (r.nextBoolean())
	    {
		int v1 = g1.getOneVertex(e);
		int v2 = g1.getTheOtherVertex(e, v1);

		if (h.getVertices().contains(v1) && h.getVertices().contains(v2))
		{
		    h.addUndirectedSimpleEdge(e, v1, v2);
		}
	    }
	}

	for (int e : g2.getEdges().toIntArray())
	{
	    if (r.nextBoolean())
	    {
		int v1 = g2.getOneVertex(e);
		int v2 = g2.getTheOtherVertex(e, v1);

		if (!h.getEdges().contains(e) && h.getVertices().contains(v1) && h.getVertices().contains(v2))
		{
		    h.addUndirectedSimpleEdge(e, v1, v2);
		}
	    }
	}
	return h;
    }

}
