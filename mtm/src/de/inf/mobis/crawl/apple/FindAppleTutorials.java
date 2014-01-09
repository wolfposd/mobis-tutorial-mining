package de.inf.mobis.crawl.apple;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class FindAppleTutorials
{

    public static void parseTutorialLinksFromSavedWebsite() throws IOException
    {

        Document document = Jsoup.parse(new File("./resource/iOS Developer Library.html"), "UTF-8");
        // Jsoup.connect("http://localhost/iOS Developer Library.html").get();
        // documentsTable_Wrapper

        Elements alllinks = document.select("a");

        System.out.println(alllinks.size());

        ArrayList<String> linklist = new ArrayList<>(1400);

        for (Element o : alllinks)
        {
            for (Attribute a : o.attributes())
            {
                if (a.getValue().contains("library/ios/") && !a.getValue().endsWith("/"))
                {
                    linklist.add(a.getValue());
                }

            }
        }

        System.out.println(linklist.size());
        
        Collections.sort(linklist);

        FileWriter writer = new FileWriter("./resource/AppleLinks.txt");

        for (String s : linklist)
        {
            writer.write(s + "\n");
        }
        writer.close();
    }

    public static final File FILE = new File("./resource/iOS Developer Library.html");

    public static void parseLinksFromFile() throws IOException
    {

        BufferedReader rd = new BufferedReader(new FileReader(FILE));
        String line;

        StringBuffer buffer = new StringBuffer();
        while ((line = rd.readLine()) != null)
        {
            buffer.append(line);
            buffer.append("\n");
        }
        rd.close();
    }

}
