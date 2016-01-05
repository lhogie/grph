/*
 * (C) Copyright 2009-2013 CNRS.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser General Public License
 * (LGPL) version 2.1 which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl-2.1.html
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * Contributors:

    Luc Hogie (CNRS, I3S laboratory, University of Nice-Sophia Antipolis) 
    Aurelien Lancin (Coati research team, Inria)
    Christian Glacet (LaBRi, Bordeaux)
    David Coudert (Coati research team, Inria)
    Fabien Crequis (Coati research team, Inria)
    Grégory Morel (Coati research team, Inria)
    Issam Tahiri (Coati research team, Inria)
    Julien Fighiera (Aoste research team, Inria)
    Laurent Viennot (Gang research-team, Inria)
    Michel Syska (I3S, University of Nice-Sophia Antipolis)
    Nathann Cohen (LRI, Saclay) 
 */

package grph.dynamics;

import grph.Grph;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import toools.set.IntSet;
import cnrs.oodes.DES;

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
