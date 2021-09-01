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

public class Swing {
	public static JComponent createSwingRenderer(Grph g) {
		final byte[] bytearray = new GraphvizImageWriter().writeGraph(g, COMMAND.neato, OUTPUT_FORMAT.fig, false);
		String figText = new String(bytearray);
		JFigViewerBean figbean = new JFigViewerBean();
		figbean.setPreferredSize(new Dimension(600, 600));
		figbean.createDefaultKeyHandler(); // useful shortcut keys
		figbean.createDefaultPopupMenu(); // zoom, panning, options
		figbean.createDefaultDragHandler(); // panning via mouse-drag
		figbean.createPositionAndZoomPanel(); // show cursor position
		figbean.setAntiAlias(true);

		try {
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
		} catch (IOException ex) {
			throw new IllegalStateException(ex);
		}

	}

}
