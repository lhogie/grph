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
 
 package grph.algo.covering_packing;

import grph.Grph;
import grph.algo.StructuredLPBasedAlgorithm;
import jalinopt.Constraint;
import jalinopt.LP;
import jalinopt.LP.OptimizationType;
import jalinopt.Result;
import jalinopt.Variable;
import jalinopt.Variable.TYPE;
import toools.set.DefaultIntSet;
import toools.set.IntSet;

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
	IntSet resultSet = new DefaultIntSet();

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
