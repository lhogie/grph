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
 
 package grph.algo.covering_packing;

import grph.Grph;
import grph.GrphAlgorithm;
import grph.algo.topology.GNPTopologyGenerator;
import grph.in_memory.InMemoryGrph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

import toools.set.DefaultIntSet;
import toools.set.IntSet;
import toools.set.IntSets;

import com.carrotsearch.hppc.IntObjectMap;
import com.carrotsearch.hppc.IntObjectOpenHashMap;
import com.carrotsearch.hppc.cursors.IntCursor;

/**
 * This class provides an implementation of the algorithm proposed by Fomin, Grandoni and Kratsch
 * for computing a maximum independent set, based on <i>foldable vertices</i> and <i>mirroring</i>, in 
 * F.V. Fomin, F. Grandoni, and D. Kratsch, <b>A measure & conquer approach for the analysis of exact algorithms</b>,
 * Journal of the ACM (JACM), vol.56-5, p.25, 2009, ACM.
 *  
 * @author Gregory Morel, Vincent Levorato, Jean-Francois Lalande
 */

@SuppressWarnings("serial")
public class FominGrandoniKratschMaximumindependentSetAlgorithm extends GrphAlgorithm<IntSet> {	
	@Override
	public IntSet compute(Grph g) {
		return FominGrandoniKratschAlgorithmDeg2(g.clone());
	}
	
	/**
	 * Implementation of the algorithm described in the above article.
	 * @param g
	 * @return
	 */
	private IntSet FominGrandoniKratschAlgorithmDeg2(Grph g) {
		// Step 1: 
		int Vsize = g.getNumberOfVertices();
		if(Vsize <= 1) {
			return g.getVertices();
		}
		
		// Step 2: has g a proper connected component?
		IntSet cc = g.getConnectedComponents().iterator().next();
		if(cc.size() < Vsize) {
			Grph gc = g.getSubgraphInducedByVertices(cc);
			Grph g_c = g.clone();
			g_c.removeVertices(cc);
			
			IntSet mis = new DefaultIntSet();
			mis.addAll(FominGrandoniKratschAlgorithmDeg2(gc));
			mis.addAll(FominGrandoniKratschAlgorithmDeg2(g_c));
			return mis;
		}
		
		// Step 3: has g a dominated vertex?
		int d = getDominatedVertex(g);
		if(d >= 0) {
			g.removeVertex(d);
			return FominGrandoniKratschAlgorithmDeg2(g);
		}
		
		// Step 4: has g a degree-2 (foldable) vertex?
		// Note: if none of the conditions above holds, the neighbors of a vertex of degree 2 are necessarily
		// non-adjacent, and the vertex is foldable
		IntSet deg2 = g.getVerticesOfDegree(2);
		if(!deg2.isEmpty()) {
			int x = deg2.toIntArray()[0];
						
			Grph gf = g.clone();
			int[] Nx = g.getNeighbours(x).toIntArray(); 
			int a = Nx[0];
			int b = Nx[1];
			gf.removeVertex(x);
			IntSet Na = gf.getNeighbours(a);
			IntSet Nb = gf.getNeighbours(b);
			gf.removeVertices(a, b);
			int newx = gf.getVertices().getGreatest() + 1;
			gf.addVertex(newx);
			
			for(IntCursor c  : Na)
				gf.addUndirectedSimpleEdge(newx, c.value);
			
			for(IntCursor c  : Nb)
				// Edges may have been already added with Na
				if(!gf.areVerticesAdjacent(newx, c.value))
					gf.addUndirectedSimpleEdge(newx, c.value);
			
			IntSet misGf = FominGrandoniKratschAlgorithmDeg2(gf);
			if(misGf.contains(newx))  {
				misGf.remove(newx);
				misGf.add(a);
				misGf.add(b);
			}
			else
				misGf.add(x);
			
			return misGf;
		}
		
		// Step 5: Select a vertex v of maximum degree which minimizes |E(N(v))|
		
		int maxdegree = g.getMaxOutVertexDegrees();			// TODO: write a method for undirected graphs
        IntSet maxdeg = g.getVerticesOfDegree(maxdegree);
        int v = -1;
        int ENv=Integer.MAX_VALUE;
        for(IntCursor c : maxdeg) {
        	int y = c.value;
            int ENy = g.getSubgraphInducedByVertices(g.getNeighbours(y)).getNumberOfEdges();
            if(ENy < ENv) {
            	ENv = ENy;
            	v = y;
            }
        }
		
		// Step 6: Branching
        Grph g_v_Mv = g.clone();
        Grph g_nv = g.clone();
        
        g_v_Mv.removeVertex(v);
        IntSet Mv = getMirrors(g,v);
        g_v_Mv.removeVertices(Mv);
        
        g_nv.removeVertices(g_nv.getNeighbours(v));
        g_nv.removeVertex(v);
        
        IntSet mis1 = FominGrandoniKratschAlgorithmDeg2(g_v_Mv);
        IntSet mis2 = FominGrandoniKratschAlgorithmDeg2(g_nv);
        mis2.add(v);
        
        if(mis1.size() > mis2.size())
        	return mis1;
        else
        	return mis2;
	}

