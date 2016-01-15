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
 
 package grph.algo.ogdf;

import grph.Grph;
import grph.algo.topology.ClassicalGraphs;
import grph.in_memory.InMemoryGrph;
import grph.io.AbstractGraphReader;
import grph.io.GraphBuildException;
import grph.io.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import toools.UnitTests;

public class OGDFReader extends AbstractGraphReader
{

    @Override
    public Grph readGraph(InputStream is) throws ParseException, IOException, GraphBuildException
    {
	Grph g = new InMemoryGrph();
	BufferedReader br = new BufferedReader(new InputStreamReader(is));

	int nbVertices = Integer.valueOf(br.readLine());

	for (int i = 0; i < nbVertices; ++i)
	{
	    int v = Integer.valueOf(br.readLine());
	    g.addVertex(v);
	}

	int nbEdges = Integer.valueOf(br.readLine());

	for (int i = 0; i < nbEdges; ++i)
	{
	    int v1 = Integer.valueOf(br.readLine());
	    int v2 = Integer.valueOf(br.readLine());
	    g.addUndirectedSimpleEdge(v1, v2);
	}

	return g;
    }

    
    private static void test() throws ParseException, GraphBuildException
    {
	Grph g = ClassicalGraphs.PetersenGraph();
	Grph h = new OGDFReader().readGraph(new OGDFWriter().printGraph(g));
	UnitTests.ensure(g.equals(h));
    }
}
