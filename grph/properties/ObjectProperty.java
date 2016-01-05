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

package grph.properties;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import toools.collections.AutoGrowingArrayList;
import toools.set.IntSet;

public abstract class ObjectProperty<T> extends Property
{
	private AutoGrowingArrayList<T> objects = new AutoGrowingArrayList<T>();

	public ObjectProperty(String name)
	{
		super(name);
	}

	@Override
	public boolean haveSameValues(int a, int b)
	{
		return getValue(a).equals(getValue(b));
	}

	public T getValue(int id)
	{
		return id < objects.size() ? objects.get(id) : null;
	}

	@Override
	public String getValueAsString(int id)
	{
		T v = getValue(id);
		return v == null ? null : v.toString();
	}

	@Override
	public void cloneValuesTo(Property otherProperty)
	{
		if (!(otherProperty instanceof ObjectProperty))
			throw new IllegalArgumentException();

		ObjectProperty p = (ObjectProperty) otherProperty;
		p.objects.addAll(objects);
	}

	public void setValue(int id, T newValue)
	{
		assert id >= 0 : "id must be positive";

		T oldValue = id < objects.size() ? objects.get(id) : null;

		if (oldValue == null || newValue == null || !oldValue.equals(newValue))
		{
			this.objects.set(id, newValue);

			for (PropertyListener l : getListeners())
			{
				l.valueChanged(this, id);
			}
		}
	}

	public abstract boolean acceptValue(T value);

	public void setAllValues(IntSet s, T v)
	{
		for (int e : s.toIntArray())
		{
			setValue(e, v);
		}
	}

	@Override
	public void fromGrphBinary(DataInputStream is) throws IOException
	{
		try
		{
			objects = (AutoGrowingArrayList) new ObjectInputStream(is).readObject();
		}
		catch (ClassNotFoundException e)
		{
			throw new IOException(e);
		}
	}

	@Override
	public void toGrphBinary(DataOutputStream os) throws IOException
	{
		ObjectOutputStream oos = new ObjectOutputStream(os);
		oos.writeObject(objects);
	}

	@Override
	public long getMemoryFootprintInBytes()
	{
		// size of the empty array
		long s = 16 + 4;

		for (T t : objects)
		{
			s += sizeOf(t);
		}

		return s;
	}

	protected abstract long sizeOf(T o);
}
