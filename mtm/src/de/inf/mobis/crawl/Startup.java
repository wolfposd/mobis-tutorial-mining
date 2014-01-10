package de.inf.mobis.crawl;

import java.io.IOException;

import de.inf.mobis.crawl.apple.FindAppleTutorials;

public class Startup
{
    public static void main(String[] args) throws IOException
    {
        // FindAppleTutorials.parseTutorialLinksFromSavedWebsite();
         FindAppleTutorials.parseLinksFromFile();

        // FindAppleTutorials
        // .downloadContentsFromAppleTutorial("https://developer.apple.com/library/ios/documentation/UserExperience/Conceptual/TransitionGuide/index.html#//apple_ref/doc/uid/TP40013174-CH6-SW1");

    }
}
