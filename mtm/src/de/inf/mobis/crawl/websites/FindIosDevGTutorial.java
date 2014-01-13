package de.inf.mobis.crawl.websites;

import java.io.File;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import de.inf.mobis.crawl.util.Util;

/**
 * @author w.posdorfer
 */
public class FindIosDevGTutorial
{

    public static final String folderpath = "./download/iosdevgermany/";

    public static void parseLinksFromWebsite() throws IOException
    {

        String url = "http://www.iosdevgermany.de/tutorials/";

        Document iosdev = Jsoup.connect(url).get();

        Elements links = iosdev.select("a[href]");

        for (Element link : links)
        {

            String linkurl = link.attr("abs:href");
            if (linkurl.contains("/tutorial/") && !linkurl.endsWith("/tutorial/"))
            {
                System.out.println("Downloading: " + linkurl.substring(linkurl.lastIndexOf("/", linkurl.length() - 2)));
                downloadPage(linkurl, link.text());
            }

        }

        System.out.println("done downloading iosdevgermany");

    }

    static void downloadPage(String url, String name)
    {
        try
        {
            Document page = Jsoup.connect(url).get();
            Elements postdivs = page.select("div[id*=post]");

            if (postdivs.size() > 0)
            {
                String folder = folderpath + name + "/";
                new File(folder).mkdirs();
                Util.saveDocumentToFile(postdivs.get(0), folder + "index.html");
                Util.saveImageFromDocumentToFolder(postdivs.get(0), folder);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
