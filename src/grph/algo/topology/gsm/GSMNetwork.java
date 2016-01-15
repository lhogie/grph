package grph.algo.topology.gsm;

import grph.Grph;
import grph.algo.topology.ClassicalGraphs;
import grph.algo.topology.RandomTreeTopologyGenerator;
import grph.in_memory.InMemoryGrph;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

public class GSMNetwork
{
	final Grph g;
	final Set<Operator> operators = new HashSet<Operator>();

	public GSMNetwork(int numberOfOperators, int numberOfBSCPerOperator, int numberOfBSTPerBsc, Random prng)
	{
		g = ClassicalGraphs.completeGraph(numberOfOperators);

		for (int operatorID : g.getVertices().toIntArray())
		{
			Operator o = new Operator();
			o.id = operatorID;
			operators.add(o);

			Grph bscGraph = new InMemoryGrph();
			RandomTreeTopologyGenerator.compute(bscGraph, numberOfBSCPerOperator);

			for (int bscID : g.addGraph(bscGraph).values().toIntArray())
			{
				BSC bsc = new BSC(bscID);
				bsc.operators.add(o);
				o.BSCs.add(bsc);

				Grph bstGraph = new InMemoryGrph();
				int side = (int) Math.sqrt(numberOfBSTPerBsc);
				bstGraph.grid(side, side);
				WirelessBackhaul.degenerate(bstGraph, bstGraph.getVertices().pickRandomElement(prng), 0.9);

				for (int bstID : g.addGraph(bstGraph).values().toIntArray())
				{
					BTS bst = new BTS(bsc, bstID);
					bsc.BSTs.add(bst);
				}

				int numberOfBstGtw = prng.nextInt(numberOfBSTPerBsc - 1) + 1;
				Iterator<BTS> it = bsc.BSTs.iterator();

				for (int i = 0; i < numberOfBstGtw; ++i)
				{
					BTS interfaceBST = it.next();
					g.addUndirectedSimpleEdge(bsc.id, interfaceBST.id);
				}
			}

			int numberOfBscGtw = prng.nextInt(numberOfBSCPerOperator - 1) + 1;
			Iterator<BSC> it = o.BSCs.iterator();

			for (int i = 0; i < numberOfBscGtw; ++i)
			{
				BSC interfaceBSC = it.next();
				g.addUndirectedSimpleEdge(o.id, interfaceBSC.id);
			}
		}
	}

	public void display()
	{
		for (Operator o : operators)
		{
			g.getVertexColorProperty().setValue(o.id, 5);

			for (BSC bsc : o.BSCs)
			{
				g.getVertexColorProperty().setValue(bsc.id, 6);

				for (BTS bst : bsc.BSTs)
				{
					g.getVertexColorProperty().setValue(bst.id, 7);
				}
			}
		}

		g.display();
	}

	public static void main(String[] args)
	{
		GSMNetwork g = new GSMNetwork(5, 3, 9, new Random());
		System.out.println("operators= " + g.operators);
		g.display();
	}
}
