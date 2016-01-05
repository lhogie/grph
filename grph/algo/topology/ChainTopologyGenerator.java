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

import com.carrotsearch.hppc.cursors.IntCursor;

/**
 * Link the given nodes as a chain.
 * 
 * @author lhogie
 * 
 */

public class ChainTopologyGenerator extends RandomizedTopologyTransform
{
	private boolean createDirectedLinks = false;

	public boolean createDirectedLinks()
	{
		return createDirectedLinks;
	}

	public void createDirectedLinks(boolean createDirectedLinks)
	{
		this.createDirectedLinks = createDirectedLinks;
	}

	@Override
	public void compute(Grph graph)
	{
		int predecessor = -1;

		for (IntCursor a : graph.getVertices())
		{
			if (predecessor != -1)
			{
				graph.addSimpleEdge(predecessor, a.value, createDirectedLinks);
			}

			predecessor = a.value;
		}
	}

	public static Grph chain(Grph g, boolean directed)
	{
		ChainTopologyGenerator tg = new ChainTopologyGenerator();
		tg.createDirectedLinks(directed);
		tg.compute(g);
		return g;
	}

}
