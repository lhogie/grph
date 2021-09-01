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

package grph.in_memory;

import java.io.Serializable;

import grph.properties.ObjectProperty;
import toools.collections.primitive.LucIntSet;
import toools.exceptions.NotYetImplementedException;

public class IncidenceList implements Serializable
{
	private final ObjectProperty<GrphIntSet> p;

	public IncidenceList(int expected)
	{
		p = new ObjectProperty<GrphIntSet>(null, expected)
		{

			@Override
			public boolean acceptValue(GrphIntSet value)
			{
				return true;
			}

			@Override
			protected long sizeOf(GrphIntSet o)
			{
				throw new NotYetImplementedException();
			}

			@Override
			public void setValue(int e, String value)
			{
				throw new NotYetImplementedException();
			}

		};

	}

	public LucIntSet getValue(int e)
	{
		return p.getValue(e);
	}

	public void remove(int a, int b)
	{
		p.getValue(a).remove(b);
	}

	public boolean hasValue(int e)
	{

		return p.getValue(e) != null;
	}

	public void add(int a, int b)
	{
		p.getValue(a).add(b);
	}

	public void setEmptySet(int e)
	{
		p.setValue(e, new GrphIntSet(0));
	}

	public void unsetValue(int e)
	{
		p.setValue(e, (GrphIntSet) null);
		p.setStatus(e, false);
	}
}
