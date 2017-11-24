/* (C) Copyright 2009-2013 CNRS (Centre National de la Recherche Scientifique).

Licensed to the CNRS under one
or more contributor license agreements.  See the NOTICE file
distributed with this work for additional information
regarding copyright ownership.  The CNRS licenses this file
to you under the Apache License, Version 2.0 (the
"License"); you may not use this file except in compliance
with the License.  You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing,
software distributed under the License is distributed on an
"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
KIND, either express or implied.  See the License for the
specific language governing permissions and limitations
under the License.

*/

/* Contributors:

Luc Hogie (CNRS, I3S laboratory, University of Nice-Sophia Antipolis) 
Aurelien Lancin (Coati research team, Inria)
Christian Glacet (LaBRi, Bordeaux)
David Coudert (Coati research team, Inria)
Fabien Crequis (Coati research team, Inria)
Grégory Morel (Coati research team, Inria)
Issam Tahiri (Coati research team, Inria)
Julien Fighiera (Aoste research team, Inria)
Laurent Viennot (Gang research-team, Inria)
Michel Syska (I3S, Université Cote D'Azur)
Nathann Cohen (LRI, Saclay) 
Julien Deantoin (I3S, Université Cote D'Azur, Saclay) 

*/

/**
 * Initial Software by Luc HOGIE, Issam TAHIRI, Aurélien LANCIN, Nathann COHEN, David COUDERT.
 * Copyright © INRIA/CNRS/UNS, All Rights Reserved, 2011, v0.9
 *
 * The Grph license grants any use or destribution of both binaries and source code, if
 * a prior notification was made to the Grph development team.
 * Modification of the source code is not permitted. 
 * 
 *
 */

package grph;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.io.StringBufferInputStream;
import java.lang.reflect.Field;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.function.IntPredicate;

import javax.swing.JComponent;
import javax.swing.JPanel;

import org.miv.graphstream.ui.GraphViewerRemote;
import org.xml.sax.SAXException;

import com.kitfox.svg.SVGCache;
import com.kitfox.svg.app.beans.SVGIcon;

import grph.ElementFilter.DegreeFilter;
import grph.algo.AdjacencyMatrix;
import grph.algo.AdjacencyMatrixAlgorithm;
import grph.algo.AllPaths;
import grph.algo.AntCliqueAlgorithm;
import grph.algo.AntiGraphAlgorithm;
import grph.algo.AverageDegreeAlgorithm;
import grph.algo.ComplementAlgorithm;
import grph.algo.ConnectedComponentsAlgorithm;
import grph.algo.DensityAlgorithm;
import grph.algo.DreadnautAlgorithm;
import grph.algo.InacessibleVerticesAlgorithm;
import grph.algo.IncidenceMatrixAlgorithm;
import grph.algo.IrreflexiveAlgorithm;
import grph.algo.IsCyclicAlgorithm;
import grph.algo.IsolatedVerticesAlgorithm;
import grph.algo.LineGraphAlgorithm;
import grph.algo.MaxFlowAlgorithm;
import grph.algo.MaxFlowAlgorithmResult;
import grph.algo.OutVertexAdjacencyAsIDSetsAlgorithm;
import grph.algo.RadiusAlgorithm;
import grph.algo.SpanningTree;
import grph.algo.Tarjan;
import grph.algo.TopologicalSortingAlgorithm;
import grph.algo.VertexAdjacencyAlgorithm;
import grph.algo.VertexListAlgorithm;
import grph.algo.clustering.AllClusteringCoefficientsAlgorithm;
import grph.algo.clustering.AvgClusteringCoefficientAlgorithm;
import grph.algo.clustering.Cluster;
import grph.algo.clustering.ClusteringCoefficient;
import grph.algo.clustering.GlobalClusteringCoefficientAlgorithm;
import grph.algo.coloring.BipartitenessAlgorithm;
import grph.algo.coloring.GraphColoring;
import grph.algo.covering_packing.BranchingMinimumVertexCoverAlgorithm;
import grph.algo.covering_packing.BruteForceMinimumVertexCoverAlgorithm;
import grph.algo.covering_packing.FominGrandoniKratschMaximumindependentSetAlgorithm;
import grph.algo.covering_packing.LPBasedMaximumIndependentSetAlgorithm;
import grph.algo.covering_packing.LPBasedMaximumMatchingAlgorithm;
import grph.algo.covering_packing.LPBasedMinimumVertexCoverAlgorithm;
import grph.algo.covering_packing.NiedermeierMinimumVertexCoverAlgorithm;
import grph.algo.degree.MaxInEdgeDegreeAlgorithm;
import grph.algo.degree.MaxInVertexDegreeAlgorithm;
import grph.algo.degree.MaxOutEdgeDegreeAlgorithm;
import grph.algo.degree.MaxOutVertexDegreeAlgorithm;
import grph.algo.degree.MinInEdgeDegreeAlgorithm;
import grph.algo.degree.MinInVertexDegreeAlgorithm;
import grph.algo.degree.MinOutEdgeDegreeAlgorithm;
import grph.algo.degree.MinOutVertexDegreeAlgorithm;
import grph.algo.distance.DistanceMatrix;
import grph.algo.distance.DistanceMatrixBasedDiameterAlgorithm;
import grph.algo.distance.GirthAlgorithm;
import grph.algo.distance.MinimumEccentricityGraphCenter;
import grph.algo.distance.PageRank;
import grph.algo.distance.PredecessorMatrix;
import grph.algo.distance.StackBasedBellmanFordWeightedMatrixAlgorithm;
import grph.algo.distance.TwoSweepBFSDiameterApproximationAlgorithm;
import grph.algo.distance.UnweightedDistanceMatrixAlgorithm;
import grph.algo.distance.UnweightedPredecessorMatrixAlgorithm;
import grph.algo.k_shortest_paths.YenTopKShortestPathsAlgorithm;
import grph.algo.lad.LAD.MODE;
import grph.algo.lad.UndirectedLAD;
import grph.algo.partitionning.metis.Gpmetis;
import grph.algo.partitionning.metis.Gpmetis.Ctype;
import grph.algo.partitionning.metis.Gpmetis.Iptype;
import grph.algo.partitionning.metis.Gpmetis.Objtype;
import grph.algo.partitionning.metis.Gpmetis.Ptype;
import grph.algo.search.BFSAlgorithm;
import grph.algo.search.DijkstraAlgorithm;
import grph.algo.search.GraphSearchListener;
import grph.algo.search.SearchResult;
import grph.algo.sort.OutDegreeSorter;
import grph.algo.structural.ChordalityTestAlgorithm;
import grph.algo.structural.CompletenessAlgorithm;
import grph.algo.structural.ConnectednessAlgorithm;
import grph.algo.structural.MultigraphnessAlgorithm;
import grph.algo.structural.MultigraphnessResult;
import grph.algo.structural.ReflexivityAlgorithm;
import grph.algo.structural.RegularityAlgorithm;
import grph.algo.structural.SimplenessAlgorithm;
import grph.algo.structural.TreenessAlgorithm;
import grph.algo.structural.cliquer.FindAllCliques;
import grph.algo.subgraph_isomorphism.own.FindAllCycles;
import grph.algo.topology.ChainTopologyGenerator;
import grph.algo.topology.ClassicalGraphs;
import grph.algo.topology.GLPIssamTopologyGenerator;
import grph.algo.topology.GridTopologyGenerator;
import grph.algo.topology.KClosestNeighborsTopologyGenerator;
import grph.algo.topology.RandomNewmanWattsStrogatzTopologyGenerator;
import grph.algo.topology.RingTopologyGenerator;
import grph.algo.topology.TopologyGenerator;
import grph.algo.triangles.latapi.MatthieuLatapyTriangleAlgorithm;
import grph.algo.triangles.latapi.Result;
import grph.gui.GraphstreamBasedRenderer;
import grph.gui.Swing;
import grph.io.DotWriter;
import grph.io.EdgeListReader;
import grph.io.EdgeListWriter;
import grph.io.GraphBuildException;
import grph.io.GraphvizImageWriter;
import grph.io.GraphvizImageWriter.COMMAND;
import grph.io.GraphvizImageWriter.OUTPUT_FORMAT;
import grph.io.GrphBinaryReader;
import grph.io.GrphBinaryWriter;
import grph.io.GrphTextReader;
import grph.io.GrphTextWriter;
import grph.io.ParseException;
import grph.io.graphml.GraphMLReader;
import grph.io.graphml.GraphMLWriter;
import grph.path.ArrayListPath;
import grph.path.Path;
import grph.path.SearchResultWrappedPath;
import grph.properties.NumericalProperty;
import grph.properties.Property;
import grph.properties.StringProperty;
import grph.report.Report;
import grph.stepper.AbstractStepper;
import grph.stepper.SwingStepper;
import grph.util.Matching;
import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntIterator;
import it.unimi.dsi.fastutil.ints.IntLinkedOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import it.unimi.dsi.fastutil.ints.IntSets;
import toools.Clazz;
import toools.NotYetImplementedException;
import toools.UnitTests;
import toools.Version;
import toools.collections.Collections;
import toools.collections.Filter;
import toools.collections.LucIntSets;
import toools.collections.primitive.IntCursor;
import toools.collections.primitive.LucIntHashSet;
import toools.collections.primitive.LucIntSet;
import toools.collections.primitive.SelfAdaptiveIntSet;
import toools.gui.ColorPalette;
import toools.gui.Utilities;
import toools.gui.VGA16Palette;
import toools.io.JavaResource;
import toools.io.file.Directory;
import toools.io.file.RegularFile;
import toools.log.Logger;
import toools.math.Distribution;
import toools.math.IntMatrix;
import toools.math.MathsUtilities;
import toools.net.NetUtilities;
import toools.os.OperatingSystem;
import toools.os.Unix;
import toools.text.TextUtilities;

/**
 * A full-featured graph object, exhibiting all available operations and
 * algorithms.
 */
public abstract class Grph implements GrphPrimitives, Cloneable, Serializable
{
	// ****************************************************************************
	// ATTRIBUTES
	// ****************************************************************************

	public static Directory COMPILATION_DIRECTORY;
	private static final long serialVersionUID = 1L;
	public static Logger logger = new GrphLogger();

	static
	{
		setCompilationDirectory();

		if (GrphWebNotifications.enabled)
		{
			NetUtilities.notifyUsage(
					"http://www.i3s.unice.fr/~hogie/software/register_use.php", "grph");
			GrphWebNotifications.checkForNewVersion(logger);
		}
	}

	public static boolean useCache = true;
	public static final Set<String> HOSTS = new HashSet<String>();

	// Properties
	private Property verticesLabel;
	private Property edgesLabel;

	private NumericalProperty verticesColor;
	private NumericalProperty edgesColor;
	private NumericalProperty edgesWidth;
	private NumericalProperty verticesSize;
	private NumericalProperty verticesShape;
	private NumericalProperty edgesStyle;
	private final List<TopologyListener> listeners = new ArrayList<TopologyListener>();
	private Collection<Property> properties = null;

	// Algorithms
	private Collection<GrphAlgorithm> algos;

	public transient final GrphAlgorithm<Integer> centerAlgorithm = new MinimumEccentricityGraphCenter()
			.cacheResultForGraph(this);
	public transient final AllClusteringCoefficientsAlgorithm allClusteringCoefficientsAlgorithm = new AllClusteringCoefficientsAlgorithm();
	public transient final GrphAlgorithm<Double> avgClusteringCoefficientAlgorithm = new AvgClusteringCoefficientAlgorithm()
			.cacheResultForGraph(this);
	public transient final GrphAlgorithm<DistanceMatrix> unweightedDistanceMatrixAlgorithm = new UnweightedDistanceMatrixAlgorithm()
			.cacheResultForGraph(this);
	public transient final GrphAlgorithm<PredecessorMatrix> unweightedPredecessorMatrixAlgorithm = new UnweightedPredecessorMatrixAlgorithm()
			.cacheResultForGraph(this);
	public transient final GrphAlgorithmCache<Double> densityAlgorithm = new DensityAlgorithm()
			.cacheResultForGraph(this);
	public transient final GrphAlgorithmCache<Double> avgDegreeAlgorithm = new AverageDegreeAlgorithm()
			.cacheResultForGraph(this);
	public transient final GrphAlgorithmCache<Integer> diameterAlgorithm = new DistanceMatrixBasedDiameterAlgorithm()
			.cacheResultForGraph(this);
	public transient final GrphAlgorithm<SearchResult[]> bfsAlgorithm = new BFSAlgorithm()
			.cacheResultForGraph(this);
	public transient final GrphAlgorithmCache<Integer> radiusAlgorithm = new RadiusAlgorithm()
			.cacheResultForGraph(this);
	public transient final GrphAlgorithmCache<Integer> maxInEdgeDegreeAlgorithm = new MaxInEdgeDegreeAlgorithm()
			.cacheResultForGraph(this);
	public transient final GrphAlgorithmCache<Integer> maxInVertexDegreeAlgorithm = new MaxInVertexDegreeAlgorithm()
			.cacheResultForGraph(this);
	public transient final GrphAlgorithmCache<Integer> maxOutEdgeDegreeAlgorithm = new MaxOutEdgeDegreeAlgorithm()
			.cacheResultForGraph(this);
	public transient final GrphAlgorithmCache<Integer> maxOutVertexDegreeAlgorithm = new MaxOutVertexDegreeAlgorithm()
			.cacheResultForGraph(this);
	public transient final GrphAlgorithmCache<Integer> minInEdgeDegreeAlgorithm = new MinInEdgeDegreeAlgorithm()
			.cacheResultForGraph(this);
	public transient final GrphAlgorithmCache<Integer> minInVertexDegreeAlgorithm = new MinInVertexDegreeAlgorithm()
			.cacheResultForGraph(this);
	public transient final GrphAlgorithmCache<Integer> minOutEdgeDegreeAlgorithm = new MinOutEdgeDegreeAlgorithm()
			.cacheResultForGraph(this);
	public transient final GrphAlgorithmCache<Integer> minOutVertexDegreeAlgorithm = new MinOutVertexDegreeAlgorithm()
			.cacheResultForGraph(this);

	public transient final GrphAlgorithmCache<Boolean> completenessAlgorithm = new CompletenessAlgorithm()
			.cacheResultForGraph(this);
	public transient final GrphAlgorithmCache<Boolean> connectednessAlgorithm = new ConnectednessAlgorithm()
			.cacheResultForGraph(this);

	public transient final GrphAlgorithmCache<MultigraphnessResult> multigraphnessAlgorithm = new MultigraphnessAlgorithm()
			.cacheResultForGraph(this);
	public transient final GrphAlgorithmCache<Boolean> reflexivityAlgorithm = new ReflexivityAlgorithm()
			.cacheResultForGraph(this);
	public transient final GrphAlgorithmCache<Boolean> regularityAlgorithm = new RegularityAlgorithm()
			.cacheResultForGraph(this);
	public transient final GrphAlgorithmCache<Boolean> simplenessAlgorithm = new SimplenessAlgorithm()
			.cacheResultForGraph(this);
	public transient final GrphAlgorithmCache<Boolean> treenessAlgorithm = new TreenessAlgorithm()
			.cacheResultForGraph(this);
	public transient final GrphAlgorithmCache<int[]> vertexListAlgorithm = new VertexListAlgorithm()
			.cacheResultForGraph(this);
	public transient final GrphAlgorithmCache<Grph> complementAlgorithm = new ComplementAlgorithm()
			.cacheResultForGraph(this);

	public transient final GrphAlgorithmCache<int[][]> outNeighborsAlgorithm = new VertexAdjacencyAlgorithm.Out()
			.cacheResultForGraph(this);
	public transient final GrphAlgorithmCache<int[][]> inNeighborsAlgorithm = new VertexAdjacencyAlgorithm.In()
			.cacheResultForGraph(this);
	public transient final GrphAlgorithmCache<int[][]> inOutNeighborsAlgorithm = new VertexAdjacencyAlgorithm.InOut()
			.cacheResultForGraph(this);
	public transient final GrphAlgorithmCache<IntSet[]> vertexAdjacenciesAsIDSetsAlgorithm = new OutVertexAdjacencyAsIDSetsAlgorithm()
			.cacheResultForGraph(this);

