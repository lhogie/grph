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
 
 package grph.algo;

import java.util.Arrays;

import grph.Grph;
import grph.algo.topology.ClassicalGraphs;
import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import toools.math.IntMatrix;
import toools.text.TextUtilities;

/**
 * A class that implements the notion of adjacency matrix of a graph.
 *  
 * @author Gregory Morel
 */
public class AdjacencyMatrix{
	private IntMatrix m; 
	private Int2IntMap indicesToVertices;
	private Int2IntMap verticesToIndices;
	
	public AdjacencyMatrix(Grph g) {
		int size = g.getNumberOfVertices();
		m = new IntMatrix(size, size);
		
		int[] vertices = g.getVertices().toIntArray();
		Arrays.sort(vertices);
		
		indicesToVertices = new Int2IntOpenHashMap();
		verticesToIndices = new Int2IntOpenHashMap();
		
		int index = 0;
		for(int v : vertices) {
			indicesToVertices.put(index, v);
			verticesToIndices.put(v, index);
			index++;
		}

		for (int src : vertices)
			for (int dest : vertices) {
				int i = verticesToIndices.get(src);
				int j = verticesToIndices.get(dest);
				m.array[i][j] = g.getEdgesConnecting(src, dest).size();
			}	
	}
	
	private AdjacencyMatrix(AdjacencyMatrix original) {
		m = new IntMatrix(original.m.array);
		indicesToVertices = new Int2IntOpenHashMap();
		verticesToIndices = new Int2IntOpenHashMap();
		
		for(Integer c : original.indicesToVertices.keySet()) {
			int key = c;
			int v = original.indicesToVertices.get(key);
			indicesToVertices.put(key, v);
			verticesToIndices.put(v, key);
		}
	}
	
	public int getSize() {
		return m.width;
	}
	
	public int get(int i, int j) {
		return m.array[i][j];
	}
	
	public int getVertexFromMatrixIndex(int index)  {
		return indicesToVertices.get(index);
	}
	
	public int getMatrixIndexFromVertex(int vertex) {
		return verticesToIndices.get(vertex);
	}
	
	public static AdjacencyMatrix power(AdjacencyMatrix A, int k) {
		AdjacencyMatrix R = new AdjacencyMatrix(A);
		
		for(int i = 1; i < k; i++)
			R.m = IntMatrix.multiplication(R.m, A.m);
				
		return R;
	}

	@Override
	public String toString()  {
		StringBuilder s = new StringBuilder();
		
		// we look for the greatest entry of the matrix
		int maxValue = Integer.MIN_VALUE;
		
		for(int i = 0; i < m.width; i++) {
			maxValue = Math.max(maxValue, indicesToVertices.get(i));
			for(int j = 0; j < m.width; j++)
				maxValue = Math.max(maxValue, m.array[i][j]);	
		}
		
		int nbCharMaxValue = String.valueOf(maxValue).length();
		s.append(TextUtilities.repeat(' ', nbCharMaxValue + 2));
		
		for(int i = 0; i < m.width; i++) {
			int v = indicesToVertices.get(i);
			s.append(TextUtilities.repeat(' ', nbCharMaxValue - String.valueOf(v).length()));
			s.append(v + " ");
		}
		
		s.append("\n" + TextUtilities.repeat('-', (nbCharMaxValue + 1) * (m.width + 1)) + "\n");
					
		for (int i = 0; i < m.width; ++i) {
			int v = indicesToVertices.get(i);
			s.append(TextUtilities.repeat(' ', nbCharMaxValue - String.valueOf(v).length()));
			s.append(v + " |");
			
			for(int j = 0; j < m.width; j++) {
				s.append(TextUtilities.repeat(' ', nbCharMaxValue - String.valueOf(m.array[i][j]).length()));
				s.append(m.array[i][j] + " ");			
			}
	
			s.append('\n');
		}

		return s.toString();
	}
	
	public static void main(String[] args) {
		Grph g = ClassicalGraphs.path(5);
		System.out.println(AdjacencyMatrix.power(g.getAdjacencyMatrix(), 3));
	}
}
