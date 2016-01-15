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

package grph.algo.ogdf;

import grph.Grph;
import grph.GrphAlgorithm;

import java.io.IOException;

import toools.extern.ExternalProgram;
import toools.extern.Proces;
import toools.io.JavaResource;
import toools.io.file.Directory;
import toools.io.file.RegularFile;
import toools.net.NetUtilities;

public abstract class OGDFAlgorithm<R> extends GrphAlgorithm<R>
{
	private static RegularFile executable = new RegularFile(Grph.COMPILATION_DIRECTORY,
			"OGDF_frontend");

	public static void ensureCompiled()
	{
		if ( ! executable.exists())
		{
			try
			{
				Directory srcDirectory = new Directory(Grph.COMPILATION_DIRECTORY, "OGDF");

				if ( ! new RegularFile(srcDirectory, "_release/libOGDF.a").exists())
				{
					// downloading the source code
					RegularFile archive = new RegularFile(Grph.COMPILATION_DIRECTORY,
							"tech:ogdf.v2012.07.zip");
					Grph.logger.log("downloading OGDF to "
							+ Grph.COMPILATION_DIRECTORY.getPath());
					archive.setContent(NetUtilities
							.retrieveURLContent("http://www.ogdf.net/lib/exe/fetch.php/tech:ogdf.v2012.07.zip"));

					// unzipping
					Grph.logger.log("Unzipping OGDF...");

					if (srcDirectory.exists())
						srcDirectory.deleteRecursively();

					ExternalProgram.unarchiveInPlace(archive, Grph.logger);

					// configuring
					Grph.logger.log("Configuring OGDF...");
					Proces.exec("./makeMakefile.sh", srcDirectory);

					// compiling
					Grph.logger.log("compiling OGDF...");
					Proces.exec("make", srcDirectory);
				}

				// extracting the C++ front-end code
				Grph.logger.log("compiling OGDF-Grph front-end...");
				JavaResource res = new JavaResource(OGDFAlgorithm.class,
						"OGDF_frontend.cpp");
				res.exportToFile(new RegularFile(Grph.COMPILATION_DIRECTORY,
						"OGDF_frontend.cpp"));

				// compiling the front-end
				Proces.exec("g++", Grph.COMPILATION_DIRECTORY, "-I", "OGDF", "-O3",
						"OGDF_frontend.cpp", "-o", "OGDF_frontend", "-LOGDF/_release",
						"-lOGDF", "-pthread");

				Grph.logger.log("done");
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}

	@Override
	public R compute(Grph g)
	{
		ensureCompiled();
		byte[] stdin = new OGDFWriter().writeGraph(g);
		byte[] stdout = Proces.exec(executable.getPath(), stdin, getAlgorithmName());
		return processStdout(new String(stdout));
	}

	protected abstract R processStdout(String string);

	protected abstract String getAlgorithmName();
}