	public transient final GrphAlgorithmCache<Grph> lineGraphAlg = new LineGraphAlgorithm()
			.cacheResultForGraph(this);
	public transient final GrphAlgorithmCache<Result> trianglesAlgorithm = new MatthieuLatapyTriangleAlgorithm()
			.cacheResultForGraph(this);
	public transient final GrphAlgorithmCache<IntSet> isolatedVerticesAlgorithm = new IsolatedVerticesAlgorithm()
			.cacheResultForGraph(this);
	public transient final GrphAlgorithmCache<AdjacencyMatrix> adjacencyMatrixAlgo = new AdjacencyMatrixAlgorithm()
			.cacheResultForGraph(this);
	public transient final GrphAlgorithmCache<IntMatrix> incidenceMatrixAlgo = new IncidenceMatrixAlgorithm()
			.cacheResultForGraph(this);
	public transient final GrphAlgorithmCache<IntSet> inacessibleVertices = new InacessibleVerticesAlgorithm()
			.cacheResultForGraph(this);
	public transient final GrphAlgorithmCache<Collection<IntSet>> connectedComponentsAlg = new ConnectedComponentsAlgorithm()
			.cacheResultForGraph(this);
	public transient final GrphAlgorithmCache<Boolean> irreflexiveAlgorithm = new IrreflexiveAlgorithm()
			.cacheResultForGraph(this);
	public transient final GrphAlgorithmCache<IntArrayList> topologicalSortingAlg = new TopologicalSortingAlgorithm()
			.cacheResultForGraph(this);
	public transient final GrphAlgorithmCache<Boolean> isCyclicAlgorithm = new IsCyclicAlgorithm()
			.cacheResultForGraph(this);
	public transient final GrphAlgorithmCache<IntSet> girthAlgorithm = new GirthAlgorithm()
			.cacheResultForGraph(this);
	public transient final GrphAlgorithmCache<Integer> twoSweepDiameterAlg = new TwoSweepBFSDiameterApproximationAlgorithm()
			.cacheResultForGraph(this);
	public transient final GrphAlgorithmCache<GraphColoring> bipartiteAlgorithm = new BipartitenessAlgorithm()
			.cacheResultForGraph(this);
	public transient final GrphAlgorithmCache<IntSet> LPMaxMatchingAlgorithm = new LPBasedMaximumMatchingAlgorithm()
			.cacheResultForGraph(this);
	public transient final GrphAlgorithm<IntSet> LPMinVertexCoverAlgorithm = new LPBasedMinimumVertexCoverAlgorithm()
			.cacheResultForGraph(this);
	public transient final GrphAlgorithm<IntSet> bruteForceMinimumVertexCoverAlgorithm = new BruteForceMinimumVertexCoverAlgorithm()
			.cacheResultForGraph(this);
	public transient final GrphAlgorithm<IntSet> branchingMinimumVertexCoverAlgorithm = new BranchingMinimumVertexCoverAlgorithm()
			.cacheResultForGraph(this);
	public transient final GrphAlgorithm<IntSet> NiedermeierMinimumVertexCoverAlgorithm = new NiedermeierMinimumVertexCoverAlgorithm()
			.cacheResultForGraph(this);
	public transient final GrphAlgorithm<IntSet> LPMaximumindependentSetAlgorithm = new LPBasedMaximumIndependentSetAlgorithm()
			.cacheResultForGraph(this);
	public transient final GrphAlgorithm<IntSet> FominGrandoniKratschMaximumindependentSetAlgorithm = new FominGrandoniKratschMaximumindependentSetAlgorithm()
			.cacheResultForGraph(this);
	public final GrphAlgorithm<Collection<IntSet>> tarjanSCC = new Tarjan()
			.cacheResultForGraph(this);
	public final GrphAlgorithmCache<Boolean> chordalAlgo = new ChordalityTestAlgorithm()
			.cacheResultForGraph(this);

	// private transient final Cache cache = new Cache(this);

	// ****************************************************************************
	// CONSTRUCTORS
	// ****************************************************************************

	private static void setCompilationDirectory()
	{
		COMPILATION_DIRECTORY = Directory.getHomeDirectory().getChildDirectory(".grph")
				.getChildDirectory("native_programs");

		if (OperatingSystem.getLocalOperatingSystem() instanceof Unix)
		{
			Directory global = new Directory("/usr/local/grph/extern");

			if (global.exists() && global.canWrite())
			{
				COMPILATION_DIRECTORY = global;
			}
			else
			{
				/*
				 * System.out .println("Warning! Directory " + global.getPath()
				 * +
				 * " doesn't exist. You should create it and make it writeable to all users. Instead compiled stuff will go to "
				 * + COMPILATION_DIRECTORY.getPath());
				 */
			}
		}

		if ( ! COMPILATION_DIRECTORY.exists())
		{
			COMPILATION_DIRECTORY.mkdirs();
		}
	}

	public Grph(String name)
	{
		verticesLabel = new StringProperty(name + ".vertex labels");
		verticesColor = new NumericalProperty(name + "vertex color", 16, 15);
		verticesSize = new NumericalProperty(name + "vertex size", 16, 10);
		verticesShape = new NumericalProperty(name + "vertex shape", 1, 0);

		edgesLabel = new StringProperty(name + "edge labels");
		edgesColor = new NumericalProperty(name + "edge color", 16, 7);
		edgesWidth = new NumericalProperty(name + "edge width", 8, 1);
		edgesStyle = new NumericalProperty(name + "edge style", 1, 0);

	}

	/**
	 * Checks if this graph is an hypergraph or not (i.e. it has at least one
	 * hyper-edge)
	 * 
	 * @return true if this graph is directed
	 */
	public boolean isHypergraph()
	{
		return getNumberOfHyperEdges() > 0;
	}

	/**
	 * Tarjan's algorithm. http://en.wikipedia.org/wiki/Tarjan%27
	 * s_strongly_connected_components_algorithm
	 * 
	 * @return
	 */
	public Collection<IntSet> getStronglyConnectedComponents()
	{
		return tarjanSCC.compute(this);
	}

	public boolean isStronglyConnected()
	{
		return getStronglyConnectedComponents().size() == 1;
	}

	/**
	 * Checks if this graph is mixed or not (i.e. at least two of its edges are
	 * of different nature)
	 * 
	 * @return true if this graph is mixed
	 */
	public boolean isMixed()
	{
		int numberOfEdges = getNumberOfEdges();

		return ! (getNumberOfDirectedHyperEdges() == numberOfEdges
				|| getNumberOfDirectedSimpleEdges() == numberOfEdges
				|| getNumberOfUndirectedHyperEdges() == numberOfEdges
				|| getNumberOfUndirectedSimpleEdges() == numberOfEdges);
	}

	@Override
	public String toString()
	{
		String s = TextUtilities.toHumanString(getVertices().size()) + " vertices, ";

		s += TextUtilities.toHumanString(getNumberOfEdges());

		if ( ! storeEdges())
		{
			s += " implicit";
		}

		s += " edges";
		return s;
	}

	/**
	 * Computes the set of properties applicable to this graph.
	 * 
	 * @return
	 */
	public Collection<Property> getProperties()
	{
		if (properties == null)
		{
			properties = new ArrayList<Property>();

			for (Class c = getClass(); c != null; c = c.getSuperclass())
			{
				for (Field f : c.getDeclaredFields())
				{
					if (Property.class.isAssignableFrom(f.getType()))
					{
						try
						{
							f.setAccessible(true);
							Property p = (Property) f.get(this);
							assert p != null;
							properties.add(p);
						}
						catch (Exception e)
						{
							e.printStackTrace();
							throw new IllegalStateException();
						}
					}
				}
			}
		}

		return properties;
	}

	// ****************************************************************************
	// METHODS
	// ****************************************************************************

	// ////////////////////////////////////////////////////////////////////////////
	// Graph creation
	// /////////////////////////////////////////////////////////////////////////////

	public static Grph fromGrphText(String text)
			throws ParseException, GraphBuildException
	{
		return new GrphTextReader().readGraph(text.getBytes());
	}

	public static Grph fromGrphBinary(byte[] text)
			throws ParseException, GraphBuildException
	{
		return new GrphBinaryReader().readGraph(text);
	}

	public static Grph fromGrphTextFile(String filename)
			throws ParseException, SAXException, GraphBuildException, IOException
	{
		RegularFile f = new RegularFile(filename);
		return fromGrphText(new String(f.getContent()));
	}

	public static Grph fromGraphML(String graphMLText) throws ParseException, SAXException
	{
		return fromGraphML(graphMLText, null, null);
	}

	public static Grph fromGraphML(String graphMLText, Class vPropertyClass,
			Class ePropertyClass) throws ParseException, SAXException
	{
		try
		{
			return new GraphMLReader().readGraph(new StringBufferInputStream(graphMLText),
					vPropertyClass, ePropertyClass);
		}
		catch (IOException e)
		{
			e.printStackTrace();
			throw new IllegalStateException();
		}
	}

	/**
	 * Loads the graph from the given URL.
	 * 
	 * @param the
	 *            URL where the graph data will be read from.
	 * @return a new graph, build from the data at the given URL.
	 * @throws ParseException
	 * @throws GraphBuildException
	 * @throws IOException
	 */
	public static Grph loadOnlineGrph(String url)
			throws ParseException, GraphBuildException, IOException
	{
		return new GrphBinaryReader().readGraph(NetUtilities.retrieveURLContent(url));
	}

	// /////////////////////////////////////////////////////////////////////////////
	// Various and utility methods
	// /////////////////////////////////////////////////////////////////////////////
	/**
	 * Clears the cache for all algorithms that employs one.
	 */
	public void clearCache()
	{
		for (GrphAlgorithmCache cache : listCachingGraphAlgorithms())
		{
			cache.invalidateCachedValue();
		}
	}

	/*
	 * public Cache getCache() { return cache; }
	 */
	public PageRank getPageRanking(Random r)
	{
		PageRank pg = new PageRank(this, r);
		pg.iterate(1, getNumberOfVertices());
		return pg;
	}

	private long getTimeSpentComputing()
	{
		long sum = 0;

		for (GrphAlgorithmCache alg : listCachingGraphAlgorithms())
		{
			sum += alg.getTimeSpentComputing();
		}

		return sum;
	}

	/**
	 * Post this graph this Grph website.
	 * 
	 * @return the URL where this graph can be retrieved from.
	 * @throws IOException
	 */
	public String postOnTheWeb() throws IOException
	{
		String basedir = "http://www.i3s.unice.fr/~hogie/grph/instances/";
		byte[] response = NetUtilities.retrieveURLContent(basedir + "post.php",
				new HashMap<String, String>(), toGrphBinary());
		return basedir + new String(response);
	}

	// //////////////////////////////////////////////////////////////////////////////
	// Graphs
	// //////////////////////////////////////////////////////////////////////////////

	public Grph getSpanningTree()
	{
		return SpanningTree.computeBFSBasedSpanningTree(this);
	}

	/**
	 * Computes the complement graph for this graph.
	 * 
	 * @return the complement graph.
	 */
	public Grph getComplement()
	{
		return complementAlgorithm.compute(this);
	}

	public IntArrayList getAllOutEdgeDegrees()
	{
		IntArrayList outDegrees = new IntArrayList(getVertices().getGreatest() + 1);

		for (IntCursor v : IntCursor.fromFastUtil(getVertices()))
		{
			outDegrees.add(getVertexDegree(v.value, Grph.TYPE.edge, Grph.DIRECTION.out));
		}

		return outDegrees;
	}

	/**
	 * Computes the line graph of this graph.
	 * 
	 * @return the line graph.
	 */
	public Grph getLineGraph()
	{
		return lineGraphAlg.compute(this);
	}

	/**
	 * Computes n iterations of the "line graph" operation on this graph.
	 * 
	 * @param n
	 *            the number of iterations to perform
	 * @return the n-th line graph of this graph
	 */
	public Grph getLineGraph(int n)
	{
		if (n < 0)
			throw new IllegalArgumentException();

		if (n == 0)
		{
			return this;
		}
		else
		{
			return getLineGraph().getLineGraph(n - 1);
		}
	}

	/**
	 * Checks if this graph has only undirected simple edges.
	 * 
	 * @return true, if all edges in this graph are simple edges, false
	 *         otherwise.
	 */
	public boolean isUndirectedSimpleGraph()
	{
		return getNumberOfUndirectedSimpleEdges() == getEdges().size();
	}

	/**
	 * Checks if this graph has only directed simple edges.
	 * 
	 * @return true if all edges in this graph are directed edges, false
	 *         otherwise.
	 */
	public boolean isDirectedSimpleGraph()
	{
		return getNumberOfDirectedSimpleEdges() == getEdges().size();
	}

	// //////////////////////////////////////////////////////////////////////////////
	// Properties
	// //////////////////////////////////////////////////////////////////////////////

	// //////////////////////////////////////////////////////////////////////////////
	// Neighbors and adjacency
	// //////////////////////////////////////////////////////////////////////////////

	/**
	 * Checks if the given two vertices are connected.
	 * 
	 * @param src
	 *            a vertex
	 * @param dest
	 *            another vertex
	 * @return true if there exist an edge between the two given vertices.
	 */
	public boolean areVerticesAdjacent(int src, int dest)
	{
		return getSomeEdgeConnecting(src, dest) >= 0;
	}

	/**
	 * Computes the set of edges incident to vertices incident to the given
	 * edge.
	 * 
	 * @param e
	 *            an edge
	 * @return a set of edges
	 */
	public IntSet getEdgesAdjacentToEdge(int e)
	{
		IntSet s = new SelfAdaptiveIntSet();

		for (int v : getVerticesIncidentToEdge(e).toIntArray())
		{
			for (int en : getEdgesIncidentTo(v).toIntArray())
			{
				if (en != e)
				{
					s.add(en);
				}
			}
		}

		return s;
	}

	/**
	 * Returns an (hyper)edge or arc that connects src and dest
	 * 
	 * @param src
	 * @param dest
	 * @return
	 */
	public int getSomeEdgeConnecting(int src, int dest)
	{
		assert getVertices().contains(src);
		assert getVertices().contains(dest);
		IntSet out = getOutEdges(src);
		IntSet in = getInEdges(dest);

		if (out.isEmpty() || in.isEmpty())
		{
			return - 1;
		}
		else
		{
			if (out.size() < in.size())
			{
				for (IntCursor c : IntCursor.fromFastUtil(out))
				{
					int e = c.value;

					if (isDirectedSimpleEdge(e))
					{
						if (getDirectedSimpleEdgeHead(e) == dest)
						{
							return e;
						}
					}
					else if (isUndirectedSimpleEdge(e))
					{
						if (getTheOtherVertex(e, src) == dest)
						{
							return e;
						}
					}
					else if (isUndirectedHyperEdge(e))
					{
						if (getUndirectedHyperEdgeVertices(e).contains(dest))
						{
							return e;
						}
					}
					else if (isDirectedHyperEdge(e))
					{
						if (getDirectedHyperEdgeHead(e).contains(dest))
						{
							return e;
						}
					}
				}
			}
			else
			{
				for (IntCursor c : IntCursor.fromFastUtil(in))
				{
					int e = c.value;

					if (isDirectedSimpleEdge(e))
					{
						if (getDirectedSimpleEdgeTail(e) == src)
						{
							return e;
						}
					}
					else if (isUndirectedSimpleEdge(e))
					{
						if (getTheOtherVertex(e, dest) == src)
						{
							return e;
						}
					}
					else if (isUndirectedHyperEdge(e))
					{
						if (getUndirectedHyperEdgeVertices(e).contains(src))
						{
							return e;
						}
					}
					else if (isDirectedHyperEdge(e))
					{
						if (getDirectedHyperEdgeTail(e).contains(src))
						{
							return e;
						}
					}
				}
			}

			return - 1;
		}
	}

	// /////////////////////////////////////////////////////////////////////////////
	// Clustering
	// /////////////////////////////////////////////////////////////////////////////

