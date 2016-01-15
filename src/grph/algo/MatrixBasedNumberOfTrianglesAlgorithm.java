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

/**
 * 
 * @author Gregory Morel
 * 
 */

@SuppressWarnings("serial")
public class MatrixBasedNumberOfTrianglesAlgorithm extends GrphAlgorithm<Integer>
{

    /**
     * Computes the number of triangles in g (Note: the number of triangles in a
     * graph equals the trace of A^3 / 6, where A is the adjacency matrix of the
     * graph).
     */
    @Override
    public Integer compute(Grph g)
    {
	AdjacencyMatrix A = g.getAdjacencyMatrix();
	AdjacencyMatrix A3 = AdjacencyMatrix.power(A, 3);

	int trace = 0;
	int sz = A3.getSize();

	for (int i = 0; i < sz; ++i)
	{
	    trace += A3.get(i, i);
	}

	return trace / 6;
    }
    
   
}
