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

package grph.algo.degree;

import grph.Grph;
import grph.Grph.DIRECTION;
import grph.GrphAlgorithm;
import it.unimi.dsi.fastutil.ints.IntIterator;
import toools.exceptions.NotYetImplementedException;
import toools.math.MathsUtilities;

public abstract class AbstractDegreeAlgorithm extends GrphAlgorithm<Integer>
{
	enum STAT
	{
		minimum, maximum
	}

	protected abstract Grph.TYPE getType();

	protected abstract STAT getStat();

	protected abstract DIRECTION getDirection();

	@Override
	public final Integer compute(Grph graph)
	{
		if (getStat() == STAT.minimum)
		{
			return getMinDegree(graph, getType(), getDirection());
		}
		else
		{
			return getMaxDegree(graph, getType(), getDirection());
		}
	}

	public static int getMaxDegree(final Grph graph, final Grph.TYPE type,
			final Grph.DIRECTION dir)
	{
		return MathsUtilities.max(new IntIterator()
		{
			IntIterator i = graph.getVertices().iterator();

			@Override
			public int nextInt()
			{
				return graph.getVertexDegree(i.nextInt(), type, dir);
			}

			@Override
			public Integer next()
			{
				return nextInt();
			}

			@Override
			public int skip(int i)
			{
				throw new NotYetImplementedException();
			}

			@Override
			public boolean hasNext()
			{
				return i.hasNext();
			}
		});
	}

	public static int getMinDegree(final Grph graph, final Grph.TYPE type,
			final Grph.DIRECTION dir)
	{
		return MathsUtilities.computeMinimum(new IntIterator()
		{
			IntIterator i = graph.getVertices().iterator();

			@Override
			public int nextInt()
			{
				return graph.getVertexDegree(i.nextInt(), type, dir);
			}

			@Override
			public boolean hasNext()
			{
				return i.hasNext();
			}

			@Override
			public Integer next()
			{
				return nextInt();
			}

			@Override
			public int skip(int i)
			{
				throw new NotYetImplementedException();
			}

		});

	}
}