	/**
	 * @return a table that contains all local clustering coefficients.
	 */
	public double[] getLocalClusteringCoefficients()
	{
		return allClusteringCoefficientsAlgorithm.compute(this);
	}

	/**
	 * Computes the average clustering coefficient of this graph.
	 * 
	 * @return the average clustering coefficient the of this graph.
	 */
	public double getAverageClusteringCoefficient()
	{
		return avgClusteringCoefficientAlgorithm.compute(this);
	}

	public double getClusteringCoefficient(int v)
	{
		return ClusteringCoefficient.getLocalClusteringCoefficient(this, v);
	}

	public double getClusteringCoefficient()
	{
		return new GlobalClusteringCoefficientAlgorithm().compute(this);
	}

	public Distribution<Double> getClusteringCoefficientDistribution()
	{
		return ClusteringCoefficient.getClusteringCoefficientDistribution(this);
	}

	/**
	 * Returns the subset of vertices that have a degree equal to
	 * <code>degree</code>. For directed graphs, no distinction is made between
	 * in-edges and out-edges. To specify this, see
	 * {@link Grph#getVerticesOfDegree(int, DIRECTION)}
	 * 
	 * @param degree
	 * @return
	 */
	public IntSet getVerticesOfDegree(int degree)
	{
		DegreeFilter f = new ElementFilter.DegreeFilter(this, degree, Grph.TYPE.edge,
				Grph.DIRECTION.in_out);
		return LucIntSets.filter(getVertices(), f);
	}

	/**
	 * Returns the subset of vertices that have a degree at least
	 * <code>degree</code>. Designed only for simple, undirected graphs
	 * 
	 * @param minDegree
	 * @return
	 */
	public IntSet getVerticesOfDegreeAtLeast(int minDegree)
	{
		IntSet r = new SelfAdaptiveIntSet();

		for (IntCursor v : IntCursor.fromFastUtil(getVertices()))
		{
			if (getVertexDegree(v.value, TYPE.edge, DIRECTION.in_out) >= minDegree)
			{
				r.add(v.value);
			}
		}

		return r;
	}

	/**
	 * Returns the subset of vertices that have a in-degree or out-degree equal
	 * to <code>degree</code>, depending on the <code>dir</code> parameter's
	 * value.
	 * 
	 * @param degree
	 * @return
	 */
	public IntSet getVerticesOfDegree(int degree, Grph.DIRECTION dir)
	{
		IntSet r = new SelfAdaptiveIntSet();

		for (int v : getVertices().toIntArray())
		{
			if (getVertexDegree(v, TYPE.edge, DIRECTION.in_out) == degree)
			{
				r.add(v);
			}
		}

		return r;
	}

	// //////////////////////////////////////////////////////////////////////////////
	// Subgraphs
	// //////////////////////////////////////////////////////////////////////////////
	/**
	 * Checks if this graph contains the given graph.
	 * 
	 * @param g
	 * @return true if this graph contains the given graph, false otherwise
	 */
	public boolean contains(Grph g)
	{
		if ( ! getVertices().contains(g.getVertices())
				|| ! getEdges().contains(g.getEdges()))
		{
			return false;
		}

		for (IntCursor c : IntCursor.fromFastUtil(g.getEdges()))
		{
			int e = c.value;

			if (getEdgeNature(e) != g.getEdgeNature(e) || ! getVerticesIncidentToEdge(e)
					.equals(g.getVerticesIncidentToEdge(e)))
			{
				return false;
			}
		}

		return true;
	}

	/**
	 * 
	 * @param A
	 *            a set of vertices
	 * @param B
	 *            a set of vertices
	 * @return the bipartite subgraph induced by A and B, with edges only
	 *         between A and B
	 */
	public Grph getBipartiteSubgraphInducedByVertices(IntSet A, IntSet B)
	{
		Grph g = getSubgraphInducedByVertices(LucIntSets.union(A, B));

		for (int e : g.getEdges().toIntArray())
		{
			int u = g.getOneVertex(e);
			int v = g.getTheOtherVertex(e, u);

			if ((A.contains(u) && A.contains(v)) || (B.contains(u) && B.contains(v)))
				g.removeEdge(e);
		}

		return g;
	}

	/**
	 * Computes the subgraph induced by the given set of vertices. The class of
	 * the returned graph is the same as the class of this graph.
	 * 
	 * @param vertices
	 * @return a new graph
	 */
	public Grph getSubgraphInducedByVertices(IntSet vertices)
	{
		if ( ! getVertices().contains(vertices))
			throw new IllegalArgumentException("some vertices are not in the graph");

		if (getVertices().size() == vertices.size())
		{
			return clone();
		}
		else
		{
			IntSet edges = new SelfAdaptiveIntSet();

			for (IntCursor vc : IntCursor.fromFastUtil(vertices))
			{
				edges.addAll(getEdgesIncidentTo(vc.value));
			}

			Grph g = Clazz.makeInstance(getClass());
			g.addVertices(vertices);

			for (IntCursor ec : IntCursor.fromFastUtil(edges))
			{
				int e = ec.value;

				if (isSimpleEdge(e))
				{
					int a = getOneVertex(e);
					int b = getTheOtherVertex(e, a);

					if (vertices.contains(a) && vertices.contains(b))
					{
						g.addSimpleEdge(a, e, b, isDirectedSimpleEdge(e));
					}
				}
				else
				{
					// hyper-edge
					throw new NotYetImplementedException();
				}
			}

			return g;
		}
	}

	/**
	 * Computes the subgraph induced by the given set of edges. The class of the
	 * returned graph is the same as the class of this graph.
	 * 
	 * @param edges
	 * @return a new graph
	 */
	public Grph getSubgraphInducedByEdges(IntSet edges)
	{
		if ( ! getEdges().contains(edges))
			throw new IllegalArgumentException("some edges are not in the graph");

		if (getEdges().size() == edges.size())
		{
			return clone();
		}
		else
		{
			Grph g = Clazz.makeInstance(getClass());

			for (IntCursor ec : IntCursor.fromFastUtil(edges))
			{
				int e = ec.value;

				if (isSimpleEdge(e))
				{
					int a = getOneVertex(e);
					int b = getTheOtherVertex(e, a);
					g.addSimpleEdge(a, e, b, isDirectedSimpleEdge(e));
				}
				else
				{
					// hyper-edge
					throw new NotYetImplementedException();
				}
			}

			return g;
		}
	}

	/**
	 * Assigns the given color to all vertices in the given set. This operation
	 * dynamically changes the graphical representations of this graph.
	 * 
	 * @param vertices
	 *            the set of vertices for which the color will be set
	 * @param color
	 *            the color to assign to vertices
	 */
	public void highlightVertices(IntSet vertices, int color)
	{
		for (int v : vertices.toIntArray())
		{
			getVertexColorProperty().setValue(v, color);
		}
	}

	public void highlightVertices(int... vertices)
	{
		highlightVertices(new IntOpenHashSet(vertices));
	}

	/**
	 * Assigns the given color to all edges in the given set. This operation
	 * dynamically changes the graphical representations of this graph.
	 * 
	 * @param edges
	 *            the set of edges for which the color will be set
	 * @param color
	 *            the color to assign to edges
	 */
	public void highlightEdges(IntSet edges, int color)
	{
		for (int e : edges.toIntArray())
		{
			getEdgeColorProperty().setValue(e, color);
		}
	}

	/**
	 * Assigns the given color to all vertices and edges in the given graph.
	 * This operation dynamically changes the graphical representations of this
	 * graph.
	 * 
	 * @param subgraph
	 *            the graph that must be selected
	 * @param color
	 *            the color to assign to the graph
	 */
	public void highlight(Grph subgraph)
	{
		highlight(subgraph, consumeHighlightColor());
	}

	private ColorPalette palette = new VGA16Palette();
	private int highlighColorIndex = 0;

	private int consumeHighlightColor()
	{
		int c = highlighColorIndex;
		highlighColorIndex = highlighColorIndex + 1 % palette.getNumberOfColors();
		return c;
	}

	public void highlight(Grph subgraph, int color)
	{
		highlightVertices(subgraph.getVertices(), color);
		highlightEdges(subgraph.getEdges(), color);
	}

	public IntSet getShortestCycle()
	{
		return girthAlgorithm.compute(this);
	}

	/**
	 * The girth of a graph is defined as the length of a shortest cycle, or
	 * infinite if the graph is acyclic. Since we cannot test infinity, the
	 * girth of an acyclic graph is here defined as 0.
	 * 
	 * @return the girth of the graph
	 */
	public int getGirth()
	{
		return girthAlgorithm.compute(this).size();
	}

	// /////////////////////////////////////////////////////////////////////////////
	// Isomorphism

	/**
	 * Returns true if this graph is isomorphic to the given graph, false
	 * otherwise.
	 */
	public boolean isIsomorphicTo(Grph anotherGraph)
	{
		return new DreadnautAlgorithm().areIsomorphic(this, anotherGraph);
	}

	/**
	 * Returns if the given "pattern graph" is partially isomorphic to a
	 * subgraph in this graph.
	 */
	public Collection<Matching> getPartialSubgraphIsomorphism(Grph pattern,
			boolean findAllSolutions)
	{
		return new UndirectedLAD().lad(this, pattern, grph.algo.lad.LAD.MODE.PARTIAL,
				findAllSolutions);
	}

	/**
	 * Returns if the given "pattern graph" is isomorphic to a subgraph in this
	 * graph.
	 */
	public List<Matching> getInducedSubgraphIsomorphism(Grph pattern,
			boolean findAllSolutions)
	{
		return new UndirectedLAD().lad(this, pattern, MODE.INDUCED, findAllSolutions);
	}

	// //////////////////////////////////////////////////////////////////////////////
	// Covering and Packing

	/**
	 * Tests if the given set in a dominating set of this graph.
	 * 
	 * @param s
	 * @return
	 */
	public boolean isDominatingSet(IntSet s)
	{
		IntSet coveredVertices = new SelfAdaptiveIntSet();

		for (int v : s.toIntArray())
		{
			coveredVertices.add(v);
			coveredVertices.addAll(this.getNeighbours(v));
		}

		return coveredVertices.equals(getVertices());
	}

	/**
	 * Tests if the given set is an independent set (a.k.a. stable set)
	 * 
	 * @param s
	 * @return
	 */
	public boolean isIndependentSet(IntSet s)
	{
		int[] e = s.toIntArray();

		for (int a : e)
		{
			for (int b : e)
			{
				if (a != b)
				{
					if (areVerticesAdjacent(a, b))
					{
						return false;
					}
				}
			}
		}

		return true;
	}

	/**
	 * Computes if the given set of edges forms a matching of this graph.
	 * 
	 * @param s
	 * @return
	 */
	public boolean isMatching(IntSet s)
	{
		int[] e = s.toIntArray();

		for (int a : e)
		{
			for (int b : e)
			{
				if (a != b)
				{
					if (areEdgesAdjacent(a, b))
					{
						return false;
					}
				}
			}
		}

		return true;
	}

	/**
	 * Tests if the given set in a vertex cover of this graph.
	 * 
	 * @param s
	 * @return
	 */
	public boolean isVertexCover(IntSet s)
	{
		IntSet coveredEdges = new SelfAdaptiveIntSet();

		for (int v : s.toIntArray())
		{
			coveredEdges.addAll(getEdgesIncidentTo(v));
		}

		return coveredEdges.equals(getEdges());
	}

	/**
	 * Computes a maximum (cardinality) matching.
	 * 
	 * @return the edges set of a maximum matching
	 */
	public IntSet getMaximumMatching()
	{
		return LPMaxMatchingAlgorithm.compute(this);
	}

	/**
	 * Implemented Minimum Vertex Cover algorithms.
	 * <dl>
	 * <dd>-<code> INTEGER_PROGRAMMING </code> solves the problem via a
	 * translation into an integer program and an external solver;</dd>
	 * <dd>-<code> BRUTE_FORCE </code> is a naive algorithm that tests all
	 * subsets and gives poor performances on large graphs;</dd>
	 * <dd>-<code> BRANCHING </code> is a recursive branching algorithm based on
	 * the rule: "in any vertex cover, either take {v} or its neighbors";</dd>
	 * <dd>-<code> NIEDERMEIER </code> is an improved branching algorithm, with
	 * good performances.
	 * </dl>
	 */
	public enum MinVertexCoverAlgorithm
	{
		BRUTE_FORCE, INTEGER_PROGRAMMING, BRANCHING, NIEDERMEIER
	}

	/**
	 * Computes a vertex cover of minimum cardinality. By default, the algorithm
	 * used for is Niedermeier's. You can specify another algorithm by giving
	 * its name as a parameter (see below).
	 * 
	 * @return
	 */
	public IntSet getMinimumVertexCover()
	{
		return getMinimumVertexCover(MinVertexCoverAlgorithm.INTEGER_PROGRAMMING);
	}

	/**
	 * Computes a minimum vertex cover by applying the specified algorithm.
	 * 
	 * @return
	 */
	public IntSet getMinimumVertexCover(MinVertexCoverAlgorithm algorithm)
	{
		switch (algorithm)
		{
		case BRUTE_FORCE:
			return bruteForceMinimumVertexCoverAlgorithm.compute(this);
		case INTEGER_PROGRAMMING:
			return LPMinVertexCoverAlgorithm.compute(this);
		case BRANCHING:
			return branchingMinimumVertexCoverAlgorithm.compute(this);
		case NIEDERMEIER:
			return NiedermeierMinimumVertexCoverAlgorithm.compute(this);
		}
		throw new IllegalArgumentException(
				algorithm + ": unimplemented Minimum Vertex Cover algorithm.");
	}

	/**
	 * Implemented Maximum Independent Set algorithms.
	 * <dl>
	 * <dd>-<code> INTEGER_PROGRAMMING </code> solves the problem via a
	 * translation into an integer program and an external solver;</dd>
	 * <dd>-<code>FOMIN_GRANDONI_KRATSCH</code> is an improved branching
	 * algorithm, with good performances (and a time complexity of O*(1.227^n)).
	 * </dl>
	 */
	public enum MaxIndependentSetAlgorithm
	{
		INTEGER_PROGRAMMING, FOMIN_GRANDONI_KRATSCH
	}

	/**
	 * Computes a independent set of maximum cardinality, based on
	 * 
	 * @return
	 */
	public IntSet getMaximumIndependentSet()
	{
		return getMaximumIndependentSet(
				MaxIndependentSetAlgorithm.FOMIN_GRANDONI_KRATSCH);
	}

	public IntSet getMaximumIndependentSet(MaxIndependentSetAlgorithm algorithm)
	{
		switch (algorithm)
		{
		case INTEGER_PROGRAMMING:
			return LPMaximumindependentSetAlgorithm.compute(this);
		case FOMIN_GRANDONI_KRATSCH:
			return FominGrandoniKratschMaximumindependentSetAlgorithm.compute(this);
		}
		throw new IllegalArgumentException(
				algorithm + ": unimplemented Maximum Independent Set algorithm");
	}

	/**
	 * Computes the number of triangles in the graph.
	 * 
	 * @return the number of triangles.
	 */
	public int getNumberOfTriangles()
	{
		return trianglesAlgorithm.compute(this).numberOfTriangles;
	}

	private final Collection<GrphAlgorithm> listGraphAlgorithms()
	{
		if (algos == null)
		{
			algos = new HashSet<GrphAlgorithm>();

			for (Class c = getClass(); c != null; c = c.getSuperclass())
			{
				for (Field f : c.getDeclaredFields())
				{
					if (GrphAlgorithm.class.isAssignableFrom(f.getType()))
					{
						try
						{
							algos.add((GrphAlgorithm) f.get(this));
						}
						catch (Throwable e)
						{
							e.printStackTrace();
							throw new IllegalStateException(e);
						}
					}
				}
			}
		}

		return algos;
	}

	private final Collection<? extends GrphAlgorithm> listGraphAlgorithms(Class c)
	{
		Filter f = new Filter.FilterObjectByClass(c);
		return Collections.filter(new ArrayList<GrphAlgorithm>(listGraphAlgorithms()), f);
	}

