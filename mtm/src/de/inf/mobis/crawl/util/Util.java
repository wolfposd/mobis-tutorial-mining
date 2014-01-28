package de.inf.mobis.crawl.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.jsoup.Connection.Response;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * 
 * @author w.posdorfer
 * 
 */
public class Util
{

    public static String normalizeFolderName(String name)
    {
        return name.replaceAll("[^^a-zA-Z0-9\\._\\- ]", "");
    }

    public static String getName(Document d)
    {
        String name = d.location();
        if (!name.endsWith(".html"))
            name += ".html";
        return name.substring(name.lastIndexOf("/") + 1);
    }

    public static void saveDocumentToFile(Element e, String f)
    {
        try
        {
            FileWriter writer = new FileWriter(new File(f), false);
            writer.write(e.html());
            writer.close();
        }
        catch (IOException io)
        {
            io.printStackTrace();
        }
    }

    public static void saveImageFromDocumentToFolder(Element doc, String folder)
    {
        List<String> empty = Collections.emptyList();
        saveImageFromDocumentToFolder(doc, folder, empty);
    }

    public static void saveImageFromDocumentToFolder(Element doc, String folder, Collection<String> ignoreByName)
    {
        Elements images = doc.select("img");

        folder = folder + "/img/";

        if (images.size() > 0)
        {
            new File(folder).mkdirs();
        }

        for (Element e : images)
        {
            String imageurl = e.attr("abs:src");

            String name = imageurl.substring(imageurl.lastIndexOf("/") + 1);
            if (!ignoreByName.contains(name))
            {
                try
                {
                    Response resultImageResponse = Jsoup.connect(imageurl).ignoreContentType(true).execute();
                    FileOutputStream out = new FileOutputStream(new File(folder + name));
                    out.write(resultImageResponse.bodyAsBytes());
                    out.close();
                }
                catch (HttpStatusException e1)
                {
                    System.err.println("Error downloading " + imageurl + " CODE=" + e1.getStatusCode());
                }
                catch (IOException e1)
                {
                }
            }
        }
    }

    public static int occurencesOf(String original, String match)
    {
        int result = 0;

        int index = 0;

        while ((index = original.indexOf(match, index)) != -1)
        {
            result++;
            index += match.length();
        }

        return result;
    }
}
