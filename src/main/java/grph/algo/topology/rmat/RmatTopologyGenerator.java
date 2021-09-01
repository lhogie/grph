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
 
 package grph.algo.topology.rmat;

import java.io.IOException;

import grph.Grph;
import grph.algo.topology.TopologyGenerator;
import grph.in_memory.InMemoryGrph;
import grph.io.EdgeListReader;
import toools.extern.ExternalProgram;
import toools.extern.Proces;
import toools.io.JavaResource;
import toools.io.file.Directory;
import toools.io.file.RegularFile;

public class RmatTopologyGenerator implements TopologyGenerator
{

    private double a, b, c;
    private int s, s2, e;

    public double getA()
    {
	return a;
    }

    public void setA(double a)
    {
	this.a = a;
    }

    public double getB()
    {
	return b;
    }

    public void setB(double b)
    {
	this.b = b;
    }

    public double getC()
    {
	return c;
    }

    public void setC(double c)
    {
	this.c = c;
    }

    public int getS()
    {
	return s;
    }

    public void setS(int s)
    {
	this.s = s;
    }

    public int getS2()
    {
	return s2;
    }

    public void setS2(int s2)
    {
	this.s2 = s2;
    }

    public int getE()
    {
	return e;
    }

    public void setE(int e)
    {
	this.e = e;
    }

    @Override
    public void compute(Grph g)
    {
	ensureCompiled();

	Directory d = Directory.getSystemTempDirectory();

	// run the program
	Proces.exec(executable.getPath(), d, "" + getA(), "" + getB(), "" + getC(), "" + getS(), "" + getS2(), ""
		+ getE());

	// the output if a file called "edges"
	// we need to read it and alter the graph with it
	// then delete it
	RegularFile f = new RegularFile(d, "edges");

	try
	{
	    EdgeListReader.alterGraph(g, f.createReadingStream(), true, false, g.getEdgeWidthProperty());
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	    throw new IllegalStateException();
	}

	f.delete();
    }

    private static final RegularFile executable = new RegularFile(Grph.COMPILATION_DIRECTORY, "RmatTopologyGenerator");

    private static void ensureCompiled()
    {
	if (!executable.exists())
	{
	    JavaResource res = new JavaResource(RmatTopologyGenerator.class, "RmatTopologyGenerator.c");

	    try
	    {
		ExternalProgram.compileCSource(res, Grph.COMPILATION_DIRECTORY, Grph.logger);
	    }
	    catch (IOException e)
	    {
		throw new IllegalStateException("cannot compile " + res.getPath());
	    }
	}
    }

    public static void rmat(Grph g, int n, double a, double b, double c, int e, int s, int s2)
    {
	g.ensureNVertices(n);
	RmatTopologyGenerator tg = new RmatTopologyGenerator();
	tg.setA(a);
	tg.setB(b);
	tg.setC(c);
	tg.setE(e);
	tg.setS(s);
	tg.setS2(s2);
	tg.compute(g);
    }

    public static void main(String[] args)
    {
	Grph g = new InMemoryGrph();
	rmat(g, 5, 0.4, 0.1, 0.24, 400, 6, 12);
	System.out.println(g);
    }

}