	/**
	 * Lists all graph algorithms that employ a cache.
	 * 
	 * @return all caching algorithms
	 */
	public final Collection<GrphAlgorithmCache> listCachingGraphAlgorithms()
	{
		return (Collection<GrphAlgorithmCache>) listGraphAlgorithms(
				GrphAlgorithmCache.class);
	}

	private final Collection<TopologyGenerator> listTopologyGenerators()
	{
		return (Collection<TopologyGenerator>) listGraphAlgorithms(
				TopologyGenerator.class);
	}

	public GrphAlgorithm findAlgorithm(String name)
	{
		for (GrphAlgorithm algo : listGraphAlgorithms())
		{
			if (algo.getClass() == GrphAlgorithmCache.class)
			{
				GrphAlgorithmCache cache = (GrphAlgorithmCache) algo;
				algo = cache.getCachedAlgorithm();

				if (algo.getClass().getName().equals(name))
				{
					return cache;
				}
			}
			else if (algo.getClass().getName().equals(name))
			{
				return algo;
			}
		}

		return null;
	}

	/**
	 * Connect each vertex to its k-closest neighbours, according to the current
	 * topology.
	 * 
	 * @param k
	 *            the number of neighbours to connect to.
	 */
	public void connectToKClosestNeighbors(int k)
	{
		KClosestNeighborsTopologyGenerator tg = new KClosestNeighborsTopologyGenerator();
		tg.setK(k);
		tg.compute(this);
	}

	/**
	 * Connect vertices currently in the graph as a grid of dimension (w, h).
	 * 
	 * @param width
	 * @param height
	 */
	public void grid(int width, int height)
	{
		grid(width, height, false, false, false);
	}

	/**
	 * Connect vertices currently in the graph as a DIRECTED grid of dimension
	 * (w, h).
	 * 
	 * @param width
	 * @param height
	 */
	public void dgrid(int width, int height)
	{
		grid(width, height, true, false, false);
	}

	/**
	 * Connect vertices currently in the graph as a grid of dimension (w, h).
	 * 
	 * @param width
	 * @param height
	 * @param directed
	 *            indicated whether created edges should be directed or not
	 * @param createDiagonal
	 *            indicates whether diagonals edges should be created or not
	 */
	public void grid(int width, int height, boolean directed, boolean createDiagonal,
			boolean secondaryDiagonal)
	{
		GridTopologyGenerator tg = new GridTopologyGenerator();
		tg.setWidth(width);
		tg.setHeight(height);
		tg.createDirectedLinks(directed);
		tg.setGenerateDiagonal(createDiagonal);
		tg.setGenerateSecondaryDiagonal(secondaryDiagonal);
		tg.compute(this);
	}

	/**
	 * Connects the given vertices with a simple edge created on-the-fly.
	 * Whether the new edge will be directed or not depends on the directed
	 * parameter.
	 * 
	 * @param src
	 *            a vertex
	 * @param dest
	 *            another vertex
	 * @param directed
	 *            whether edges are directed or not
	 * @return the newly created edge
	 */
	public final int addSimpleEdge(int src, int dest, boolean directed)
	{
		int e = getNextEdgeAvailable();
		addSimpleEdge(src, e, dest, directed);
		return e;
	}

	/**
	 * Connects the given vertices with the given simple edge. Whether the new
	 * edge will be directed or not depends on the directed parameter.
	 * 
	 * @param src
	 *            a vertex
	 * @param dest
	 *            another vertex
	 * @param edge
	 *            the edge that will connect the two vertices
	 * @param directed
	 *            whether edges are directed or not
	 * @return the newly created edge
	 */
	public final void addSimpleEdge(int src, int edge, int dest, boolean directed)
	{
		assert src >= 0;
		assert dest >= 0;
		assert ! getEdges().contains(edge);

		if (directed)
		{
			addDirectedSimpleEdge(src, edge, dest);
		}
		else
		{
			addUndirectedSimpleEdge(edge, src, dest);
		}
	}

	private final void disconnect(int source, int edge, int destination)
	{
		if (isUndirectedHyperEdge(edge))
		{
			removeFromHyperEdge(edge, source);
			removeFromHyperEdge(edge, destination);
		}
		else
		{
			removeEdge(edge);
		}
	}

	/**
	 * Computes if the two given sets form a cut of this graph.
	 * 
	 * @param a
	 *            a set of vertices
	 * @param b
	 *            another set of vertices
	 * @return true if the two sets form a cut of this graph, false otherwise.
	 */
	public boolean isCut(IntSet a, IntSet b)
	{
		for (IntCursor v : IntCursor.fromFastUtil(getVertices()))
		{
			if ( ! (a.contains(v.value) ^ b.contains(v.value)))
			{
				return false;
			}
		}

		return true;
	}

	/**
	 * Computes if the given sets form a cut of this graph.
	 * 
	 * @param setsOfVertices
	 *            sets of vertices
	 * @return true if the sets form a cut of this graph, false otherwise.
	 */
	public boolean isCut(IntSet... setsOfVertices)
	{
		for (IntCursor vc : IntCursor.fromFastUtil(getVertices()))
		{
			int v = vc.value;
			IntSet container = null;

			for (IntSet set : setsOfVertices)
			{
				if (set.contains(v))
				{
					// if not set containing this vertex have been previously
					// found
					if (container == null)
					{
						container = set;
					}
					else
					{
						// at least two sets contain this vertex, this is not a
						// cut
						return false;
					}
				}
			}

			// no set contain this vertex, this is not a cut
			if (container == null)
			{
				return false;
			}
		}

		return true;
	}

	/**
	 * Connects the vertices in the graph according to the
	 * Random-Newman-Watts-Strogatz topology generation scheme.
	 * 
	 * @param k
	 *            the distance of the neighbours to which each vertex is
	 *            attached
	 * @param p
	 *            the probability to create a shortcut for any pair
	 */
	public void rnws(int k, double p)
	{
		RandomNewmanWattsStrogatzTopologyGenerator tg = new RandomNewmanWattsStrogatzTopologyGenerator();
		tg.setK(k);
		tg.setP(p);
		tg.compute(this);
	}

	public void rnws(int numberOfVertices, int k, double p)
	{
		ensureNVertices(numberOfVertices);
		rnws(k, p);
	}

	/**
	 * Computes the edges describing the cut formed by the two given sets.
	 * 
	 * @param a
	 *            a set of vertices
	 * @param b
	 *            another set of vertices
	 * @return the set of edges describing the cut
	 */
	public IntSet getCutEdges(IntSet a, IntSet b)
	{
		if ( ! isCut(a, b))
			throw new IllegalArgumentException("sets a and b do not form a cut");

		IntSet cutEdge = new SelfAdaptiveIntSet();

		for (IntCursor e : IntCursor.fromFastUtil(getEdges()))
		{
			IntSet vertices = getVerticesIncidentToEdge(e.value);

			if ( ! vertices.isEmpty())
			{
				IntIterator i = vertices.iterator();
				IntSet set = a.contains(i.nextInt()) ? a : b;

				while (i.hasNext())
				{
					// if one of the other vertices is not contained in the same
					// set
					if ( ! set.contains(i.nextInt()))
					{
						cutEdge.add(e.value);
						break;
					}
				}
			}
		}

		return cutEdge;
	}

	/**
	 * Compute the size of the cut.
	 * 
	 * @param a
	 *            a set of vertices
	 * @param b
	 *            another set of vertices
	 * @return the size of the cut
	 */
	public int getCutSize(IntSet a, IntSet b)
	{
		return getCutEdges(a, b).size();
	}

	/**
	 * Disconnect the two given vertices.
	 * 
	 * @param src
	 *            a vertex
	 * @param destination
	 *            another vertex
	 */
	public final void disconnect(int src, int destination)
	{
		for (IntCursor thisEdge : IntCursor
				.fromFastUtil(getEdgesConnecting(src, destination)))
		{
			disconnect(src, thisEdge.value, destination);
		}
	}

	public void ring(boolean directed)
	{
		RingTopologyGenerator.ring(this, directed);
	}

	public void ring(IntSet vertices, boolean directed)
	{
		RingTopologyGenerator.ring(this, vertices, directed);
	}

	/**
	 * Creates a directed ring consisting of the vertices already in the graph.
	 */
	public void dring()
	{
		ring(true);
	}

	/**
	 * Creates an undirected ring consisting of the vertices already in the
	 * graph.
	 */
	public void ring()
	{
		ring(false);
	}

	/**
	 * Removes the given vertices from the graph. Incident simple edges are also
	 * removed. Hyperedges are kept.
	 * 
	 * @param vertices
	 *            the vertices to be removed
	 */
	public void removeVertices(int... vertices)
	{
		for (int v : vertices)
		{
			removeVertex(v);
		}
	}

	/**
	 * Removes the given vertices from the graph. Incident simple edges are also
	 * removed. Hyperedges are kept.
	 * 
	 * @param vertices
	 *            the set of vertices to be removed
	 */
	public void removeEdges(IntSet edges)
	{
		for (IntCursor e : IntCursor.fromFastUtil(edges))
		{
			removeEdge(e.value);
		}
	}

	enum EDGE_NATURE
	{
		USE, DSE, DHE, UHE
	};

	public static enum DIRECTION
	{
		in, out, in_out
	}

	public static enum TYPE
	{
		edge, vertex
	}

	/**
	 * Computes the eccentricity of the given vertex.
	 * 
	 * @param v
	 * @return
	 */
	public int getEccentricity(int v, NumericalProperty weights)
	{
		return search(v, weights).maxDistance();
	}

	public int getEccentricity(int v)
	{
		return getEccentricity(v, null);
	}

	/**
	 * Checks if this graph contains the given graph.
	 * 
	 * @param backingGrph
	 * @return true if this graph contains the given graph, false, otherwise.
	 */
	@Override
	public boolean equals(Object o)
	{
		return o instanceof Grph && equals((Grph) o);
	}

	/**
	 * Checks if this graph equals the given graph.
	 * 
	 * @param g
	 * @return true if this graph contains the given graph.
	 */
	public boolean equals(Grph g)
	{
		if ( ! getVertices().equals(g.getVertices()) || ! getEdges().equals(g.getEdges()))
		{
			return false;
		}

		for (IntCursor c : IntCursor.fromFastUtil(g.getEdges()))
		{
			int e = c.value;

			if (getEdgeNature(e) != g.getEdgeNature(e) || ! g.getVerticesIncidentToEdge(e)
					.equals(g.getVerticesIncidentToEdge(e)))
			{
				return false;
			}
		}

		return true;
	}

	/**
	 * Compares this graph and the given graph and returns the first difference
	 * found.
	 * 
	 * @param g
	 * @return the first difference that is found between the two graphs.
	 */
	public final String getDifference(Grph g)
	{
		if (getVertices().size() != g.getVertices().size())
		{
			return "not same number of vertices";
		}
		else if (getEdges().size() != g.getEdges().size())
		{
			return "not same number of edges";
		}
		else
		{
			// checks if graphs have the same vertices
			for (IntCursor c : IntCursor.fromFastUtil(getVertices()))
			{
				int v = c.value;

				if ( ! g.getVertices().contains(v))
				{
					return "vertex " + v + " not found";
				}
			}

			// checks if graphs have the same edges, with same nature
			for (IntCursor c : IntCursor.fromFastUtil(getEdges()))
			{
				int e = c.value;

				if ( ! g.getEdges().contains(e))
				{
					return "edge " + e + " not found";
				}
				else if (isDirectedSimpleEdge(e))
				{
					if (g.isDirectedSimpleEdge(e))
					{
						if (getDirectedSimpleEdgeTail(e) != g
								.getDirectedSimpleEdgeTail(e))
						{
							return "arc " + e + " does not have the same tail ("
									+ getDirectedSimpleEdgeTail(e) + " and "
									+ g.getDirectedSimpleEdgeTail(e) + ")";
						}
						else if (getDirectedSimpleEdgeHead(e) != g
								.getDirectedSimpleEdgeHead(e))
						{
							return "arc " + e + " does not have the same head";
						}
					}
					else
					{
						return "edge is not an arc";
					}
				}
				else if (isUndirectedSimpleEdge(e))
				{
					if ( ! g.isUndirectedSimpleEdge(e))
					{
						return "edge " + e + " is not undirected";
					}
					else
					{
						int a = getOneVertex(e);
						int b = getTheOtherVertex(e, a);
						int ga = getOneVertex(e);
						int gb = getTheOtherVertex(e, a);

						if ( ! (a == ga && b == gb || a == b && b == ga))
						{
							return "edge " + e + " does not have the same ends: (" + a
									+ ", " + b + ")/(" + ga + ", " + gb + ")";
						}
					}
				}
				else if (isUndirectedHyperEdge(e))
				{
					if (g.isUndirectedHyperEdge(e))
					{
						if ( ! getUndirectedHyperEdgeVertices(e)
								.equals(g.getUndirectedHyperEdgeVertices(e)))
						{
							return "hyper edge does not have the same vertices";
						}
					}
					else
					{
						return "edge is not an hyperedge";
					}
				}
			}

			return null;
		}
	}

	/**
	 * Gets the nature of the given edge (undirected simple edge, directed
	 * simple edge, undirected hyper-edge or directed-hyperedge).
	 * 
	 * @param e
	 * @return
	 */
	public EDGE_NATURE getEdgeNature(int e)
	{
		if (isDirectedSimpleEdge(e))
		{
			return EDGE_NATURE.DSE;
		}
		else if (isUndirectedSimpleEdge(e))
		{
			return EDGE_NATURE.USE;
		}
		else if (isUndirectedHyperEdge(e))
		{
			return EDGE_NATURE.UHE;
		}
		else if (isDirectedHyperEdge(e))
		{
			return EDGE_NATURE.UHE;
		}
		else
		{
			throw new IllegalStateException("unknow edge type");
		}
	}

	public void undirectionalizeEdge(int e)
	{
		assert isDirectedSimpleEdge(e);
		int t = getDirectedSimpleEdgeTail(e);
		int h = getDirectedSimpleEdgeHead(e);
		removeEdge(e);
		addUndirectedSimpleEdge(e, t, h);
	}

	public void revertEdge(int e)
	{
		assert isDirectedSimpleEdge(e);
		int t = getDirectedSimpleEdgeTail(e);
		int h = getDirectedSimpleEdgeHead(e);
		removeEdge(e);
		addUndirectedSimpleEdge(e, h, t);
	}

	/**
	 * Computes the set of edges incident to the given vertex.
	 * 
	 * @param thisVertex
	 *            a vertex
	 * @return a set of edges
	 */
	public IntSet getEdgesIncidentTo(int v)
	{
		assert storeEdges();
		assert getVertices().contains(v);
		return LucIntSets.union(getInOnlyEdges(v), getOutOnlyEdges(v),
				getInOutOnlyEdges(v));
	}

	/**
	 * Clears this graph. All vertices and edges are removed.
	 */
	public void clear()
	{
		for (int e : getEdges().toIntArray())
		{
			removeEdge(e);
		}

		for (int v : getVertices().toIntArray())
		{
			removeVertex(v);
		}

		assert isNull();
	}

	/**
	 * Computes the set of out-edges for the given vertex. This set may contain
	 * edges or any types.
	 * 
	 * @param v
	 *            a vertex
	 * @return a set of edges
	 */
	public LucIntSet getOutEdges(int v)
	{
		assert getVertices().contains(v) : "vertex does not exist: " + v;
		assert storeEdges();
		return LucIntSets.unionTo(new LucIntHashSet(), getOutOnlyEdges(v),
				getInOutOnlyEdges(v));
	}

	/**
	 * Computes the set of out-edges for the given vertex. This set may contain
	 * edges or any types.
	 * 
	 * @param v
	 *            a vertex
	 * @return a set of edges
	 */
	public LucIntSet getInEdges(int v)
	{
		assert storeEdges();
		assert getVertices().contains(v) : "vertex does not exist: " + v;
		return LucIntSets.union(getInOnlyEdges(v), getInOutOnlyEdges(v));
	}

