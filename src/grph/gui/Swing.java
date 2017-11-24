package grph.gui;

import java.awt.Dimension;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.JComponent;

import grph.Grph;
import grph.io.GraphvizImageWriter;
import grph.io.GraphvizImageWriter.COMMAND;
import grph.io.GraphvizImageWriter.OUTPUT_FORMAT;
import jfig.gui.JFigViewerBean;

public class Swing
{
	public static JComponent createSwingRenderer(Grph g)
	{
		final byte[] bytearray = new GraphvizImageWriter().writeGraph(g, COMMAND.neato,
				OUTPUT_FORMAT.fig, false);
		String figText = new String(bytearray);
		JFigViewerBean figbean = new JFigViewerBean();
		figbean.setPreferredSize(new Dimension(600, 600));
		figbean.createDefaultKeyHandler(); // useful shortcut keys
		figbean.createDefaultPopupMenu(); // zoom, panning, options
		figbean.createDefaultDragHandler(); // panning via mouse-drag
		figbean.createPositionAndZoomPanel(); // show cursor position
		figbean.setAntiAlias(true);

		try
		{
			File tempFile = File.createTempFile("lmu", "fig");
			tempFile.deleteOnExit();
			FileOutputStream fos = new FileOutputStream(tempFile);
			fos.write(figText.getBytes());
			fos.flush();
			fos.close();
			figbean.setURL(tempFile.toURI().toURL());
			figbean.doFullRedraw();
			figbean.doZoomFit();

			return figbean;
		}
		catch (IOException ex)
		{
			throw new IllegalStateException(ex);
		}

	}

}
