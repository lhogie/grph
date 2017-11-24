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
 
 

package grph.stepper;

import java.io.IOException;

import grph.Grph;
import grph.algo.search.BFSAlgorithm;
import grph.algo.search.GraphSearchListener;
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
