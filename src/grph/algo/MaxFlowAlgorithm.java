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

package grph.algo;

import grph.Grph;
import grph.algo.topology.PlanarGraphTopologyGenerator;
import grph.in_memory.InMemoryGrph;
import grph.properties.ColorProperty;
import grph.properties.NumericalProperty;
import grph.properties.ObjectProperty;
import grph.properties.StringProperty;
import jalinopt.Constraint;
import jalinopt.LP;
import jalinopt.LPSolver;
import jalinopt.LinearExpression;
import jalinopt.PipedLPSolver;
import jalinopt.Result;
import jalinopt.cplex.CPLEX_SSH;

import java.io.IOException;
import java.util.Random;

import toools.set.IntSets;

import com.carrotsearch.hppc.cursors.IntCursor;

public class MaxFlowAlgorithm
{
	public MaxFlowAlgorithmResult compute(Grph g, int s, int t, NumericalProperty capacities)
	{
		return compute(g, s, t, capacities, LPSolver.getDefaultSolver());
	}

	public MaxFlowAlgorithmResult compute(Grph g, int s, int t, NumericalProperty capacities, LPSolver solver)
	{
		MaxFlowAlgorithmResult r = (MaxFlowAlgorithmResult) g.getCache().get(this, s, t);

		if (r == null)
		{
			r = _compute(g, s, t, capacities, solver);
			g.getCache().put(r, this, s, t);
		}

		return r;
	}

	private MaxFlowAlgorithmResult _compute(Grph g, int s, int t, NumericalProperty capacities, LPSolver solver)
	{
		LP lp = new LP();
		LinearExpression objective = lp.getObjective();

		for (IntCursor c : g.getOutEdges(s))
		{
			int e = c.value;

			if (!g.isLoop(e))
			{
				objective.addTerm(1, lp.getVariableByName(e));
			}
		}

		for (IntCursor c : g.getEdges())
		{
			int e = c.value;

			if (!g.isLoop(e))
			{
				// imposes that the flow on every edge is positive or zero
				Constraint positivityConstraint = lp.addConstraint(">=", 0);
				positivityConstraint.getLeftHandSide().addTerm(1, lp.getVariableByName(e));

				// imposes that the flow assigned to one given edge does not
				// exceed
				// its capacity
				Constraint capacityConstraint = lp.addConstraint("<=", capacities.getValueAsDouble(e));
				capacityConstraint.getLeftHandSide().addTerm(1, lp.getVariableByName(e));
			}
		}

		// imposes that the outgoing traffic from a given edge is equal to
		// incoming traffic
		for (int v : g.getVertices().toIntArray())
		{
			if (v != s && v != t)
			{
				Constraint constraint = lp.addConstraint("=", 0);

				for (IntCursor inC : g.getInEdges(v))
				{
					int e = inC.value;

					if (!g.isLoop(e))
					{
						constraint.addTerm(1, lp.getVariableByName(e));
					}
				}

				for (int e : g.getOutEdges(v).toIntArray())
				{
					if (!g.isLoop(e))
					{
						constraint.getLeftHandSide().addTerm(-1, lp.getVariableByName(e));
					}
				}
			}
		}

		for (IntCursor inC : g.getInEdges(s))
		{
			Constraint constraint = lp.addConstraint("=", 0);
			constraint.getLeftHandSide().addTerm(1, lp.getVariableByName(inC.value));
		}

		for (IntCursor outC : g.getOutEdges(t))
		{
			Constraint constraint = lp.addConstraint("=", 0);
			constraint.getLeftHandSide().addTerm(1, lp.getVariableByName(outC.value));
		}

		Result r = lp.solve(solver);

		if (r == null)
		{
			return null;
		}
		else
		{
			return new MaxFlowAlgorithmResult(r, s, t);
		}
	}

	public static void main(String[] args) throws IOException
	{
		PipedLPSolver.DEBUG = true;
		ClassLoader.getSystemClassLoader().setDefaultAssertionStatus(true);
		Grph g = new InMemoryGrph();
		g.addNVertices(10);
		PlanarGraphTopologyGenerator.perform(g, new Random());

		g.highlightVertices(IntSets.from(0, g.getNumberOfVertices() - 1));

		System.out.println(g.getShortestPath(0, g.getNumberOfVertices() - 1));

		int s = 0;
		int t = g.getNumberOfVertices() - 1;

		NumericalProperty capacities = new NumericalProperty("edge capacity", 64, 1);

		MaxFlowAlgorithmResult maxFlow = new MaxFlowAlgorithm().compute(g, s, t, capacities, new CPLEX_SSH("musclotte"));
		System.out.println(maxFlow);

		NumericalProperty edgeColors = new ColorProperty("edge color");
		ObjectProperty<String> edgelabels = new StringProperty("edge labels");
		g.setEdgesWidth(capacities);
		maxFlow.display(g);

		System.out.println("flow: " + maxFlow.getFlow());
		g.display();

	}
}
