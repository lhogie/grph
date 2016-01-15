package grph.algo;
import grph.Grph;
import grph.algo.topology.ClassicalGraphs;
import toools.set.BitVectorSet;
import toools.set.IntSet;
import toools.thread.Generator;

import com.carrotsearch.hppc.IntArrayList;
import com.carrotsearch.hppc.cursors.IntCursor;

public class FindAllCycles extends Generator<IntArrayList>
{
	private final Grph g;
	private final int[][] adj;
	private final IntArrayList currentPath = new IntArrayList();
	private final IntSet alreadyVisited = new BitVectorSet(0);
	private final IntArrayList currentNeighbor = new IntArrayList();
	private final boolean only;
	private final IntSet startingVertices;

	public FindAllCycles(Grph g)
	{
		this(g, g.getVertices(), true);
	}

	public FindAllCycles(Grph g, IntSet startingVertices, boolean only)
	{
		if (startingVertices == null)
			throw new NullPointerException();

		this.g = g;
		this.startingVertices = startingVertices;
		this.only = only;
		this.adj = g.getOutNeighborhoods();
	}

	@Override
	public void produce()
	{
		for (IntCursor c : startingVertices)
		{
			int startingVertex = c.value;
			currentPath.add(startingVertex);
			currentNeighbor.add(0);
			alreadyVisited.add(startingVertex);

			if (only)
			{
				// add all vertices lower than startingVertex
				for (IntCursor cc : g.getVertices())
				{
					if (cc.value < startingVertex)
					{
						alreadyVisited.add(cc.value);
					}
				}
			}

			while ( ! currentPath.isEmpty())
			{
				int u = currentPath.get(currentPath.size() - 1);
				int d = adj[u].length;

				// we already have explored all neighbors of u
				if (currentNeighbor.get(currentNeighbor.size() - 1) == d)
				{
					currentPath.remove(currentPath.size() - 1);
					currentNeighbor.remove(currentNeighbor.size() - 1);
					alreadyVisited.remove(u);
					continue;
				}

				final int nextNeighbor = adj[u][currentNeighbor.get(currentNeighbor
						.size() - 1)];
				currentNeighbor.set(currentNeighbor.size() - 1,
						currentNeighbor.get(currentNeighbor.size() - 1) + 1);

				// we have found a circuit
				if (nextNeighbor == startingVertex)
				{
					// yield(currentPath)
					deliver(currentPath);
					continue;
				}

				if (alreadyVisited.contains(nextNeighbor)
						|| ! pathExists(nextNeighbor, startingVertex, alreadyVisited))
					continue;

				currentPath.add(nextNeighbor);
				alreadyVisited.add(nextNeighbor);
				currentNeighbor.add(0);
			}
		}

		return;
	}

	private boolean pathExists(final int src, final int dest, IntSet verticesToAvoid)
	{
		IntArrayList queue = new IntArrayList(g.getNumberOfVertices());
		queue.add(src);
		IntSet set = new BitVectorSet(0);
		set.add(src);
		set.addAll(verticesToAvoid);

		while ( ! queue.isEmpty())
		{
			int v = queue.remove(0);

			for (int n : adj[v])
			{
				if (n == dest)
					return true;

				if ( ! set.contains(n))
				{
					set.add(n);
					queue.add(n);
				}
			}
		}

		return false;
	}
	
	public static void main(String[] args)
	{
		int n = 2;
		Grph g = ClassicalGraphs.grid(n, n);

		FindAllCycles algo = new FindAllCycles(g);

		for (IntArrayList v : algo)
		{
			System.out.println(v);
		}
		
		System.out.println("DONE");
	}
}
