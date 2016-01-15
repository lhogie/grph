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
import grph.in_memory.InMemoryGrph;
import toools.math.IntMatrix;

import com.carrotsearch.hppc.cursors.IntCursor;

/**
 * Compute the incidence matrix for the given graph. Lines are vertices. Columns
 * are edges. Each cell indicates if the corresponding vertex is incident to the
 * given edge. In the case of directed edges, 1 indicates that the vertices is
 * head, -1 that it is tail. In the case of undirected edges, 1 indicates that
 * the vertex is incident.
 * 
 * 
 * @author lhogie
 * 
 */

public class IncidenceMatrixAlgorithm extends GrphAlgorithm<IntMatrix>
{

    @Override
    public IntMatrix compute(Grph g)
    {
	int n = g.getVertices().size() + 1;
	IntMatrix matrix = new IntMatrix(n, g.getEdges().size());

	for (IntCursor c : g.getEdges())
	{
	    int e = c.value;

	    if (g.isUndirectedSimpleEdge(e))
	    {
		int v1 = g.getOneVertex(e);
		int v2 = g.getTheOtherVertex(e, v1);
		matrix.set(v1, e, 1);
		matrix.set(v2, e, 1);
	    }
	    else if (g.isDirectedSimpleEdge(e))
	    {
		int h = g.getDirectedSimpleEdgeHead(e);
		int t = g.getDirectedSimpleEdgeTail(e);
		matrix.set(h, e, 1);
		matrix.set(t, e, -1);
	    }
	    else if (g.isUndirectedHyperEdge(e))
	    {
		for (IntCursor cv : g.getVerticesIncidentToEdge(e))
		{
		    matrix.set(cv.value, e, 1);
		}
	    }
	    else if (g.isDirectedHyperEdge(e))
	    {
		for (IntCursor cv : g.getDirectedHyperEdgeTail(e))
		{
		    matrix.set(cv.value, e, -1);
		}
		for (IntCursor cv : g.getDirectedHyperEdgeHead(e))
		{
		    matrix.set(cv.value, e, 1);
		}
	    }
	}

	return matrix;
    }

    public static void main(String[] args)
    {
	Grph g = new InMemoryGrph();
	g.dgrid(3, 3);
	g.display();
	System.out.println(g.getIncidenceMatrix());
    }

}
