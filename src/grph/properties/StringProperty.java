package grph.properties;



public class StringProperty extends ObjectProperty<String>
{

	public StringProperty(String name)
	{ 
		super(name);
	}


	@Override
	public boolean acceptValue(String value)
	{
		return true;
	}

	@Override
	public void setValue(int e, String value)
	{
		super.setValue(e, value);
	}

	@Override
	protected long sizeOf(String o)
	{
		return 16 + o.length() * 2;
	}
}
