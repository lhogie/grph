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
 
 

package grph.algo.distance;

import grph.Grph;
import grph.algo.search.SearchResult;
import grph.in_memory.InMemoryGrph;
import toools.math.Distribution;
import toools.math.IntMatrix;

/**
 * A matrix of all distances in a graph.
 * 
 * @author lhogie
 * 
 */
public class DistanceMatrix extends IntMatrix
{
	public DistanceMatrix(int width, int height)
	{
		super(width, height);
		fill(-1);
	}

	public <R extends SearchResult> DistanceMatrix(R[] r)
	{
		super(r.length, r.length);

		for (int i = 0; i < r.length; ++i)
		{
			if (r[i] != null)
			{
				setColumn(i, r[i].distances);
			}
		}
	}

	public boolean theresAPath(int destination, int source)
	{
		return get(source, destination) != -1;
	}

	public float getDistance(int destination, int source)
	{
		float d = get(source, destination);

		if (d == -1)
		{
			throw new IllegalStateException(
					"cannot compute a distance because the two vertices are not connected");
		}
		else
		{
			return d;
		}
	}

	public int getLongestPathLength()
	{
		int greatestDistance = 0;

		for (int i = 0; i < width; ++i)
		{
			int[] c = getColumn(i);

			if (c != null)
			{
				for (int e : c)
				{
					if (e >= 0)
					{
						if (e > greatestDistance)
						{
							greatestDistance = e;
						}
					}
				}
			}
		}

		return greatestDistance;
	}

	public Distribution<Float> getDistribution()
	{
		Distribution<Float> r = new Distribution<Float>(
				"Distance distribution");
		int w = this.width;

		for (int i = 0; i < w; ++i)
		{
			int[] c = this.getColumn(i);

			for (int j = 0; j < c.length; ++j)
			{
				float d = c[j];

				if (d >= 0)
				{
					r.addOccurence(d);
				}
			}
		}

		return r;
	}

	public static void main(String[] args)
	{
		Grph g = new InMemoryGrph();
		g.grid(2, 2);

		DistanceMatrix m = g.getDistanceMatrix(null);
		System.out.println(m);
	}

}
