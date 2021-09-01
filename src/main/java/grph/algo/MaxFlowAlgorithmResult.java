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

package grph.algo;

import grph.Grph;
import it.unimi.dsi.fastutil.ints.Int2DoubleMap;
import it.unimi.dsi.fastutil.ints.Int2DoubleOpenHashMap;
import jalinopt.LP;
import jalinopt.Result;
import jalinopt.Variable;
import toools.collections.primitive.IntCursor;

public class MaxFlowAlgorithmResult
{
	private final double flow;
	private final Int2DoubleMap edgeAssigments = new Int2DoubleOpenHashMap();
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

	public Int2DoubleMap getAssigments()
	{
		return edgeAssigments;
	}

	public void display(Grph g)
	{
		for (IntCursor n : IntCursor.fromFastUtil(getAssigments().keySet()))
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
