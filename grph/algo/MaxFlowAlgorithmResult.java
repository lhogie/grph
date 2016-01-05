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
import jalinopt.LP;
import jalinopt.Result;
import jalinopt.Variable;

import com.carrotsearch.hppc.IntDoubleMap;
import com.carrotsearch.hppc.IntDoubleOpenHashMap;
import com.carrotsearch.hppc.cursors.IntCursor;

public class MaxFlowAlgorithmResult
{
	private final double flow;
	private final IntDoubleMap edgeAssigments = new IntDoubleOpenHashMap();
	private final int s, t;

	public MaxFlowAlgorithmResult(Result r, int s, int t)
	{
		this.s = s;
		this.t = t;
		this.flow = r.getObjective();

		for (Variable v : r.getVariables())
		{
			edgeAssigments.put(LP.var2i(v), v.getValue());
		}
	}

	public double getFlow()
	{
		return flow;
	}

	public IntDoubleMap getAssigments()
	{
		return edgeAssigments;
	}

	public void display(Grph g)
	{
		for (IntCursor n : getAssigments().keys())
		{
			int e = n.value;
			double a = getAssigments().get(e);

			if (a > 0)
			{
				 g.getEdgeColorProperty().setValue(e, 4);
				 g.getEdgeColorProperty().setValue(e, String.valueOf(a));
			}
		}

		g.getVertexLabelProperty().setValue(s, "S");
		g.getVertexLabelProperty().setValue(t, "T");
	}

	@Override
	public String toString()
	{
		StringBuilder b = new StringBuilder();

		b.append("flow from " + s + " to " + t + " is " + flow);

		return b.toString();
	}

}