	/**
	 * Computes the set of out-neighbors of the given vertex. Since this
	 * operation is frequent, its result is cached.
	 * 
	 * @param v
	 * @return
	 */
	public LucIntSet getOutNeighbors(int v)
	{
		assert getVertices().contains(v) : "vertex does not exist: " + v;
		assert v >= 0 : v;

		if ( ! storeEdges())
		{
			return LucIntSets.unionTo(new LucIntHashSet(), getOutOnlyElements(v),
					getInOutOnlyElements(v));
		}
		else
		{
			LucIntSet outs = new DefaultIntSet();

			for (IntCursor c : IntCursor.fromFastUtil(getOutEdges(v)))
			{
				int e = c.value;

				if (isSimpleEdge(e))
				{
					outs.add(getTheOtherVertex(e, v));
				}
				else if (isUndirectedHyperEdge(e))
				{
					outs.addAll(getUndirectedHyperEdgeVertices(e));
				}
				else if (isDirectedHyperEdge(e))
				{
					outs.addAll(getDirectedHyperEdgeHead(e));
				}
			}

			return outs;
		}
	}

	public LucIntSet getOutOnlyEdges(int v)
	{
		assert getVertices().contains(v) : "vertex does not exist: " + v;
		assert storeEdges();
		return getOutOnlyElements(v);
	}

	public LucIntSet getInOnlyEdges(int v)
	{
		assert getVertices().contains(v) : "vertex does not exist: " + v;
		assert storeEdges();
		return getInOnlyElements(v);
	}

	public LucIntSet getInOutOnlyEdges(int v)
	{
		assert getVertices().contains(v) : "vertex does not exist: " + v;
		assert storeEdges();
		return getInOutOnlyElements(v);
	}

	/**
	 * Adds an edge connecting the two given vertices.
	 * 
	 * @param v1
	 *            a vertex
	 * @param v2
	 *            another vertex
	 * @return the new edge
	 */
	public int addUndirectedSimpleEdge(int v1, int v2)
	{
		assert v1 >= 0;
		assert v2 >= 0;

		int e = storeEdges() ? getNextEdgeAvailable() : - 1;
		addUndirectedSimpleEdge(e, v1, v2);
		return e;
	}

	/**
	 * Creates a new vertex in the graph. This new vertex will be the lowest
	 * natural number available.
	 * 
	 * @return the newly created vertex
	 */
	public int addVertex()
	{
		int v = getNextVertexAvailable();
		addVertex(v);
		return v;
	}

	protected IntSet getInElements(int v)
	{
		assert v >= 0 : v;

		IntSet in_only = getInOnlyElements(v);
		IntSet in_out_only = getInOutOnlyElements(v);

		if (in_only == null && in_out_only == null)
		{
			return IntSets.EMPTY_SET;
		}
		else if (in_only != null)
		{
			return in_only;
		}
		else if (in_out_only != null)
		{
			return in_out_only;
		}
		else
		{
			return LucIntSets.union(in_only, in_out_only);
		}
	}

	/**
	 * Checks if the given edge is a simple edge.
	 * 
	 * @param edge
	 *            an edge
	 * @return true if the given edge is a simple one, false otherwise
	 */
	public boolean isSimpleEdge(int edge)
	{
		assert getEdges().contains(edge);
		return ! isHyperEdge(edge);
	}

	public boolean isHyperEdge(int e)
	{
		return isDirectedHyperEdge(e) || isUndirectedHyperEdge(e);
	}

	/**
	 * Disconnect the given vertex, i.e. remove all edges incident to it.
	 * 
	 * @param v
	 *            the vertex to be isolated
	 */
	public void disconnectVertex(int v)
	{
		assert containsVertex(v) : v;

		if (storeEdges())
		{
			for (int e : getEdgesIncidentTo(v).toIntArray())
			{
				if (isSimpleEdge(e))
				{
					removeEdge(e);
				}
				else if (isUndirectedHyperEdge(e))
				{
					removeFromHyperEdge(e, v);
				}
				else if (isDirectedSimpleEdge(e))
				{
					removeFromDirectedHyperEdgeHead(e, v);
					removeFromDirectedHyperEdgeTail(e, v);
				}
				else
				{
					throw new IllegalStateException();
				}
			}
		}
		else
		{
			for (int n : getOutOnlyElements(v).toIntArray())
			{
				removeEdge(v, n);
			}

			for (int n : getInOutOnlyElements(v).toIntArray())
			{
				removeEdge(v, n);
			}
		}
	}

	public IntSet getInNeighbours(int v)
	{
		assert getVertices().contains(v) : "vertex does not exist: " + v;

		assert v >= 0 : v;

		if ( ! storeEdges())
		{
			return getInElements(v);
		}
		else
		{
			IntSet ins = new SelfAdaptiveIntSet();

			for (int e : getInEdges(v).toIntArray())
			{
				if (isSimpleEdge(e))
				{
					ins.add(getTheOtherVertex(e, v));
				}
				else if (isUndirectedHyperEdge(e))
				{
					ins.addAll(getUndirectedHyperEdgeVertices(e));
				}
				else if (isDirectedHyperEdge(e))
				{
					ins.addAll(getDirectedHyperEdgeTail(e));
				}
			}

			return ins;
		}
	}

	/**
	 * Computes the set of vertices incident to the given edge.
	 * 
	 * @param thisEdge
	 * @return
	 */
	public LucIntSet getVerticesIncidentToEdge(int e)
	{
		if ( ! storeEdges())
			throw new IllegalAccessError();

		assert getEdges().contains(e);

		if (isSimpleEdge(e))
		{
			int a = getOneVertex(e);
			int b = getTheOtherVertex(e, a);

			if (a == b)
			{
				return LucIntSet.singleton(a);
			}
			else
			{
				DefaultIntSet s = new DefaultIntSet();
				s.add(a);
				s.add(b);
				return s;
			}
		}
		else if (isUndirectedSimpleEdge(e))
		{
			return getUndirectedHyperEdgeVertices(e);
		}
		else
		{
			if (getNavigation() == DIRECTION.in_out)
			{
				return LucIntSets.unionTo(new LucIntHashSet(),
						getDirectedHyperEdgeTail(e), getDirectedHyperEdgeHead(e));
			}
			else if (getNavigation() == DIRECTION.in)
			{
				return getDirectedHyperEdgeTail(e);
			}
			else
			{
				return getDirectedHyperEdgeHead(e);
			}
		}
	}

	public static void main(String[] args)
	{
		Grph g = ClassicalGraphs.PetersenGraph();
		g.clone();
	}

	/**
	 * Computes a clone of this graph.
	 */
	@Override
	public final Grph clone()
	{
		Grph clone = Clazz.makeInstance(getClass());

		for (IntCursor v : IntCursor.fromFastUtil(getVertices()))
		{
			clone.addVertex(v.value);
		}

		for (IntCursor c : IntCursor.fromFastUtil(getEdges()))
		{
			int e = c.value;

			if (isUndirectedSimpleEdge(e))
			{
				int a = getOneVertex(e);
				int b = getTheOtherVertex(e, a);
				clone.addUndirectedSimpleEdge(e, a, b);
			}
			else if (isDirectedSimpleEdge(e))
			{
				int t = getDirectedSimpleEdgeTail(e);
				int h = getDirectedSimpleEdgeHead(e);
				clone.addDirectedSimpleEdge(t, e, h);
			}
			else if (isUndirectedHyperEdge(e))
			{
				for (int v : getUndirectedHyperEdgeVertices(e).toIntArray())
				{
					clone.addToUndirectedHyperEdge(e, v);
				}
			}
			else if (isDirectedHyperEdge(e))
			{
				for (int v : getDirectedHyperEdgeTail(e).toIntArray())
				{
					clone.addToDirectedHyperEdgeTail(e, v);
				}

				for (int v : getDirectedHyperEdgeHead(e).toIntArray())
				{
					clone.addToDirectedHyperEdgeHead(e, v);
				}
			}
		}

		for (Property p : getProperties())
		{
			if (p.getName() != null)
			{
				Property cloneProperty = clone.findPropertyByName(p.getName());
				p.cloneValuesTo(cloneProperty);
			}
		}

		return clone;
	}

	public Property findPropertyByName(String name)
	{
		for (Property p : getProperties())
		{
			if (p.getName() != null && p.getName().equals(name))
			{
				return p;
			}
		}

		return null;
	}

	/**
	 * Obtains the list of topology listeners for this graph.
	 * 
	 * @return the list of listeners
	 */
	public List<TopologyListener> getTopologyListeners()
	{
		return this.listeners;
	}

	/**
	 * Checks if this graph is directed or not (i.e. all its edges are directed)
	 * 
	 * @return true if this graph is directed
	 */
	public boolean isDirected()
	{
		return getNumberOfUndirectedEdges() == 0;
	}

	/**
	 * Computes a native-Grph textual expression of this graph.
	 * 
	 * @return an ASCII expression for this graph.
	 */
	public String toGrphText()
	{
		return new GrphTextWriter().printGraph(this);
	}

	public String toEdgeList()
	{
		return new EdgeListWriter().printGraph(this);
	}

	/**
	 * Computes a native-Grph binary expression of this graph.
	 * 
	 * @return an binary expression for this graph.
	 */
	public byte[] toGrphBinary()
	{
		return new GrphBinaryWriter().writeGraph(this);
	}

	/**
	 * Computes a GraphML (XML) expression of this graph.
	 * 
	 * @return Computes a string containing the GraphML (XML) expression of this
	 *         graph.
	 */
	public String toGraphML()
	{
		return new GraphMLWriter().printGraph(this);
	}

	/**
	 * Computes a GraphViz expression of this graph.
	 * 
	 * @return Computes a string containing a GraphViz expression of this graph.
	 */
	public String toDot()
	{
		return new DotWriter().printGraph(this);
	}

	/**
	 * Computes the exact diameter of this graph. The value is cached until the
	 * graph has changed.
	 * 
	 * @return the diameter the of this graph.
	 */
	public int getDiameter()
	{
		return diameterAlgorithm.compute(this);
	}

	/**
	 * Computes the maximum flow from the given source to the given destination.
	 * There must exist a path between the source and the destination.
	 * 
	 * @param s
	 *            a source vertex
	 * @param t
	 *            a destination vertex
	 * @return the result of the maxflow algorithms, which consists in the value
	 *         for the flow as well as flow assignments for every edge involved.
	 */
	public MaxFlowAlgorithmResult computeMaxFlow(int s, int t, NumericalProperty weights)
	{
		return new MaxFlowAlgorithm().compute(this, s, t, weights);
	}

	/**
	 * Computes the exact density of this graph. The value is cached until the
	 * graph has changed.
	 * 
	 * @return the diameter the of this graph.
	 */
	public double getDensity()
	{
		return densityAlgorithm.compute(this);
	}

	/**
	 * Computes the exact radius of this graph. The value is cached until the
	 * graph has changed.
	 * 
	 * @return the diameter the of this graph.
	 */
	public int getRadius()
	{
		return radiusAlgorithm.compute(this);
	}

	/**
	 * Checks if this graph is a null graph.
	 * 
	 * @return true if this graph is a null graph, false otherwise.
	 */
	public boolean isNull()
	{
		return getVertices().isEmpty() && getEdges().isEmpty();
	}

	/**
	 * Checks if this graph is a trivial graph.
	 * 
	 * @return true if this graph is a trivial graph.
	 */
	public boolean isTrivial()
	{
		return getVertices().size() == 1 && getEdges().isEmpty();
	}

	public int getMaxInEdgeDegrees()
	{
		return maxInEdgeDegreeAlgorithm.compute(this);
	}

	public int getMaxOutEdgeDegrees()
	{
		return maxOutEdgeDegreeAlgorithm.compute(this);
	}

	public int getMinInEdgeDegrees()
	{
		return minInEdgeDegreeAlgorithm.compute(this);
	}

	public int getMinOutEdgeDegrees()
	{
		return minOutEdgeDegreeAlgorithm.compute(this);
	}

	public int getMaxInVertexDegrees()
	{
		return maxInVertexDegreeAlgorithm.compute(this);
	}

	public int getMaxOutVertexDegrees()
	{
		return maxOutVertexDegreeAlgorithm.compute(this);
	}

	public int getMinInVertexDegrees()
	{
		return minInVertexDegreeAlgorithm.compute(this);
	}

	public int getMinOutVertexDegrees()
	{
		return minOutVertexDegreeAlgorithm.compute(this);
	}

	/**
	 * Checks if this graph is a complete graph.
	 * 
	 * @return true if this graph is a complete graph, false otherwise.
	 */
	public boolean isComplete()
	{
		return completenessAlgorithm.compute(this);
	}

	/**
	 * Checks if this graph is made of one single partition
	 * 
	 * @return true if this graph is a connected graph, false otherwise.
	 */
	public boolean isConnected()
	{
		return connectednessAlgorithm.compute(this);
	}

	/**
	 * Checks if this graph is a reflexive graph.
	 * 
	 * @return true if this graph is a reflexive graph, false otherwise.
	 */
	public boolean isReflexive()
	{
		return reflexivityAlgorithm.compute(this);
	}

	/**
	 * Checks if this graph is a regular graph.
	 * 
	 * @return true if this graph is a regular graph, false otherwise.
	 */
	public boolean isRegular()
	{
		return regularityAlgorithm.compute(this);
	}

	public Collection<VertexPair> getEdgePairs()
	{
		if (getNumberOfHyperEdges() > 0)
			throw new IllegalStateException("this graph has hyperedges");

		Collection<VertexPair> pairs = new ArrayList();

		for (IntCursor c : IntCursor.fromFastUtil(getEdges()))
		{
			int e = c.value;
			int v1 = getOneVertex(e);
			int v2 = getTheOtherVertex(e, v1);
			VertexPair p = new VertexPair(v1, v2);
			pairs.add(p);
		}

		return pairs;
	}

	/**
	 * 
	 * @return the number of hyper edges in this graph.
	 */
	public int getNumberOfHyperEdges()
	{
		return getNumberOfDirectedHyperEdges() + getNumberOfUndirectedHyperEdges();
	}

	/**
	 * 
	 * @return the number of simple edges in this graph.
	 */
	public int getNumberOfSimpleEdges()
	{
		return getNumberOfDirectedSimpleEdges() + getNumberOfUndirectedSimpleEdges();
	}

	/**
	 * 
	 * @return the number of directed edges in this graph.
	 */
	public int getNumberOfDirectedEdges()
	{
		return getNumberOfDirectedHyperEdges() + getNumberOfDirectedSimpleEdges();
	}

	/**
	 * 
	 * @return the number of undirected edges in this graph.
	 */
	public int getNumberOfUndirectedEdges()
	{
		return getNumberOfUndirectedHyperEdges() + getNumberOfUndirectedSimpleEdges();
	}

	/**
	 * Checks if this graph is has multiple edges in it.
	 * 
	 * @return true if this graph has multiple edges, false otherwise.
	 */
	public boolean hasMultipleEdges()
	{
		return multigraphnessAlgorithm.compute(this) != null;
	}

	/**
	 * Checks if this graph is a tree.
	 * 
	 * @return true if this graph is a tree, false otherwise.
	 */
	public boolean isTree()
	{
		return treenessAlgorithm.compute(this);
	}

	/**
	 * Computes the predecessor matrix for this graph.
	 * 
	 * @param weighted
	 * 
	 * @return the predecessor matrix for this graph. public PredecessorMatrix
	 *         getPredecessorMatrix(boolean weighted) { return weighted ?
	 *         weightedPredecessorMatrixAlgorithm.compute(this) :
	 *         unweightedPredecessorMatrixAlgorithm.compute(this); }
	 */

	/**
	 * Computes the version of the Grph library installed.
	 * 
	 * @return a string denoting the version of Grph.
	 */
	public static Version getVersion()
	{
		try
		{
			Version v = new Version();
			v.set(new String(new JavaResource("/grph-version.txt").getByteArray()));
			return v;
		}
		catch (IOException e)
		{
			throw new IllegalStateException(e);
		}
	}

	public void displayReport() throws IOException
	{
		Report r = new Report(this);
		r.display();
	}

	/**
	 * Display this graph in a graphical frame, using automatic layout.
	 */
	public void display()
	{
		displayGraphstream_0_4_2();
	}

