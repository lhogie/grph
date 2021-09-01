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

package grph.edit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import grph.Grph;
import it.unimi.dsi.fastutil.ints.IntSet;
import toools.collections.primitive.SelfAdaptiveIntSet;

public class ShellEditor
{
	private final Grph g;

	public ShellEditor(Grph g)
	{
		this.g = g;
	}

	private IntSet getMatching(boolean v, String re)
	{
		IntSet r = new SelfAdaptiveIntSet(0);

		for (int e : (v ? g.getVertices() : g.getEdges()).toIntArray())
		{
			if (String.valueOf(e).matches(re))
			{
				r.add(e);
			}
		}

		return r;
	}

	public void execute(String text)
	{
		List<String> tokens = new ArrayList(Arrays.asList(text.trim().split(" +")));
		System.out.println(tokens);
		String cmd = tokens.remove(0);

		if (cmd.equals("av"))
		{
			g.addVertex();
		}
		else if (cmd.equals("ae"))
		{
			for (int src : getMatching(true, tokens.get(0)).toIntArray())
			{
				for (int dest : getMatching(true, tokens.get(1)).toIntArray())
				{
					g.addUndirectedSimpleEdge(src, dest);
				}
			}
		}
		else if (cmd.equals("rv"))
		{
			for (String re : tokens)
			{
				for (int v : getMatching(true, re).toIntArray())
				{
					g.removeVertex(v);
				}
			}
		}
		else if (cmd.equals("re"))
		{
			for (String re : tokens)
			{
				for (int v : getMatching(false, re).toIntArray())
				{
					g.removeVertex(v);
				}
			}
		}
		else if (cmd.equals("sv"))
		{
			for (String re : tokens)
			{
				g.highlightVertices(getMatching(true, re));
			}
		}
		else if (cmd.equals("se"))
		{
			for (String re : tokens)
			{
				g.highlightEdges(getMatching(true, re));
			}
		}
		else
		{
			throw new IllegalArgumentException("unknown command: " + cmd);
		}
	}

}
