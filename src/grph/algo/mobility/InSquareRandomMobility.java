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

package grph.algo.mobility;

import grph.algo.topology.manet.EuclideanGrph;
import grph.properties.PropertyListener;
import cnrs.oodes.DES;

public class InSquareRandomMobility<G extends EuclideanGrph> extends InSquareMobility<G>
{

	public InSquareRandomMobility(DES<G> des, double sideLength)
	{
		super(des, sideLength);
	}

	@Override
	public double move(int v)
	{
		G g = getGrph();
		Location l = getGrph().getLocation(v);
		l.x += getDes().getPRNG().nextDouble() * 2 - 1;
		l.y += getDes().getPRNG().nextDouble() * 2 - 1;
		minmax(l);
		
		for (PropertyListener li : g.getVertexLocations().getListeners())
		{
			li.valueChanged(g.getVertexLocations(), v);
		}
		return getPRNG().nextDouble();
	}

	@Override
	public String toString(int v)
	{
		return "moving v" + v + " to new location " + getGrph().getLocation(v);
	}

}
