package grph.in_memory;

import grph.properties.NumericalProperty;

public class SimpleEdgeIncidenceList extends NumericalProperty
{
	public SimpleEdgeIncidenceList()
	{
		super("simple edge incidence list",  32, -56);
	}

	@Override
	public void setValue(int id, long newValue)
	{
		assert newValue >= 0;
		super.setValue(id, newValue);
	}

	public static void main(String[] args)
	{
		SimpleEdgeIncidenceList p = new SimpleEdgeIncidenceList();
		p.setValue(5, 0);
		System.out.println(p.getValue(5));
	}

}
