package de.inf.mobis.crawl;

import java.io.IOException;

import de.inf.mobis.crawl.analyze.Analyze;
import de.inf.mobis.crawl.websites.FindAndroidTutorials;
import de.inf.mobis.crawl.websites.FindAppleTutorials;
import de.inf.mobis.crawl.websites.FindKirupaTutorials;
import de.inf.mobis.crawl.websites.FindRayTutorials;
import de.inf.mobis.crawl.websites.FindVogellaTutorials;
import de.inf.mobis.crawl.websites.FindWindowsTutorials;

/**
 * 
 * @author w.posdorfer
 * 
 */
public class Startup
{
    public static void main(String[] args) throws IOException
    {
        Analyze.analyzeForCode();
    }

    public static void downloadTutorials() throws IOException
    {
        FindAppleTutorials.parseTutorialLinksFromSavedWebsite();
        FindAppleTutorials.parseLinksFromFile();
        FindAppleTutorials.removeRevisionHistory();

        FindAndroidTutorials.parseLinksFromWebsite();
        FindWindowsTutorials.parseLinksFromWebsite();

        // community
        FindRayTutorials.downloadTutorial();
        FindVogellaTutorials.downloadTutorial();
        FindKirupaTutorials.downloadTutorial();
    }

}
