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
 
 package grph.io;

import java.io.IOException;
import java.io.PrintStream;

import grph.Grph;
import grph.properties.Property;

public class RDFTurtleWriter extends AbstractGraphTextWriter
{

    @Override
    public void printGraph(Grph g, PrintStream printStream) throws IOException
    {
	printStream.println("@prefix grph: <http://www-sop.inria.fr/members/Luc.Hogie/grph/>");
/*
	for (int v : g.getVertices().toIntArray())
	{
	    printStream.print("grph:node" + v);
	    printStream.print(" grph:colored ");

	    for (Property p : g.getProperties())
	    {
		if (p.getTarget() == TYPE.vertex)
		{

		}
	    }
	    printStream.print(g.getVertexColorProperty().getValue(v));
	    printStream.print(" .\n");
	}
*/
/*	for (int e : g.getEdges().toIntArray())
	{
	    int s = g.getOneVertex(e);
//	    printStream.append("grph:edge" + e);
	    printStream.append("_:b" + (e+1));
	    printStream.append(" grph:starts_from");
	    printStream.print(" grph:node" + s);
	    printStream.append(" .\n");

	    int t = g.getTheOtherVertex(e, s);
	    printStream.append("_:b" + (e+1));
	    printStream.append(" grph:heads_to");
	    printStream.print(" grph:node" + t);
	    printStream.append(" .\n");
	}*/

	for (int e : g.getEdges().toIntArray())
	{
	    int s = g.getOneVertex(e);
	    printStream.print("_:node" + s);
//	    printStream.append("grph:edge" + e);
	    printStream.append(" grph:edge");
	    int t = g.getTheOtherVertex(e, s);
	    printStream.print(" _:node" + t);
	    printStream.append(" .\n");
	}
}

    private void print(int element, Property p, PrintStream printStream)
    {
	String value = p.getValueAsString(element);

	if (value != null)
	{
	    printStream.print(element);
	    printStream.print(' ');
	    printStream.print("grph:" + p.getName());
	    printStream.print(' ');
	    printStream.print(value);
	    printStream.print(' ');
	    printStream.print('.');
	    printStream.print('\n');
	}
    }

    
    public static void main(String[] args)
    {
	Grph g = new grph.in_memory.InMemoryGrph();
	g.addNVertices(3);
	g.dring();	System.out.println(new RDFTurtleWriter().printGraph(g));
    }

}
