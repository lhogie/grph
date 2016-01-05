package grph.algo.search;

import grph.properties.NumericalProperty;

public abstract class WeightedSingleSourceSearchAlgorithm extends SingleSourceSearchAlgorithm<SearchResult>
{
	private final NumericalProperty weightProperty;

	public NumericalProperty getWeightProperty()
	{
		return weightProperty;
	}

	public WeightedSingleSourceSearchAlgorithm(NumericalProperty weightProperty)
	{
		this.weightProperty = weightProperty;
	}
}
