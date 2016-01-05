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
