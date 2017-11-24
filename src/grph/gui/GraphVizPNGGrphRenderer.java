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
 
 package grph.gui;

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

import grph.Grph;
import grph.io.GraphvizImageWriter;
import grph.io.GraphvizImageWriter.COMMAND;
import grph.io.GraphvizImageWriter.OUTPUT_FORMAT;
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
