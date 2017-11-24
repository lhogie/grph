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
import java.util.List;
import java.util.Set;

import grph.Grph;
import grph.in_memory.InMemoryGrph;

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
