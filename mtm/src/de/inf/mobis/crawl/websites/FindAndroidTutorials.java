package de.inf.mobis.crawl.websites;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import de.inf.mobis.crawl.util.Util;

/**
 * @author w.posdorfer
 */
public class FindAndroidTutorials
{

    private static final String TRAINING_INDEX_HTML = "http://developer.android.com/training/index.html";
    private static final String COMPONENTS_INDEX_HTML = "http://developer.android.com/guide/components/index.html";
    private static final String DESIGN_INDEX_HTML = "http://developer.android.com/design/index.html";
    private static final String DISTRIBUTE_INDEX_HTML = "http://developer.android.com/distribute/index.html";

    public static final String FOLDERPATH = "./download/android-official/";

    public static void parseLinksFromWebsite() throws IOException
    {

        Document training = Jsoup.connect(TRAINING_INDEX_HTML).get();
        Document components = Jsoup.connect(COMPONENTS_INDEX_HTML).get();
        Document design = Jsoup.connect(DESIGN_INDEX_HTML).get();
        Document distribute = Jsoup.connect(DISTRIBUTE_INDEX_HTML).get();

        parselinksFromList(findRelevantLinks(training.select("li[class=nav-section]")));
        parselinksFromList(findRelevantLinks(components.select("li[class=nav-section]")));
        parselinksFromList(findRelevantLinks(design.select("li[class=nav-section]")));
        parselinksFromList(findRelevantLinks(distribute.select("li[class=nav-section]")));

    }

    private static ArrayList<String> findRelevantLinks(Elements navsections)
    {
        ArrayList<String> urlsToDownload = new ArrayList<>();

        for (Element e : navsections)
        {
            Elements links = e.getElementsByAttribute("href");

            for (Element link : links)
            {
                String url = link.attr("abs:href");
                if (!TRAINING_INDEX_HTML.equals(url) && !COMPONENTS_INDEX_HTML.equals(url)
                        && !DESIGN_INDEX_HTML.equals(url) && !DISTRIBUTE_INDEX_HTML.equals(url))
                    urlsToDownload.add(url);
            }

        }
        return urlsToDownload;
    }

    static void parselinksFromList(List<String> links)
    {
        for (String link : links)
        {
            try
            {
                Document d = Jsoup.connect(link).get();

                if (!d.html().contains("images/resource-tutorial.png") && !d.html().contains("<h3>Blog Articles</h3>"))
                {
                    String foldername = findFolderName(link);
                    String name = link.substring(link.lastIndexOf("/"));
                    System.out.println(foldername + name);

                    new File(FOLDERPATH + foldername + "/").mkdirs();

                    Elements divs = d.select("div[id=doc-col]");

                    if (divs.size() > 0)
                    {
                        Util.saveDocumentToFile(divs.get(0), FOLDERPATH + foldername + "/" + name);
                        Util.saveImageFromDocumentToFolder(divs.get(0), FOLDERPATH + "/" + foldername);
                    }
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }

        }

    }

    static String findFolderName(String link)
    {
        try
        {
            String pathname = "training/";

            if (!link.contains("training/") && link.contains("guide/"))
            {
                pathname = "guide/";
            }
            else if (!link.contains("training/") && link.contains("design/"))
            {
                pathname = "design/";
            }
            else if (!link.contains("training/") && link.contains("distribute/"))
            {
                pathname = "distribute/";
            }

            int pathIndex = link.indexOf(pathname);
            String filename = link.substring(pathIndex + pathname.length(), link.lastIndexOf("/"));
            filename = filename.replace("/", "_");
            return filename;
        }
        catch (Exception e)
        {
        }
        return "noname";
    }

}
