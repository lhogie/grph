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

package grph.algo.covering_packing;

import grph.Grph;
import grph.algo.StructuredLPBasedAlgorithm;
import it.unimi.dsi.fastutil.ints.IntSet;
import jalinopt.Constraint;
import jalinopt.LP;
import jalinopt.LP.OptimizationType;
import jalinopt.Result;
import jalinopt.Variable;
import jalinopt.Variable.TYPE;
import toools.collections.primitive.SelfAdaptiveIntSet;

/**
 * An algorithm to solve the maximum bipartite matching problem. It is based on
 * the classical reduction to a network flow problem, described by a linear
 * program and then solved by a solver.
 * 
 * @author Gregory Morel
 */
@SuppressWarnings("serial")
public class LPBasedMaximumMatchingAlgorithm extends StructuredLPBasedAlgorithm<IntSet>
{
	@Override
	protected OptimizationType getOptimizationType()
	{
		return OptimizationType.MAX;
	}

	@Override
	protected TYPE getVariableType(Variable v)
	{
		return TYPE.BOOLEAN;
	}

	@Override
	protected void setObjective(LP lp, Grph g)
	{
		for (int v : g.getEdges().toIntArray())
		{
			lp.getObjective().addTerm(1, lp.getVariableByName(v));
		}
	}

	@Override
	protected void setConstraints(Grph g, LP p)
	{
		for (int v : g.getVertices().toIntArray())
		{
			Constraint constraint = p.addConstraint();

			for (int e : g.getEdgesIncidentTo(v).toIntArray())
			{
				constraint.getLeftHandSide().addTerm(1, p.getVariableByName(e));
			}

			constraint.setOperator("<=");
			constraint.setRightHandSide(1);
		}
	}

	@Override
	protected IntSet processResult(Result r)
	{
		IntSet resultSet = new SelfAdaptiveIntSet();

		for (Variable variable : r.getVariables())
		{
			if (variable.getValue() == 1)
			{
				resultSet.add(LP.var2i(variable));
			}
		}

		return resultSet;
	}
}
