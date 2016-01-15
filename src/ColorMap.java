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
 
 import java.awt.Color;

public class ColorMap
{
    private final Color[] colors = new Color[16];
    private int defaultVertexColor = 0;

    public int getDefaultVertexColor()
    {
	return defaultVertexColor;
    }

    public void setDefaultVertexColor(int defaultVertexColor)
    {
	this.defaultVertexColor = defaultVertexColor;
    }

    public int getDefaultEdgeColor()
    {
	return defaultEdgeColor;
    }

    public void setDefaultEdgeColor(int defaultEdgeColor)
    {
	this.defaultEdgeColor = defaultEdgeColor;
    }

    private int defaultEdgeColor = 15;

    public ColorMap()
    {
	colors[0] = Color.white;
	colors[15] = Color.black;
    }

    public Color getColor(int index)
    {
	return colors[index];
    }

    public void setColor(int index, Color c)
    {
	if (c == null)
	    throw new NullPointerException();

	if (0 >= index || index >= 15)
	    throw new IllegalArgumentException("cannot change extrem colors");

	colors[index] = c;
    }
}
