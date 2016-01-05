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
import grph.GrphAlgorithm;
import jalinopt.LP;
import jalinopt.LPSolver;
import jalinopt.Result;

@SuppressWarnings("serial")
public abstract class LPBasedAlgorithm<R> extends GrphAlgorithm<R>
{
    public static boolean SHOW_LP = false;
    public static boolean SHOW_SOL = false;

    protected abstract LP getLP(Grph g);

    protected abstract R processResult(Result result);

    @Override
    public R compute(Grph g)
    {
	Result r = computeResult(g);
	return processResult(r);
    }

    protected Result computeResult(Grph g)
    {
	LP p = getLP(g);

	if (SHOW_LP)
	    System.out.println(p);

	LPSolver solver = getSolver();
	Grph.logger.log("Calling LP solver " + solver);
	Result result = solver.solve(p);

	if (SHOW_SOL)
	    System.out.println(result);

	return result;
    }

    /**
     * Gets the LP solver that will be used to compute the algorithm.
     * By default use the default Jalinopt solver.
     * You may override it if you want to use another solver.
     * @return
     */
    public LPSolver getSolver()
    {
	return LPSolver.getDefaultSolver();
    }
}
