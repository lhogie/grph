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
 
 

package grph.dynamics;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cnrs.minides.DES;
import grph.Grph;
import it.unimi.dsi.fastutil.ints.IntSet;
import toools.collections.primitive.LucIntSet;

public abstract class DynamicsModel<G extends Grph>
{
	private final DES<G> des;
	private List<DynamicsListener<G>> listeners = new ArrayList<DynamicsListener<G>>();

	public DynamicsModel(DES<G> des)
	{
		this.des = des;
	}

	public abstract double change(int v);

	public void apply(IntSet elements)
	{
		for (int n : elements.toIntArray())
		{
			apply(n);
		}
	}

	public void apply(int t)
	{
		des.getEventQueue().add(new DynamicsEvent<G>(t, this, des, des.getTime() + getPRNG().nextDouble()));
	}

	public DES<G> getDes()
	{
		return des;
	}

	protected G getGrph()
	{
		return getDes().getSystem();
	}

	protected Random getPRNG()
	{
		return getDes().getPRNG();
	}

	public abstract String toString(int vertex);

	public List<DynamicsListener<G>> getListeners()
	{
		return listeners;
	}

}
