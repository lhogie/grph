package grph.properties;
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



import grph.in_memory.InMemoryGrph;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import toools.MemoryObject;

import com.carrotsearch.hppc.BitSet;

public abstract class Property implements MemoryObject, Serializable
{
	private final String name;

	private final List<PropertyListener> listeners = new ArrayList<PropertyListener>();
	private BitSet statusBitset = new BitSet();


	public Property(String name)
	{
		this.name = name;
	}


	public void toGrphBinary(DataOutputStream os) throws IOException
	{
		os.writeInt(statusBitset.bits.length);

		for (long l : statusBitset.bits)
		{
			os.writeLong(l);
		}
	}

	public void fromGrphBinary(DataInputStream is) throws IOException
	{
		int numWords = is.readInt();
		long[] bits = new long[numWords];

		for (int i = 0; i < numWords; ++i)
		{
			bits[i] = is.readLong();
		}

		statusBitset = new BitSet(bits, numWords);
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

	public  void cloneValuesTo(Property p)
	{
		p.statusBitset = (BitSet) statusBitset.clone();
	}
}
