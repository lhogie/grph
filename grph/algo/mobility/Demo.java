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
 
 package grph.algo.mobility;

import grph.Grph;
import grph.TopologyListener;
import grph.in_memory.InMemoryGrph;
import toools.set.IntSet;
import cnrs.oodes.DES;

public class Demo
{
    public static void main(String[] args)
    {
	Grph g = new InMemoryGrph();
	g.grid(5, 5);
	DES<Grph> des = new DES<Grph>(g) {
	    @Override
	    public boolean isTerminated()
	    {
		return getTime() > 100;
	    }
	};

	MobilityModel mm = new OneChangePerSecondMobility(des);
	mm.apply(g.getVertices());
	g.display();
	// des.setStepped(true);
	des.setRealTimeAccelerationFactor(0.5);
	des.run();

	System.out.println("terminated " + des.getNumberOfProcessedEvents());
	
	TopologyListener l = new TopologyListener() {

	    @Override
	    public void vertexRemoved(Grph graph, int vertex)
	    {
	    }
	    
	    @Override
	    public void vertexAdded(Grph graph, int vertex)
	    {
	    }

	    @Override
	    public void undirectedHyperEdgeRemoved(Grph graph, int edge, IntSet incidentVertices)
	    {
		// TODO Auto-generated method stub
		
	    }

	    @Override
	    public void directedSimpleEdgeRemoved(Grph Grph, int edge, int a, int b)
	    {
		// TODO Auto-generated method stub
		
	    }

	    @Override
	    public void undirectedSimpleEdgeRemoved(Grph Grph, int edge, int a, int b)
	    {
		// TODO Auto-generated method stub
		
	    }

	    @Override
	    public void directedSimpleEdgeAdded(Grph Grph, int edge, int src, int dest)
	    {
		// TODO Auto-generated method stub
		
	    }

	    @Override
	    public void undirectedSimpleEdgeAdded(Grph Grph, int edge, int a, int b)
	    {
		// TODO Auto-generated method stub
		
	    }

	    @Override
	    public void undirectedHyperEdgeAdded(Grph graph, int edge)
	    {
		// TODO Auto-generated method stub
		
	    }

	    @Override
	    public void directedHyperEdgeAdded(Grph graph, int edge)
	    {
		// TODO Auto-generated method stub
		
	    }

	    @Override
	    public void directedHyperEdgeRemoved(Grph graph, int edge, IntSet src, IntSet dest)
	    {
		// TODO Auto-generated method stub
		
	    }

	    @Override
	    public void vertexAddedToDirectedHyperEdgeTail(Grph Grph, int e, int v)
	    {
		// TODO Auto-generated method stub
		
	    }

	    @Override
	    public void vertexAddedToDirectedHyperEdgeHead(Grph Grph, int e, int v)
	    {
		// TODO Auto-generated method stub
		
	    }

	    @Override
	    public void vertexAddedToUndirectedSimpleEdge(Grph Grph, int edge, int vertex)
	    {
		// TODO Auto-generated method stub
		
	    }

	    @Override
	    public void vertexRemovedFromUndirectedHyperEdge(Grph g, int edge, int vertex)
	    {
		// TODO Auto-generated method stub
		
	    }

	    @Override
	    public void vertexRemovedFromDirectedHyperEdgeTail(Grph g, int e, int v)
	    {
		// TODO Auto-generated method stub
		
	    }

	    @Override
	    public void vertexRemovedFromDirectedHyperEdgeHead(Grph g, int e, int v)
	    {
		// TODO Auto-generated method stub
		
	    }
	};
    }
}
