package grph.algo.msp;

import grph.Grph;
import grph.in_memory.InMemoryGrph;
import grph.properties.NumericalProperty;
import grph.properties.Property;

import java.util.ArrayList;

import toools.set.IntSet;

public class KruskalAlgorithm
{
	public static Grph kruskalByTom(Grph g, NumericalProperty edgesWeight)
	{
		// add all the edges with their weight in an ArrayList and sort it

		IntSet edges = g.getEdges();
		int[] edgesArray = edges.toIntArray();
		ArrayList<EdgeProperty> edgesValues = new ArrayList<EdgeProperty>();

		for (int i = 0; i < edgesArray.length; i++)
		{
			EdgeProperty edgeValue = new EdgeProperty(edgesArray[i], edgesWeight.getValueAsInt(edgesArray[i]));
			edgesValues.add(edgeValue);
		}
		java.util.Collections.sort(edgesValues);

		// fill the new graph with lowest edges and checks if there is no cycle.
		Grph mst = new InMemoryGrph();

		for (int i = 0; i < edgesValues.size(); i++)
		{

			int edge = edgesValues.get(i).getId();
			int v1 = g.getOneVertex(edge);
			int v2 = g.getTheOtherVertex(edge, v1);

			if (!mst.containsVertex(v1))
				mst.addVertex(v1);
			if (!mst.containsVertex(v2))
				mst.addVertex(v2);
			mst.addSimpleEdge(v1, edge, v2, false);

			IntSet cycle = mst.getShortestCycle();
			if (!cycle.isEmpty())
				mst.removeEdge(edge);

			if (mst.getNumberOfEdges() >= g.getNumberOfVertices() - 1)
				break;
		}

		// get graph's properties
		copyProperties(g, mst);

		return mst;
	}

	private static void copyProperties(Grph g, Grph target)
	{

		NumericalProperty verticesColor = g.getVertexColorProperty();
		Property verticesLabel = g.getVertexLabelProperty();
		NumericalProperty verticesShape = g.getVertexShapeProperty();
		NumericalProperty verticesSize = g.getVertexSizeProperty();

		target.setVerticesColor(verticesColor);
		target.setVerticesLabel(verticesLabel);
		target.setVerticesShape(verticesShape);
		target.setVerticesSize(verticesSize);

		NumericalProperty edgesColor = g.getEdgeColorProperty();
		Property edgesLabel = g.getEdgeLabelProperty();
		NumericalProperty edgesStyle = g.getEdgeStyleProperty();
		NumericalProperty edgesWidth = g.getEdgeWidthProperty();

		target.setEdgesColor(edgesColor);
		target.setEdgesLabel(edgesLabel);
		target.setEdgesStyle(edgesStyle);
		target.setEdgesWidth(edgesWidth);

	}

	public static void main(String[] args)
	{
		Grph g = new InMemoryGrph();
		g.grid(5,  5);
		Grph mst = kruskalByTom(g, g.getEdgeWidthProperty());
		mst.display();
	}
}
