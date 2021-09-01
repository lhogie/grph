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
