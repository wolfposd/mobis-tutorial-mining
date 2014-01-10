package de.inf.mobis.crawl;

import java.io.IOException;

import de.inf.mobis.crawl.apple.FindAppleTutorials;

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
    }

    public static void downloadAppleTutorials() throws IOException
    {
         FindAppleTutorials.parseTutorialLinksFromSavedWebsite();
         FindAppleTutorials.parseLinksFromFile();
       // FindAppleTutorials.removeRevisionHistory();
    }
}
