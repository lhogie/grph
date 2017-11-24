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

package grph.algo.lad;

import java.io.IOException;
import java.util.Collection;

import grph.Grph;
import grph.in_memory.InMemoryGrph;
import grph.util.Matching;
import toools.UnitTests;

/**
 * LAD is a program (in C) for solving the subgraph isomorphism problem, the
 * goal of which is to decide if there exists a copy of a pattern graph in a
 * target graph. It can be used to solve induced or partial subgraph isomorphism
 * problems. The user may specify additional compatibility relationships among
 * the nodes. LAD is distributed under the CeCILL-B FREE SOFTWARE LICENSE.
 * 
 * @author lhogie
 * 
 */

public class UndirectedLAD extends LAD
{

	@Override
	protected String getName()
	{
		return "LAD";
	}

	public static void main(String[] args)
	{
		Grph target = new InMemoryGrph();
		target.grid(3, 3);

		Grph pattern = new InMemoryGrph();
		pattern.grid(2, 2);

		Collection<Matching> r = new UndirectedLAD().lad(target, pattern, MODE.INDUCED,
				true);

		for (Matching m : r)
		{
			System.out.println(m.pattern2graph());
		}

		UnitTests.ensure(r.size() == 32);

		target.highlightVertices(
				r.iterator().next().graph2pattern().keySet().toIntArray());
		target.display();
	}

	private static void test() throws IOException
	{
		Grph target = new InMemoryGrph();
		target.grid(2, 3);

		Grph pattern = new InMemoryGrph();
		pattern.grid(2, 2);

		Collection<Matching> s = new UndirectedLAD().lad(target, pattern, MODE.INDUCED,
				true);
		UnitTests.ensureEqual(s.size(), 16);
	}
}
