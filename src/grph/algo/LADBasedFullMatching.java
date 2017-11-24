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

package grph.algo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import grph.Grph;
import grph.algo.lad.LAD.MODE;
import grph.algo.lad.DirectedLAD;
import grph.algo.lad.LAD;
import grph.algo.lad.UndirectedLAD;
import grph.in_memory.InMemoryGrph;
import grph.properties.ColorProperty;
import grph.properties.Property;
import grph.util.Matching;

public class LADBasedFullMatching
{
	public static Collection<Matching> compute(Grph g, Grph p,
			List<Property> graphProperties, boolean findAllMatchings,
			List<Property> patternProperties)
	{
		return compute(g, p, findAllMatchings, true, graphProperties, patternProperties);
	}

	public static Collection<Matching> compute(Grph g, Grph p, boolean findAllMatchings,
			boolean matchDirection, List<Property> graphProperties,
			List<Property> patternProperties)
	{
		Collection<Matching> matchings = new ArrayList<Matching>();

		 LAD lad = matchDirection ? new DirectedLAD() : new UndirectedLAD();
		
		for (Matching m : lad.lad(g.toUndirectedGraph(), p.toUndirectedGraph(),
				MODE.INDUCED, true))
		{
			if (( ! matchDirection || directionsAreOk(g, p, m))
					&& (propertiesAreOk(g, p, m, graphProperties, patternProperties)))
			{
				matchings.add(m);

				if ( ! findAllMatchings)
				{
					return matchings;
				}
			}
		}

		return matchings;
	}

	private static boolean propertiesAreOk(Grph g, Grph pattern, Matching matching,
			List<Property> graphProperties, List<Property> patternProperties)
	{
		if (graphProperties.size() != patternProperties.size())
			throw new IllegalArgumentException();

		for (int i = 0; i < graphProperties.size(); ++i)
		{
			Property gp = graphProperties.get(i);
			Property pp = patternProperties.get(i);

			for (int pv : pattern.getVertices().toIntArray())
			{
				int gv = matching.pattern2graph().get(pv);

				if ( ! gp.getValueAsString(pv).equals(pp.getValueAsString(gv)))
				{
					return false;
				}
			}
		}

		return true;
	}

	private static boolean directionsAreOk(Grph g, Grph p, Matching matching)
	{
		for (int pe : p.getEdges().toIntArray())
		{
			if (p.isDirectedSimpleEdge(pe))
			{
				int pt = p.getDirectedSimpleEdgeTail(pe);
				int ph = p.getDirectedSimpleEdgeHead(pe);
				int gt = matching.pattern2graph().get(pt);
				int gh = matching.pattern2graph().get(ph);

				if ( ! g.areVerticesAdjacent(gt, gh))
				{
					return false;
				}
			}
		}

		return true;
	}

	public static void main(String[] args)
	{
		Grph g = new InMemoryGrph();
		g.dgrid(3, 3);
		ColorProperty gcp = new ColorProperty("acolor");
		gcp.setValue(0, 5);
		gcp.setValue(1, 6);
		gcp.setValue(2, 7);
		gcp.setValue(3, 5);
		gcp.setValue(4, 6);
		gcp.setValue(5, 7);

		Grph h = new InMemoryGrph();
		h.ensureNVertices(3);
		h.chain(true);
		ColorProperty hcp = new ColorProperty("b");
		hcp.setValue(0, 5);
		hcp.setValue(1, 6);
		hcp.setValue(2, 7);

		Collection<Matching> r = compute(g, h, true, true, null, null);
		System.out.println(r.size());
		System.out.println(r);

		g.display();
		h.display();
	}

}
