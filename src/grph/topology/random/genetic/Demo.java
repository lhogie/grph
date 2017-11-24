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

import cnrs.i3s.papareto.Evaluator;
import cnrs.i3s.papareto.NoRepresentation;
import cnrs.i3s.papareto.Population;
import cnrs.i3s.papareto.Representation;
import grph.Grph;

public class Demo
{
	public static void main(String[] args)
	{
		System.out.println("starting");

		Representation<Grph, Grph> r = new NoRepresentation<>();

		GraphPopulation p = new GraphPopulation();

		p.getEvaluators().add(new Evaluator<Grph, Grph>()
		{

			@Override
			public double evaluate(Grph g, Population<Grph, Grph> p)
			{
				if (g.isNull() || ! g.isConnected() || g.hasMultipleEdges()
						|| g.hasLoops()
						|| g.getNumberOfUndirectedEdges() != g.getNumberOfEdges())
				{
					return Double.NEGATIVE_INFINITY;
				}
				else
				{
					int ddiff = Math
							.abs(g.getTwoSweepBFSDiameterApproximatedDiameter() - 10);
					int ndiff = Math.abs(g.getVertices().size() - 10);
					double avgD = Math.abs(g.getAverageDegree() - 10);
					return - (ddiff + ndiff + avgD);
				}
			}
		});

		Random random = new Random();
		p.fillRandomly(30, random);
		// p.setAllowsAsynchronousUpdates(true);
		// p.monitor();

		while (true)
		{
			p.getEvolver().iterate(p, random);
			System.out.println(p);

			if (p.getIndividualAt(0).fitness.elements[0] == 0)
			{
				break;

			}
		}

		System.out.println("completed");
		p.getIndividualAt(0).object.display();
	}

}
