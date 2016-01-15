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

package grph.algo.topology.manet;

import grph.properties.Property;
import grph.properties.PropertyListener;

public class Manet extends EuclideanGrph
{
	private double range = 1;
	private WavePropagationModel propagationModel = new FreeSpaceWavePropagationModel();

	public Manet()
	{
		super();
		// each node movement may change its neighborhood
		getVertexLocations().getListeners().add(new PropertyListener() {

			@Override
			public void valueChanged(Property p, int id)
			{
				propagationModel.updateNeighborhood(Manet.this, id);
			}
		});
	}

	public double getRange()
	{
		return range;
	}

	public void setRange(double range)
	{
		if (range < 0)
			throw new IllegalArgumentException("range must be >= 0");

		this.range = range;
	}

}
