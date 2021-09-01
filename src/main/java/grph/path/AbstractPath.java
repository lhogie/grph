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

package grph.path;

import grph.Grph;
import grph.properties.NumericalProperty;
import it.unimi.dsi.fastutil.ints.IntSet;
import toools.collections.primitive.SelfAdaptiveIntSet;

/**
 * A abstraction of path. A path is seen as a sequence of vertices. At this
 * level, no implementation is defined.
 * 
 * @author lhogie
 * 
 */
public abstract class AbstractPath implements Path
{
	public abstract AbstractPath clone();

	/**
	 * Computes the vertices in this path as a set.
	 * 
	 * @return the vertices in this path as a set.
	 */
	@Override
	public IntSet getVertexSet()
	{
		return toVertexSet();
	}

	public IntSet toVertexSet()
	{
		int sz = getLength();
		IntSet s = new SelfAdaptiveIntSet(sz);

		for (int i = 0; i < sz; ++i)
		{
			s.add(getVertexAt(i));
		}

		return s;
	}

	public void extend(int v)
	{
		extend( - 1, v);
	}

	@Override
	public boolean isElementary()
	{
		int[] a = toVertexArray();
		IntSet s = new SelfAdaptiveIntSet(a.length);

		for (int v : a)
		{
			if (s.contains(v))
			{
				return false;
			}
			else
			{
				s.add(v);
			}
		}

		return true;
	}

	/**
	 * Checks whether the given path has the same source and destination has
	 * this path.
	 * 
	 * @param anotherPath
	 * @return true the given path has the same source and destination has this
	 *         path, false otherwise
	 */
	@Override
	public boolean permitsSameTrip(Path anotherPath)
	{
		return this.getSource() == anotherPath.getSource()
				&& this.getDestination() == anotherPath.getDestination();
	}

	/**
	 * Checks whether this path is a shortest path in the given graph.
	 * 
	 * @param g
	 * @return true this path is a shortest path in the given graph, false
	 *         otherwise.
	 */
	@Override
	public boolean isShortestPath(Grph g, NumericalProperty weights)
	{
		return g.search(getSource(), weights).distances[getDestination()] == getLength();
	}

	/**
	 * Assigns the given color to all vertices in this path, when applied to the
	 * given graph.
	 * 
	 * @param g
	 * @param color
	 */
	@Override
	public void setColor(Grph g, int color)
	{
		int sz = getNumberOfVertices();

		for (int i = 0; i < sz; ++i)
		{
			int v = getVertexAt(i);
			g.getVertexColorProperty().setValue(v, color);
		}

		for (int i = 1; i < sz; ++i)
		{
			int e = getEdgeHeadingToVertexAt(i);

			if (e >= 0)
			{
				g.getEdgeColorProperty().setValue(e, color);
			}
		}
	}

	/**
	 * Checks whether this path is a cycle or not.
	 * 
	 * @return true if this path is a cycle or not, false otherwise
	 */
	@Override
	public boolean isCycle()
	{
		return getSource() == getDestination();
	}

	/**
	 * Checks whether this path is hamiltonian on the given graph.
	 * 
	 * @param g
	 *            the graph
	 * @return true if this path is hamiltonian on the given graph, false
	 *         otherwise
	 */
	@Override
	public boolean isHamiltonian(Grph g)
	{
		return getLength() == g.getNumberOfVertices() && ! hasLoop();
	}

	@Override
	public boolean hasLoop()
	{
		int[] a = toVertexArray();
		IntSet visited = new SelfAdaptiveIntSet(a.length);

		for (int c : a)
		{
			if (visited.contains(c))
			{
				return true;
			}
			else
			{
				visited.add(c);
			}
		}

		return false;
	}

	@Override
	public boolean equals(Object obj)
	{
		return obj instanceof Path && equals((Path) obj);
	}

	public boolean equals(Path p)
	{
		int sz = getNumberOfVertices();

		if (sz != p.getNumberOfVertices())
		{
			return false;
		}
		else
		{
			for (int i = 0; i < sz; ++i)
			{
				if (getVertexAt(i) != p.getVertexAt(i))
				{
					return false;
				}
			}

			for (int i = 1; i < sz; ++i)
			{
				int e = getEdgeHeadingToVertexAt(i);
				int pe = p.getEdgeHeadingToVertexAt(i);

				if ((e < 0 && pe >= 0) || (e >= 0 && pe < 0)
						|| (e >= 0 && pe >= 0 && e != pe))
				{
					return false;
				}
			}

			return true;
		}
	}

	@Override
	public int getLength()
	{
		return getNumberOfVertices() - 1;
	}

	@Override
	public String toString()
	{
		int n = getNumberOfVertices();

		if (n == 0)
		{
			return "[path does not exist]";
		}
		else
		{
			StringBuilder b = new StringBuilder();

			for (int i = 0; i < n; ++i)
			{
				int v = getVertexAt(i);
				b.append("v" + v);

				if (i < n - 1)
				{
					int e = getEdgeHeadingToVertexAt(i + 1);

					if (e >= 0)
					{
						b.append(" e" + e);
					}
				}

				if (i < n - 1)
				{
					b.append(' ');
				}
			}

			return b.toString();
		}

	}

	@Override
	public int hashCode()
	{
		return toString().hashCode();
	}

	@Override
	public String whyNotApplicable(Grph g)
	{
		int[] list = toVertexArray();

		if (list == null)
		{
			return "no vertices";
		}
		else
		{
			for (int i = list.length - 1; i > 0; --i)
			{
				int v = list[i];
				int pred = list[i - 1];

				if ( ! g.getVertices().contains(v))
				{
					return "vertex  " + v + " not in graph";
				}
				else if ( ! g.getVertices().contains(pred))
				{
					return "vertex  " + pred + " not in graph";
				}
				else if ( ! g.areVerticesAdjacent(pred, v))
				{
					return "no edge from " + pred + " to " + v;
				}
			}

			return null;
		}
	}

	/**
	 * Checks whether this path is applicable to the given graph. A path may not
	 * be applicable if the graph does not have some of the vertices or edges
	 * defined by this path.
	 * 
	 * @param g
	 * @return true if this path is applicable to the given graph, false
	 *         otherwise.
	 */
	@Override
	public boolean isApplicable(Grph g)
	{
		return whyNotApplicable(g) == null;
	}
}
