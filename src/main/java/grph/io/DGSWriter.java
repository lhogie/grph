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

package grph.io;

import java.io.IOException;
import java.io.PrintStream;

import grph.Grph;
import grph.TopologyListener;
import it.unimi.dsi.fastutil.ints.IntSet;
import toools.collections.primitive.LucIntSet;

public class DGSWriter extends AbstractGraphTextWriter
{
	private final TopologyListener listener = new MG();
	private PrintStream printStream;
	private Grph g;

	@Override
	public void printGraph(Grph graph, PrintStream printStream) throws IOException
	{
		if (this.g != null)
			throw new IllegalStateException("busy");

		this.g = graph;
		graph.getTopologyListeners().add(listener);
		this.printStream = printStream;
		printStream.append("DGS002\n");
		printStream.append("graph 0 0\n");
	}

	private class MG implements TopologyListener
	{
		private int step = 0;

		@Override
		public void vertexAdded(Grph graph, int vertex)
		{
			step();
			printStream.append("an " + vertex + "\n");
		}

		private void step()
		{
			printStream.append("st " + this.step++ + "\n");
		}

		@Override
		public void vertexRemoved(Grph graph, int vertex)
		{
			step();
			printStream.append("dn " + vertex + "\n");
		}

		@Override
		public void directedSimpleEdgeAdded(Grph graph, int edge, int src, int dest)
		{
			step();
			int a = graph.getOneVertex(edge);
			int b = graph.getTheOtherVertex(edge, a);
			printStream.append("ae " + edge + " " + a + " " + b + " 1\n");
		}

		@Override
		public void undirectedSimpleEdgeAdded(Grph graph, int edge, int a, int b)
		{
			step();
			printStream.append("ae " + edge + " " + a + " " + b + " 1\n");
		}

		@Override
		public void undirectedHyperEdgeAdded(Grph g, int edge)
		{
			step();
			printStream.append("ae " + edge + "\n");
		}

		@Override
		public void directedHyperEdgeAdded(Grph g, int edge)
		{
			step();
			printStream.append("ae " + edge + "\n");
		}

		@Override
		public void directedSimpleEdgeRemoved(Grph g, int edge, int a, int b)
		{
			step();
			printStream.append("de " + edge + "\n");
		}

		@Override
		public void undirectedSimpleEdgeRemoved(Grph g, int edge, int a, int b)
		{
			step();
			printStream.append("de " + edge + "\n");
		}

		@Override
		public void undirectedHyperEdgeRemoved(Grph g, int edge, IntSet incidentVertices)
		{
			step();
			printStream.append("de " + edge + "\n");
		}

		@Override
		public void directedHyperEdgeRemoved(Grph g, int edge, IntSet src, IntSet dest)
		{
			step();
			printStream.append("de " + edge + "\n");
		}

		@Override
		public void vertexAddedToDirectedHyperEdgeTail(Grph g, int e, int v)
		{
			step();
			System.err.println("unsupported");
		}

		@Override
		public void vertexAddedToDirectedHyperEdgeHead(Grph g, int e, int v)
		{
			step();
			System.err.println("unsupported");
		}

		@Override
		public void vertexAddedToUndirectedSimpleEdge(Grph g, int edge, int vertex)
		{
			step();
			System.err.println("unsupported");
		}

		@Override
		public void vertexRemovedFromUndirectedHyperEdge(Grph g, int edge, int vertex)
		{
			step();
			System.err.println("unsupported");
		}

		@Override
		public void vertexRemovedFromDirectedHyperEdgeTail(Grph g, int e, int v)
		{
			step();
			System.err.println("unsupported");
		}

		@Override
		public void vertexRemovedFromDirectedHyperEdgeHead(Grph g, int e, int v)
		{
			step();
			System.err.println("unsupported");
		}
	}
}
