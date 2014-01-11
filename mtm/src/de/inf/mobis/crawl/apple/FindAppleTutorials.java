package de.inf.mobis.crawl.apple;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import de.inf.mobis.crawl.util.Util;

/**
 * 
 * @author w.posdorfer
 */
public class FindAppleTutorials
{

    public static final int IOS7STYLE = 1;
    public static final int OLDSTYLE_NEXTBUTTON = 2;
    public static final int OLDSTYLE_LINKCOLLECTION = 3;

    static final String DOWNLOADPATH = "./download/apple/";

    public static void parseTutorialLinksFromSavedWebsite() throws IOException
    {
        Document document = Jsoup.parse(new File("./resource/iOS Developer Library.html"), "UTF-8");

        Elements alllinks = document.select("a");

        System.out.println(alllinks.size());

        ArrayList<String> linklist = new ArrayList<>(1400);
        HashSet<String> duplicateCheck = new HashSet<>();

        for (Element o : alllinks)
        {
            for (Attribute a : o.attributes())
            {
                if (a.getValue().contains("library/ios/") && !a.getValue().endsWith("/"))
                {
                    if (duplicateCheck.add(a.getValue()))
                        linklist.add(a.getValue());
                }

            }
        }
        duplicateCheck.clear();
        System.out.println(linklist.size());

        Collections.sort(linklist);

        FileWriter writer = new FileWriter("./resource/AppleLinks.txt");

        for (String s : linklist)
        {
            writer.write(s + "\n");
        }
        writer.close();
    }

    public static final File FILE = new File("./resource/AppleLinks.txt");

    public static void parseLinksFromFile() throws IOException
    {

        new File(DOWNLOADPATH).mkdirs();

        System.out.println("Start Parsing Links");
        BufferedReader rd = new BufferedReader(new FileReader(FILE));
        String line = "";

        int i = 1;
        while ((line = rd.readLine()) != null)
        {
            System.out.println("Download " + i + " / 1783");
            downloadContentsFromAppleTutorial(line);
            i++;
        }
        rd.close();
        System.out.println("Done Parsing Links");
    }

    public static void downloadContentsFromAppleTutorial(String tutorialWebsite)
    {
        try
        {
            Document doc = Jsoup.connect(tutorialWebsite).get();
            int style = findStyle(doc);

            String folderpath = DOWNLOADPATH + doc.title().replaceAll("[^a-zA-Z0-9 ]*", "") + "/";

            new File(folderpath).mkdirs();

            switch (style)
            {
            case IOS7STYLE:
                downloadIOS7STYLE(doc, folderpath);
                break;
            case OLDSTYLE_NEXTBUTTON:
                downloadOldStyleNextButton(doc, folderpath);
                break;
            case OLDSTYLE_LINKCOLLECTION:
                downloadOldStyleLinkCollection(doc, folderpath);
                break;
            default:
                downloadPageSimple(doc, folderpath);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private static void downloadPageSimple(Document doc, String folderpath)
    {
        Util.saveDocumentToFile(doc, folderpath + "part_0.html");
        Util.saveImageFromDocumentToFolder(doc, folderpath);
    }

    public static int findStyle(Document doc)
    {
        String search = doc.html();
        if (search.contains("CSS/style-1.1.11.css"))
        {
            return IOS7STYLE;
        }
        else if (search.contains("<a class='nextLink' rel='next' href=")
                || search.contains("<a class=\"nextLink\" rel=\"next\" href="))
        {
            return OLDSTYLE_NEXTBUTTON;
        }
        else if (search.contains("collectionColumn"))
        {
            return OLDSTYLE_LINKCOLLECTION;
        }
        else
        {
            return -1;
        }
    }

    public static void downloadIOS7STYLE(Document document, String folderpath)
    {
        Elements navparts = document.getElementsByClass("nav-parts");
        if (navparts.size() >= 1)
        {
            Element nav = navparts.get(0);

            Elements links = nav.select("a");

            for (int i = 0; i < links.size(); i++)
            {
                Element elemt = links.get(i);

                if (elemt.attributes() != null)
                {
                    String link = elemt.attr("abs:href");
                    downloadPageAndImages(link, folderpath, "part_" + i + ".html");
                }
            }
        }

    }

    public static void downloadOldStyleNextButton(Document document, String folderpath)
    {
        Util.saveDocumentToFile(document, folderpath + "part_" + 0 + ".html");

        for (int i = 1; hasNextButton(document); i++)
        {
            Elements e = document.getElementsByClass("nextLink");

            if (e.size() > 0)
            {
                String newlink = e.get(0).attr("abs:href");
                try
                {
                    document = Jsoup.connect(newlink).get();
                }
                catch (IOException e1)
                {
                }
            }

            Util.saveDocumentToFile(document, folderpath + "part_" + i + ".html");
            Util.saveImageFromDocumentToFolder(document, folderpath);
        }
    }

    public static boolean hasNextButton(Document document)
    {
        return document.html().contains("<a class='nextLink' rel='next' href=")
                || document.html().contains("<a class=\"nextLink\" rel=\"next\" href=");
    }

    public static void downloadOldStyleLinkCollection(Document document, String folderpath)
    {
        // collectionColumn

        Elements columns = document.getElementsByClass("collectionColumn");

        ArrayList<String> links = new ArrayList<>();

        for (Element e : columns)
        {
            Elements linksinlist = e.select("a");

            for (Element ee : linksinlist)
            {
                links.add(ee.attr("abs:href"));
            }
        }

        for (int i = 0; i < links.size(); i++)
        {
            downloadPageAndImages(links.get(i), folderpath, "part_" + i + ".html");
        }
    }

    static void downloadPageAndImages(String link, String folderpath, String filename)
    {
        try
        {
            Document doc = Jsoup.connect(link).get();
            Util.saveDocumentToFile(doc, folderpath + filename);
            Util.saveImageFromDocumentToFolder(doc, folderpath);
        }
        catch (IOException e1)
        {
        }
    }

    public static void removeRevisionHistory()
    {
        for (File tutFolder : new File(DOWNLOADPATH).listFiles())
        {
            if (tutFolder.isDirectory())
            {

                File[] files = tutFolder.listFiles(new FileFilter()
                {
                    public boolean accept(File pathname)
                    {
                        return pathname.getName().contains(".html");
                    }
                });

                if (files.length > 1)
                {
                    try
                    {
                        Document parse = Jsoup.parse(files[files.length - 1], "UTF-8");

                        if (parse.html().contains("<h1 id=\"pageTitle\">Document Revision History</h1>"))
                        {
                            files[files.length - 1].delete();
                        }
                    }
                    catch (IOException e)
                    {
                    }
                }

            }
        }
    }

    public static void removeEmptyIMGFolder()
    {
        for (File tutfolder : new File(DOWNLOADPATH).listFiles())
        {

            File[] folder = tutfolder.listFiles(new FileFilter()
            {
                public boolean accept(File pathname)
                {
                    return pathname.isDirectory();
                }
            });
            if (folder != null)
                for (File imgfolder : folder)
                {
                    if (imgfolder.getName().contains("img"))
                    {
                        if (imgfolder.listFiles().length == 0)
                        {
                            imgfolder.delete();
                            System.out.println(imgfolder);
                        }
                    }
                }

        }
    }
}
