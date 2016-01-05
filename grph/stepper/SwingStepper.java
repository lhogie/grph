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

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * This is the equivalent of the StepAlgo class in Mascopt.
 * 
 * @author lhogie
 * 
 */

public class SwingStepper extends AbstractStepper
{
    final Object lock = new Object();
    JTextArea logTextArea = new JTextArea();

    public SwingStepper(Grph g)
    {
	this(g, true, true, true, true);
    }
    
    public SwingStepper(Grph g, final boolean vertexEvent, final boolean edgeEvent, final boolean colorEvent, final boolean labelEvent)
    {
	super(g, vertexEvent, edgeEvent, colorEvent, labelEvent);
	JFrame f = new JFrame("Grph stepper");
	f.setSize(800, 600);
	JPanel stepPanel = new JPanel();

	final JTextField stepTextField = new JTextField("1", 3);
	JButton stepButton = new JButton();
	stepButton.setLayout(new GridLayout(1, 3));
	stepButton.add(new JLabel("Next "));
	stepButton.add(stepTextField);
	stepButton.add(new JLabel(" step(s)"));

	ActionListener al = new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent arg0)
	    {
		synchronized (lock)
		{
		    String t = stepTextField.getText();

		    if (t.isEmpty())
		    {
			setNumberOfStepsInAJump(Integer.MAX_VALUE);
		    }
		    else if (t.matches("[0-9]+"))
		    {
			setNumberOfStepsInAJump(Integer.valueOf(t));
		    }
		    else
		    {
			setNumberOfStepsInAJump(1);
		    }

		    setNumberOfStepsUntilNextWait(getNumberOfStepsInAJump());
		    lock.notifyAll();
		}
	    }
	};

	logTextArea.setEditable(false);

	JPanel buttonPanel = new JPanel();
	buttonPanel.add(stepButton);

	stepButton.addActionListener(al);
	stepTextField.addActionListener(al);
	stepPanel.setLayout(new BorderLayout());
	stepPanel.add(buttonPanel, BorderLayout.NORTH);
	// stepPanel.add(new JScrollPane(logTextArea), BorderLayout.CENTER);
	stepPanel.add(g.createSwingRenderer(), BorderLayout.CENTER);
	f.setContentPane(stepPanel);
	f.setVisible(true);
    }


    @Override
    protected void waitForUserInteraction()
    {
	try
	{
	    synchronized (lock)
	    {
		System.out.println("wait");
		lock.wait();
	    }
	}
	catch (InterruptedException e)
	{
	    e.printStackTrace();
	}
    }

    public static void main(String[] args)
    {
	final Grph g = new grph.in_memory.InMemoryGrph();
	new SwingStepper(g);
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
    protected void printImpl(String s)
    {
	logTextArea.append(s + "\n");
    }

}
