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
 
 package grph;

import toools.Version;
import toools.log.Logger;
import toools.net.NetUtilities;

public class GrphWebNotifications
{
	public static boolean enabled = true;

	static void checkForNewVersion(final Logger logger)
	{
		new Thread() {
			@Override
			public void run()
			{
				try
				{
					String s = new String(NetUtilities.retrieveURLContent("http://www.i3s.unice.fr/~hogie/grph/releases/last-version.txt"));

					// in some case an access to the network exists but the
					// acess to internet is block by a portal
					// so we get a valid HTTP result but not the content of the
					// file (we get the result from the portal
					// which is likely to ask for login information
					if (s.matches("[0-9.]+"))
					{
						Version lastVersion = new Version();
						lastVersion.set(s);
						
						if (lastVersion.isNewerThan(Grph.getVersion()))
						{
							logger.log("A new version (" + lastVersion + ") of Grph is available at http://www.i3s.unice.fr/~hogie/grph/");
							logger.log("Current version is " + Grph.getVersion());
						}
					}
				}
				catch (Throwable e)
				{
				}
			}
		}.start();
	}
}
