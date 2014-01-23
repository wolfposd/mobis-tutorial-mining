package de.inf.mobis.crawl;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import de.inf.mobis.crawl.analyze.AnalyzeImages;
import de.inf.mobis.crawl.analyze.DynamicAnalyzer;
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
    public static void main(String[] args) throws IOException, InvocationTargetException, InterruptedException
    {
        // DynamicAnalyzer.analyze(new AnalyzeTextCodeRatio());
        //
        // DynamicAnalyzer.analyze(new AnalyzeImageCount());

        AnalyzeImages aa = new AnalyzeImages();
        DynamicAnalyzer.analyze(aa);
        aa.startImages();
    }

    public static void downloadTutorials() throws IOException
    {

        // official
        FindAppleTutorials.parseLinksFromWebsite();
        FindAndroidTutorials.parseLinksFromWebsite();
        FindWindowsTutorials.parseLinksFromWebsite();

        // community
        FindRayTutorials.downloadTutorial();
        FindVogellaTutorials.downloadTutorial();
        FindKirupaTutorials.downloadTutorial();
    }

}
