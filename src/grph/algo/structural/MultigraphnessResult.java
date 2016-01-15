package grph.algo.structural;

import toools.set.IntSet;

public class MultigraphnessResult
{
	public int v1, v2;
	public IntSet edges;
	
	@Override
	public String toString()
	{
		return v1 + " > " + edges + " > " + v2;
	}
}
