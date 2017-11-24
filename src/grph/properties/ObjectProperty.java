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

import it.unimi.dsi.fastutil.ints.IntSet;
import toools.collections.AutoGrowingArrayList;

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
		if ( ! (otherProperty instanceof ObjectProperty))
			throw new IllegalArgumentException();

		ObjectProperty p = (ObjectProperty) otherProperty;
		p.objects.addAll(objects);
	}

	public void setValue(int id, T newValue)
	{
		assert id >= 0 : "id must be positive";

		T oldValue = id < objects.size() ? objects.get(id) : null;

		if (oldValue == null || newValue == null || ! oldValue.equals(newValue))
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
