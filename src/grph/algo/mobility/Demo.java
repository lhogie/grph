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
 
 package grph.algo.mobility;

import cnrs.minides.DES;
import grph.Grph;
import grph.TopologyListener;
import grph.in_memory.InMemoryGrph;
import it.unimi.dsi.fastutil.ints.IntSet;
import toools.collections.primitive.LucIntSet;

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
