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
import cnrs.oodes.DES;
import cnrs.oodes.Event;

public class DynamicsEvent<G extends Grph> extends Event<G>
{
	private final int vertex;

	public int getVertex()
	{
		return vertex;
	}

	private final DynamicsModel<G> dynamicModel;

	public DynamicsEvent(int n, DynamicsModel<G> mm, DES<G> des, double date)
	{
		super(des, date);
		this.vertex = n;
		this.dynamicModel = mm;
	}

	@Override
	protected void execute()
	{
		// perform change
		double timeOffset = dynamicModel.change(vertex);

		for (DynamicsListener<G> l : dynamicModel.getListeners())
			l.change(dynamicModel, this);

		// plan next change
		getSimulation().getEventQueue().add(new DynamicsEvent<G>(vertex, dynamicModel, getSimulation(), getOccurenceDate() + timeOffset));
	}

	@Override
	public String toString()
	{
		return dynamicModel.toString(vertex);
	}
}
