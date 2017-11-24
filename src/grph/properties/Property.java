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

package grph.properties;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import grph.in_memory.InMemoryGrph;
import toools.MemoryObject;

public abstract class Property implements MemoryObject, Serializable
{
	private final String name;
	private final List<PropertyListener> listeners = new ArrayList<PropertyListener>();
	private BitSet statusBitset = new BitSet();

	public Property(String name)
	{
		this.name = name;
	}

	public boolean isSetted(int id)
	{
		return statusBitset.get(id);
	}

	public void setStatus(int id, boolean setStatus)
	{
		if (setStatus)
		{
			statusBitset.set(id);
		}
		else
		{
			statusBitset.clear(id);
		}
	}

	public void unsetItAll()
	{
		statusBitset.clear();
	}

	public List<PropertyListener> getListeners()
	{
		return listeners;
	}

	public String getName()
	{
		return name;
	}

	public abstract void setValue(int e, String value);

	public abstract String getValueAsString(int e);

	public static StringBuilder toString(InMemoryGrph g, int e)
	{
		StringBuilder b = new StringBuilder();
		Iterator<Property> i = g.getProperties().iterator();

		while (i.hasNext())
		{
			Property p = i.next();
			b.append(p.getValueAsString(e));

			if (i.hasNext())
			{
				b.append(',');
				b.append(' ');
			}
		}

		return b;
	}

	public abstract boolean haveSameValues(int a, int b);

	/**
	 * Finds a given property by its name.
	 * 
	 * @param name
	 *            the name of the property to be looked up
	 * @return the property object
	 */
	public static Property findProperty(String name, Set<Property> properties)
	{
		for (Property p : properties)
		{
			if (p.getName().equals(name))
			{
				return p;
			}
		}

		return null;
	}

	public void cloneValuesTo(Property p)
	{
		p.statusBitset = (BitSet) statusBitset.clone();
	}
}
