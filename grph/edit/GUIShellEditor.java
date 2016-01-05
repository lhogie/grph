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

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class GUIShellEditor extends ShellEditor
{
    private final JTextField tf = new JTextField();
    private final JTextArea p = new JTextArea();

    public GUIShellEditor(Grph g)
    {
	super(g);
	JFrame f = new JFrame("Grph editor");
	f.setSize(600, 800);
	f.getContentPane().setLayout(new BorderLayout());
	f.getContentPane().add(tf, BorderLayout.SOUTH);
	f.getContentPane().add(p, BorderLayout.CENTER);

	tf.addActionListener(new AL());

	f.setVisible(true);
    }

    private class AL implements ActionListener
    {
	@Override
	public void actionPerformed(ActionEvent event)
	{
	    execute(tf.getText());
	    p.append("\n" + tf.getText());
	    tf.setText("");
	}

    }


    public static void main(String[] args)
    {
	Grph g = new grph.in_memory.InMemoryGrph();
	g.grid(5, 5);
	new GUIShellEditor(g);
    }
}
