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

import grph.algo.mobility.EuclideanMobilityModel;
import grph.algo.mobility.Location;
import grph.in_memory.InMemoryGrph;
import grph.properties.ObjectProperty;

public class EuclideanGrph extends InMemoryGrph
{
	private final ObjectProperty<Location> vertexLocations;
	private EuclideanMobilityModel<Manet> mm;

	public EuclideanGrph()
	{
		this.vertexLocations = new ObjectProperty<Location>("node location") {
			@Override
			public boolean acceptValue(Location value)
			{
				return true;
			}

			@Override
			public String getName()
			{
				return "location";
			}

			@Override
			public void setValue(int id, String value)
			{
				String[] numbers = value.trim().replace('(', ' ').replace(')', ' ').replace(',', ' ').trim().split(" +");
				setValue(id, new Location(Double.valueOf(numbers[0]), Double.valueOf(numbers[1])));
			}

			@Override
			public String getValueAsString(int id)
			{
				return getValue(id).toString();
			}

			@Override
			protected long sizeOf(Location o)
			{
				return 32;
			}
		};
	}

	public ObjectProperty<Location> getVertexLocations()
	{
		return vertexLocations;
	}

	public Location getLocation(int v)
	{
		Location l = vertexLocations.getValue(v);

		if (l == null)
		{
			synchronized (vertexLocations)
			{
				vertexLocations.setValue(v, l = mm.createDefaultLocation(v));
			}
		}

		return l;
	}

	public void setLocation(int v, Location l)
	{
		vertexLocations.setValue(v, l);
	}

	public void setMobilityModel(EuclideanMobilityModel<Manet> mm)
	{
		this.mm = mm;
		mm.apply(getVertices());
	}
}
