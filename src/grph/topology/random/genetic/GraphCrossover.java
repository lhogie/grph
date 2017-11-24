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

package grph.topology.random.genetic;

import java.util.Random;

import cnrs.i3s.papareto.NewChildOperator;
import cnrs.i3s.papareto.Population;
import grph.Grph;

/**
 * It takes half of the vertices in g1 and the other half in g2. It takes half
 * of the edges in g1 and the other half in g2.
 * 
 * @author lhogie
 *
 */

public class GraphCrossover extends NewChildOperator<Grph, Grph>
{

	@Override
	public Grph createNewChild(Population<Grph, Grph> p, Random r)
	{
		assert p.size() > 1;

		Grph g1 = p.pickRandomIndividual(r).object;
		Grph g2 = g1;

		while (g2 == g1)
		{
			g2 = p.pickRandomIndividual(r).object;
		}

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
				if ( ! h.getVertices().contains(v))
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

				if ( ! h.getEdges().contains(e) && h.getVertices().contains(v1)
						&& h.getVertices().contains(v2))
				{
					h.addUndirectedSimpleEdge(e, v1, v2);
				}
			}
		}
		return h;
	}

}
