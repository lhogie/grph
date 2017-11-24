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
