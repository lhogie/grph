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
