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
 
package grph.path;

import grph.Grph;
import grph.properties.NumericalProperty;
import it.unimi.dsi.fastutil.ints.IntSet;
import toools.collections.primitive.LucIntSet;

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
    
    @Deprecated
    IntSet getVertexSet();
    
    IntSet toVertexSet();
	String whyNotApplicable(Grph g);
}
