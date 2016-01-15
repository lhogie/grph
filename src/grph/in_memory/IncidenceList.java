package grph.in_memory;

import grph.properties.ObjectProperty;
import toools.NotYetImplementedException;
import toools.set.IntSet;

public class IncidenceList
{
	private final ObjectProperty<GrphInternalSet> p;

	public IncidenceList()
	{
		p = new ObjectProperty<GrphInternalSet>(null) {

			@Override
			public boolean acceptValue(GrphInternalSet value)
			{
				return true;
			}

			@Override
			protected long sizeOf(GrphInternalSet o)
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

	public IntSet getValue(int e)
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
		p.setValue(e, new GrphInternalSet());
	}

	public void unsetValue(int e)
	{
		p.setValue(e, (GrphInternalSet) null);
		p.setStatus(e, false);
	}
}
