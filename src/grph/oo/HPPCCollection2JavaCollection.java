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

package grph.oo;

import java.util.Collection;
import java.util.Iterator;

import toools.NotYetImplementedException;

import com.carrotsearch.hppc.ObjectCollection;
import com.carrotsearch.hppc.cursors.ObjectCursor;

class HPPCCollection2JavaCollection<V> implements Collection<V>
{
	private ObjectCollection<V> c;

	public HPPCCollection2JavaCollection(ObjectCollection<V> c)
	{
		this.c = c;
	}

	@Override
	public boolean add(V arg0)
	{
		throw new NotYetImplementedException();
	}

	@Override
	public boolean addAll(Collection<? extends V> arg0)
	{
		throw new NotYetImplementedException();
	}

	@Override
	public void clear()
	{
		throw new NotYetImplementedException();
	}

	@Override
	public boolean contains(Object o)
	{
		return c.contains((V) o);
	}

	@Override
	public boolean containsAll(Collection<?> otherCollection)
	{
		if (otherCollection.size() > size())
		{
			return false;
		}
		else
		{
			for (Object o : otherCollection)
			{
				if (!contains(o))
				{
					return false;
				}
			}

			return true;
		}
	}

	@Override
	public boolean isEmpty()
	{
		return c.isEmpty();
	}

	@Override
	public Iterator<V> iterator()
	{
		final Iterator<ObjectCursor<V>> i = c.iterator();
		return new Iterator<V>()
		{

			@Override
			public boolean hasNext()
			{
				return i.hasNext();
			}

			@Override
			public V next()
			{
				return i.next().value;
			}

			@Override
			public void remove()
			{
				i.remove();
			}

		};
	}

	@Override
	public boolean remove(Object e)
	{
		throw new NotYetImplementedException();
	}

	@Override
	public boolean removeAll(Collection<?> arg0)
	{
		throw new NotYetImplementedException();
	}

	@Override
	public boolean retainAll(Collection<?> arg0)
	{
		throw new NotYetImplementedException();
	}

	@Override
	public int size()
	{
		return c.size();
	}

	@Override
	public Object[] toArray()
	{
		return c.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a)
	{
		throw new NotYetImplementedException();
	}

}
