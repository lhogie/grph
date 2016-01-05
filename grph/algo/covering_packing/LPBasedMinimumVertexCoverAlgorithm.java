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
import grph.in_memory.InMemoryGrph;
import jalinopt.LP.OptimizationType;
/**
 * computes the minimum vertex cover for the given graph (using LP).
 * @author lhogie
 *
 */
public class LPBasedMinimumVertexCoverAlgorithm extends LPBasedFubarAlgorithm
{

	@Override
	protected OptimizationType getOptimizationType()
	{
		return OptimizationType.MIN;
	}

	@Override
	protected String getOperator()
	{
		return ">=";
	}

	public static void main(String[] args)
	{
		Grph g = new InMemoryGrph();
		g.grid(4, 4);
		g.highlightVertices(new LPBasedMinimumVertexCoverAlgorithm().compute(g));
		g.display();
	}
}