	/**
	 * Original algorithm, with foldable vertices that may be of degree at most 4, published in
	 * F.V. Fomin, F. Grandoni, and D. Kratsch, <b>Measure and conquer: a simple O (2^(0.288 n))
	 * independent set algorithm </b>, Proceedings of the seventeenth annual ACM-SIAM symposium on Discrete algorithm,
  	 * pp.18--25, 2006, ACM. 
  	 * In this implementation, foldable vertices may be of any degree.
	 * 
	 * Tests have shown that restriction to degree-2 foldable vertices,
	 * as proposed in the above article, is about 3 times faster.
	 * @param g
	 * @return
	 */
	private IntSet FominGrandoniKratschAlgorithm(Grph g) {
		// (slightly improved) Step 0: 
		int Vsize = g.getNumberOfVertices();
		if(Vsize <= 1) {
			return g.getVertices();
		}
		
		// Step 1: has g a proper connected component?
		IntSet cc = g.getConnectedComponents().iterator().next();
		if(cc.size() < Vsize) {
			Grph gc = g.getSubgraphInducedByVertices(cc);
			Grph g_c = g.clone();
			g_c.removeVertices(cc);
			
			IntSet mis = new DefaultIntSet();
			mis.addAll(FominGrandoniKratschAlgorithm(gc));
			mis.addAll(FominGrandoniKratschAlgorithm(g_c));
			return mis;
		}
		
		// Step 2: has g a dominated vertex?
		int d = getDominatedVertex(g);
		if(d >= 0) {
			g.removeVertex(d);
			return FominGrandoniKratschAlgorithm(g);
		}
		
		// Step 3: Foldable vertices
		int fv = getFoldableVertex(g);	// returns -1 if there is no foldable vertex
		if(fv != -1) {
			IntObjectMap<IntSet> hashmap = new IntObjectOpenHashMap<IntSet>();
			IntSet misfolded = FominGrandoniKratschAlgorithm(getFoldedGraph(g, fv, hashmap));
			
			IntSet mis = new DefaultIntSet();
			boolean fold = false;
			 for(int v : misfolded.toIntArray())
				 if(hashmap.containsKey(v)) {
					 mis.addAll(hashmap.get(v));
					 fold=true;
				 }
				 else
					 mis.add(v);

			 if(!fold)
				 mis.add(fv);

			 return mis;
		}

		// (improved) Step 4: take a vertex of maximum degree and branch 
		int maxdegree = g.getMaxOutVertexDegrees();			// TODO: write a method for undirected graphs
        IntSet maxdeg = g.getVerticesOfDegree(maxdegree);
        
        int v = -1;        
        int ENv=Integer.MAX_VALUE;
        for(IntCursor c : maxdeg) {
        	int y = c.value;
            int ENy = g.getSubgraphInducedByVertices(g.getNeighbours(y)).getNumberOfEdges();
            if(ENy < ENv) {
            	ENv = ENy;
            	v = y;
            }
        }
		
        Grph g_v_Mv = g.clone();
        Grph g_nv = g.clone();
        
        g_v_Mv.removeVertex(v);
        IntSet Mv = getMirrors(g,v);
        g_v_Mv.removeVertices(Mv);
        
        g_nv.removeVertices(g_nv.getNeighbours(v));
        g_nv.removeVertex(v);
        
        IntSet mis1 = FominGrandoniKratschAlgorithm(g_v_Mv);
        IntSet mis2 = FominGrandoniKratschAlgorithm(g_nv);
        mis2.add(v);
        
        if(mis1.size() > mis2.size())
        	return mis1;
        else
        	return mis2;
	}

