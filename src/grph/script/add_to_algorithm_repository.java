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

    public add_to_algorithm_repository(RegularFile launcher)
	{
		super(launcher);
		// TODO Auto-generated constructor stub
	}

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
	new add_to_algorithm_repository(null).run("-d", "/Users/lhogie/dev/src/jalinopt/bin", "-l", "coucou");
    }
}
