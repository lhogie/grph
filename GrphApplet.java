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
 
 import java.applet.Applet;
import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Event;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JEditorPane;
import javax.swing.JScrollPane;

import toools.io.JavaResource;
import bsh.EvalError;
import bsh.Interpreter;

public class GrphApplet extends Applet implements ActionListener, KeyListener
{
	TextField tf;
	JEditorPane ta;
	Interpreter interpreter;
	String all;
	List<String> history;
	int index = 0;

	@Override
	public void init()
	{

	}

	@Override
	public void destroy()
	{
	}

	@Override
	public void start()
	{
		tf = new TextField("g = new Grph();");

		ta = new JEditorPane("text/html", "<b>Welcome to <i>Grph (v" + getVersion() + ")</i></b>");
		interpreter = new Interpreter();

		all = "";
		history = new ArrayList<String>();

		ta.setEditable(false);
		setLayout(new BorderLayout());
		add("Center", new JScrollPane(ta));
		add("South", tf);
		tf.addActionListener(this);
		tf.addKeyListener(this);
		history.add("");

		try
		{
			interpreter.eval("import grph.Grph;");
		}
		catch (EvalError e)
		{
			e.printStackTrace();
		}
	}

	private String getVersion()
	{
		try
		{
			return new String(new JavaResource("/grph-version.txt").getByteArray());
		}
		catch (IOException e)
		{
			return "(unknown version)";
		}
	}

	@Override
	public void stop()
	{

	}

	@Override
	public void processEvent(AWTEvent e)
	{
		if (e.getID() == Event.WINDOW_DESTROY)
		{
			System.exit(0);
		}
	}

	@Override
	public String getAppletInfo()
	{
		return "Grph";
	}

	@Override
	public void actionPerformed(ActionEvent arg0)
	{
		String req = tf.getText();
		tf.setText("");
		history.remove(history.size() - 1);
		history.add(req);
		history.add("");
		index = history.size() - 1;

		try
		{
			Object result = interpreter.eval(req);
			ta.setText(all += "<br><br><i>&gt; " + req + "</i><br><b>" + result.toString() + "</b>");
		}
		catch (EvalError e)
		{
			ta.setText(all += "<br><br><font color=red>" + e.getMessage() + "</font>");
		}

	}

	@Override
	public void keyPressed(KeyEvent e)
	{
		KeyEvent ke = (KeyEvent) e;

		if (e.getKeyCode() == KeyEvent.VK_UP)
		{
			if (index > 0)
			{
				tf.setText(history.get(--index));
			}
		}
		else if (e.getKeyCode() == KeyEvent.VK_DOWN)
		{
			if (index < history.size() - 1)
			{
				tf.setText(history.get(++index));
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent arg0)
	{
	}

	@Override
	public void keyTyped(KeyEvent arg0)
	{
		this.history.set(history.size() - 1, tf.getText());
	}

}