	/**
	 * An other implementation, where the folding operation has been replaced by the more general
	 * <i>struction</i> operation proposed by C. Ebenegger, P.L. Hammer and D. de Werra,
	 * <b>Pseudo-Boolean functions and stability of graphs</b>, North-Holland mathematics studies,  
	 * vol.95, pp. 83-97, 1984, Elsevier.
	 * Tests have shown that this implementation is faster than the degree-4 folding algorithm, but
	 * slower than the degree-2 folding algorithm.
	 *  
	 * @param g
	 * @return
	 */
	private IntSet FominGrandoniKratschAlgorithmStruction(Grph g) {
		// Step 0: 
		int Vsize = g.getNumberOfVertices();
		if(Vsize <= 1) {
			return g.getVertices();
		}
		
		// Step 1: has g a proper connected component?
		IntSet cc = g.getConnectedComponents().iterator().next();
		if(cc.size() < Vsize) {
			Grph gc = g.getSubgraphInducedByVertices(cc);
			Grph g_c = g.clone();
			g_c.removeVertices(cc);
			
			IntSet mis = new DefaultIntSet();
			mis.addAll(FominGrandoniKratschAlgorithmStruction(gc));
			mis.addAll(FominGrandoniKratschAlgorithmStruction(g_c));
			return mis;
		}
		
		// Step 2: has g a dominated vertex?
		int d = getDominatedVertex(g);
		if(d >= 0) {
			g.removeVertex(d);
			return FominGrandoniKratschAlgorithmStruction(g);
		}
		
		// Step 3: Struction
		// We choose as the center of the struction a vertex v0 of small degree
		// which does not lead to a greater number of vertices than in the original graph
		// (the number of non-edges in the neighborhood of v0 must be <= |N(v)| + 1)
		// If there is no such a vertex, we don't perform the struction. 
		int v0 = -1;
		for(IntCursor c : g.getVertices()) {
			IntSet Nc = g.getNeighbours(c.value);
			if(g.getSubgraphInducedByVertices(Nc).getComplement().getNumberOfUndirectedSimpleEdges() <= Nc.size() + 1) {
				v0 = c.value;
				break;
			}
		}		
		
		if(v0 != -1) {
			IntObjectMap<int[]> hashmap = new IntObjectOpenHashMap<int[]>();
			IntSet misStruction = FominGrandoniKratschAlgorithmStruction(struction(g, v0, hashmap)); 
			IntSet mis = new DefaultIntSet();
			
			boolean fold = false;
			for(int v : misStruction.toIntArray())
				if(hashmap.containsKey(v)) {
					mis.addAll(hashmap.get(v));
					fold=true;
				}
				else
					mis.add(v);

			if(!fold)
				mis.add(v0);

			return mis;
		}
		
		// Step 4: Select a vertex v of maximum degree which minimizes |E(N(v))| and branch
        int maxdegree = g.getMaxOutVertexDegrees();			// TODO: write a method for undirected graphs
        IntSet maxdeg = g.getVerticesOfDegree(maxdegree);
        int v = -1;
        int ENv=Integer.MAX_VALUE;
        for(IntCursor c : maxdeg) {
        	int y = c.value;
            int ENy = g.getSubgraphInducedByVertices(g.getNeighbours(y)).getNumberOfEdges();
            if(ENy < ENv) {
            	ENv = ENy;
            	v = y;
            }
        }
		
        Grph g_v_Mv = g.clone();
        Grph g_nv = g.clone();
        
        g_v_Mv.removeVertex(v);
        IntSet Mv = getMirrors(g,v);
        g_v_Mv.removeVertices(Mv);
        
        g_nv.removeVertices(g_nv.getNeighbours(v));
        g_nv.removeVertex(v);
        
        IntSet mis1 = FominGrandoniKratschAlgorithmStruction(g_v_Mv);
        IntSet mis2 = FominGrandoniKratschAlgorithmStruction(g_nv);
        mis2.add(v);
        
        if(mis1.size() > mis2.size())
        	return mis1;
        else
        	return mis2;
	}
	
