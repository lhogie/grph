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
 
 package grph.algo.ogdf;

import grph.algo.topology.ClassicalGraphs;
import toools.UnitTests;

public class OGDFPlanarityAlgorithm extends BooleanOGDFAlgorithm
{

    @Override
    protected String getAlgorithmName()
    {
	return "planarity";
    }

    public static void main(String[] args)
    {
	System.out.println();
    }
    
    private static void test()
    {
	// makes sure the Petersen Graph is not planar
	UnitTests.ensure(new OGDFPlanarityAlgorithm().compute(ClassicalGraphs.PetersenGraph()) == false);
    }
}
