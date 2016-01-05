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

package grph.algo.subgraph_isomorphism.own;

import grph.Grph;
import grph.in_memory.InMemoryGrph;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Demo
{
	public static void main(String[] args)
	{

		Grph g = new InMemoryGrph();
		g.dgrid(3, 3);
		g.getVertexLabelProperty().setValue(0, "a");
		g.getVertexLabelProperty().setValue(1, "a");
		g.getVertexLabelProperty().setValue(2, "b");
		g.getVertexLabelProperty().setValue(3, "b");
		g.getVertexLabelProperty().setValue(4, "c");
		g.getVertexLabelProperty().setValue(5, "d");
		g.getVertexLabelProperty().setValue(6, "b");
		g.getVertexLabelProperty().setValue(7, "c");
		g.getVertexLabelProperty().setValue(8, "a");

		g.display();

		List<String> patterns = new ArrayList<String>();
		// patterns.add("adcb+");
		// patterns.add("cca");
		patterns.add("bcda");

		Set<Grph> result = new VertexLabelBasedSubgraphMatcher()
				.findAllMatches(g, patterns, g.getVertexLabelProperty(), null);
		Grph rg = result.iterator().next();
		System.out.println(rg);
		g.highlightVertices(rg.getVertices());
	}

}
