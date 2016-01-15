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

package grph.algo.topology;

import grph.Grph;

/**
 * Creates a grid with diagonal lines.
 * 
 * @author lhogie
 * 
 */

public class GridTopologyGenerator extends RandomizedTopologyTransform
{
	private int width, height = -1;
	private boolean diags = false;
	private boolean diags2 = false;
	private boolean directed = false;

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
				c(graph, i, j, i + 1, j);
				c(graph, i, j, i, j + 1);

				if (diags)
				{
					c(graph, i, j, i + 1, j + 1);

					if (diags2)
					{
						c(graph, i, j, i + 1, j - 1);
					}
				}
			}
		}
	}

	private void c(Grph g, int i1, int j1, int i2, int j2)
	{
		if (i2 < width && j2 < height && j2 >= 0)
		{
			g.addSimpleEdge(i1 + width * j1, i2 + width * j2, createDirectedLinks());
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
