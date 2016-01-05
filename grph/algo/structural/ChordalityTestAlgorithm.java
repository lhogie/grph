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
 
 package grph.algo.structural;

import grph.Grph;
import grph.GrphAlgorithm;
import grph.in_memory.InMemoryGrph;
import toools.set.IntSet;
/**
 * Computes if the graph is chordal
 * @author lhogie
 *
 */
public class ChordalityTestAlgorithm extends GrphAlgorithm<Boolean>
{

    @Override
    public Boolean compute(Grph g)
    {
	Grph clone = g.clone();

	while (true)
	{
	    IntSet sv = clone.getSimplicialVertices();

	    if (sv.isEmpty())
	    {
		if (clone.isNull())
		{
		    return true;
		}
		else
		{
		    return false;
		}
	    }
	    else
	    {
		clone.removeVertices(sv);
	    }
	}
    }


    
    public static void main(String[] args)
    {
	Grph g = new InMemoryGrph();
	//g.createVertices(30);
	g.grid(3, 3);
	System.out.println(g.isChordal());
    }
}
