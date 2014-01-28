package de.inf.mobis.crawl.analyze;

import java.io.File;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * @author w.posdorfer
 */
public class AnalyzeLinks implements IAnalyze
{
    // { "android-community", "android-official", "apple-community",
    // "apple-official", "win-official", "windows-community" };

    String[] apiLinks = { "/reference/android/", "/reference/android/", "/Reference/", "/Reference/",
            "library/windowsphone/develop/", "library/windowsphone/develop/" };
    private int _index;

    private EasyFileWriter writer = new EasyFileWriter();

    private File CSVFILE = new File(DynamicAnalyzer.ANALYSISPATH + "link_count.csv");

    private int _links = 0;
    private int _api = 0;

    public AnalyzeLinks()
    {
        writer.initFileWriter(CSVFILE);
        writer.writeln("Tutorial, API-Links, Normal-Links");
        writer.closeFileWriter();
    }

    @Override
    public void startingTutorialFolder(File tutorialFolder, int index)
    {
        _index = index;

        writer.initFileWriter(CSVFILE, true);
        writer.writeln(tutorialFolder.getName() + ", , ");
    }

    @Override
    public void startingSubFolder(File subfolder)
    {
        _api = 0;
        _links = 0;
    }

    @Override
    public void startingHTMLFile(File htmlFile)
    {
    }

    @Override
    public void parsedHTMLFileToDocument(Document document)
    {

        Elements ahrefs = document.select("a[href]");

        String match = apiLinks[_index];

        for (Element ahref : ahrefs)
        {
            String link = ahref.attr("href");

            if (link.endsWith(".jpg") || link.endsWith(".png"))
            {
                continue;
            }
            else if (link.contains(match))
            {
                _api++;
            }
            else
            {
                _links++;
            }
        }

    }

    @Override
    public void endingHTMLFile(File htmlFile)
    {
    }

    @Override
    public void endingSubFolder(File subfolder)
    {
        writer.writeln(String.format("%s, %d, %d", subfolder.getName(), _api, _links));
    }

    @Override
    public void endingTutorialFolder(File tutorialFolder)
    {
        writer.writeln(" , , ");
        writer.writeln(" , , ");
        writer.writeln(" , , ");
        writer.closeFileWriter();
    }

}
