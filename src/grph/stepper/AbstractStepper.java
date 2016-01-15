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
 
 package grph.stepper;

import grph.Grph;
import grph.TopologyListener;
import grph.io.DynamicsLogger;
import grph.properties.Property;
import grph.properties.PropertyListener;
import toools.set.IntSet;

/**
 * This is the equivalent of the StepAlgo class in Mascopt.
 * 
 * @author lhogie
 * 
 */

public abstract class AbstractStepper
{
    private int numberOfStepsUntilNextWait = 1;
    private int numberOfStepsInAJump = 1;
    private boolean verbose = true;

    public AbstractStepper(Grph g)
    {
	this(g, true, true, true, true);
    }

    public AbstractStepper(Grph g, final boolean vertexEvent, final boolean edgeEvent, final boolean colorEvent,
	    final boolean labelEvent)
    {
	new DynamicsLogger(g);
	g.getTopologyListeners().add(new TopologyListener() {

	    @Override
	    public void vertexRemoved(Grph graph, int vertex)
	    {
		if (vertexEvent && --numberOfStepsUntilNextWait == 0)
		{
		    waitForUserInteraction();
		}
	    }

	    @Override
	    public void vertexAdded(Grph graph, int vertex)
	    {
		if (vertexEvent && --numberOfStepsUntilNextWait == 0)
		{
		    waitForUserInteraction();
		}
	    }

	    @Override
	    public void directedSimpleEdgeAdded(Grph Grph, int edge, int src, int dest)
	    {
		if (edgeEvent && --numberOfStepsUntilNextWait == 0)
		{
		    waitForUserInteraction();
		}

	    }

	    @Override
	    public void undirectedSimpleEdgeAdded(Grph Grph, int edge, int a, int b)
	    {
		if (edgeEvent && --numberOfStepsUntilNextWait == 0)
		{
		    waitForUserInteraction();
		}

	    }

	    @Override
	    public void undirectedHyperEdgeAdded(Grph graph, int edge)
	    {
		if (edgeEvent && --numberOfStepsUntilNextWait == 0)
		{
		    waitForUserInteraction();
		}

	    }

	    @Override
	    public void directedHyperEdgeAdded(Grph graph, int edge)
	    {
		if (edgeEvent && --numberOfStepsUntilNextWait == 0)
		{
		    waitForUserInteraction();
		}

	    }

	    @Override
	    public void directedSimpleEdgeRemoved(Grph Grph, int edge, int a, int b)
	    {

		if (edgeEvent && --numberOfStepsUntilNextWait == 0)
		{
		    waitForUserInteraction();
		}
	    }

	    @Override
	    public void undirectedSimpleEdgeRemoved(Grph Grph, int edge, int a, int b)
	    {

		if (edgeEvent && --numberOfStepsUntilNextWait == 0)
		{
		    waitForUserInteraction();
		}
	    }

	    @Override
	    public void undirectedHyperEdgeRemoved(Grph graph, int edge, IntSet incidentVertices)
	    {

		if (edgeEvent && --numberOfStepsUntilNextWait == 0)
		{
		    waitForUserInteraction();
		}
	    }

	    @Override
	    public void directedHyperEdgeRemoved(Grph graph, int edge, IntSet src, IntSet dest)
	    {

		if (edgeEvent && --numberOfStepsUntilNextWait == 0)
		{
		    waitForUserInteraction();
		}
	    }

	    @Override
	    public void vertexAddedToDirectedHyperEdgeTail(Grph Grph, int e, int v)
	    {

		if (edgeEvent && --numberOfStepsUntilNextWait == 0)
		{
		    waitForUserInteraction();
		}
	    }

	    @Override
	    public void vertexAddedToDirectedHyperEdgeHead(Grph Grph, int e, int v)
	    {

		if (edgeEvent && --numberOfStepsUntilNextWait == 0)
		{
		    waitForUserInteraction();
		}
	    }

	    @Override
	    public void vertexAddedToUndirectedSimpleEdge(Grph Grph, int edge, int vertex)
	    {

		if (edgeEvent && --numberOfStepsUntilNextWait == 0)
		{
		    waitForUserInteraction();
		}
	    }

	    @Override
	    public void vertexRemovedFromUndirectedHyperEdge(Grph g, int edge, int vertex)
	    {

		if (edgeEvent && --numberOfStepsUntilNextWait == 0)
		{
		    waitForUserInteraction();
		}
	    }

	    @Override
	    public void vertexRemovedFromDirectedHyperEdgeTail(Grph g, int e, int v)
	    {

		if (edgeEvent && --numberOfStepsUntilNextWait == 0)
		{
		    waitForUserInteraction();
		}
	    }

	    @Override
	    public void vertexRemovedFromDirectedHyperEdgeHead(Grph g, int e, int v)
	    {

		if (edgeEvent && --numberOfStepsUntilNextWait == 0)
		{
		    waitForUserInteraction();
		}
	    }

	});

	g.getVertexColorProperty().getListeners().add(new PropertyListener() {

	    @Override
	    public void valueChanged(Property g, int id)
	    {
		if (verbose)
		{
		    print("vertex color changed to " + g.getValueAsString(id));
		}

		if (colorEvent && --numberOfStepsUntilNextWait == 0)
		{
		    waitForUserInteraction();
		}
	    }
	});

	g.getEdgeColorProperty().getListeners().add(new PropertyListener() {

	    @Override
	    public void valueChanged(Property g, int id)
	    {
		if (verbose)
		{
		    print("edge color changed to " + g.getValueAsString(id));
		}

		if (colorEvent && --numberOfStepsUntilNextWait == 0)
		{
		    waitForUserInteraction();
		}
	    }
	});

	g.getVertexLabelProperty().getListeners().add(new PropertyListener() {

	    @Override
	    public void valueChanged(Property g, int id)
	    {
		if (labelEvent && --numberOfStepsUntilNextWait == 0)
		{
		    waitForUserInteraction();
		}
	    }
	});

	g.getEdgeLabelProperty().getListeners().add(new PropertyListener() {

	    @Override
	    public void valueChanged(Property g, int id)
	    {
		if (labelEvent && --numberOfStepsUntilNextWait == 0)
		{
		    waitForUserInteraction();
		}
	    }
	});
    }

    protected void print(String s)
    {
	if (verbose)
	{
	    printImpl(s);
	}
    }

    protected abstract void printImpl(String s);

    public boolean isVerbose()
    {
	return verbose;
    }

    public void setVerbose(boolean verbose)
    {
	this.verbose = verbose;
    }

    public int getNumberOfStepsUntilNextWait()
    {
	return numberOfStepsUntilNextWait;
    }

    public void setNumberOfStepsUntilNextWait(int numberOfStepsUntilNextWait)
    {
	this.numberOfStepsUntilNextWait = numberOfStepsUntilNextWait;
    }

    public int getNumberOfStepsInAJump()
    {
	return numberOfStepsInAJump;
    }

    public void setNumberOfStepsInAJump(int numberOfStepsInAJump)
    {
	this.numberOfStepsInAJump = numberOfStepsInAJump;
    }

    protected abstract void waitForUserInteraction();
}
