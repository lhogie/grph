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
import grph.algo.search.BFSAlgorithm;
import grph.algo.search.GraphSearchListener;

import java.io.IOException;

import toools.io.JavaResource;
import toools.io.Utilities;

/**
 * This is the equivalent of the StepAlgo class in Mascopt.
 * 
 * @author lhogie
 * 
 */

public class ConsoleStepper extends AbstractStepper
{
    private boolean verbose = true;

    public ConsoleStepper(Grph g)
    {
	this(g, true, true, true, true);
    }

    public ConsoleStepper(Grph g, final boolean vertexEvent, final boolean edgeEvent, final boolean colorEvent,
	    final boolean labelEvent)
    {
	super(g, vertexEvent, edgeEvent, colorEvent, labelEvent);
    }

    public static void main(String[] args)
    {
	final Grph g = new grph.in_memory.InMemoryGrph();
	new ConsoleStepper(g);
	g.display();
	g.grid(5, 5);

	new BFSAlgorithm().compute(g, 0, Grph.DIRECTION.out, new GraphSearchListener() {

	    @Override
	    public DECISION vertexFound(int v)
	    {
		g.getVertexColorProperty().setValue(v, 4);
		return DECISION.CONTINUE;
	    }

	    @Override
	    public void searchStarted()
	    {
	    }

	    @Override
	    public void searchCompleted()
	    {
	    }
	});
    }

    @Override
    protected void waitForUserInteraction()
    {
	if (verbose)
	    System.out.println();

	String s = Utilities.readUserInput("Grph stepper> ", ".*");

	if (s.equals("quit") || s.equals("q") || s.equals("exit"))
	{
	    System.exit(0);
	}
	else if (s.equals("end") || s.equals("stop"))
	{
	    setNumberOfStepsUntilNextWait(Integer.MAX_VALUE);
	    System.out.println("the stepper is now deactivated");
	}
	else if (s.matches("[0-9]+"))
	{
	    setNumberOfStepsInAJump(Integer.valueOf(s));
	    setNumberOfStepsUntilNextWait(getNumberOfStepsInAJump());

	    System.out.println("the stepper will now jump " + getNumberOfStepsInAJump() + " step(s)");
	}
	else if (s.equals("help") || s.equals("h"))
	{
	    try
	    {
		System.out.println(new String(new JavaResource(getClass(), "stepper-help.txt").getByteArray()));
	    }
	    catch (IOException e)
	    {
		System.err.println("Cannot find help!");
	    }

	    waitForUserInteraction();
	}
	else if (s.equals("verbose") || s.equals("v"))
	{
	    verbose = !verbose;
	    System.out.println("verbose mode " + (verbose ? "on" : "off"));
	    waitForUserInteraction();
	}
	else if (s.equals(""))
	{
	    setNumberOfStepsUntilNextWait(getNumberOfStepsInAJump());

	    if (!verbose)
	    {
		System.out.println("jumping " + getNumberOfStepsInAJump() + " step(s)");
	    }
	}
	else
	{
	    System.err.println("I beg your pardon?");
	    waitForUserInteraction();
	}

    }

    @Override
    protected void printImpl(String s)
    {
	System.out.println("*** " + s);
    }

}
