package de.inf.mobis.crawl;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import de.inf.mobis.crawl.analyze.AnalyzeImageCount;
import de.inf.mobis.crawl.analyze.AnalyzeLinks;
import de.inf.mobis.crawl.analyze.AnalyzeStatistics;
import de.inf.mobis.crawl.analyze.AnalyzeTextCodeRatio;
import de.inf.mobis.crawl.analyze.DynamicAnalyzer;
import de.inf.mobis.crawl.extract.ExtractAll;
import de.inf.mobis.crawl.extract.ExtractPerTutorial;
import de.inf.mobis.crawl.extract.ExtractTitles;
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
    	//DynamicAnalyzer.analyze(new ExtractAll());
    	//DynamicAnalyzer.analyze(new ExtractPerTutorial());
    	//DynamicAnalyzer.analyze(new ExtractTitles());
         //DynamicAnalyzer.analyze(new AnalyzeTextCodeRatio());
        //
         //DynamicAnalyzer.analyze(new AnalyzeImageCount());

        // AnalyzeImages aa = new AnalyzeImages();
        // DynamicAnalyzer.analyze(aa);
        // aa.startImages();

        //DynamicAnalyzer.analyze(new AnalyzeLinks());
        DynamicAnalyzer.analyze(new AnalyzeStatistics());
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
