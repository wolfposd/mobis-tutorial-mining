package de.inf.mobis.crawl;

import java.io.IOException;

import de.inf.mobis.crawl.websites.FindAndroidTutorials;
import de.inf.mobis.crawl.websites.FindAppleTutorials;
import de.inf.mobis.crawl.websites.FindRayTutorials;
import de.inf.mobis.crawl.websites.FindVogellaTutorials;

/**
 * 
 * @author w.posdorfer
 * 
 */
public class Startup
{
    public static void main(String[] args) throws IOException
    {
        // official
        downloadAppleTutorials();
        FindAndroidTutorials.parseLinksFromWebsite();

        // community
        FindRayTutorials.downloadTutorial();
        FindVogellaTutorials.downloadTutorial();

        // TODO
        // win official msdn....
        // win http://www.kirupa.com/windowsphone/
    }

    public static void downloadAppleTutorials() throws IOException
    {
        FindAppleTutorials.parseTutorialLinksFromSavedWebsite();
        FindAppleTutorials.parseLinksFromFile();
        FindAppleTutorials.removeRevisionHistory();
    }

}
