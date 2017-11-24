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
 
 

package grph.algo.partitionning.metis;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import grph.Grph;
import grph.algo.labelling.Incrementlabelling;
import grph.algo.labelling.Relabelling;
import grph.in_memory.InMemoryGrph;
import it.unimi.dsi.fastutil.ints.IntSet;
import toools.UnitTests;
import toools.collections.primitive.SelfAdaptiveIntSet;
import toools.collections.primitive.LucIntSet;
import toools.extern.ExternalProgram;
import toools.extern.Proces;
import toools.io.file.Directory;
import toools.io.file.RegularFile;
import toools.log.Logger;
import toools.log.StdOutLogger;
import toools.net.NetUtilities;
import toools.text.TextUtilities;

public class Gpmetis
{
	public final static RegularFile CMD = new RegularFile(Grph.COMPILATION_DIRECTORY, "metis-5.0.2/build/programs/gpmetis");

	public static void ensureCompiled(Logger logger)
	{
		if (!CMD.exists())
		{
			try
			{
				ExternalProgram.ensureCommandsAreAvailable("gcc", "make", "cmake");
				Directory srcDir = CMD.getParent().getParent().getParent();
				RegularFile sourceArchive = new RegularFile(srcDir.getPath() + ".tar.gz");

				if (!sourceArchive.exists())
				{
					logger.log("Downloading http://glaros.dtc.umn.edu/gkhome/fetch/sw/metis/metis-5.0.2.tar.gz");
					sourceArchive.setContent(NetUtilities.retrieveURLContent("http://glaros.dtc.umn.edu/gkhome/fetch/sw/metis/metis-5.0.2.tar.gz"));
				}

				logger.log(srcDir.getPath());
				logger.log("Unarchiving");
				Proces.exec("tar", sourceArchive.getParent(), "xzf", "metis-5.0.2.tar.gz");
				logger.log("Configuring");

				logger.log("replaces the architecture-dependant target directory by one fixed");
				RegularFile makeFile = new RegularFile(srcDir, "Makefile");
				String makeText = new String(makeFile.getContent());
				makeText = makeText.replace("BUILDDIR = build/$(systype)-$(cputype)", "BUILDDIR = build");
				makeFile.setContent(makeText.getBytes());

				Proces.exec("make", srcDir, "config");
				logger.log("Compiling");
				Proces.exec("make", srcDir);
				logger.log("Done");
			}
			catch (IOException e)
			{
				throw new IllegalStateException(e);
			}
		}
	}

	public enum Ptype {
		rb, // Recursive bisectioning
		kway
		// Direct k-way partitioning [default]
	}

	public enum Ctype {
		rm, // Random matching
		shem
		// Sorted heavy-edge matching [default]
	}

	public enum Iptype {
		grow, // Grow a bisection using a greedy scheme [default for ncon=1]
		random
		// Compute a bisection at random [default for ncon>1]
	}

	public enum Objtype {
		cut, // Minimize the edgecut [default]
		vol
		// Minimize the total communication volume
	}

	/**
	 * 
	 * @param g
	 * @param numberOfPartitions
	 * @param ptype
	 *            Specifies the scheme to be used for computing the k-way
	 *            partitioning.
	 * @param ctype
	 *            Specifies the scheme to be used to match the vertices of the
	 *            graph during the coarsening.
	 * @param iptype
	 *            Specifies the scheme to be used to compute the initial
	 *            partitioning of the graph. applies only when ptype=rb.
	 * @param objtype
	 *            Specifies the objective that the partitioning routines will
	 *            optimize. applies only when ptype=kway.
	 * @param contig
	 *            Specifies that the partitioning routines should try to produce
	 *            partitions that are contiguous. Note that if the input graph
	 *            is not connected this option is ignored. Applies only when
	 *            ptype=kway.
	 * @param minconn
	 *            Specifies that the partitioning routines should try to
	 *            minimize the maximum degree of the subdomain graph, i.e., the
	 *            graph in which each partition is a node, and edges connect
	 *            subdomains with a shared interface. Applies only when
	 *            ptype=kway.
	 * @param ufactor
	 *            Specifies the maximum allowed load imbalance among the
	 *            partitions. A value of x indicates that the allowed load
	 *            imbalance is 1+x/1000. For ptype=rb, the load imbalance is
	 *            measured as the ratio of the 2*max(left,right)/(left+right),
	 *            where left and right are the sizes of the respective
	 *            partitions at each bisection. For ptype=kway, the load
	 *            imbalance is measured as the ratio of max_i(pwgts[i])/avgpwgt,
	 *            where pwgts[i] is the weight of the ith partition and avgpwgt
	 *            is the sum of the total vertex weights divided by the number
	 *            of partitions requested. For ptype=rb, the default value is 1
	 *            (i.e., load imbalance of 1.001). For ptype=kway, the default
	 *            value is 30 (i.e., load imbalance of 1.03).
	 * @param niter
	 *            Specifies the number of iterations for the refinement
	 *            algorithms at each stage of the uncoarsening process. Default
	 *            is 10.
	 * @param ncuts
	 *            Specifies the number of different partitionings that it will
	 *            compute. The final partitioning is the one that achieves the
	 *            best edgecut or communication volume. Default is 1.
	 * @param seed
	 *            Selects the seed of the random number generator.
	 * @param dbglvl
	 *            Selects the dbglvl.
	 * @return
	 */
	public List<IntSet> compute(Grph g, int numberOfPartitions, Random r)
	{
		return compute(g, numberOfPartitions, Ptype.kway, Ctype.shem, Iptype.random, Objtype.cut, false, false, 30, 10, 1, r);
	}

