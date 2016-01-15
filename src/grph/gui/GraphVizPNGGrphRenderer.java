package grph.gui;

import grph.Grph;
import grph.io.GraphvizImageWriter;
import grph.io.GraphvizImageWriter.COMMAND;
import grph.io.GraphvizImageWriter.OUTPUT_FORMAT;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import toools.gui.Utilities;

public class GraphVizPNGGrphRenderer extends GraphRenderer
{
	public GraphVizPNGGrphRenderer(Grph g)
	{
		super(g);
	}

	public JComponent toGraphvizPNGAWTComponent()
	{
		try
		{
			final byte[] bytearray = new GraphvizImageWriter().writeGraph(getG(), COMMAND.neato, OUTPUT_FORMAT.svg, false);
			InputStream in = new ByteArrayInputStream(bytearray);
			final BufferedImage image = ImageIO.read(in);
			final JLabel label = new JLabel("", SwingConstants.CENTER);
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			final Dimension imageSize = new Dimension(image.getWidth(), image.getHeight());
			Dimension dd = Utilities.limit(imageSize, screenSize, 0.75);
			label.setPreferredSize(dd);

			label.addComponentListener(new ComponentListener() {

				@Override
				public void componentShown(ComponentEvent arg0)
				{
				}

				@Override
				public void componentResized(ComponentEvent cd)
				{
					Dimension d = Utilities.limit(imageSize, label.getSize(), 1);
					Image scaledImage = image.getScaledInstance((int) d.getWidth(), (int) d.getHeight(), Image.SCALE_SMOOTH);
					label.setIcon(new ImageIcon(scaledImage, null));
				}

				@Override
				public void componentMoved(ComponentEvent arg0)
				{
				}

				@Override
				public void componentHidden(ComponentEvent arg0)
				{
				}
			});

			return label;
		}
		catch (IOException e)
		{
			throw new IllegalStateException(e);
		}
	}

	@Override
	public JComponent getComponent()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void update()
	{
		// TODO Auto-generated method stub
		
	}
}
