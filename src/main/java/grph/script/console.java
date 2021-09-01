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
Julien Deantoni (I3S, Université Cote D'Azur, Saclay) 

*/
 
 

package grph.script;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import bsh.EvalError;
import bsh.Interpreter;
import grph.Grph;
import it.unimi.dsi.fastutil.doubles.DoubleArrayList;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import j4u.CommandLine;
import jline.ConsoleReader;
import toools.StopWatch;
import toools.exceptions.ExceptionUtilities;
import toools.extern.Proces;
import toools.io.file.Directory;
import toools.io.file.RegularFile;
import toools.io.ser.Serializer;
import toools.math.Distribution;
import toools.text.TextUtilities;

public class console extends AbstractGrphScript
{

	public console(RegularFile launcher)
	{
		super(launcher);
		// TODO Auto-generated constructor stub
	}

	
	@Override
	public int runScript(CommandLine cmdLine)
	{
		ClassLoader.getSystemClassLoader().setDefaultAssertionStatus(true);
		Interpreter beanShellInterpreter = new Interpreter();

		try
		{
			beanShellInterpreter.eval("import grph.Grph;");
		}
		catch (EvalError e)
		{
			e.printStackTrace();
		}

		try
		{
			printMessage("Grph - the unpronouceable Graph library. Version " + getVersion());
			printMessage("Webpage: http://www-sop.inria.fr/members/Luc.Hogie/grph/");
			printMessage("Copyright CNRS/INRIA/I3S/UNS. 2008-2011.");
			printMessage("");
			ConsoleReader jLineStdInput = new ConsoleReader();
			loadHistory(jLineStdInput);

			List<String> st = new ArrayList<String>();
			for (Method m : Grph.class.getMethods())
			{
				st.add(m.getName());
			}

			// Completor completor = new ArgumentCompletor(new
			// SimpleCompletor(st.toArray(new String[0])), );
			// cr.addCompletor(completor);
			StopWatch stopWatch = null;
			boolean debug = false;

			while (true)
			{
				printMessage("");
				String cmd = jLineStdInput.readLine("Grph> ").trim();
				saveHistory(jLineStdInput);

				if (cmd.equals("exit") || cmd.equals("quit") || cmd.equals("bye"))
				{
					break;
				}
				else if (cmd.equals("debug"))
				{
					debug = !debug;
					printMessage("Debug " + (debug ? "on" : "off"));
				}
				else if (cmd.equals("help"))
				{
					printMessage("This is Grph " + getVersion() + " console. It is based on BeanShell " + Interpreter.VERSION);
					printMessage("Additional commands include:");
					printMessage("\thelp\t\tshow this help message");
					printMessage("\tchrono\t\ttoggle the chronometer");
					printMessage("\texit|quit|bye\tterminate the Grph console");
					printMessage("\tdebug\t\tprints full stack trace when an error occurs");
					printMessage("\t!\t\tescape to a subshell (ex: !ls)");
				}
				else if (cmd.equals("chrono"))
				{
					stopWatch = stopWatch == null ? new StopWatch() : null;
					printMessage("Chronometer " + (stopWatch == null ? "off" : "on"));
				}
				else if (cmd.startsWith("!"))
				{
					List<String> tokens = Arrays.asList(cmd.substring(1).split(" +"));
					String command = tokens.get(0);
					String[] arguments = tokens.subList(1, tokens.size()).toArray(new String[0]);
					byte[] result = Proces.exec(command, Directory.getCurrentDirectory(), arguments);
					printMessage(new String(result));
				}
				else
				{
					if (!cmd.endsWith(";"))
						cmd = cmd + ";";

					// if a function is called, the console considers that
					// it is a static method of the Grph class
					if (cmd.matches("[a-zA-Z0-9]+\\(.*"))
					{
						cmd = "Grph." + cmd;
					}

					try
					{
						if (stopWatch != null)
						{
							stopWatch.reset();
						}

						Object result = beanShellInterpreter.eval(cmd);

						if (stopWatch != null)
						{
							printMessage("*** computed in " + stopWatch.getElapsedTime() + "ms *** ");
						}

						if (result != null)
						{
							if (result instanceof Distribution)
							{
								Distribution d = (Distribution) result;
								printMessage(d.toGNUPlotData(false));
								d.display(null);
							}
							else if (result instanceof byte[])
							{
								printMessage(TextUtilities.toHex((byte[]) result, ' '));
							}
							else if (result instanceof int[])
							{
								printMessage(new IntArrayList((int[]) result));
							}
							else if (result instanceof double[])
							{
								printMessage(new DoubleArrayList((double[]) result));
							}
							else
							{
								printMessage(result);
							}
						}
					}
					catch (EvalError e)
					{
						String msg = ExceptionUtilities.extractMsgFromStrackTrace(e);

						if (debug || msg == null)
						{
							e.printStackTrace();
						}
						else
						{
							error(msg);
						}
					}
				}
			}
		}
		catch (IOException ex)
		{
			ex.printStackTrace();
		}

		printMessage("Goodbye.");

		return 0;
	}

	private void saveHistory(ConsoleReader jLineStdInput) throws IOException
	{
		List<String> list = jLineStdInput.getHistory().getHistoryList();

		// retains only the 100 last elements
		list = new ArrayList<String>(list.subList(Math.max(list.size() - 100, 0), list.size()));
		getDataFile("history").setContent(Serializer.getDefaultSerializer().toBytes(list));
	}

	private void loadHistory(ConsoleReader jLineStdInput) throws IOException
	{
		RegularFile historyFile = getDataFile("history");

		if (historyFile.exists())
		{
			for (String l : (List<String>) Serializer.getDefaultSerializer().fromBytes(historyFile.getContent()))
			{
				jLineStdInput.getHistory().addToHistory(l);
			}
		}
	}

	private void error(String msg)
	{
		System.err.println("Error: " + msg);
		printMessage("");
	}

	@Override
	public String getShortDescription()
	{
		return "Runs the Grph Java console. It is based on BeanShell, which dynamically interprets Java code. It also offers some improvements: for example you"
				+ "don't have to type variables.";
	}

}
