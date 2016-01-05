package grph.algo;

import grph.Grph;
import grph.algo.search.SearchResult;
import grph.algo.topology.ClassicalGraphs;
import grph.in_memory.InMemoryGrph;
import grph.properties.NumericalProperty;
import toools.set.IntSet;

public class SpanningTree
{
	public static Grph computeBFSBasedSpanningTree(Grph g)
	{
		return computeBFSBasedSpanningTree(g, g.getVertices().getGreatest(), g.isDirected());
	}

	public static Grph computeBFSBasedSpanningTree(Grph g, int root, boolean directed)
	{
		SearchResult bfs = g.bfs(root);
		Grph spanningTree = new InMemoryGrph();

		for (int v : g.getVertices().toIntArray())
		{
			if (v != root)
			{
				int predecessor = bfs.predecessors[v];
				IntSet edges = g.getEdgesConnecting(predecessor, v);
				int anEdge = edges.iterator().next().value;
				spanningTree.addSimpleEdge(predecessor, anEdge, v, directed);
			}
		}

		return spanningTree;
	}

	public static void main(String[] args)
	{
		Grph g = ClassicalGraphs.grid(5, 5);
	
		System.out.println(g.isDirected());
		Grph spanningTree = computeBFSBasedSpanningTree(g);

		spanningTree.setVerticesLabel(new NumericalProperty("vl") {
			@Override
			public long getValue(int e)
			{
				return e;
			}
		});
		spanningTree.display();
	}
}