	public List<IntSet> compute(Grph g, int numberOfPartitions, Ptype ptype, Ctype ctype, Iptype iptype, Objtype objtype, boolean contig, boolean minconn,
			int ufactor, int niter, int ncuts, Random r)
	{
		ensureCompiled(StdOutLogger.SYNCHRONIZED_INSTANCE);

		Grph h = relabelIfNecessary(g);

		try
		{
			// write the file that will be given to METIS
			RegularFile inFile = new RegularFile(Directory.getSystemTempDirectory(), "metis-input-file");
			new MetisWriter().writeGraph(h, inFile);
			// Proces.TRACE_CALLS = true;
			// call the METIS process

			Collection<String> parms = new ArrayList<String>(Arrays.asList("-ptype=" + ptype.name(), "-ctype=" + ctype.name(), "-iptype=" + iptype.name(),
					"-objtype=" + objtype.name()));

			if (contig)
			{
				parms.add("-contig");
			}

			if (minconn)
			{
				parms.add("-minconn");
			}

			parms.addAll(Arrays.asList("-ufactor=" + ufactor, "-niter=" + niter, "-ncuts=" + ncuts, "-seed=" + r.nextInt(), inFile.getName(), ""
					+ numberOfPartitions));

			byte[] stdout = Proces.exec(CMD.getPath(), inFile.getParent(), parms.toArray(new String[0]));

			inFile.delete();
			// System.out.println(new String(stdout));

			// retrieves the content from the file generated by METIS
			RegularFile outFile = new RegularFile(inFile.getParent(), inFile.getName() + ".part." + numberOfPartitions);
			// System.out.println(new String(outFile.getContent()));
			List<String> lines = TextUtilities.splitInLines(new String(outFile.getContent()));
			outFile.delete();

			// builds the partitionning
			List<IntSet> sets = createEmptySets(numberOfPartitions);

			for (int l = 0; l < lines.size(); ++l)
			{
				// in Metis, assignment for vertex 1 is at line 0
				int v = l + 1;

				// if the graph was relabelled, we need to restore the original
				// labelling
				if (g != h)
				{
					--v;
				}
				// System.out.println("line:" + lines.get(l) + "$");
				int setIndex = Integer.valueOf(lines.get(l));
				IntSet set = sets.get(setIndex);
				set.add(v);
			}

			return sets;
		}
		catch (IOException e)
		{
			throw new IllegalStateException(e);
		}
	}

	private Grph relabelIfNecessary(Grph g)
	{
		if (g.containsVertex(0))
		{
			Relabelling rl = new Incrementlabelling(1);
			return rl.compute(g);
		}
		else
		{
			return g;
		}
	}

	protected List<IntSet> createEmptySets(int numberOfPartitions)
	{
		List<IntSet> sets = new ArrayList<IntSet>();

		for (int i = 0; i < numberOfPartitions; ++i)
		{
			sets.add(new SelfAdaptiveIntSet());
		}

		return sets;
	}

	
	
	public static void main2(String[] args)
	{
		Grph g = new InMemoryGrph();
		// g.createVertices(30);
		// g.glp();
		g.grid(10, 10);
		Collection<IntSet> sets = new Gpmetis().compute(g, 10, new Random());
		g.highlightVertices(sets);
		g.display();

	}

	private static void test()
	{
		Grph g = new InMemoryGrph();
		g.grid(10, 10);
		Collection<IntSet> sets = new Gpmetis().compute(g, 10, new Random());
		UnitTests.ensure(sets.size() == 10);
	}

}
