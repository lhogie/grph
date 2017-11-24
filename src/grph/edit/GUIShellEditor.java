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
 
 package grph.edit;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import grph.Grph;

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