	public void displaySVGSalamander()
	{
		try
		{
			byte[] svgdata = new GraphvizImageWriter().writeGraph(this);
			URI uri = SVGCache.getSVGUniverse().loadSVG(new ByteArrayInputStream(svgdata),
					"myImage");
			final SVGIcon icon = new SVGIcon();
			icon.setSvgURI(uri);

			final JPanel p = new JPanel()
			{
				@Override
				public void paintComponent(Graphics g)
				{
					icon.paintIcon(this, g, 0, 0);
				}
			};

			p.addComponentListener(new ComponentListener()
			{

				@Override
				public void componentShown(ComponentEvent e)
				{
				}

				@Override
				public void componentResized(ComponentEvent e)
				{
					icon.setPreferredSize(p.getSize());
					p.repaint();
				}

				@Override
				public void componentMoved(ComponentEvent e)
				{
				}

				@Override
				public void componentHidden(ComponentEvent e)
				{
				}
			});

			p.setPreferredSize(new Dimension(400, 400));
			icon.setUseAntiAlias(true);
			icon.setScaleToFit(true);
			icon.setClipToViewbox(true);

			Utilities.displayInJFrame(p, "SalamanderSVG");
		}
		catch (Exception e)
		{
			throw new IllegalArgumentException(e);
		}

	}

	public JComponent createSwingRenderer()
	{
		return Swing.createSwingRenderer(this);
	}

	/**
	 * Performs a random change in this graph.
	 * 
	 * @param r
	 */
	public void performRandomTopologicalChange(Random r)
	{
		assert r != null;

		// if work on vertices
		if ( ! storeEdges() || r.nextBoolean() || getVertices().isEmpty())
		{
			double rd = r.nextDouble();

			// if create a new vertex
			if (rd < 0.5 || getVertices().isEmpty())
			{
				addVertex();
			}
			// else remove an existing vertex
			else if (rd < 0.66666 && getVertices().size() >= 2)
			{
				int v = getVertices().pickRandomElement(r);
				int n = getEdgesIncidentTo(v).size();
				removeVertex(v);

				while (n-- > 0)
				{
					int a = getVertices().pickRandomElement(r);
					int b = getVertices().pickRandomElement(r);
					addUndirectedSimpleEdge(a, b);
				}
			}
		}
		// work on edges
		else
		{
			double rd = r.nextDouble();

			// if create
			if (rd < 0.5 || getEdges().isEmpty())
			{
				int a = getVertices().pickRandomElement(r);
				int b = getVertices().pickRandomElement(r);

				if (r.nextBoolean())
				{
					addDirectedSimpleEdge(a, b);
				}
				else
				{
					addUndirectedSimpleEdge(a, b);
				}
			}
			else
			// else remove
			{
				removeEdge(getEdges().pickRandomElement(r));
			}
		}
	}

	/**
	 * Adds an arc connecting the given source and destination vertices.
	 * 
	 * @param tail
	 *            the source of the arc
	 * @param head
	 *            the destination of the arc
	 * @return the value of the new arc
	 */
	public int addDirectedSimpleEdge(int tail, int head)
	{
		assert tail >= 0;
		assert head >= 0;

		int e = storeEdges() ? getNextEdgeAvailable() : - 1;
		addDirectedSimpleEdge(tail, e, head);
		return e;
	}

	/**
	 * Computes the set of isolated vertices in this graph.
	 * 
	 * @return a set of vertices
	 */
	public IntSet getIsolatedVertices()
	{
		return isolatedVerticesAlgorithm.compute(this);
	}

	/**
	 * Computes the order of this graph.
	 * 
	 * @return the order of this graph.
	 */
	public int getOrder()
	{
		return getVertices().size();
	}

	/**
	 * Computes the number of vertices in this graph.
	 * 
	 * @return the number of vertices in this graph.
	 */
	public int getNumberOfVertices()
	{
		return getVertices().size();
	}

	/**
	 * Computes the set of neighbors of the given vertex, cumulating in and out
	 * neighbors.
	 * 
	 * @param v
	 *            a vertex
	 * @return the set of neighbors of v
	 */
	public LucIntSet getNeighbours(int v)
	{
		assert v >= 0;
		LucIntSet in = getInNeighbors(v);
		LucIntSet out = getOutNeighbors(v);

		if (in.isEmpty() && out.isEmpty())
		{
			return LucIntSet.EMPTY_SET;
		}
		else if (in.isEmpty())
		{
			return out;
		}
		else if (out.isEmpty())
		{
			return in;
		}
		else
		{
			return LucIntSets.union(in, out);
		}
	}

	/**
	 * Computes the (open) neighborhood of the set s of vertices (that is the
	 * set of vertices that have at least one neighbour in s, and do not belong
	 * to s)
	 * 
	 * @param s
	 *            a set of vertices
	 * @return the set of (open) neighbors of s
	 */
	public IntSet getNeighbours(IntSet s)
	{
		IntSet n = new SelfAdaptiveIntSet();

		for (int v : s.toIntArray())
			n.addAll(getNeighbours(v));

		return LucIntSets.difference(n, s);
	}

	/**
	 * Computes different types of degrees of the given vertex.
	 * 
	 * @param v
	 *            a vertex
	 * @param type
	 *            the type of degree (edge or vertex)
	 * @param dir
	 *            the direction of the degree (in or out)
	 * @return the value of the specified degree
	 */
	public int getVertexDegree(int v, Grph.TYPE type, Grph.DIRECTION dir)
	{
		assert getVertices().contains(v) : "vertex not in graph : " + v;

		if (type == Grph.TYPE.edge)
		{
			if (dir == Grph.DIRECTION.in)
			{
				return getInEdges(v).size();
			}
			else if (dir == Grph.DIRECTION.out)
			{
				return getOutEdges(v).size();
			}
			else
			{
				return LucIntSets.union(getInEdges(v), getOutEdges(v)).size();
			}
		}
		else
		{
			if (dir == Grph.DIRECTION.in)
			{
				return getInNeighbors(v).size();
			}
			else if (dir == Grph.DIRECTION.out)
			{
				return getOutNeighbors(v).size();
			}
			else
			{
				return LucIntSets.union(getInNeighbors(v), getOutNeighbors(v)).size();
			}
		}
	}

	public int getEdgeDegree(int v)
	{
		return getVertexDegree(v, Grph.TYPE.edge, Grph.DIRECTION.in_out);
	}

	public int getVertexDegree(int v)
	{
		return getVertexDegree(v, Grph.TYPE.vertex, Grph.DIRECTION.in_out);
	}

	public Collection<IntSet> getGpmetisPartitionning(int numberOfPartitions, Random r)
	{
		return new Gpmetis().compute(this, numberOfPartitions, r);
	}

	public Collection<IntSet> getGpmetisPartitionning(int numberOfPartitions, Ptype ptype,
			Ctype ctype, Iptype iptype, Objtype objtype, boolean contig, boolean minconn,
			int ufactor, int niter, int ncuts, Random r)
	{
		return new Gpmetis().compute(this, numberOfPartitions, ptype, ctype, iptype,
				objtype, contig, minconn, ufactor, niter, ncuts, r);
	}

	public int getInEdgeDegree(int v)
	{
		return getVertexDegree(v, Grph.TYPE.edge, Grph.DIRECTION.in);
	}

	public int getInVertexDegree(int v)
	{
		return getVertexDegree(v, Grph.TYPE.vertex, Grph.DIRECTION.in);
	}

	public int getOutEdgeDegree(int v)
	{
		return getVertexDegree(v, Grph.TYPE.edge, Grph.DIRECTION.out);
	}

	public int getOutVertexDegree(int v)
	{
		return getVertexDegree(v, Grph.TYPE.vertex, Grph.DIRECTION.out);
	}

	public IntSet getEdgesConnecting(int src, int dest)
	{
		assert getVertices().contains(src);
		assert getVertices().contains(dest);
		return LucIntSets.intersection(getOutEdges(src), getInEdges(dest));
	}

	public void addHyperEdge(int edge, int... vertices)
	{
		assert getEdges().contains(edge);
		addUndirectedHyperEdge(edge);

		for (int v : vertices)
		{
			assert v >= 0;
			addToUndirectedHyperEdge(edge, v);
		}
	}

	public LucIntSet getInNeighbors(int v)
	{
		assert v >= 0 : v;

		if ( ! storeEdges())
		{
			return (LucIntSet) Collections.unionTo(LucIntSet.class, getInOnlyElements(v),
					getInOutOnlyElements(v));
		}
		else
		{
			LucIntSet ins = new DefaultIntSet();

			for (IntCursor c : IntCursor.fromFastUtil(getInEdges(v)))
			{
				int e = c.value;

				if (isSimpleEdge(e))
				{
					ins.add(getTheOtherVertex(e, v));
				}
				else if (isUndirectedHyperEdge(e))
				{
					ins.addAll(getUndirectedHyperEdgeVertices(e));
				}
				else if (isDirectedHyperEdge(e))
				{
					ins.addAll(getDirectedHyperEdgeTail(e));
				}
			}

			return ins;
		}
	}

	public LucIntSet getNeighbours(int vertex, Grph.DIRECTION direction)
	{
		assert vertex >= 0;

		if (direction == direction.out)
		{
			return getOutNeighbors(vertex);
		}
		else if (direction == direction.in)
		{
			return getInNeighbors(vertex);
		}
		else
		{
			return getNeighbours(vertex);
		}
	}

	public IntSet getKClosestNeighbors(int vertex, int k, NumericalProperty edgeWeights)
	{
		assert k >= 0;
		assert vertex >= 0;

		if (k == 0)
		{
			return IntSets.EMPTY_SET;
		}
		else
		{
			IntLinkedOpenHashSet queue = new IntLinkedOpenHashSet();
			queue.addAndMoveToLast(vertex);
			double[] distance = new double[getVertices().getGreatest() + 1];
			Arrays.fill(distance, - 1);
			distance[vertex] = 0;

			IntSet r = new SelfAdaptiveIntSet();

			while ( ! queue.isEmpty())
			{
				int v = queue.removeFirstInt();

				for (int e : getOutEdges(v).toIntArray())
				{
					double weight = edgeWeights == null ? 1
							: edgeWeights.getValueAsDouble(e);

					int neighbour = getTheOtherVertex(e, v);
					double d = distance[v] + weight;

					// if this vertex was not yet visited
					if (distance[neighbour] == - 1 || distance[neighbour] > d)
					{
						distance[neighbour] = d;

						queue.addAndMoveToLast(neighbour);
						r.add(neighbour);

						if (r.size() == k)
						{
							return r;
						}
					}
				}
			}

			if (r.size() > k)
				throw new IllegalStateException();

			return r;
		}
	}

	public IntSet getNeighborsAtMaxDistance(int vertex, int maxDistance,
			NumericalProperty p)
	{
		assert maxDistance >= 0;
		assert vertex >= 0;

		if (maxDistance == 0)
		{
			return IntSets.EMPTY_SET;
		}
		else
		{
			IntLinkedOpenHashSet queue = new IntLinkedOpenHashSet();
			queue.addAndMoveToLast(vertex);
			double[] distance = new double[getVertices().getGreatest() + 1];
			Arrays.fill(distance, - 1);
			distance[vertex] = 0;
			IntSet r = new SelfAdaptiveIntSet();

			while ( ! queue.isEmpty())
			{
				int v = queue.removeFirstInt();

				for (int e : getOutEdges(v).toIntArray())
				{
					double weight = p.getValueAsDouble(e);
					int neighbour = getTheOtherVertex(e, v);
					double neighborDistance = distance[v] + weight;

					if (neighborDistance < maxDistance)
					{
						r.add(neighbour);

						if (distance[neighbour] == - 1)
						{
							queue.addAndMoveToLast(neighbour);
						}

						if (distance[neighbour] == - 1
								|| neighborDistance < distance[neighbour])
						{
							distance[neighbour] = neighborDistance;
						}
					}
				}
			}

			return r;
		}
	}

	public IntSet getNeighboursAtMostKHops(int k, int v)
	{
		List<IntSet> fringes = getFringes(k, v);
		IntSet r = new SelfAdaptiveIntSet();

		for (int i = 1; i <= k && i < fringes.size(); ++i)
		{
			r.addAll(fringes.get(i));
		}

		return r;
	}

	public List<IntSet> getFringes(int numberOfHops, int root)
	{
		assert numberOfHops >= 0;
		assert getVertices().contains(root);

		List<IntSet> hops = new ArrayList();
		hops.add(IntSets.singleton(root));

		for (int hop = 1; hop <= numberOfHops; ++hop)
		{
			LucIntHashSet verticesAtThisHop = new LucIntHashSet();

			for (IntCursor v : IntCursor.fromFastUtil(hops.get(hop - 1)))
			{
				verticesAtThisHop.addAll(getOutNeighbors(v.value));
			}

			// remove the vertices that were already found at previous hops
			for (int j = 0; j < hop; ++j)
			{
				verticesAtThisHop.removeAll(hops.get(j));
			}

			// if we have reached the border of the graph, no need to iterate
			// again
			if (verticesAtThisHop.isEmpty())
			{
				break;
			}

			hops.add(verticesAtThisHop);
		}

		return hops;
	}

	/**
	 * Computes the set of self-loops incident to the given vertex
	 * 
	 * @param v
	 *            any vertex belonging to the graph
	 * @return the set of self-loops (edges) incident to v
	 */
	public IntSet getLoops(int v)
	{
		assert v >= 0;
		IntSet loops = new LucIntHashSet();

		for (IntCursor edge : IntCursor.fromFastUtil(getOutEdges(v)))
		{
			if (isLoop(edge.value))
			{
				loops.add(edge.value);
			}
		}

		return loops;
	}

	/**
	 * Checks whether the given edge is a self-loop or not.
	 * 
	 * @return true if the given edge is a loop
	 */
	public boolean isLoop(int edge)
	{
		if (getNavigation() != DIRECTION.in_out)
			throw new IllegalAccessError(
					"cannot determine if this is a loop becase in_out navigation is not allowed");

		assert getEdges().contains(edge);

		if (isSimpleEdge(edge))
		{
			int a = getOneVertex(edge);
			int b = getTheOtherVertex(edge, a);
			return a == b;
		}
		else if (isDirectedHyperEdge(edge))
		{
			return getDirectedHyperEdgeTail(edge).equals(getDirectedHyperEdgeHead(edge));
		}

		return false;
	}

	/**
	 * Checks if the given vertex has self-loops on it.
	 * 
	 * @param v
	 *            a vertex
	 * @return true if vertex v has a loop, false otherwise
	 */
	public boolean hasLoop(int v)
	{
		assert getVertices().contains(v);

		for (IntCursor edge : IntCursor.fromFastUtil(getOutEdges(v)))
		{
			if (isLoop(edge.value))
			{
				return true;
			}
		}

		return false;
	}

	/**
	 * Returns a set of vertices that are inaccessible (e.g. no edge head to
	 * them)
	 * 
	 * @param
	 * @param
	 * @param graph
	 * @return
	 */
	public IntSet getInaccessibleVertices()
	{
		return inacessibleVertices.compute(this);
	}

	/**
	 * Computes the set of connected component on this graph.
	 * 
	 * @return a set of vertex sets, each described a connected component
	 */
	public Collection<IntSet> getConnectedComponents()
	{
		return connectedComponentsAlg.compute(this);
	}

	/**
	 * Connect the vertices in this graph as chain.
	 * 
	 * @param directed
	 *            whether edges should be directed or not
	 */
	public void chain(boolean directed)
	{
		ChainTopologyGenerator tg = new ChainTopologyGenerator();
		tg.createDirectedLinks(directed);
		tg.compute(this);
	}

	/**
	 * Computes the connected component containing the given vertex, according
	 * to the given search direction.
	 * 
	 * @param vertex
	 *            a vertex
	 * @param direction
	 *            a direction (in, out or in_out)
	 * @return a set of edges describing the connected component
	 */
	public IntSet getConnectedComponentContaining(int vertex, Grph.DIRECTION direction)
	{
		final IntSet l = new SelfAdaptiveIntSet();
		l.add(vertex);

		new BFSAlgorithm().compute(this, vertex, direction, new GraphSearchListener()
		{

			@Override
			public void searchStarted()
			{
			}

			@Override
			public DECISION vertexFound(int v)
			{
				l.add(v);
				return DECISION.CONTINUE;
			}

			@Override
			public void searchCompleted()
			{
				// TODO Auto-generated method stub

			}

		});

		return l;
	}

