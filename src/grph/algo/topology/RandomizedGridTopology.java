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
 
 

package grph.algo.topology;

import grph.Grph;

/**
 * Creates a grid with diagonal lines.
 * 
 * @author lhogie
 * 
 */

public class RandomizedGridTopology extends RandomizedTopologyTransform
{
	private int width, height = -1;
	private boolean diags = false;
	private boolean diags2 = false;
	private boolean directed = false;
	private float edgeProbability;

	public float getEdgeProbability()
	{
		return edgeProbability;
	}

	public void setEdgeProbability(float edgeProbability)
	{
		this.edgeProbability = edgeProbability;
	}

	public boolean createDirectedLinks()
	{
		return directed;
	}

	public void createDirectedLinks(boolean directed)
	{
		this.directed = directed;
	}

	public int getWidth()
	{
		return width;
	}

	public void setWidth(int width)
	{
		if (width < 0)
			throw new IllegalArgumentException();

		this.width = width;
	}

	public int getHeight()
	{
		return height;
	}

	public void setHeight(int height)
	{
		if (height < 0)
			throw new IllegalArgumentException();

		this.height = height;
	}

	@Override
	public void compute(Grph graph)
	{
		graph.ensureNVertices(1);

		for (int i = 0; i < width; ++i)
		{
			for (int j = 0; j < height; ++j)
			{
				c(graph, i, j, i + 1, j, edgeProbability);
				c(graph, i, j, i, j + 1, edgeProbability);

				if (diags)
				{
					c(graph, i, j, i + 1, j + 1, edgeProbability);

					if (diags2)
					{
						c(graph, i, j, i + 1, j - 1, edgeProbability);
					}
				}
			}
		}
	}

	private void c(Grph g, int i1, int j1, int i2, int j2, float p)
	{
		if (i2 < width && j2 < height && j2 >= 0)
		{
			int e = g.addSimpleEdge(i1 + width * j1, i2 + width * j2, createDirectedLinks());
			g.getEdgeColorProperty().setValue(e, Math.random() < p ? 0 : 1);
		}

	}

	public void setGenerateDiagonal(boolean b)
	{
		this.diags = b;

	}

	public void setGenerateSecondaryDiagonal(boolean b)
	{
		this.diags2 = b;

	}

}
