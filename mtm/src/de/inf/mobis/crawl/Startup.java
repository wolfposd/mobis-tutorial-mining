package de.inf.mobis.crawl;

import java.io.IOException;

import de.inf.mobis.crawl.apple.FindAppleTutorials;
import de.inf.mobis.crawl.websites.FindAndroidTutorials;
import de.inf.mobis.crawl.websites.FindIosDevGTutorial;

/**
 * 
 * @author w.posdorfer
 * 
 */
public class Startup
{
    public static void main(String[] args) throws IOException
    {
         downloadAppleTutorials();
        // downloadIOSDevGermanyTutorials();
        //downloadAndroidDeveloperTutorials();
    }

    public static void downloadAppleTutorials() throws IOException
    {
        FindAppleTutorials.parseTutorialLinksFromSavedWebsite();
        FindAppleTutorials.parseLinksFromFile();
        FindAppleTutorials.removeRevisionHistory();
    }

    public static void downloadIOSDevGermanyTutorials() throws IOException
    {
        FindIosDevGTutorial.parseLinksFromWebsite();
    }

    public static void downloadAndroidDeveloperTutorials() throws IOException
    {
        FindAndroidTutorials.parseLinksFromWebsite();
    }
}
