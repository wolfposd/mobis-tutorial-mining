package de.inf.mobis.crawl.websites;

import java.io.IOException;
import java.util.Arrays;

/**
 * @author w.posdorfer
 */
public class FindVogellaTutorials extends EasyDownload

{

    public FindVogellaTutorials()
    {
        super("http://www.vogella.com/tutorials/android.html", "./download/android-community/", "div[class=article]",
                "li > a[href]", "div[class=article]", Arrays.asList(
                        "100x120xlars-hackagotchi.png.pagespeed.ic.gx2Sf13j9r.png",
                        "xflattr.png.pagespeed.ic.5ZB2lDsqgv.png"));
    }

    @Override
    String folderReplace(String s)
    {
        return s;
    }

    public static void downloadTutorial()
    {
        try
        {
            new FindVogellaTutorials().parseLinksFromWebsite();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

}
