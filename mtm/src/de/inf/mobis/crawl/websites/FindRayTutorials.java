package de.inf.mobis.crawl.websites;

import java.io.IOException;
import java.util.ArrayList;

/**
 * @author w.posdorfer
 */
public class FindRayTutorials extends EasyDownload
{
    private static final String WEBSITE = "http://www.raywenderlich.com/tutorials";

    private static final String PATH = "./download/apple-community2/";

    public FindRayTutorials()
    {
        super(WEBSITE, PATH, "div[class=content-wrapper]", "li > a[href]", "div[class=content-wrapper]",
                new ArrayList<String>());
    }

    @Override
    String folderReplace(String s)
    {
        return s.replace("Ray Wenderlich", "").trim();
    }

    public static void downloadTutorial()
    {
        try
        {
            new FindRayTutorials().parseLinksFromWebsite();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

}
