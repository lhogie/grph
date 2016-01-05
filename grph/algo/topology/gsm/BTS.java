package grph.algo.topology.gsm;

import toools.set.DefaultIntSet;
import toools.set.IntSet;

public class BTS
{
	public BTS(BSC b, int d)
	{
		this.bsc = b;
		this.id = d;
	}

	final BSC bsc;
	final int id;
	final IntSet userDevices = new DefaultIntSet();
}
