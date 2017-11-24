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

import java.util.Arrays;

import grph.Grph;
import it.unimi.dsi.fastutil.ints.Int2IntMap;

public class SubGraphIsomorphism4Julien
{

	public Int2IntMap compute(Grph g, Grph pattern)
    {

	for (int v : g.getVertices().toIntArray())
	{
	    Int2IntMap matching = foo(g, pattern, v);

	    if (matching != null)
	    {
		return matching;
	    }
	}

	return null;
    }

    private Int2IntMap foo(Grph g, Grph pattern, int v)
    {
	int pv = pattern.getVertices().iterator().nextInt();
	int[] p2g = new int[g.getVertices().size()];
	int[] g2p = new int[g.getVertices().size()];
	Arrays.fill(p2g, -1);
	Arrays.fill(g2p, -1);
	p2g[pv] = v;
	g2p[v] = pv;

	for (int pn : pattern.getNeighbours(pv).toIntArray())
	{
	    if (p2g[pn] == -1)
	    {
		
	    }
	}

	return null;
    }

}
