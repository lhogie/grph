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

import grph.Grph;
import grph.io.GraphvizImageWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import toools.io.file.Directory;
import toools.io.file.RegularFile;
import toools.log.Logger;
import toools.math.Distribution;
import toools.math.IntMatrix;
import toools.text.TextUtilities;

public class Report
{
    private final Grph g;
    private final Collection<Metric> properties = new ArrayList<Metric>();

    public Report(Grph g)
    {
	this.g = g;

	properties.add(new Metric("is the graph cyclic", g.isCyclic()));
	properties.add(new Metric("is the graph complete", g.isComplete()));
	properties.add(new Metric("is the graph connected", g.isConnected()));
	properties.add(new Metric("is the graph a digraph", g.isDirected()));
	properties.add(new Metric("is the graph directed", g.isDirected()));
	properties.add(new Metric("is the graph an hypergraph", g.isHypergraph()));
	properties.add(new Metric("is the graph a tree", g.isTree()));
	properties.add(new Metric("is the graph irreflexive", g.isIrreflexive()));
	properties.add(new Metric("is the graph mixed", g.isMixed()));
	properties.add(new Metric("is the graph regular", g.isRegular()));
	properties.add(new Metric("is the graph null", g.isNull()));
	properties.add(new Metric("is the graph simple", g.isSimple()));
	properties.add(new Metric("is the graph trivial", g.isTrivial()));
	properties.add(new Metric("is the graph a multigraph", g.hasMultipleEdges()));
	properties.add(new Metric("is the graph an antigraph", g.isAntigraph()));

	properties.add(new Metric("number of vertices", g.getVertices().size()));
	properties.add(new Metric("number of edges", g.getEdges().size()));
	properties.add(new Metric("avg degree", g.getAverageDegree()));

	properties.add(new Metric("avg out degree", g.getAverageDegree(Grph.TYPE.vertex, Grph.DIRECTION.out)));

	properties.add(new Metric("clustering coefficient", g.getAverageClusteringCoefficient()));

	properties.add(new Metric("clustering coefficient distribution", g.getClusteringCoefficientDistribution()));

	properties.add(new Metric("number of connected components", g.getConnectedComponents().size()));

	if (g.isConnected())
	{
	    properties.add(new Metric("diameter", g.getDiameter()));
	}

	properties.add(new Metric("distance distribution", g.getDistanceMatrix(null).getDistribution()));

	properties.add(new Metric("number of inacessible vertices", g.getInaccessibleVertices()));

	properties.add(new Metric("number of triangles", g.getNumberOfTriangles()));
	properties.add(new Metric("minimum vertex cover", g.getMinimumVertexCover().size()));

	properties.add(new Metric("radius", g.getRadius()));

	properties.add(new Metric("density", g.getDensity()));
	properties.add(new Metric("topological distance matrix", g.getDistanceMatrix(null)));
	properties.add(new Metric("degree distribution", g.getDegreeDistribution(Grph.TYPE.vertex, Grph.DIRECTION.in_out),
		"the out-vertex degree distribution of the graph\nEach line gives the number of occurence of a given instance"));
    }

    public Grph getGrph()
    {
	return g;
    }

    public Collection<Metric> getProperties()
    {
	return properties;
    }

    public Collection<Metric> findBooleanProperties()
    {
	Collection<Metric> r = new ArrayList<Metric>();

	for (Metric p : properties)
	{
	    if (p.getValue() instanceof Boolean)
	    {
		r.add(p);
	    }
	}

	return r;
    }

    public Collection<Metric> findScalarProperties()
    {
	Collection<Metric> r = new ArrayList<Metric>();

	for (Metric p : properties)
	{
	    if (p.getValue() instanceof Number)
	    {
		r.add(p);
	    }
	}

	return r;
    }

    public Collection<Metric> findDistributionProperties()
    {
	Collection<Metric> r = new ArrayList<Metric>();

	for (Metric p : properties)
	{
	    if (p.getValue() instanceof Distribution)
	    {
		r.add(p);
	    }
	}

	return r;
    }

    public Collection<Metric> findMatrixProperties()
    {
	Collection<Metric> r = new ArrayList<Metric>();

	for (Metric p : properties)
	{
	    if (p.getValue() instanceof IntMatrix)
	    {
		r.add(p);
	    }
	}

	return r;
    }

    public static void  storeForAurelien(Report report, Directory destination, Logger logger, PropertyFilenameCalculator pfc)
	    throws IOException
    {
	if (!destination.exists())
	{
	    destination.mkdirs();
	}

	for (Metric p : report.getProperties())
	{
	    RegularFile f = new RegularFile(destination, pfc.computeFilename(p));
	    logger.log("writing file " + f.getPath());
	    String comment = p.getComment() == null ? "# " + p.getName() : TextUtilities.prefixEachLineBy(
		    p.getComment(), "# ");
	    f.setContent((comment + "\n\n" + p.getValue().toString()).getBytes());
	}
    }

