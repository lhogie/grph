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
 
 package grph.algo;

import grph.Grph;
import grph.algo.topology.ClassicalGraphs;
import grph.path.Path;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class HamiltonianPaths
{
    public static Set<Path> find(int sourceVertex, Grph g, int maxNumberOfPaths)
    {
	Set<Path> r = new HashSet<Path>();

	List<Collection<Path>> allPaths = AllPaths.compute(sourceVertex, g, Integer.MAX_VALUE, maxNumberOfPaths, false);

	for (Path p : allPaths.get(allPaths.size() - 1))
	{
	    if (p.isHamiltonian(g))
	    {
		r.add(p);
	    }
	}

	return r;
    }
    
    public static void main(String[] args)
    {
	Grph g  = ClassicalGraphs.grid(2, 2);
	Set<Path> s = HamiltonianPaths.find(0, g, 10);
	System.out.println(s.size());
	
	for (Path p : s)
	{
	    System.out.println(p);
	}
	
    }
}
