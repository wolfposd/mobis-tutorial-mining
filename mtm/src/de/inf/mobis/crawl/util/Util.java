package de.inf.mobis.crawl.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Util
{

    /**
     * @deprecated
     */
    public static void downloadURLToFile(String url, String file)
    {
        try
        {
            Document d = Jsoup.connect(url).get();
            FileWriter writer = new FileWriter(new File(file), false);
            writer.write(d.toString());
            writer.close();
        }
        catch (IOException io)
        {
            io.printStackTrace();
        }

    }

    public static void saveDocumentToFile(Document d, String f)
    {
        try
        {
            FileWriter writer = new FileWriter(new File(f), false);
            writer.write(d.html());
            writer.close();
        }
        catch (IOException io)
        {
            io.printStackTrace();
        }
    }

    public static void saveImageFromDocumentToFolder(Document doc, String folder)
    {
        Elements images = doc.select("img");

        folder = folder + "/img/";
        new File(folder).mkdirs();

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
