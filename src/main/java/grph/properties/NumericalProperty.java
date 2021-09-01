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

package grph.properties;

import java.awt.Color;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.Random;

import grph.Grph;
import grph.algo.topology.ClassicalGraphs;
import it.unimi.dsi.fastutil.ints.IntSet;
import toools.UnitTests;
import toools.collections.primitive.IntCursor;
import toools.collections.primitive.SelfAdaptiveIntSet;
import toools.exceptions.NotYetImplementedException;
import toools.gui.ColorPalette;
import toools.gui.EGA16Palette;

public class NumericalProperty extends Property
{
	private BitSet valuesBitset;
	private final int numberOfBitsPerValue;
	private long defaultValue;
	private ColorPalette palette = new EGA16Palette();

	public NumericalProperty(String name)
	{
		this(name, 32, 0);
	}

	public NumericalProperty(String name, int numberOfBits, long defaultValue)
	{
		super(name);

		if (numberOfBits > 64)
			throw new IllegalArgumentException("too many bits: " + numberOfBits);

		this.valuesBitset = new BitSet();
		this.numberOfBitsPerValue = numberOfBits;
		this.defaultValue = defaultValue;
	}

	public ColorPalette getPalette()
	{
		return palette;
	}

	public void setPalette(ColorPalette palette)
	{
		this.palette = palette;
	}

	public String toString(IntSet s)
	{
		List<String> l = new ArrayList<String>();

		for (int e : s.toIntArray())
		{
			l.add(e + " => " + getValue(e));
		}

		return l.toString();
	}

	public long getValue(int id)
	{
		if (isSetted(id))
		{
			int startIndex = id * numberOfBitsPerValue;
			long v = 0;

			for (int i = 0; i < numberOfBitsPerValue; ++i)
			{
				if (valuesBitset.get(startIndex + i))
				{
					v |= 1L << i;
				}
			}

			return v;
		}
		else
		{
			return defaultValue;
		}
	}

	public int getValueAsInt(int id)
	{
		return (int) getValue(id);
	}

	public double getValueAsDouble(int id)
	{
		return Double.longBitsToDouble(getValue(id));
	}

	public float getValueAsFloat(int id)
	{
		return Float.intBitsToFloat(getValueAsInt(id));
	}

	public void setValue(int id, double newValue)
	{
		setValue(id, Double.doubleToLongBits(newValue));
	}

	public void setValue(int id, float newValue)
	{
		setValue(id, Float.floatToIntBits(newValue));
	}

	public Color getValueAsColor(int e)
	{
		if (palette == null)
			throw new IllegalStateException("no palette defined");

		return palette.getColor(getValueAsInt(e));
	}

	public void setValue(final int id, final long newValue)
	{
		assert id >= 0;
		assert numberOfBitsPerValue == 64
				|| newValue >> numberOfBitsPerValue == 0 : "the value requires more than "
						+ numberOfBitsPerValue + " bits to be stored";

		long oldValue = getValue(id);

		if (newValue != oldValue)
		{
			int startIndex = id * numberOfBitsPerValue;
			// valuesBitset.ensureCapacity(startIndex + numberOfBitsPerValue);

			for (int i = 0; i < numberOfBitsPerValue; ++i)
			{
				// if the ith bit is set
				if (((newValue >> i) & 1) != 0)
				{
					valuesBitset.set(startIndex + i);
				}
				else
				{
					valuesBitset.clear(startIndex + i);
				}
			}

			setStatus(id, true);

			for (PropertyListener l : getListeners())
			{
				l.valueChanged(this, id);
			}
		}

		assert getValue(id) == newValue : "expecting " + newValue + " but got "
				+ getValue(id);
	}

	public static void main(String[] args)
	{
		NumericalProperty p = new NumericalProperty("cc", 4, 0);
		p.setValue(0, 12);
		System.out.println(p.getValue(0));

	}

	@Override
	public void setValue(int id, String value)
	{
		setValue(id, Long.valueOf(value));
	}

	@Override
	public String getValueAsString(int id)
	{
		return String.valueOf(getValue(id));
	}

	private static void testInt()
	{
		Grph g = ClassicalGraphs.cycle(1000);
		NumericalProperty p = new NumericalProperty("test", 64, 0);
		p.setValue(435, 3);
		UnitTests.ensure(p.getValueAsInt(434) == 0);
		UnitTests.ensure(p.getValueAsInt(435) == 3);
		p.setValue(5, Math.PI);
		UnitTests.ensure(p.getValueAsDouble(5) == Math.PI);
	}

	private static void testDouble()
	{
		Grph g = ClassicalGraphs.grid(10, 10);
		NumericalProperty p = new NumericalProperty("test", 64, 10);
		p.setValue(435, 3);
		UnitTests.ensure(p.getValueAsInt(434) == 10);
		UnitTests.ensure(p.getValueAsInt(435) == 3);
		p.setValue(5, Math.PI);
		UnitTests.ensure(p.getValueAsDouble(5) == Math.PI);
	}

	@Override
	public void cloneValuesTo(Property otherProperty)
	{
		super.cloneValuesTo(otherProperty);

		if ( ! (otherProperty instanceof NumericalProperty))
			throw new IllegalArgumentException();

		NumericalProperty p = (NumericalProperty) otherProperty;

		// if the target property has less bits, we risk to loose data
		if (p.numberOfBitsPerValue < numberOfBitsPerValue)
			throw new IllegalArgumentException();

		p.defaultValue = defaultValue;
		p.valuesBitset = (BitSet) valuesBitset.clone();
	}

	public IntSet findElementsWithValue(long value, IntSet s)
	{
		IntSet r = new SelfAdaptiveIntSet(s.size());

		for (IntCursor c : IntCursor.fromFastUtil(s))
		{
			if (getValue(c.value) == value)
			{
				r.add(c.value);
			}
		}

		return r;
	}

	@Override
	public boolean haveSameValues(int a, int b)
	{
		return getValue(a) == getValue(b);
	}

	@Override
	public long getMemoryFootprintInBytes()
	{
		return (valuesBitset.length() * 64 + 4 + 8) / 8;
	}

	public void randomize(IntSet set, Random random)
	{
		long maxValue = 1 << numberOfBitsPerValue;

		for (int e : set.toIntArray())
		{
			long value = Math.abs(random.nextLong()) % maxValue;
			setValue(e, value);
		}
	}
}