	private void testgetConnectedComponentContaining()
	{
		Grph g = ClassicalGraphs.PetersenGraph();
		IntSet cc = g.getConnectedComponentContaining(0, Grph.DIRECTION.out);
		UnitTests.ensureEqual(cc.size(), g.getVertices().size());
	}

	/**
	 * Checks if this graph is a simple graph
	 * 
	 * @return true is this graph is a simple graph, false otherwise.
	 */
	public boolean isSimple()
	{
		return simplenessAlgorithm.compute(this);
	}

	/**
	 * Checks if this graph is an irreflexive graph
	 * 
	 * @return true is this graph is an irreflexive graph, false otherwise.
	 */
	public boolean isIrreflexive()
	{
		return irreflexiveAlgorithm.compute(this);
	}

	/**
	 * Computes a degree distribution for this graph
	 * 
	 * @param type
	 *            the type of the distribution (edge or vertex)
	 * @param dir
	 *            the direction (in, out or in_out)
	 * @return the distribution object
	 */
	public Distribution<Integer> getDegreeDistribution(Grph.TYPE type, Grph.DIRECTION dir)
	{
		Distribution<Integer> d = new Distribution<Integer>("Degree distribution");

		for (IntCursor thisVertex : IntCursor.fromFastUtil(getVertices()))
		{
			d.addOccurence(getVertexDegree(thisVertex.value, type, dir));
		}

		return d;
	}

	/**
	 * Checks if the given vertex is a simplicial one.
	 * 
	 * @param v
	 *            a vertex
	 * @return true if vertex v is simplicial, false otherwise.
	 */
	public boolean isSimplicial(int v)
	{
		IntSet neighbors = getNeighbours(v);
		neighbors.add(v);
		return isClique(neighbors);
	}

	/**
	 * Prunes this graph (remove leaves)
	 * 
	 * @param recursive
	 *            if pruning should continue while there are still leaves in
	 *            this graph
	 * @return
	 */
	public IntSet prune(boolean recursive)
	{
		return removeVertices(new ElementFilter.LeafFilter(this), recursive);
	}

	/**
	 * Removes all vertices matching the given filter.
	 * 
	 * @param filter
	 *            a vertex filter
	 * @param recursive
	 * @return the set of removed vertices
	 */
	public IntSet removeVertices(IntPredicate filter, boolean recursive)
	{
		IntSet r = new SelfAdaptiveIntSet();

		do
		{
			IntSet s = LucIntSets.filter(getVertices(), filter);

			if (s.isEmpty())
				break;
			else
			{
				r.addAll(s);
				removeVertices(s);
			}
		}
		while (recursive);

		return r;
	}

	/**
	 * Checks if the given set of vertices describes a clique in this graph.
	 * 
	 * @param s
	 *            a set of vertices
	 * @return true if s is a clique in this graph, false otherwise.
	 */
	public boolean isClique(IntSet s)
	{
		int[] array = s.toIntArray();

		for (int a : array)
		{
			for (int b : array)
			{
				if (a != b)
				{
					if (getEdgesConnecting(a, b).isEmpty())
					{
						return false;
					}
				}
			}
		}

		return true;
	}

	/**
	 * Checks if this graph has a cycle, by initiating the search from the given
	 * source. This method is useful in the case of digraphs.
	 * 
	 * @param src
	 *            a vertex
	 * @return true if a cycle has been found, false otherwise.
	 */
	public boolean hasCycles(int src)
	{
		assert src >= 0;

		byte[] colors = new byte[getVertices().getGreatest() + 1];

		if (colors[src] == 0)
		{
			if (hasCycles_visit(src, colors))
			{
				return true;
			}
		}

		return false;

	}

	private boolean hasCycles_visit(int v, byte[] colors)
	{
		colors[v] = 1;

		for (IntCursor u : IntCursor.fromFastUtil(getOutNeighbors(v)))
		{
			int ucolor = colors[u.value];

			if (ucolor == 1)
			{
				return true;
			}
			else if (ucolor == 0)
			{
				if (hasCycles_visit(u.value, colors))
				{
					return true;
				}
			}
		}

		colors[v] = 2;
		return false;
	}

	/**
	 * Topological sort of the vertices in this graph.
	 * 
	 * @return
	 */
	public IntArrayList sortTopologically()
	{
		return topologicalSortingAlg.compute(this);
	}

	public IntArrayList getAllInEdgeDegrees()
	{
		IntArrayList inDegrees = new IntArrayList(getVertices().getGreatest() + 1);

		for (IntCursor v : IntCursor.fromFastUtil(getVertices()))
		{
			inDegrees.add(getVertexDegree(v.value, Grph.TYPE.edge, Grph.DIRECTION.in));
		}

		return inDegrees;
	}

	/**
	 * Creates n vertices in this graph.
	 * 
	 * @param n
	 *            the number of vertices to be created.
	 */
	public IntSet addNVertices(int n)
	{
		assert n >= 0 : "cannot create a negative number of vertices";
		IntSet s = new SelfAdaptiveIntSet();

		for (int i = 0; i < n; ++i)
		{
			s.add(addVertex());
		}

		return s;
	}

	/**
	 * Checks whether this graph is cyclic or not (o circuit in the case of
	 * digraphs).
	 * 
	 * @return true is this graph has at least one cycle (or circuit).
	 */
	public boolean isCyclic()
	{
		return isCyclicAlgorithm.compute(this);
	}

	/**
	 * Computes a GLP topology from the vertices in the graph.
	 * 
	 * @return t
	 */
	public void glp()
	{
		GLPIssamTopologyGenerator glp = new GLPIssamTopologyGenerator();
		glp.compute(this);
	}

	/**
	 * Computes the average degree of this graph.
	 * 
	 * @return the average degree
	 */
	public double getAverageDegree()
	{
		return avgDegreeAlgorithm.compute(this);

	}

	/**
	 * Computes the average degree of this graph.
	 * 
	 * @param type
	 *            the type of degree (edge or vertex)
	 * @param direction
	 *            the direction (in, out, in_out)
	 * @return the average degree
	 */
	public double getAverageDegree(Grph.TYPE type, Grph.DIRECTION direction)
	{
		IntArrayList l = new IntArrayList();

		for (IntCursor c : IntCursor.fromFastUtil(getVertices()))
		{
			l.add(getVertexDegree(c.value, type, direction));
		}

		return MathsUtilities.computeAverage(l);
	}

	/**
	 * Computes a shortest path from the given source to the given destination.
	 * 
	 * @param source
	 *            a vertex
	 * @param destination
	 *            another vertex
	 * @return a shortest path from source to destination, or null if no path
	 *         can be found.
	 */
	public SearchResultWrappedPath getShortestPath(int source, int destination,
			NumericalProperty edgeWeights)
	{
		return new SearchResultWrappedPath(search(source, edgeWeights), source,
				destination);
	}

	public Path getShortestPath(int source, int destination)
	{
		return getShortestPath(source, destination, null);
	}

	/**
	 * Performs a BFS from the given vertex.
	 * 
	 * @param source
	 *            a vertex to start the BFS from.
	 * @return the data that could be computed along the BFS.
	 */
	public SearchResult bfs(int source)
	{
		return new BFSAlgorithm().compute(this, source);
	}

	public SearchResult[] bfs(LucIntSet sources)
	{
		return new BFSAlgorithm().compute(this, sources);
	}

	public boolean containsAPath(int source, final int destination)
	{
		final boolean[] found = new boolean[1];
		found[0] = false;

		new BFSAlgorithm().compute(this, source, Grph.DIRECTION.out,
				new GraphSearchListener()
				{

					@Override
					public void searchStarted()
					{
					}

					@Override
					public DECISION vertexFound(int v)
					{
						if (v == destination)
						{
							found[0] = true;
							return DECISION.STOP;
						}
						else
						{
							return DECISION.CONTINUE;
						}
					}

					@Override
					public void searchCompleted()
					{
					}

				});

		return found[0];
	}

	private void testcontainsAPath()
	{
		Grph g = ClassicalGraphs.PetersenGraph();
		UnitTests.ensure(containsAPath(0, 5));
	}

	/**
	 * Computes the vertex adjacency of the graph in the form of a two-dimension
	 * int array. This is useful for performance reasons and should be used when
	 * the graph does not change very often.
	 * 
	 * The set of vertices adjacent to vertex v are found in the array
	 * getVertexAdjacencies()[v]
	 * 
	 * @return
	 */
	public int[][] getOutNeighborhoods()
	{
		return outNeighborsAlgorithm.compute(this);
	}

	public int[][] getInNeighborhoods()
	{
		return inNeighborsAlgorithm.compute(this);
	}

	public int[][] getNeighborhoods()
	{
		return inOutNeighborsAlgorithm.compute(this);
	}

	public int[][] getNeighbors(Grph.DIRECTION direction)
	{
		if (direction == Grph.DIRECTION.in)
		{
			return getInNeighborhoods();
		}
		else if (direction == Grph.DIRECTION.out)
		{
			return getOutNeighborhoods();
		}
		else
		{
			return getNeighborhoods();
		}
	}

	/**
	 * Computues a two-sweeps approximation of the diameter of this graph. This
	 * algorithm is adequante to simple graphs.
	 * 
	 * @return an approximation of the diameter.
	 */
	public int getTwoSweepBFSDiameterApproximatedDiameter()
	{
		return twoSweepDiameterAlg.compute(this);
	}

	/**
	 * Connect all vertices in this graph as a clique.
	 */
	public void clique()
	{
		clique(getVertices());
	}

	/**
	 * Checks whether this graph is an antigraph.
	 * 
	 * @return true if this graph is an antigraph, false otherwise.
	 */
	public boolean isAntigraph()
	{
		return new AntiGraphAlgorithm().compute(this);
	}

	public Collection<Path> getAllPaths()
	{
		return new AllPaths().compute(this);
	}

	public Set<Path> getAllCycles()
	{
		return new FindAllCycles().compute(this);
	}

	/**
	 * Computes a distance matrix for this graph.
	 * 
	 * @return a distance matrix.
	 */
	public DistanceMatrix getDistanceMatrix(NumericalProperty edgeWeights)
	{
		if (edgeWeights == null)
		{
			return unweightedDistanceMatrixAlgorithm.compute(this);
		}
		else
		{
			return new StackBasedBellmanFordWeightedMatrixAlgorithm(edgeWeights)
					.compute(this);
		}
	}

	/**
	 * Computes a adjacency matrix for this graph.
	 * 
	 * @return a adjacency matrix.
	 */
	public AdjacencyMatrix getAdjacencyMatrix()
	{
		return adjacencyMatrixAlgo.compute(this);
	}

	/**
	 * Computes a incidence matrix for this graph.
	 * 
	 * @return a incidence matrix.
	 */
	public IntMatrix getIncidenceMatrix()
	{
		return incidenceMatrixAlgo.compute(this);
	}

	/**
	 * Deletes the given vertices from the graph.
	 * 
	 * @param s
	 *            the set of vertices to be removed from the graph.
	 */
	public void removeVertices(IntSet s)
	{
		for (IntCursor c : IntCursor.fromFastUtil(s))
		{
			removeVertex(c.value);
		}
	}

	public void addVertices(IntSet s)
	{
		for (IntCursor c : IntCursor.fromFastUtil(s))
		{
			addVertex(c.value);
		}
	}

	/**
	 * Sort all vertices by degree.
	 * 
	 * @return a degree-sorted array of all vertices.
	 */
	public int[] sortVerticesByDegree()
	{
		return new OutDegreeSorter().sort(this, getVertices());
	}

	/**
	 * Computes a graphic file containing a representation of this graph
	 * generated by GraphViz.
	 * 
	 * @param cmd
	 *            the command to use (dot, neato, etc)
	 * @param outputFormat
	 *            the format for the graphic data (PNG, PDF, etc)
	 * @param destination
	 *            the directory where the graphic file will be written to
	 * @return the generated graphviz file
	 * @throws IOException
	 */
	public RegularFile toGraphviz(COMMAND cmd, OUTPUT_FORMAT outputFormat,
			Directory destination) throws IOException
	{
		RegularFile f = new RegularFile(destination,
				destination.getUniqFileName("grph-", '.' + outputFormat.name()));
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		new GraphvizImageWriter().writeGraph(this, cmd, OUTPUT_FORMAT.png, false, bos);
		if ( ! destination.exists())
			destination.mkdirs();
		f.setContent(bos.toByteArray());
		return f;
	}

	public RegularFile toGraphviz(COMMAND cmd, OUTPUT_FORMAT outputFormat)
	{
		try
		{
			return toGraphviz(cmd, outputFormat, Directory.getSystemTempDirectory());
		}
		catch (IOException e)
		{
			throw new IllegalStateException(e);
		}
	}

	/**
	 * Checks if the given vertex is a source.
	 * 
	 * @param v
	 *            a vertex in the graph
	 * @return true if the vertex is a source one, false otherwise.
	 */
	public boolean isSource(int v)
	{
		return getInEdgeDegree(v) == 0 && getOutEdgeDegree(v) > 0;
	}

	/**
	 * Checks if the given vertex is a sink.
	 * 
	 * @param v
	 *            a vertex in the graph
	 * @return true if the vertex is a sink one, false otherwise.
	 */
	public boolean isSink(int v)
	{
		return getInEdgeDegree(v) > 0 && getOutEdgeDegree(v) == 0;
	}

	/**
	 * Checks if the given vertex is a leaf.
	 * 
	 * @param v
	 *            a vertex in the graph
	 * @return true if the vertex is a leaf, false otherwise.
	 */
	public boolean isLeaf(int v)
	{
		LucIntSet inEdges = getInEdges(v);

		// if there is only one way to enter the vertex
		if (inEdges.size() == 1)
		{
			LucIntSet outEdges = getOutEdges(v);
			int outDegree = outEdges.size();

			// no way out, it's a leaf !!!
			if (outDegree == 0)
			{
				return true;
			}
			// one way out? if it's the same way as the way in, it's an
			// undirected edge, okay, it's a leaf
			else if (outDegree == 1)
			{
				return inEdges.getGreatest() == outEdges.getGreatest();
			}
			// many ways to get out, it's not a leaf
			else
			{
				return false;
			}
		}
		else
		{
			return false;
		}
	}

	/**
	 * Determines the largest connected component in the graph.
	 * 
	 * @return the set of vertices describing the largest connected component in
	 *         the graph
	 */
	public IntSet getLargestConnectedComponent()
	{
		Collection<IntSet> cc = getConnectedComponents();

		if (cc.isEmpty())
		{
			return null;
		}
		else
		{
			Iterator<IntSet> i = cc.iterator();
			IntSet greatest = i.next();

			while (i.hasNext())
			{
				IntSet s = i.next();

				if (s.size() > greatest.size())
				{
					greatest = s;
				}
			}

			return greatest;
		}
	}

	/**
	 * Computes a textual description for this graph.
	 * 
	 * @return a textual description of not specified form.
	 */
	public String getDescription()
	{
		StringBuilder b = new StringBuilder();

		b.append(toString());
		b.append('\n');
		b.append("density=" + getDensity());
		b.append('\n');
		b.append("avg degree=" + getAverageDegree());

		return b.toString();
	}

	/**
	 * Ensure this graph has at least the specified amount of vertices. If not,
	 * additional vertices will be created.
	 * 
	 * @param n
	 *            the minimum number of vertices required for this graph.
	 */
	public void ensureNVertices(int n)
	{
		addNVertices(Math.max(n - getVertices().size(), 0));
	}

	/**
	 * Computes the maximum clique in this graph, using an ant-based algorithm
	 * defined by Chistine Solnon. This algorithm is implemented in C.
	 * 
	 * @return a set of vertices describing the clique
	 */
	public IntSet getMaximumClique()
	{
		return new AntCliqueAlgorithm().compute(this);
	}

	/**
	 * Checks if this graph is chordal or not.
	 * 
	 * @return true if this graph is chordal, false otherwise.
	 */
	public boolean isChordal()
	{
		return chordalAlgo.compute(this);
	}

