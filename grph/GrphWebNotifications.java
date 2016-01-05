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
