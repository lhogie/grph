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
 
 

package grph.algo.mobility;

public class Location
{
	double x, y;

	public Location(double x, double y)
	{
		this.x = x;
		this.y = y;
	}

	public String toString()
	{
		return "(" + x + ", " + y + ")";
	}

	@Override
	public int hashCode()
	{
		return (x + ":" + y).hashCode();
	}

	@Override
	public boolean equals(Object obj)
	{
		return obj instanceof Location && equals((Location) obj);
	}

	public boolean equals(Location l)
	{
		return l.x == x && l.y == y;
	}

	public double getDistanceTo(Location l)
	{
		double dx = x - l.x;
		double dy = y - l.y;
		return Math.sqrt(dx * dx + dy * dy);
	}
}
