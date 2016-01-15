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
 
 package grph.edit;

import grph.Grph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import toools.set.DefaultIntSet;
import toools.set.IntSet;

public class ShellEditor
{
    private final Grph g;

    public ShellEditor(Grph g)
    {
	this.g = g;
    }

    private IntSet getMatching(boolean v, String re)
    {
	IntSet r = new DefaultIntSet();

	for (int e : (v ? g.getVertices() : g.getEdges()).toIntArray())
	{
	    if (String.valueOf(e).matches(re))
	    {
		r.add(e);
	    }
	}

	return r;
    }

    public void execute(String text)
    {
	List<String> tokens = new ArrayList(Arrays.asList(text.trim().split(" +")));
	System.out.println(tokens);
	String cmd = tokens.remove(0);

	if (cmd.equals("av"))
	{
	    g.addVertex();
	}
	else if (cmd.equals("ae"))
	{
	    for (int src : getMatching(true, tokens.get(0)).toIntArray())
	    {
		for (int dest : getMatching(true, tokens.get(1)).toIntArray())
		{
		    g.addUndirectedSimpleEdge(src, dest);
		}
	    }
	}
	else if (cmd.equals("rv"))
	{
	    for (String re : tokens)
	    {
		for (int v : getMatching(true, re).toIntArray())
		{
		    g.removeVertex(v);
		}
	    }
	}
	else if (cmd.equals("re"))
	{
	    for (String re : tokens)
	    {
		for (int v : getMatching(false, re).toIntArray())
		{
		    g.removeVertex(v);
		}
	    }
	}
	else if (cmd.equals("sv"))
	{
	    for (String re : tokens)
	    {
		g.highlightVertices(getMatching(true, re));
	    }
	}
	else if (cmd.equals("se"))
	{
	    for (String re : tokens)
	    {
		g.highlightEdges(getMatching(true, re));
	    }
	}
	else
	{
	    throw new IllegalArgumentException("unknown command: " + cmd);
	}
    }

}
