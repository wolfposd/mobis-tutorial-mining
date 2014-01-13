package de.inf.mobis.crawl.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * 
 * @author w.posdorfer
 * 
 */
public class Util
{

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
        Elements images = doc.select("img");

        folder = folder + "/img/";

        if (images.size() > 0)
        {
            new File(folder).mkdirs();
        }

        for (Element e : images)
        {
            String imageurl = e.attr("abs:src");

            String name = imageurl.substring(imageurl.lastIndexOf("/"));

            try
            {
                Response resultImageResponse = Jsoup.connect(imageurl).ignoreContentType(true).execute();
                FileOutputStream out = new FileOutputStream(new File(folder + name));
                out.write(resultImageResponse.bodyAsBytes());
                out.close();
            }
            catch (IOException e1)
            {
            }

        }
    }
}
