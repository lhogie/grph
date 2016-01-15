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
 
 package grph.algo.coloring;

import java.util.ArrayList;
import java.util.List;

import toools.set.DefaultIntSet;
import toools.set.IntSet;

/**
 * This class implements the notion of graph coloring. Each color class consists of an IntSet (the vertices of this color).
 * Color classes are stored in an ArrayList.
 * (Note: the coloring consistency is not checked. That is, a vertex may appear in several color classes. This can be helpful,
 * for example for fractional coloring).  
 * 
 * @author Gregory Morel
 */

public class GraphColoring {
	private final List<IntSet> coloring;
	
	/**
	 * Constructs an empty coloring.
	 */
	public GraphColoring() {
		coloring = new ArrayList<IntSet>();
	}
	
	/**
	 * Constructs an empty coloring with the specified number of empty color classes.
	 * @param numberOfColors number of predefinite color classes
	 */
	public GraphColoring(int numberOfColors) {
		this();
		for(int i = 0; i < numberOfColors; i++)
			coloring.add(new DefaultIntSet());
	}
	
	/**
	 * Appends the specified color class to this coloring.
	 * @param s a set of vertices
	 */
	public void addColorClass(IntSet s) {
		coloring.add(s);
	}
	
	/**
	 * Returns the color class indexed by the specified nonnegative integer.
	 * @param color index of the color class to return
	 * @return the specified color class
	 */
	public IntSet getColorClass(int color) {
		return coloring.get(color);
	}
	
	/**
	 * Appends the specified vertex of the specified color class.
	 * @param v a vertex
	 * @param c an index of a color class
	 */
	public void addVertexToClass(int v, int c) {
		coloring.get(c).add(v);
	}
	
	/**
	 * Returns a string representation of a graph coloring. The string consists of one color class per line.
	 * @return a string representation of a graph coloring.
	 */
	@Override
	public String toString(){
		String s = "";
		
		for(IntSet i : coloring)
			s += i.toString() + "\n";
		
		return s;
	}
}
