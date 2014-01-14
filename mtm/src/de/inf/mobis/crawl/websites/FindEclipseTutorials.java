package de.inf.mobis.crawl.websites;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;

import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import de.inf.mobis.crawl.util.Util;

public class FindEclipseTutorials
{

    public static final HashSet<String> ignorePixx = new HashSet<String>(Arrays.asList("back.gif",
            "button1-addthis.gif", "printer.gif", "tag_1.gif", "tag_2.gif", "tag_3.gif", "tag_4.gif", "tag_5.gif",
            "tag_6.gif", "tag_7.gif", "tip.gif"));

    public static final String folderpath = "./download/eclipse/";

    // http://www.eclipse.org/articles/index.php
    public static void parseLinksFromWebsite() throws IOException
    {

        Document doc = Jsoup.connect("http://www.eclipse.org/articles/index.php").get();

        Element box = doc.select("div[class=resources]").get(0);

        Elements inBox = box.getElementsByAttribute("href");

        for (int i = 0; i < inBox.size(); i++)
        {
            String url = inBox.get(i).attr("abs:href");
            System.out.println("Downloading " + (i + 1) + "/" + inBox.size() + " ==> " + inBox.get(i).text());
            if (url.contains("resources/"))
            {
                followLinkandDownload(url);
            }

        }

    }

    static void followLinkandDownload(String url)
    {
        try
        {

            Document doc = Jsoup.connect(url).get();

            String title = doc.title().replaceAll("[^a-zA-Z0-9\\._\\- ]", "");

            Elements links = doc.select("a[href*=articles/][target!=_self]");

            for (Element link : links)
            {

                String slink = fixLink(link.attr("abs:href"));

                Document doc2 = Jsoup.connect(slink).get();
                String folder = folderpath + title + "/";
                new File(folder).mkdirs();

                Elements article = doc2.getElementsByAttributeValue("class", "article");
                if (article.size() > 0)
                {
                    Util.saveDocumentToFile(article.get(0), folder + Util.getName(doc2));
                }
                else
                {
                    Util.saveDocumentToFile(doc2, folder + Util.getName(doc2));
                }
                Util.saveImageFromDocumentToFolder(doc2, folder, ignorePixx);

            }

        }
        catch (HttpStatusException e)
        {
            System.err.println("failure with " + url + " CODE=" + e.getStatusCode());
        }
        catch (IOException e)
        {
        }
    }

    static String fixLink(String link)
    {
        if (link.contains("file="))
        {
            link = link.replace("article.php?file=", "");
        }
        return link;
    }

    public static void removeEmptyIMGFolder()
    {
        for (File folder : new File(folderpath).listFiles())
        {
            if (folder.isDirectory())
            {
                for (File ffolder : folder.listFiles())
                {
                    if (ffolder.isDirectory() && ffolder.listFiles().length == 0)
                    {
                        ffolder.delete();
                    }
                }
            }
        }
    }

}
