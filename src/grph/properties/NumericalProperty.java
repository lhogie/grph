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

import grph.Grph;
import grph.algo.topology.ClassicalGraphs;

import java.awt.Color;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import toools.UnitTests;
import toools.gui.ColorPalette;
import toools.gui.EGA16Palette;
import toools.set.DefaultIntSet;
import toools.set.IntSet;

import com.carrotsearch.hppc.BitSet;
import com.carrotsearch.hppc.cursors.IntCursor;

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

		return palette.getColorAtIndex(getValueAsInt(e));
	}

	public void setValue(final int id, final long newValue)
	{
		assert id >= 0;
		assert numberOfBitsPerValue == 64 || newValue >> numberOfBitsPerValue == 0 : "the value requires more than " + numberOfBitsPerValue
				+ " bits to be stored";

		long oldValue = getValue(id);

		if (newValue != oldValue)
		{
			int startIndex = id * numberOfBitsPerValue;
			valuesBitset.ensureCapacity(startIndex + numberOfBitsPerValue);

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

		assert getValue(id) == newValue : "expecting " + newValue + " but got " + getValue(id);
	}

	public static void main(String[] args)
	{
		NumericalProperty p = new NumericalProperty("cc", 4, 0);
		p.setValue(0, 12);
		System.out.println(p.getValue(0));

	}

	@Override
	public void fromGrphBinary(DataInputStream is) throws IOException
	{
		super.fromGrphBinary(is);
		defaultValue = is.readLong();

		int numWords = is.readInt();
		long[] bits = new long[numWords];

		for (int i = 0; i < numWords; ++i)
		{
			bits[i] = is.readLong();
		}

		valuesBitset = new BitSet(bits, numWords);
	}

	@Override
	public void toGrphBinary(DataOutputStream os) throws IOException
	{
		super.toGrphBinary(os);
		os.writeLong(defaultValue);
		os.writeInt(valuesBitset.bits.length);

		for (long l : valuesBitset.bits)
		{
			os.writeLong(l);
		}
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

	private static void test()
	{
		Grph g = ClassicalGraphs.cycle(1000);
		NumericalProperty p = new NumericalProperty("test", 64, 0);
		p.setValue(435, 3);
		UnitTests.ensure(p.getValueAsInt(434) == 0);
		UnitTests.ensure(p.getValueAsInt(435) == 3);
		p.setValue(5, Math.PI);
		UnitTests.ensure(p.getValueAsDouble(5) == Math.PI);
	}

	@Override
	public void cloneValuesTo(Property otherProperty)
	{
		super.cloneValuesTo(otherProperty);

		if (!(otherProperty instanceof NumericalProperty))
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
		IntSet r = new DefaultIntSet();

		for (IntCursor c : s)
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
		return (valuesBitset.length() * 64 + 4 + 8)/8;
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
