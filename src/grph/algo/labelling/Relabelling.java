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
 
 

package grph.algo.labelling;

import grph.Grph;
import grph.GrphAlgorithm;
import grph.in_memory.InMemoryGrph;
import it.unimi.dsi.fastutil.ints.IntSet;
import toools.Clazz;
import toools.NotYetImplementedException;
import toools.collections.primitive.SelfAdaptiveIntSet;
import toools.collections.primitive.IntCursor;
import toools.collections.primitive.LucIntSet;

/**
 * produces a graph with the same topology but relabelled.
 * 
 * @author lhogie
 * 
 */
public abstract class Relabelling extends GrphAlgorithm<Grph>
{
	public abstract int getVertexLabel(int v);

	public abstract int getEdgeLabel(int e);

	@Override
	public Grph compute(Grph g)
	{
		Grph gg = Clazz.makeInstance(g.getClass());

		for (IntCursor vc : IntCursor.fromFastUtil(g.getVertices()))
		{
			gg.addVertex(getVertexLabel(vc.value));
		}

		for (IntCursor ec : IntCursor.fromFastUtil(g.getEdges()))
		{
			int e = ec.value;

			if (g.isUndirectedSimpleEdge(e))
			{
				int a = g.getOneVertex(e);
				int b = g.getTheOtherVertex(e, a);
				gg.addUndirectedSimpleEdge(getEdgeLabel(e), getVertexLabel(a), getVertexLabel(b));
			}
			else
			{
				throw new NotYetImplementedException();
			}
		}

		return gg;
	}

	public IntSet computeVertexSet(IntSet s)
	{
		IntSet r = new SelfAdaptiveIntSet();

		for (IntCursor c : IntCursor.fromFastUtil(s))
		{
			r.add(getVertexLabel(c.value));
		}

		return r;
	}

	public IntSet computeEdgeSet(IntSet s)
	{
		IntSet r = new SelfAdaptiveIntSet();

		for (IntCursor c : IntCursor.fromFastUtil(s))
		{
			r.add(getEdgeLabel(c.value));
		}

		return r;
	}

	public static void main(String[] args)
	{
		Grph g = new InMemoryGrph();
		g.addUndirectedSimpleEdge(6, 4, 12);
		g.addUndirectedSimpleEdge(9, 4, 120);
		g.addUndirectedSimpleEdge(876, 67, 12);
		g.addUndirectedSimpleEdge(56, 4, 67);
		g.display();

		new ContiguousLabelling().compute(g).display();
	}
}