	/**
	 * Comptes the set of simplitial vertices in this graph.
	 * 
	 * @return a set containing all simplitial vertices.
	 */
	public IntSet getSimplicialVertices()
	{
		IntSet s = new SelfAdaptiveIntSet();

		for (IntCursor c : IntCursor.fromFastUtil(getVertices()))
		{
			if (isSimplicial(c.value))
			{
				s.add(c.value);
			}
		}

		return s;
	}

	/**
	 * Assign adequate color to the given vertices so that they distinguish
	 * clearly on graphical views of this graph.
	 * 
	 * @param vertices
	 *            a set of vertices to be colored (selected)
	 */
	public void highlightVertices(IntSet vertices)
	{
		highlightVertices(vertices, consumeHighlightColor());
	}

	public void highlightEdges(IntSet edges)
	{
		highlightEdges(edges, consumeHighlightColor());
	}

	/**
	 * Computes a report for this graph.
	 * 
	 * @return A reference to a PDF file containing the report.
	 * @throws IOException
	 */
	public RegularFile report() throws IOException
	{
		Report report = new Report(this);
		RegularFile pdfFile = report.computePDFReport();
		return pdfFile;
	}

	/**
	 * Computes the set of source vertices in this graph.
	 * 
	 * @return a set containing all source vertices.
	 */
	public IntSet getSources()
	{
		IntSet r = new SelfAdaptiveIntSet();

		for (IntCursor c : IntCursor.fromFastUtil(getVertices()))
		{
			if (isSource(c.value))
			{
				r.add(c.value);
			}
		}

		return r;
	}

	/**
	 * Computes the set of sink vertices in this graph.
	 * 
	 * @return a set containing all sink vertices.
	 */
	public IntSet getSinks()
	{
		IntSet r = new SelfAdaptiveIntSet();

		for (IntCursor c : IntCursor.fromFastUtil(getVertices()))
		{
			if (isSink(c.value))
			{
				r.add(c.value);
			}
		}

		return r;
	}

	public int getNumberOfEdges()
	{
		return getNumberOfDirectedHyperEdges() + getNumberOfDirectedSimpleEdges()
				+ getNumberOfUndirectedHyperEdges() + getNumberOfUndirectedSimpleEdges();
	}

	public int getSize()
	{
		return getNumberOfEdges();
	}

	private static Object loadAlgorithm(String libname, String classname)
	{
		// files older than 1 second are updated
		long peremption = 1000L;

		try
		{
			String filename = libname + ".jar";
			Directory localAlgoRepository = new Directory(
					Directory.getSystemTempDirectory(), "grph/algo");

			if ( ! localAlgoRepository.exists())
			{
				localAlgoRepository.mkdirs();
			}

			RegularFile jarFile = new RegularFile(localAlgoRepository, filename);

			// if the file does not yet exist or is too old
			if ( ! jarFile.exists() || jarFile.getAgeMs() > peremption)
			{
				String url = "http://www-sop.inria.fr/members/Luc.Hogie/grph/algo/"
						+ libname + ".grphlib";

				// re-download it, if possible
				try
				{
					byte[] jarBytes = NetUtilities.retrieveURLContent(url);
					jarFile.setContent(jarBytes);
				}
				catch (Throwable ioe)
				{
					System.err.println("Unable to retrieve " + url);
				}
			}

			// loads the JAR file
			URLClassLoader child = new URLClassLoader(
					new URL[] { jarFile.toFile().toURL() },
					ClassLoader.getSystemClassLoader());
			Class classToLoad = child.loadClass(classname);
			return classToLoad.newInstance();
		}
		catch (Throwable t)
		{
			throw new IllegalStateException(t);
		}
	}

	/**
	 * Checks if this graph is bipartite.
	 * 
	 * @return true is this graph is bipartite, false otherwise.
	 */
	public boolean isBipartite()
	{
		// See the compute() method of the BipartenessAlgorithm class
		return bipartiteAlgorithm.compute(this) != null;
	}

	/**
	 * Connect the given set of vertices as a clique.
	 * 
	 * @param s
	 *            a set of vertices in the graph
	 */
	public void clique(IntSet s)
	{
		int[] a = s.toIntArray();

		for (int i = 0; i < a.length; ++i)
		{
			for (int j = 0; j < i; ++j)
			{
				addUndirectedSimpleEdge(a[i], a[j]);
			}
		}
	}

	public void highlightVertices(Collection<IntSet> sets)
	{
		for (IntSet s : sets)
		{
			highlightVertices(s, consumeHighlightColor());
		}
	}

	public IntArrayList bfsList(int sourceVertex)
	{
		final IntArrayList l = new IntArrayList();

		new BFSAlgorithm().compute(this, sourceVertex, Grph.DIRECTION.out,
				new GraphSearchListener()
				{

					@Override
					public DECISION vertexFound(int v)
					{
						l.add(v);
						return DECISION.CONTINUE;
					}

					@Override
					public void searchStarted()
					{
					}

					@Override
					public void searchCompleted()
					{
					}
				});

		return l;
	}

	public AbstractStepper stepMonitor()
	{
		// return new ConsoleStepper(this);
		return new SwingStepper(this);
	}

	public void highlight(Collection<Cluster> clusters)
	{
		highlight(clusters, true);
	}

	public void highlight(Collection<Cluster> clusters, boolean highlightClusterHeads)
	{
		int color = 1;

		for (Cluster c : clusters)
		{
			highlightVertices(c, color++);

			if (highlightClusterHeads)
			{
				getVertexShapeProperty().setValue(c.getHead(), 1);
			}
		}
	}

	public void highlightVertex(int v, int color)
	{
		getVertexColorProperty().setValue(v, color);

	}

	public void displayImg()
	{
		toGraphviz(COMMAND.fdp, OUTPUT_FORMAT.png).open();

	}

	/**
	 * Retrieves the vertex at the center of the graph.
	 * 
	 * @return the vertex at the center of the graph.
	 */
	public int getCenter()
	{
		return centerAlgorithm.compute(this);
	}

	public IntSet getVerticesAccessibleThrough(int vertex, int e)
	{
		if ( ! getOutEdges(vertex).contains(e))
			throw new IllegalArgumentException(
					"edge " + e + " is not incident to vertex " + vertex);

		IntSet r = new LucIntHashSet();

		if (isUndirectedSimpleEdge(e))
		{
			r.add(getTheOtherVertex(e, vertex));
		}
		else if (isDirectedSimpleEdge(e))
		{
			r.add(getDirectedSimpleEdgeHead(e));
		}
		else if (isUndirectedHyperEdge(e))
		{
			r.addAll(getUndirectedHyperEdgeVertices(e));
		}
		else if (isDirectedHyperEdge(e))
		{
			r.addAll(getDirectedHyperEdgeHead(e));
		}

		return r;
	}

	public IntSet getVerticesAccessibleThrough(int sourceVertex, IntSet links)
	{
		IntSet r = new LucIntHashSet();

		for (IntCursor cl : IntCursor.fromFastUtil(links))
		{
			int e = cl.value;

			if ( ! getOutEdges(sourceVertex).contains(e))
				throw new IllegalArgumentException(
						"edge " + e + " is not incident to vertex " + sourceVertex);

			if (isUndirectedSimpleEdge(e))
			{
				r.add(getTheOtherVertex(e, sourceVertex));
			}
			else if (isDirectedSimpleEdge(e))
			{
				r.add(getDirectedSimpleEdgeHead(e));
			}
			else if (isUndirectedHyperEdge(e))
			{
				r.addAll(getUndirectedHyperEdgeVertices(e));
			}
			else if (isDirectedHyperEdge(e))
			{
				r.addAll(getDirectedHyperEdgeHead(e));
			}
		}

		return r;
	}

	public boolean hasLoops()
	{
		for (int e : getEdges().toIntArray())
		{
			if (isLoop(e))
			{
				return true;
			}
		}

		return false;
	}

	public Int2IntMap addGraph(Grph otherGraph)
	{
		Int2IntMap v_localV = new Int2IntOpenHashMap();

		for (int v : otherGraph.getVertices().toIntArray())
		{
			v_localV.put(v, addVertex());
		}

		for (int e : otherGraph.getEdges().toIntArray())
		{
			if (otherGraph.isUndirectedSimpleEdge(e))
			{
				int a = otherGraph.getOneVertex(e);
				int b = otherGraph.getTheOtherVertex(e, a);
				addUndirectedSimpleEdge(v_localV.get(a), v_localV.get(b));
			}
			else
			{
				throw new NotYetImplementedException();
			}
		}

		return v_localV;
	}

	public void merge(Grph otherGraph)
	{
		// adds any vertices that are not already in
		for (int v : otherGraph.getVertices().toIntArray())
		{
			if ( ! getVertices().contains(v))
			{
				addVertex(v);
			}
		}

		for (int e : otherGraph.getEdges().toIntArray())
		{
			if (otherGraph.isUndirectedSimpleEdge(e))
			{
				int a = otherGraph.getOneVertex(e);
				int b = otherGraph.getTheOtherVertex(e, a);
				addUndirectedSimpleEdge(a, b);
			}
			else if (otherGraph.isDirectedSimpleEdge(e))
			{
				int a = otherGraph.getOneVertex(e);
				int b = otherGraph.getTheOtherVertex(e, a);
				addDirectedSimpleEdge(a, b);
			}
			else
			{
				throw new NotYetImplementedException();
			}
		}
	}

	/**
	 * 
	 * @param a
	 * @param b
	 * @param allowMultipleEdges
	 */
	public void contractVertices(int a, int b)
	{
		contractVertices(a, b, false);
	}

	public void contractVertices(int a, int b, boolean allowMultipleEdges)
	{
		for (int e : getEdgesIncidentTo(a).toIntArray())
		{
			if (isUndirectedSimpleEdge(e))
			{
				int neighbour = getTheOtherVertex(e, a);

				if ( ! areVerticesAdjacent(b, neighbour) || allowMultipleEdges)
				{
					addUndirectedSimpleEdge(b, neighbour);
				}
			}
			else
			{
				throw new NotYetImplementedException();
			}

			removeEdge(e);
		}

		removeVertex(a);
	}

	/**
	 * Builds a new graph from the given CAIDA map.
	 * 
	 * @param filename
	 * @throws ParseException
	 * @throws IOException
	 * @throws GraphBuildException
	 */
	public static Grph fromCaidaMap(RegularFile f)
			throws IOException, GraphBuildException, ParseException
	{
		return new EdgeListReader().readGraph(f.getContent());
	}

	public void contractEdge(int e)
	{
		if ( ! isSimpleEdge(e))
			throw new IllegalArgumentException();

		int a = getOneVertex(e);
		int b = getTheOtherVertex(e, a);
		contractVertices(a, b);
	}

	public boolean areEdgesAdjacent(int a, int b)
	{
		return ! LucIntSet
				.difference(getVerticesIncidentToEdge(a), getVerticesIncidentToEdge(b))
				.isEmpty();
	}

	public static int getDefaultNumberOfThreads()
	{
		return Runtime.getRuntime().availableProcessors() * 4;
	}

	public List<ArrayListPath> getKShortestPaths(int s, int t, int k,
			NumericalProperty edgeWeights)
	{
		return new YenTopKShortestPathsAlgorithm().compute(this, s, t, k, edgeWeights);
	}

	public Collection<IntSet> getCliques()
	{
		return new FindAllCliques().compute(this);
	}

	public void getCircleStar()
	{

		for (int i = 1; i < getNumberOfVertices(); i++)
		{
			if (i == getNumberOfVertices() - 1)
			{
				addUndirectedSimpleEdge(i, 1);
				addUndirectedSimpleEdge(i, 0);
			}
			else
			{
				addUndirectedSimpleEdge(i, i + 1);
				addUndirectedSimpleEdge(i, 0);
			}
		}
	}

	public Grph toUndirectedGraph()
	{
		Grph g = Clazz.makeInstance(getClass());
		g.getVertices().addAll(getVertices());

		for (int e : getEdges().toIntArray())
		{
			if ( ! isSimpleEdge(e))
				throw new IllegalStateException("unsupported edge");

			int u = getOneVertex(e);
			int v = getTheOtherVertex(e, u);
			g.addUndirectedSimpleEdge(u, v);
		}

		return g;
	}

	public String toString_elements()
	{
		return getVertices().size() + " vertices: " + getVertices() + "\n"
				+ getEdges().size() + " edges: " + getEdges();
	}

	private static void testCloneAndEquals()
	{
		Grph g = ClassicalGraphs.PetersenGraph();
		Grph h = g.clone();
		UnitTests.ensure(g.equals(h));
	}

	public Property getVertexLabelProperty()
	{
		return verticesLabel;
	}

	public void setVerticesLabel(Property verticesLabel)
	{
		this.verticesLabel = verticesLabel;
	}

	public Property getEdgeLabelProperty()
	{
		return edgesLabel;
	}

	public void setEdgesLabel(Property edgesLabel)
	{
		this.edgesLabel = edgesLabel;
	}

	public NumericalProperty getVertexColorProperty()
	{
		return verticesColor;
	}

	public void setVerticesColor(NumericalProperty verticesColor)
	{
		this.verticesColor = verticesColor;
	}

	public NumericalProperty getEdgeColorProperty()
	{
		return edgesColor;
	}

	public void setEdgesColor(NumericalProperty edgesColor)
	{
		this.edgesColor = edgesColor;
	}

	public NumericalProperty getEdgeWidthProperty()
	{
		return edgesWidth;
	}

	public void setEdgesWidth(NumericalProperty edgesWidth)
	{
		this.edgesWidth = edgesWidth;
	}

	public NumericalProperty getVertexSizeProperty()
	{
		return verticesSize;
	}

	public void setVerticesSize(NumericalProperty verticesSize)
	{
		this.verticesSize = verticesSize;
	}

	public NumericalProperty getVertexShapeProperty()
	{
		return verticesShape;
	}

	public void setVerticesShape(NumericalProperty verticesShape)
	{
		this.verticesShape = verticesShape;
	}

	public NumericalProperty getEdgeStyleProperty()
	{
		return edgesStyle;
	}

	public void setEdgesStyle(NumericalProperty edgesStyle)
	{
		this.edgesStyle = edgesStyle;
	}

	public boolean isUndirectedSimpleEdge(int u, int v)
	{
		assert ! storeEdges();
		return getInOutOnlyElements(u).contains(v);
	}

	public boolean isDirectedSimpleEdge(int u, int v)
	{
		assert ! storeEdges();
		return getOutOnlyElements(u).contains(v);
	}

	public LucIntSet getOppositeEdges(int e)
	{
		LucIntSet r = new SelfAdaptiveIntSet();
		assert isDirectedSimpleEdge(e);
		int tail = getDirectedSimpleEdgeTail(e);
		int head = getDirectedSimpleEdgeHead(e);

		for (int oe : getOutOnlyEdges(head).toIntArray())
		{
			if (getDirectedSimpleEdgeHead(oe) == tail)
			{
				r.add(oe);
			}
		}

		return r;
	}

	public SearchResult search(int v, NumericalProperty edgeWeights)
	{
		if (edgeWeights != null)
		{
			return new DijkstraAlgorithm(edgeWeights).compute(this, v);
		}
		else
		{
			return bfs(v);
		}
	}

	public int getFartestVertex(final int v)
	{
		return getFartestVertex(v, null);
	}

	public int getFartestVertex(final int v, NumericalProperty edgeWeights)
	{
		return search(v, edgeWeights).farestVertex();
	}

	/**
	 * Display this graph in a graphical frame, using GraphStream 0.4.2.
	 * 
	 * @return
	 */
	public GraphViewerRemote displayGraphstream_0_4_2()
	{
		// Utilities.displayInJFrame(toGraphStream_0_4_2_AWTComponent(),
		// "Grph Graphstream v0.4.2 driver");
		return toGraphStream_0_4_2_AWTComponent().displayInAFrame();
	}

	public GraphstreamBasedRenderer toGraphStream_0_4_2_AWTComponent()
	{
		return new GraphstreamBasedRenderer(this);
	}

}
