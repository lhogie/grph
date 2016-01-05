package grph.algo.distance;

import grph.Grph;
import grph.algo.search.StackBasedBellmanFordAlgorithm;
import grph.algo.topology.ClassicalGraphs;
import grph.properties.NumericalProperty;

import java.util.Random;

public class StackBasedBellmanFordWeightedMatrixAlgorithm extends WeightedDistanceMatrixAlgorithm
{

	
	public StackBasedBellmanFordWeightedMatrixAlgorithm(NumericalProperty edgeWeights)
	{
		super(edgeWeights);
	}

	@Override
	public DistanceMatrix compute(Grph g)
	{
		return new DistanceMatrix(new StackBasedBellmanFordAlgorithm(getEdgeWeights()).compute(g));
	}
	
	public static void main(String[] args)
	{
		Grph g = ClassicalGraphs.PetersenGraph();
		NumericalProperty weights = new NumericalProperty("w", 4, 0);
		weights.randomize(g.getVertices(), new Random());
		System.out.println(weights.toString(g.getVertices()));
		System.out.println(new StackBasedBellmanFordWeightedMatrixAlgorithm(weights).compute(g));
	}
}
