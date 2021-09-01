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
Julien Deantoni (I3S, Université Cote D'Azur, Saclay) 

*/
 
 
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
 
 package grph.algo.search;
import grph.Grph;
import grph.GrphAlgorithm;
import grph.algo.MultiThreadProcessing;
import grph.algo.distance.DistanceMatrixBasedDiameterAlgorithm;
import grph.in_memory.InMemoryGrph;
import toools.StopWatch;
import toools.math.MathsUtilities;

/**
 * compute the diameter via n*bfs without storing the matrices
 * 
 * @author lhogie
 * 
 */
public class DistanceListsBasedDiameterAlgorithm extends GrphAlgorithm<Integer>
{
    class A
    {
	int n = -1;
    }

    @Override
    public Integer compute(final Grph graph)
    {
	assert graph != null;
	final A a = new A();

	new MultiThreadProcessing(graph.getVertices()) {

	    @Override
	    protected void run(int threadID, int source)
	    {
	    	SearchResult r = new BFSAlgorithm().compute(graph, source);
		int max = MathsUtilities.max(r.distances);

		if (max > a.n)
		{
		    a.n = max;
		}
	    }
	};

	return a.n;
    }

    public static void main(String[] args)
    {
	Grph g = new InMemoryGrph();
	g.getOutNeighborhoods();
	int n = 50;
	g.grid(n, n);
	StopWatch sw = new StopWatch();
	System.out.println(new DistanceMatrixBasedDiameterAlgorithm().compute(g));
	System.out.println(sw.getElapsedTime() + "ms");
	sw.reset();
	System.out.println(new DistanceListsBasedDiameterAlgorithm().compute(g));
	System.out.println(sw.getElapsedTime() + "ms");
    }
}
