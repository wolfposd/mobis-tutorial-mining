package de.inf.mobis.crawl.analyze;

import java.io.File;
import java.io.FileFilter;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * @author w.posdorfer
 */
public class Analyze
{

    public static final String DOWNLOADPATH = "./download/";

    public static final String[] folders = { "android-community", "android-official", "apple-community",
            "apple-official", "win-official", "windows-community" };

    public static final String ANALYSISPATH = "./analysis/";

    public static void analyzeForCode()
    {

        String[] codeBlocks = { "pre[class=programlisting]", "pre", "td[class=code]", "div[class=codesample clear]",
                "div[class=codeSnippetContainerCode]", "dl[class=kASHglobal]" };

        for (int i = 0; i < folders.length; i++)
        {
            System.out.println("Starting " + folders[i]);
            File folder = new File(DOWNLOADPATH + folders[i] + "/");
            analyzeForCode(folder, codeBlocks[i]);
        }

    }

    static void analyzeForCode(File folder, String codeblockQuery)
    {
        String CSVPATH = folder.getName() + ".csv";

        try (FileWriter writer = new FileWriter(ANALYSISPATH + CSVPATH))
        {

            writer.write(String.format("%s, %s, %s\n", folder.getName(), "Text", "Code"));

            for (File subfolder : folder.listFiles(new FolderFilter()))
            {

                BigInteger curcodelength = BigInteger.valueOf(0);
                BigInteger htmltextlength = BigInteger.valueOf(0);
                for (File htmlFile : subfolder.listFiles(new NotFolderFilter()))
                {
                    try
                    {
                        Document d = Jsoup.parse(htmlFile, "UTF-8");

                        Elements codeBlocks = d.select(codeblockQuery);

                        for (Element codeblock : codeBlocks)
                        {
                            curcodelength = curcodelength.add(BigInteger.valueOf(codeblock.text().length()));
                        }
                        htmltextlength = htmltextlength.add(BigInteger.valueOf(d.text().length()));
                    }
                    catch (IOException e)
                    {
                    }
                }

                BigInteger plaintextlength = htmltextlength.subtract(curcodelength);
                writer.write(String.format("%s, %s, %s\n", subfolder.getName(), plaintextlength, curcodelength));
            }
        }
        catch (Exception e)
        {
        }

    }

    public static class FolderFilter implements FileFilter
    {
        public boolean accept(File pathname)
        {
            return pathname.isDirectory();
        }
    }

    public static class NotFolderFilter implements FileFilter
    {
        public boolean accept(File pathname)
        {
            return !pathname.isDirectory();
        }
    }
}
