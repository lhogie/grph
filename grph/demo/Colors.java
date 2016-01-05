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

package grph.demo;

import grph.Grph;
import grph.in_memory.InMemoryGrph;

import java.util.Random;

import toools.gui.TrueColors24Map;
import toools.thread.Threads;

public class Colors
{
	public static void main(String[] args)
	{
		ClassLoader.getSystemClassLoader().setDefaultAssertionStatus(true);
		Random r = new Random();

		Grph g = new InMemoryGrph();
		g.grid(6, 6);
		g.getVertexLabelProperty().setValue(4, "salut");
		g.getVertexColorProperty().setPalette(new TrueColors24Map());
		g.display();

		for (int i = 0;; ++i)
		{
			int color = (int) (r.nextDouble() * g.getVertexColorProperty()
					.getPalette().getNumberOfColors());
			g.getVertexColorProperty().setValue(
					g.getVertices().pickRandomElement(r), color);
			g.getEdgeColorProperty().setValue(
					g.getEdges().pickRandomElement(r), color);
			Threads.sleepMs(500);
		}
	}
}
