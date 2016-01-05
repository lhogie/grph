/*
 * (C) Copyright 2009-2013 CNRS.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser General Public License
 * (LGPL) version 2.1 which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl-2.1.html
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * Contributors:

    Luc Hogie (CNRS, I3S laboratory, University of Nice-Sophia Antipolis) 
    Aurelien Lancin (Coati research team, Inria)
    Christian Glacet (LaBRi, Bordeaux)
    David Coudert (Coati research team, Inria)
    Fabien Crequis (Coati research team, Inria)
    Grégory Morel (Coati research team, Inria)
    Issam Tahiri (Coati research team, Inria)
    Julien Fighiera (Aoste research team, Inria)
    Laurent Viennot (Gang research-team, Inria)
    Michel Syska (I3S, University of Nice-Sophia Antipolis)
    Nathann Cohen (LRI, Saclay) 
 */
 
 package grph.algo.topology;

import grph.Grph;
import grph.in_memory.InMemoryGrph;

import java.util.Random;

import brite.Graph.Edge;
import brite.Graph.Graph;
import brite.Main.ParseConfFile;
import brite.Model.Model;
import brite.Topology.Topology;
import brite.Util.RandomGenManager;
import brite.Util.Util;

/**
 * Provides a bridge to the BRITE topology generator
 * (http://www.cs.bu.edu/brite/).
 * 
 * @author lhogie
 * 
 */

public class BriteTopologyGenerator extends RandomizedTopologyTransform
{

    public ALGO getAlgo()
    {
	return algo;
    }

    public void setAlgo(ALGO algo)
    {
	this.algo = algo;
    }

    public enum ALGO
    {
	RT_WAXMAN, RT_BARABASI, AS_WAXMAN, AS_BARABASI, HI_TOPDOWN, HI_BOTTOMUP, RT_FILE, AS_FILE

	, RT_BARABASI2, AS_BARABASI2, RT_GLP, AS_GLP
    };

    public enum NODE_PLACEMENT
    {
	Random, HeavyTailed
    };

    public enum GrowthType
    {
	Incremental, All
    };

    public enum BWDist
    {
	Constant, Uniform, HeavyTailed, Exponential
    }

    private ALGO algo = ALGO.AS_WAXMAN;
    private int numberOfSquaresInTheMainPlane = 1000;
    private int numberOfSquaresInInnerPlanes = 100;
    private NODE_PLACEMENT nodePlacement = NODE_PLACEMENT.HeavyTailed;
    private GrowthType growthType = GrowthType.Incremental;
    private int numberOfNeighboringNodesEachNewNodeConnects = 1;
    private double waxmanAlpha = 0.15;
    private double waxmanBeta = 0.2;
    private BWDist bwDist = BWDist.Constant;
    private double bWMin = 10;
    private double bWMax = 1024;

    @Override
    public void compute(Grph graph)
    {
	String model = createModel(graph);
	// System.out.println(model);
	Topology topo = callBrite(model, getPRNG());

	for (Edge be : topo.getGraph().getEdgesArray())
	{
	    int source = be.getSrc().getID();
	    int destination = be.getDst().getID();
	    graph.addUndirectedSimpleEdge(source, destination);
	}
    }

    public static Topology callBrite(String model, Random random)
    {
	RandomGenManager rgm = new RandomGenManager();
	rgm.setAssignSeed(random.nextLong());
	rgm.setBWSeed(random.nextLong());
	rgm.setConnectNodesSeed(random.nextLong());
	rgm.setEdgeConnSeed(random.nextLong());
	rgm.setGroupingSeed(random.nextLong());
	rgm.setPlaceNodesSeed(random.nextLong());

	/* create our glorious model and give it a random gen manager */
	Model m = ParseConfFile.Parse(model);
	m.setRandomGenManager(rgm);

	/* now create our wonderful topology. ie call model.generate() */
	Topology t = new Topology(m);

	/* check if our wonderful topology is connected */
	Util.MSGN("Checking for connectivity:");
	Graph g = t.getGraph();
	return t;

    }

    public String createModel(Grph graph)
    {
	String model = "BriteConfig\nBeginModel\n";
	model += "\tName = " + (algo.ordinal() + 1) + " \t# " + algo.name() + "\n";
	model += "\tN = " + graph.getVertices().size() + "\n";
	model += "\tHS = " + numberOfSquaresInTheMainPlane + "\n";
	model += "\tLS = " + numberOfSquaresInInnerPlanes + "\n";
	model += "\tNodePlacement = " + (nodePlacement.ordinal() + 1) + " \t# " + nodePlacement.name() + "\n";
	model += "\tGrowthType = " + (growthType.ordinal() + 1) + " \t# " + growthType.name() + "\n";
	model += "\tm = " + numberOfNeighboringNodesEachNewNodeConnects + "\n";
	model += "\talpha = " + waxmanAlpha + "\n";
	model += "\tbeta = " + waxmanBeta + "\n";
	model += "\tBWDist = " + (bwDist.ordinal() + 1) + " \t# " + bwDist.name() + "\n";
	model += "\tBWMin = " + bWMin + "\n";
	model += "\tBWMax = " + bWMax + "\n";
	model += "EndModel";
	return model;
    }

