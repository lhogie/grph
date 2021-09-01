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
 
 package grph.report;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;

import toools.extern.Proces;
import toools.io.file.Directory;
import toools.io.file.RegularFile;

public class LatexDocument
{
    private final Directory location;
    private final Collection<RegularFile> files = new HashSet<>();
    private final StringBuffer latex = new StringBuffer();

    public LatexDocument(Directory location)
    {
	if (location.exists())
	    throw new IllegalArgumentException("directory already exists: " + location.getPath());

	location.mkdirs();
	this.location = location;
    }

    public Directory getLocation()
    {
	return location;
    }

    public Collection<RegularFile> getFiles()
    {
	return files;
    }

    public StringBuffer getLatex()
    {
	return latex;
    }

    public void addFile(String name, byte[] data) throws IOException
    {
	RegularFile f = new RegularFile(location, name);
	f.setContent(data);
	files.add(f);
    }

    public RegularFile compile(boolean tableOfContents, boolean bibtex) throws IOException
    {
	// System.out.println(latex.toString());
	RegularFile latexFile = new RegularFile(location, "document.tex");
	latexFile.setContent(latex.toString().getBytes());
	Proces.exec("pdflatex", location, "document");
	
	if (tableOfContents)
	{
	    Proces.exec("pdflatex", location, "document");
	}

	return new RegularFile(location, "document.pdf");
    }
}
