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
 
 

package grph.algo.subgraph_isomorphism.own;

import java.util.ArrayList;
import java.util.Collection;

import grph.Grph;
import grph.algo.topology.ClassicalGraphs;
import grph.path.Path;
import grph.properties.Property;
import grph.properties.StringProperty;

public class EdgeLabelBasedSubgraphMatcher extends LabelBasedSubgraphMatcher
{

	@Override
	public String pathToString(Path p, Property vertexLabels,
			Property edgeLabels)
	{
		if (p.getLength() > 0)
		{
			StringBuilder b = new StringBuilder();
			int sz = p.getNumberOfVertices();

			for (int i = 1; i < sz; ++i)
			{
				int e = p.getEdgeHeadingToVertexAt(i);

				if (e >= 0)
				{
					b.append(edgeLabels.getValueAsString(e));
				}
			}

			return b.toString();
		}
		else
		{
			return "";
		}
	}

	public static void main(String[] args)
	{
		Grph g = ClassicalGraphs.grid(4, 4);

		Property p = new StringProperty("edge label");

		for (int e : g.getEdges().toIntArray())
		{
			p.setValue(e, "e" + e);
		}

		g.display();

		Collection<String> patterns = new ArrayList<String>();
		patterns.add("e8e10e12");
		System.out.println(new EdgeLabelBasedSubgraphMatcher()
				.findAllPathsMatching(g, patterns, null, p));
	}

}
