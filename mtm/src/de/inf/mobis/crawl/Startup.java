package de.inf.mobis.crawl;

import java.io.IOException;

import de.inf.mobis.crawl.apple.FindAppleTutorials;

public class Startup
{
    public static void main(String[] args) throws IOException
    {
        FindAppleTutorials.parseTutorialLinksFromSavedWebsite();
    }
}
