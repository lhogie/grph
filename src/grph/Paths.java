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

import grph.path.Path;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class Paths
{

	public static Path findLongestPath(Collection<Path> paths)
	{
		assert paths != null;
		assert !paths.isEmpty();

		Iterator<Path> i = paths.iterator();
		Path longestPath = i.next();
		int maxLength = longestPath.getLength();

		while (i.hasNext())
		{
			Path thisPath = i.next();
			int thisLength = thisPath.getLength();

			if (thisLength > maxLength)
			{
				maxLength = thisLength;
				longestPath = thisPath;
			}
		}

		return longestPath;
	}

	public static Collection<Path> findPathHeadingFrom(int source, Collection<Path> paths)
	{
		assert source > 0;
		assert paths != null;

		Collection<Path> result = new ArrayList<Path>();

		for (Path thisPath : paths)
		{
			if (thisPath.getSource() == source)
			{
				result.add(thisPath);
			}
		}

		return result;
	}

	public static Collection<Path> findPathsPassingBy(int someVertex, Collection<Path> paths)
	{
		assert someVertex > 0;
		assert paths != null;
		Collection<Path> result = new ArrayList<Path>();

		for (Path thisPath : paths)
		{
			if (thisPath.containsVertex(someVertex))
			{
				result.add(thisPath);
			}
		}

		return result;
	}

	public static Collection<Path> findPathsConnecting(int source, int destination, Collection<Path> paths)
	{
		assert source > 0;
		assert destination > 0;
		assert paths != null;

		Collection<Path> result = new ArrayList<Path>();

		for (Path thisPath : paths)
		{
			if (thisPath.getSource() == source && thisPath.getDestination() == destination)
			{
				result.add(thisPath);
			}
		}

		return result;
	}

	public static Collection<Path> findPathsHeadingTo(int destination, Collection<Path> paths)
	{
		assert destination > 0;
		assert paths != null;
		Collection<Path> result = new ArrayList<Path>();

		for (Path thisPath : paths)
		{
			if (thisPath.getDestination() == destination)
			{
				result.add(thisPath);
			}
		}

		return result;
	}
}