	/**
	 * Looks for a dominated vertex in g, that is a vertex v such that there is a vertex w with
	 * N[w] included in N[v].
	 */
	private int getDominatedVertex(Grph g) {
		int x = -1;
		HashMap<IntSet, Integer> dom = new HashMap<IntSet, Integer>();
		//ObjectIntMap<IntSet> dom = new ObjectIntOpenHashMap<IntSet>();
		
		// List all neighborhoods in N
		ArrayList<IntSet> N = new ArrayList<IntSet>();
		
		for(int v : g.getVertices().toIntArray()) {
			IntSet Nv = new DefaultIntSet();
			Nv.addAll(g.getNeighbours(v));
			Nv.add(v);
			N.add(Nv);
			dom.put(Nv, v);
		}
		
		IntSets.quickSortSet(N, 0, N.size()-1);
		boolean xfound = false;
		int i = N.size() - 1;
		
		while(i > 0 && !xfound) {
			IntSet Nx = N.get(i);
			
			int j = 0;
			while(j < i && !xfound) {
				IntSet Ny = N.get(j);
				if(Nx.contains(Ny)) {
					x = dom.get(Nx);
					xfound = true;
				}
				
				j++;
			}
			
			i--;
		}
		
		return x;
	}
	
	/**
	 * Return the vertices that are mirrors of v in the graph g
	 * @param g
	 * @param v
	 * @return
	 */
	private IntSet getMirrors(Grph g, int v) {
		IntSet Nv = g.getNeighbours(v);
		// U is the set of vertices at distance 2 from v
		IntSet U = IntSets.difference(g.getNeighboursAtMostKHops(2, v), Nv);
		
		IntSet Mv = new DefaultIntSet();
		
        //test for each u if N(v) \ N(u) is a clique
        //if true, u is a mirror of v
		for(IntCursor u : U) {
			IntSet Nu = g.getNeighbours(u.value);
			IntSet Nv_Nu = IntSets.difference(Nv, Nu);
			if(g.isClique(Nv_Nu))
				Mv.add(u.value);
		}
		
		return Mv;
	}
	
	/**
	 * Looks for a foldable vertex, thas is a vertex whose neighborhood does not contain an antitriangle
	 * @param g
	 * @return
	 */
	private static int getFoldableVertex(Grph g) {
		for(IntCursor c : g.getVertices()) {
			int v = c.value;
			IntSet Nv = g.getNeighbours(v);
			Grph GBarNv = g.getSubgraphInducedByVertices(Nv).getComplement();
			if(GBarNv.getNumberOfTriangles() == 0)
				return v;
		}
		
		return -1;
	}

	/**
	 * Computes the graph obtained from <code>g</code> by folding vertex <code>v</code>.
	 * The parameter <code>hashmap</code> allows to retrieve what vertices have been folded. 
	 * @param g
	 * @param v
	 * @param hashmap 
	 * @return
	 */
	private Grph getFoldedGraph(Grph g, int v, IntObjectMap<IntSet> hashmap) {
		int[] Nv = g.getNeighbours(v).toIntArray();
		Grph foldedG = g.clone();
		foldedG.removeVertex(v);
		 
		IntSet newv = new DefaultIntSet();
		
		for(int i = 0; i < Nv.length - 1; i++)
			for(int j = i + 1; j < Nv.length; j++) {
				int nvi = Nv[i];
				int nvj = Nv[j];
				
				if(!g.areVerticesAdjacent(nvi, nvj)) {
					// Step 1
					int newVertex = foldedG.addVertex();
					
					IntSet s = new DefaultIntSet();
					s.addAll(nvi,nvj);
					hashmap.put(newVertex, s);
					
					// Step 2 
					for(int ni : foldedG.getNeighbours(nvi).toIntArray())
						foldedG.addUndirectedSimpleEdge(newVertex, ni);
					
					for(int nj : foldedG.getNeighbours(nvj).toIntArray())
						if(!foldedG.areVerticesAdjacent(newVertex, nj))
							foldedG.addUndirectedSimpleEdge(newVertex, nj);
					
					// For Step 3
					newv.add(newVertex);
				}
			}
		
		int[] tabnewv = newv.toIntArray();
		for(int i = 0; i < tabnewv.length - 1; i++)
			for(int j = i + 1; j < tabnewv.length; j++)
				if(!foldedG.areEdgesAdjacent(i, j))
					foldedG.addUndirectedSimpleEdge(i, j);
		
		foldedG.removeVertices(Nv);
		
		return foldedG;
	}

