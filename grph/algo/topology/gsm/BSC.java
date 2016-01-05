package grph.algo.topology.gsm;

import java.util.HashSet;
import java.util.Set;

public class BSC
{
	public BSC(int id)
	{
		this.id = id;
	}

	final Set<Operator>  operators = new HashSet<Operator>();
	final int id;
	final Set<BTS> BSTs = new HashSet<BTS>();
}
