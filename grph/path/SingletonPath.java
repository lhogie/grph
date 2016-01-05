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
 
 package grph.path;


public class SingletonPath extends AbstractPath
{
//    private static final int[] emptyArray = new int[0];
    private final int vertex;

    public SingletonPath(int v)
    {
	this.vertex = v;
    }

    @Override
    public String toString()
    {
	return String.valueOf(vertex);
    }

    @Override
    public int getSource()
    {
	return vertex;
    }

    @Override
    public void setSource(int v)
    {
	throw new IllegalStateException("path can't be changed");
    }

    @Override
    public int getVertexAt(int i)
    {
	if (i != 0)
	    throw new IllegalStateException("out of bounds");

	return vertex;
    }

    @Override
    public AbstractPath clone()
    {
	return new SingletonPath(vertex);
    }

    @Override
    public int getDestination()
    {
	return vertex;
    }

    @Override
    public int[] toVertexArray()
    {
	return new int[] { vertex };
    }

    @Override
    public boolean containsVertex(int someVertex)
    {
	return vertex == someVertex;
    }



    @Override
    public int indexOfVertex(int v)
    {
	return v == this.vertex ? 0 : -1;
    }

    @Override
    public void extend(int e, int v)
    {
	throw new PathNotModifiableException();
    }


    @Override
    public void reverse()
    {
    }

    @Override
    public int getEdgeHeadingToVertexAt(int i)
    {
	throw new IllegalStateException("no edge in a singleton path");
    }

    @Override
    public int getNumberOfVertices()
    {
	return 1;
    }
}
