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
 
 package grph.algo.ogdf;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import grph.Grph;
import grph.algo.topology.ClassicalGraphs;
import grph.in_memory.InMemoryGrph;
import grph.io.AbstractGraphReader;
import grph.io.GraphBuildException;
import grph.io.ParseException;
import toools.UnitTests;

public class OGDFReader extends AbstractGraphReader
{

    @Override
    public Grph readGraph(InputStream is) throws ParseException, IOException, GraphBuildException
    {
	Grph g = new InMemoryGrph();
	BufferedReader br = new BufferedReader(new InputStreamReader(is));

	int nbVertices = Integer.valueOf(br.readLine());

	for (int i = 0; i < nbVertices; ++i)
	{
	    int v = Integer.valueOf(br.readLine());
	    g.addVertex(v);
	}

	int nbEdges = Integer.valueOf(br.readLine());

	for (int i = 0; i < nbEdges; ++i)
	{
	    int v1 = Integer.valueOf(br.readLine());
	    int v2 = Integer.valueOf(br.readLine());
	    g.addUndirectedSimpleEdge(v1, v2);
	}

	return g;
    }

    
    private static void test() throws ParseException, GraphBuildException
    {
	Grph g = ClassicalGraphs.PetersenGraph();
	Grph h = new OGDFReader().readGraph(new OGDFWriter().printGraph(g));
	UnitTests.ensure(g.equals(h));
    }
}