    public String computeTextualReport()
    {
	StringBuilder b = new StringBuilder();


	for (Metric p : findBooleanProperties())
	{
	    b.append(p.getName() + ": " + p.getValue().toString());

	    if (p.getComment() != null)
	    {
		b.append(" (" + p.getComment() + ")");
	    }

	    b.append('\n');
	}
	
	for (Metric p : findScalarProperties())
	{
	    b.append(p.getName() + ": " + p.getValue().toString());

	    if (p.getComment() != null)
	    {
		b.append(" (" + p.getComment() + ")");
	    }

	    b.append('\n');
	}

	return b.toString();
    }

    public RegularFile computePDFReport() throws IOException
    {
	return computePDFReport(true, false, true);
    }

    public RegularFile computePDFReport(boolean includeTitle, boolean includeTableOfContents, boolean drawGraph)
	    throws IOException
    {

	LatexDocument doc = new LatexDocument(Directory.createTempDirectory(new Directory("/tmp/grph/report"), "", ""));
	// System.out.println("report is at " + doc.getLocation().getPath());
	StringBuffer latex = doc.getLatex();
	latex.append("\\documentclass{article}\n");
	latex.append("\\usepackage{graphicx}\n");
	latex.append("\\usepackage{xspace}\n");
	latex.append("\\usepackage{url}\n");
	latex.append("\\newcommand{\\grph}{\\textsl{Grph}\\xspace}\n");
	latex.append("\\author{Generated by \\grph \\footnotesize{v" + Grph.getVersion()
		+ "} \\\\ \\footnotesize{\\url{http://www-sop.inria.fr/members/Luc.Hogie/grph/}}}\n");
	latex.append("\\title{Grph report}\n");
	latex.append("\\begin{document}\n");

	if (includeTitle)
	{
	    latex.append("\\maketitle\n");
	}

	if (includeTableOfContents)
	{
	    latex.append("\\tableofcontents\n");
	}

	if (drawGraph)
	{
	    doc.addFile("graphviz.pdf", new GraphvizImageWriter().writeGraph(g));
	    addFigure(latex, "graphviz", "Graphviz ("
		    + GraphvizImageWriter.computeMostAppropriateDrawingCommand(g).name() + ") view of the graph");
	}

	// latex.append("\\begin{minipage}{\textwidth}\n");


	
	
	latex.append("\\section{Structural metrics}\n");

	latex.append("\\noindent\n");

	for (Metric p : findBooleanProperties())
	{
	    latex.append(p.getName() + "\\dotfill $" + p.getValue() + "$");

	    if (p.getComment() != null)
	    {
		latex.append(" (" + p.getComment() + ")");
	    }

	    latex.append("\\\\\n");
	}

	latex.append("\\section{Scalar metrics}\n");

	latex.append("\\noindent\n");

	for (Metric p : findScalarProperties())
	{
	    latex.append(p.getName() + "\\dotfill $" + p.getValue().toString() + "$");

	    if (p.getComment() != null)
	    {
		latex.append(" (" + p.getComment() + ")");
	    }

	    latex.append("\\\\\n");
	}

	// latex.append("\\end{minipage}\n");
	latex.append("\\section{Distribution metrics}\n");

	for (Metric p : findDistributionProperties())
	{
	    latex.append("\\subsection{" + p.getName() + "}\n");
	    String filename = p.getName().replace(' ', '-') + ".pdf";
	    Distribution distribution = (Distribution) p.getValue();
	    doc.addFile(filename, distribution.toPDF());
	    addFigure(latex, filename, p.getName());
	}

	latex.append("\\end{document}\n");

	RegularFile pdfFile = doc.compile(includeTableOfContents, false);
	return pdfFile;
    }

    private void addFigure(StringBuffer latex, String name, String caption)
    {
	// latex.append("\\begin{figure}[h]\n");
	latex.append("    \\begin{center}\n");
	latex.append("        \\includegraphics[width=0.8\\textwidth]{" + name + "}\n");
	latex.append("    \\end{center}\n");
	// latex.append("    \\caption{" + caption + "}\n");
	// latex.append("\\end{figure}\n");

    }

    public static void main(String[] args) throws IOException
    {
	System.out.println("starting");

	// create the graph
	Grph g = new grph.in_memory.InMemoryGrph();
	g.addNVertices(30);
	g.glp();
//	g.grid(30, 30);

	Report report = new Report(g);
	RegularFile pdfFile = report.computePDFReport();
	System.out.println(pdfFile.getPath());

	/*
	 * // write them all
	 * 
	 * report.storeForAurelien(new Directory("/tmp/repot"), new
	 * StdOutLogger(), new PropertyFilenameCalculator() {
	 * 
	 * @Override public String computeFilename(Property p) { return
	 * "mongraph - " + p.getName() + ".txt"; } });
	 */

	// System.out.println(report.computeTextualReport());
    }
}
