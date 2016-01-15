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
 
 package grph.script;

import java.util.Collection;
import java4unix.AbstractShellScript;
import java4unix.ArgumentSpecification;
import java4unix.License;

public abstract class AbstractGrphScript extends AbstractShellScript
{

    @Override
    protected void declareArguments(Collection<ArgumentSpecification> argumentSpecifications)
    {
    }

    @Override
    public String getAuthor()
    {
	return "Luc Hogie";
    }

    @Override
    public final License getLicence()
    {
	return new License("GPL", null);
    }

    @Override
    public String getVersion()
    {
	return "0.9";
    }

    @Override
    public String getYear()
    {
	return "2008-2012";
    }

    @Override
    public String getApplicationName()
    {
	return "grph";
    }

}
