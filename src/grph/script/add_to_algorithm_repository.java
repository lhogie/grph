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
 
 package grph.script;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java4unix.CommandLine;
import java4unix.OptionSpecification;

import toools.extern.Proces;
import toools.io.file.Directory;
import toools.io.file.RegularFile;
import toools.net.NetUtilities;

public class add_to_algorithm_repository extends AbstractGrphScript
{

    @Override
    protected void declareOptions(Collection<OptionSpecification> optionSpecifications)
    {
	optionSpecifications.add(new OptionSpecification("--directory", "-d", ".*", null, "the directory containing the class files of the algorithms"));
	optionSpecifications.add(new OptionSpecification("--lib", "-l", ".*", null, "the name of the library"));

    }

    @Override
    public int runScript(CommandLine cmdLine) throws Throwable
    {
	String directoryName = getOptionValue(cmdLine, "-d");
	String jarName = getOptionValue(cmdLine, "-l") + ".graphlib";
	Directory d = new Directory(directoryName);
	RegularFile jarFile = new RegularFile(d, jarName);
	printMessage("building library file " + jarFile.getPath());
	Proces.exec("jar", d, "cf", jarName, ".");
//	printMessage(new String(ExternalProgram.execNoFail("jar", d, "tf", jarName)));

	printMessage("Uploading it to Grph algorithms repository...");
	Map<String, String> args = new HashMap<String, String>();
	args.put("jarName", jarName);
	NetUtilities.retrieveURLContent("http://www-sop.inria.fr/members/Luc.Hogie/grph/algo/post.php", args,
		jarFile.getContent());
	jarFile.delete();

	printMessage("Completed");
	return 0;
    }

    @Override
    public String getShortDescription()
    {
	return "Releases the given classes to the Grph algorithms repository";
    }

    public static void main(String[] args) throws Throwable
    {
	new add_to_algorithm_repository().run("-d", "/Users/lhogie/dev/src/jalinopt/bin", "-l", "coucou");
    }
}
