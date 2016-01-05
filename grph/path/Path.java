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
 
 /**
 *	This file is part of Grph.
 *	
 *  Grph is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General  License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Grph is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General  License for more details.
 *
 *  You should have received a copy of the GNU General  License
 *  along with Grph.  If not, see <http://www.gnu.org/licenses/>. *
 */

package grph.path;

import grph.Grph;
import grph.properties.NumericalProperty;
import toools.set.IntSet;

/**
 * A ion of path. A path is seen as a sequence of vertices. At this level, no
 * implementation is defined.
 * 
 * @author lhogie
 * 
 */
public interface Path
{
    /**
     * The first vertex in the path
     * 
     * @return the first vertex in the path
     */
    int getSource();
    void setSource(int v);

    /**
     * The last vertex in the path
     * 
     * @return the last vertex in the path
     */
    int getDestination();

    

    int getNumberOfVertices();
    int getVertexAt(int i);
    int indexOfVertex(int successor);
    boolean containsVertex(int someVertex);

    int getEdgeHeadingToVertexAt(int i);
    

    void extend(int v);
    void extend(int throughEdge, int v);



    


    /**
     * Computes the length of this path.
     * 
     * @return the length of this path
     */
    int getLength();


    boolean isElementary();

    /**
     * Checks whether the given path has the same source and destination has
     * this path.
     * 
     * @param anotherPath
     * @return true the given path has the same source and destination has this
     *         path, false otherwise
     */
    boolean permitsSameTrip(Path anotherPath);

    /**
     * Checks whether this path is applicable to the given graph. A path may not
     * be applicable if the graph does not have some of the vertices or edges
     * defined by this path.
     * 
     * @param g
     * @return true if this path is applicable to the given graph, false
     *         otherwise.
     */
    boolean isApplicable(Grph g);

    /**
     * Checks whether this path is a shortest path in the given graph.
     * 
     * @param g
     * @return true this path is a shortest path in the given graph, false
     *         otherwise.
     */
    boolean isShortestPath(Grph g, NumericalProperty weights);

    /**
     * Assigns the given color to all vertices in this path, when applied to the
     * given graph.
     * 
     * @param g
     * @param color
     */
    void setColor(Grph g, int color);

    /**
     * Checks whether this path is a cycle or not.
     * 
     * @return true if this path is a cycle or not, false otherwise
     */
    boolean isCycle();

    /**
     * Checks whether this path is hamiltonian on the given graph.
     * 
     * @param g
     *            the graph
     * @return true if this path is hamiltonian on the given graph, false
     *         otherwise
     */
    boolean isHamiltonian(Grph g);
    boolean hasLoop();
    Object clone();
    void reverse();


    /**
     * Computes the sequence of vertices as an array of ints.
     * 
     * @return the sequence of vertices as an array of ints
     */
    int[] toVertexArray();
    IntSet getVertexSet();
}
