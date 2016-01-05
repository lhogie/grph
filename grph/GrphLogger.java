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

package grph;

import java.io.FileNotFoundException;
import java.io.PrintStream;

import toools.io.file.Directory;
import toools.io.file.RegularFile;
import toools.log.Logger;

public class GrphLogger extends Logger
{
	public GrphLogger()
	{
		super(true);
	}

	private static RegularFile grphLogFile = new RegularFile(Directory.getHomeDirectory()
			.getChildDirectory(".grph"), "grph.log");
	private static PrintStream filePs;

	static
	{
		if ( ! grphLogFile.getParent().exists())
			grphLogFile.getParent().mkdirs();

		try
		{
			filePs = new PrintStream(grphLogFile.createWritingStream());
		}
		catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void logImpl(Object o)
	{
		System.out.println(o);
		filePs.println(o);
		filePs.flush();
	}

}