	/**
	 * Computes the graph obtained from <code>g</code> by applying struction with vertex <code>v0</code>
	 * as center.
	 * The parameter <code>map</code> allows to retrieve what vertices have been merged during the struction. 
	 * @param g
	 * @param v
	 * @param hashmap 
	 * @return
	 */
	private static Grph struction(Grph g, int v0, IntObjectMap<int[]> map) {
		Grph newg = g.clone();
		
		if(g.getNumberOfVertices() == 0)
			return newg;
		
		int[] nv0 = newg.getNeighbours(v0).toIntArray();
		Arrays.sort(nv0);

		for(int i = 0; i < nv0.length - 1; i++)
			for(int j = i + 1; j < nv0.length; j++) {
				if(!newg.areVerticesAdjacent(nv0[i], nv0[j])) {
					int newv = newg.addVertex();
					newg.addVertex(newv);
					map.put(newv, new int[] {nv0[i], nv0[j]});
					
					for(int nvi : g.getNeighbours(nv0[i]).toIntArray())
						newg.addUndirectedSimpleEdge(newv, nvi);
					
					for(int nvj : g.getNeighbours(nv0[j]).toIntArray())
						if(!newg.areVerticesAdjacent(newv, nvj))
							newg.addUndirectedSimpleEdge(newv, nvj);
				}
			}
		
		int[] newVertices = map.keys().toArray(); 
		for(int ij = 0; ij < newVertices.length - 1; ij++)
			for(int kl = ij + 1; kl < newVertices.length; kl++)
				if(map.get(newVertices[ij])[0] != map.get(newVertices[kl])[0] || g.areVerticesAdjacent(map.get(newVertices[ij])[1], map.get(newVertices[kl])[1]))
					newg.addUndirectedSimpleEdge(newVertices[ij], newVertices[kl]);
		
		newg.removeVertex(v0);
		newg.removeVertices(nv0);
		
		return newg;
	}
	
	// Comparisons of the three algorithms
	public static void main(String[] args) {
		FominGrandoniKratschMaximumindependentSetAlgorithm falg = new FominGrandoniKratschMaximumindependentSetAlgorithm();
		int NUMBER_OF_VERTICES = 75;
		double GNP_PROBABILITY = 0.4;
		
		int NUMBER_OF_GRAPHS = 20;
		
		GNPTopologyGenerator gnp = new GNPTopologyGenerator();
		gnp.setPRNG(new Random(1));
		gnp.setProbability(GNP_PROBABILITY);
		gnp.setAcceptLoops(false);
		
		for(int i = 0; i < NUMBER_OF_GRAPHS; i++) {
			Grph g = new InMemoryGrph();
			g.addNVertices(NUMBER_OF_VERTICES);
			gnp.compute(g);

			System.out.println("*********************************************************");
			System.out.println("Graph " + i);
			
			// Fomin, Grandoni, Kratsch
			long debFGK = System.currentTimeMillis();
			IntSet misFGK = falg.FominGrandoniKratschAlgorithm(g);
			long finFGK = System.currentTimeMillis();
			long dureeFGK = finFGK - debFGK;
			System.out.println("MIS FGK :         " + misFGK + " Durée : " + dureeFGK);
			
			long debFGK2 = System.currentTimeMillis();
			IntSet misFGK2 = falg.FominGrandoniKratschAlgorithmDeg2(g);
			long finFGK2 = System.currentTimeMillis();
			long dureeFGK2 = finFGK2 - debFGK2;
			System.out.println("MIS FGK :         " + misFGK2 + " Durée : " + dureeFGK2);
			
			long debFGKstruc = System.currentTimeMillis();
			IntSet misFGKstruc = falg.FominGrandoniKratschAlgorithmStruction(g);
			long finFGKstruc = System.currentTimeMillis();
			long dureeFGKstruc = finFGKstruc - debFGKstruc;
			System.out.println("MIS FGK struc :   " + misFGKstruc + " Durée : " + dureeFGKstruc);
		}
	}
}
