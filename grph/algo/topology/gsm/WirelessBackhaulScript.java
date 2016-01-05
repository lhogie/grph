package grph.algo.topology.gsm;

import grph.io.GraphvizImageWriter.COMMAND;
import grph.io.GraphvizImageWriter.OUTPUT_FORMAT;
import grph.script.AbstractGrphScript;

import java.io.PrintStream;
import java.util.Collection;
import java.util.Random;
import java4unix.CommandLine;
import java4unix.OptionSpecification;

import toools.io.file.Directory;
import toools.io.file.RegularFile;
import toools.set.ElementPrinter;

public class WirelessBackhaulScript extends AbstractGrphScript
{

	@Override
	protected void declareOptions(Collection<OptionSpecification> optionSpecifications)
	{
		optionSpecifications.add(new OptionSpecification("--numberOfBTSs", null, "[0-9]+", "30", "the number of BTSs"));
		optionSpecifications.add(new OptionSpecification("--numberOfGTWs", null, "[0-9]+", "3", "the number of BSCs"));
		optionSpecifications.add(new OptionSpecification("--density", null, ".*", "0.8", "the number of BSCs"));
	}

	@Override
	public int runScript(CommandLine cmdLine) throws Throwable
	{
		ClassLoader.getSystemClassLoader().setDefaultAssertionStatus(true);
		int bts = Integer.valueOf(getOptionValue(cmdLine, "--numberOfBTSs"));
		int bsc = Integer.valueOf(getOptionValue(cmdLine, "--numberOfGTWs"));
		double density = Double.valueOf(getOptionValue(cmdLine, "--density"));
		
		if (density < 0 || density > 1)
			throw new IllegalArgumentException("invalid density " + density +  ". Please give a value between 0 and 1");

		final WirelessBackhaul b = new WirelessBackhaul(bts, bsc, new Random(), density);
		RegularFile imageFile = b.g.toGraphviz(COMMAND.neato, OUTPUT_FORMAT.png, Directory.getCurrentDirectory());
		System.out.println("Generating file " + imageFile.getName());

		RegularFile datFile = new RegularFile(imageFile.getPath().replaceAll("\\.png$", ".dat"));
		PrintStream p = new PrintStream(datFile.createWritingStream());
		System.out.println("Generating file " + datFile.getName());

		p.println("NumNodes = " + b.g.getNumberOfVertices() + ";");
		p.println("Nodes = " + b.g.getVertices() + ";");
		p.println("NumEdges = " + b.g.getNumberOfEdges() + ";");
		p.println("Edges = " + b.g.getEdges().toString(new ElementPrinter() {

			@Override
			public String toString(int e)
			{
				int v1 = b.g.getOneVertex(e);
				int v2 = b.g.getTheOtherVertex(e, v1);
				int c = b.edgeCapacities.getValueAsInt(e);
				return "<" + v1 + ", " + v2 + ", " + c + ">";
			}
		}) + ";");
		p.println("Gateways = " + b.bscIDs() + ";");
		p.close();

		return 0;
	}

	@Override
	public String getShortDescription()
	{
		return "Computes a wireless backhaul network for Alvinice's use.";
	}

	public static void main(String[] args) throws Throwable
	{
		new WirelessBackhaulScript().run("--numberOfBTSs=30", "--numberOfGTWs=5", "--density=0.8");
	}
}
