package grph.in_memory;

import it.unimi.dsi.fastutil.ints.IntArrayList;

class AutoSizingIntArrayList extends IntArrayList
{
	@Override
	public int set(int index, int k)
	{
		if (size() < index + 1)
			size(index + 1);
		
		return super.set(index, k);
	}
}
