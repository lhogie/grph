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
    private final Collection<RegularFile> files = new HashSet();
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
