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
Julien Deantoni (I3S, Université Cote D'Azur, Saclay) 

*/
 
 

package grph.algo.subgraph_isomorphism.own;

import java.util.ArrayList;
import java.util.Collection;

import grph.Grph;
import grph.algo.topology.ClassicalGraphs;
import grph.path.Path;
import grph.properties.Property;
import grph.properties.StringProperty;

public class VertexLabelBasedSubgraphMatcher extends LabelBasedSubgraphMatcher
{

	@Override
	public String pathToString(Path p, Property vertexLabels,
			Property edgeLabels)
	{
		StringBuilder b = new StringBuilder();

		for (int v : p.toVertexArray())
		{
			b.append(vertexLabels.getValueAsString(v));
		}

		return b.toString();
	}

	public static void main(String[] args)
	{
		Grph g = ClassicalGraphs.grid(4, 4);
		Property p = new StringProperty("vertex label", 0);

		for (int v : g.getVertices().toIntArray())
		{
			p.setValue(v, "v" + v);
		}

		g.display();

		Collection<String> patterns = new ArrayList<String>();
		patterns.add("v2.*v12");
		System.out.println(new VertexLabelBasedSubgraphMatcher()
				.findAllPathsMatching(g, patterns, p, null));
	}

}
