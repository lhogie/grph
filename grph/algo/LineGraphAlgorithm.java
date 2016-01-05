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

/**
 * Compute the line graph of the given graph.
 * 
 * @author Gregory Morel
 * 
 */
@SuppressWarnings("serial")
public class LineGraphAlgorithm extends GrphAlgorithm<Grph>
{
	@Override
	public Grph compute(Grph g)
	{
		Grph lineGraph = new InMemoryGrph();

		for (int e : g.getEdges().toIntArray())
		{
			lineGraph.addVertex(e);
			lineGraph.getVertexLabelProperty().setValue(e, g.getEdgeLabelProperty().getValueAsString(e));
			lineGraph.getVertexColorProperty().setValue(e, g.getEdgeColorProperty().getValueAsString(e));
		}

		// Because of some assertions, the previous loop has to be completed
		// before this one
		for (int a : g.getEdges().toIntArray())
		{
			for (int b : g.getEdgesAdjacentToEdge(a).toIntArray())
			{
				if (!lineGraph.areVerticesAdjacent(a, b))
				{
					lineGraph.addUndirectedSimpleEdge(a, b);
				}
			}
		}

		return lineGraph;
	}
}
