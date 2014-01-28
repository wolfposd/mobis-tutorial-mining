package de.inf.mobis.crawl.analyze;

import static de.inf.mobis.crawl.analyze.AnalyzeTextCodeRatio.CODEBLOCKS;

import java.io.File;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import de.inf.mobis.crawl.util.Util;

public class AnalyzeStatistics implements IAnalyze
{

    private String[][] filters = { { "div[class=section]" }, { "div[class=jd-descr]" }, {}, {}, {}, {} };

    private String[] sections = { "div[class=section]", "h2", "h2", "section", "div[class=sectionblock]", "body" };

    private File CSVFILE = new File(DynamicAnalyzer.ANALYSISPATH + "descriptionalstats.csv");

    private EasyFileWriter writer = new EasyFileWriter();

    private int _index;

    private long wordcount;
    private int pages;

    private int oLists;
    private int oListItems;
    private int uLists;
    private int uListItems;

    private int numSections;

    private int uListItemsWordCount;

    private int oListItemsWordCount;

    private DecimalFormat format;

    public AnalyzeStatistics()
    {
        DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.GERMAN);
        otherSymbols.setDecimalSeparator('.');
        otherSymbols.setGroupingSeparator(',');

        format = new DecimalFormat("#.##", otherSymbols);

        writer.initFileWriter(CSVFILE);

        writer.writeln("Tutorial, Wordcount, Pages, Sections,"
                + " Unordered-Lists, ulistItems, ulistItemsWordcount, avgUListWordCount, "
                + " Ordered-Lists, olistItems, olistItemsWordcount, avgOListWordCount");
        writer.closeFileWriter();
    }

    @Override
    public void startingTutorialFolder(File tutorialFolder, int index)
    {
        _index = index;

        writer.initFileWriter(CSVFILE, true);

        writer.writeln(tutorialFolder.getName() + ", ");
    }

    @Override
    public void startingSubFolder(File subfolder)
    {
        wordcount = 0;
        pages = 0;
        oLists = 0;
        oListItems = 0;
        oListItemsWordCount = 0;
        uLists = 0;
        uListItems = 0;
        uListItemsWordCount = 0;
        numSections = 0;
    }

    @Override
    public void startingHTMLFile(File htmlFile)
    {
        pages++;
    }

    @Override
    public void parsedHTMLFileToDocument(Document document)
    {
        Elements interestingElements = new Elements();
        if (filters[_index].length > 0)
        {
            for (String filter : filters[_index])
            {
                interestingElements.addAll(document.select(filter));
            }
        }
        else
        {
            interestingElements.add(document);
        }

        for (Element e : interestingElements)
        {
            Elements codeblocks = e.select(CODEBLOCKS[_index]);
            for (Element ee : codeblocks)
            {
                ee.remove();
            }

            wordcount += Util.occurencesOf(e.text(), " ");

            Elements olist = e.select("ol");
            Elements ulist = e.select("ul");
            oLists += olist.size();
            uLists += ulist.size();

            Elements uListItemsElements = e.select("ul > li");
            Elements oListItemsElements = e.select("ol > li");

            uListItems += uListItemsElements.size();
            oListItems += oListItemsElements.size();

            for (Element listitem : uListItemsElements)
            {
                uListItemsWordCount += Util.occurencesOf(listitem.text(), " ");
            }
            for (Element listitem : oListItemsElements)
            {
                oListItemsWordCount += Util.occurencesOf(listitem.text(), " ");
            }

        }

        numSections += document.select(sections[_index]).size();
    }

    @Override
    public void endingHTMLFile(File htmlFile)
    {
    }

    @Override
    public void endingSubFolder(File subfolder)
    {
        writer.write(String.format("%s, %d, %d, %d, ", subfolder.getName(), wordcount, pages, numSections));

        double avgUList = uListItemsWordCount;
        double avgOList = oListItemsWordCount;

        avgUList = uListItems != 0 ? avgUList / uListItems : 0;
        avgOList = oListItems != 0 ? avgOList / oListItems : 0;
        
        

        String stringavgUList = format.format(avgUList);
        String stringavgOlist = format.format(avgOList);
        

        writer.write(String.format("%d, %d, %d, %s, ", uLists, uListItems, uListItemsWordCount, stringavgUList));
        writer.write(String.format("%d, %d, %d, %s", oLists, oListItems, oListItemsWordCount, stringavgOlist));
        writer.writeln("");
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
