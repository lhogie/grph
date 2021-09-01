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
 
 

package grph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import grph.path.Path;

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
