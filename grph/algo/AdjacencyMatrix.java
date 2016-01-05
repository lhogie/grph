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
 
 package grph.algo;

import grph.Grph;
import grph.algo.topology.ClassicalGraphs;

import java.util.Arrays;

import toools.math.IntMatrix;
import toools.text.TextUtilities;

import com.carrotsearch.hppc.IntIntMap;
import com.carrotsearch.hppc.IntIntOpenHashMap;
import com.carrotsearch.hppc.cursors.IntCursor;

/**
 * A class that implements the notion of adjacency matrix of a graph.
 *  
 * @author Gregory Morel
 */
public class AdjacencyMatrix{
	private IntMatrix m; 
	private IntIntMap indicesToVertices;
	private IntIntMap verticesToIndices;
	
	public AdjacencyMatrix(Grph g) {
		int size = g.getNumberOfVertices();
		m = new IntMatrix(size, size);
		
		int[] vertices = g.getVertices().toIntArray();
		Arrays.sort(vertices);
		
		indicesToVertices = new IntIntOpenHashMap();
		verticesToIndices = new IntIntOpenHashMap();
		
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
		indicesToVertices = new IntIntOpenHashMap();
		verticesToIndices = new IntIntOpenHashMap();
		
		for(IntCursor c : original.indicesToVertices.keys()) {
			int key = c.value;
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