    public int getNumberOfSquaresInTheMainPlane()
    {
	return numberOfSquaresInTheMainPlane;
    }

    public void setNumberOfSquaresInTheMainPlane(int numberOfSquaresInTheMainPlane)
    {
	if (numberOfSquaresInTheMainPlane < 0)
	    throw new IllegalArgumentException();

	this.numberOfSquaresInTheMainPlane = numberOfSquaresInTheMainPlane;
    }

    public int getNumberOfSquaresInInnerPlanes()
    {
	return numberOfSquaresInInnerPlanes;
    }

    public void setNumberOfSquaresInInnerPlanes(int numberOfSquaresInInnerPlanes)
    {
	if (numberOfSquaresInInnerPlanes < 0)
	    throw new IllegalArgumentException();

	this.numberOfSquaresInInnerPlanes = numberOfSquaresInInnerPlanes;
    }

    public NODE_PLACEMENT getNodePlacement()
    {
	return nodePlacement;
    }

    public void setNodePlacement(NODE_PLACEMENT nodePlacement)
    {
	this.nodePlacement = nodePlacement;
    }

    public GrowthType getGrowthType()
    {
	return growthType;
    }

    public void setGrowthType(GrowthType growthType)
    {
	this.growthType = growthType;
    }

    public int getNumberOfNeighboringNodesEachNewNodeConnects()
    {
	return numberOfNeighboringNodesEachNewNodeConnects;
    }

    public void setNumberOfNeighboringNodesEachNewNodeConnects(int numberOfNeighboringNodesEachNewNodeConnects)
    {
	if (numberOfNeighboringNodesEachNewNodeConnects < 0)
	    throw new IllegalArgumentException();

	this.numberOfNeighboringNodesEachNewNodeConnects = numberOfNeighboringNodesEachNewNodeConnects;
    }

    public double getWaxmanAlpha()
    {
	return waxmanAlpha;
    }

    public void setWaxmanAlpha(double waxmanAlpha)
    {
	this.waxmanAlpha = waxmanAlpha;
    }

    public double getWaxmanBeta()
    {
	return waxmanBeta;
    }

    public void setWaxmanBeta(double waxmanBeta)
    {
	this.waxmanBeta = waxmanBeta;
    }

    public BWDist getBwDist()
    {
	return bwDist;
    }

    public void setBwDist(BWDist bwDist)
    {
	this.bwDist = bwDist;
    }

    public double getbWMin()
    {
	return bWMin;
    }

    public void setbWBounds(double bWMin, double bWMax)
    {
	if (bWMin > bWMax)
	    throw new IllegalArgumentException();

	this.bWMin = bWMin;
	this.bWMax = bWMax;

    }

    public double getbWMax()
    {
	return bWMax;
    }

    public static void brite(Grph g, int n, ALGO algo)
    {
	g.ensureNVertices(n);
	BriteTopologyGenerator tg = new BriteTopologyGenerator();
	tg.setAlgo(algo);
	tg.compute(g);
    }

    public static void main(String[] args)
    {
	Grph g = new InMemoryGrph();
	g.addNVertices(10);
	new BriteTopologyGenerator().compute(g);
	// brite(g, 50, ALGO.RT_WAXMAN);

	g.display();
    }

    public static Grph getBrite(int n, BriteTopologyGenerator.ALGO model, BriteTopologyGenerator.NODE_PLACEMENT np,
	    BriteTopologyGenerator.GrowthType gt, BriteTopologyGenerator.BWDist bwDist, int HS, int LS, int m,
	    double alpha, double beta, double bWMin, double bWMax)
    {
	return getBrite(n, model, np, gt, bwDist, HS, LS, m, alpha, beta, bWMin, bWMax, 1l);
    }

    public static Grph getBrite(int n, BriteTopologyGenerator.ALGO model, BriteTopologyGenerator.NODE_PLACEMENT np,
	    BriteTopologyGenerator.GrowthType gt, BriteTopologyGenerator.BWDist bwDist, int HS, int LS, int m,
	    double alpha, double beta, double bWMin, double bWMax, long seed)
    {
	Grph graph = new InMemoryGrph();
	graph.addNVertices(n);
	BriteTopologyGenerator g = new BriteTopologyGenerator();
	g.setPRNG(new Random(seed));
	g.setAlgo(model);
	g.setbWBounds(bWMin, bWMax);
	g.setBwDist(bwDist);
	g.setGrowthType(gt);
	g.setNodePlacement(np);
	g.setNumberOfNeighboringNodesEachNewNodeConnects(m);
	g.setNumberOfSquaresInInnerPlanes(LS);
	g.setNumberOfSquaresInTheMainPlane(HS);
	g.setWaxmanAlpha(alpha);
	g.setWaxmanBeta(beta);
	g.compute(graph);
	return graph;
    }

}
