package de.inf.mobis.crawl.analyze;

import java.io.File;
import java.math.BigInteger;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Analyzes the files for Text-to-Code ratio
 * 
 * @author w.posdorfer
 * 
 */
public class AnalyzeTextCodeRatio extends AbstractAnalyzer
{

    /**
     * Code-Blöcke in selber Reihenfolge wie {@link DynamicAnalyzer#FOLDERS}
     */
    private final String[] _codeBlocks = { "pre[class=programlisting]", "pre", "td[class=code]",
            "div[class=codesample clear]", "div[class=codeSnippetContainerCode]", "dl[class=kASHglobal]" };

    private final EasyFileWriter _writer = new EasyFileWriter();

    private BigInteger _currentcodelength = BigInteger.valueOf(0);
    private BigInteger _currenttextlength = BigInteger.valueOf(0);

    private int _currentTutorialIndex;

    public void startingTutorialFolder(File tutorialFolder, int index)
    {
        _currentTutorialIndex = index;

        _writer.initFileWriter(new File(DynamicAnalyzer.ANALYSISPATH + tutorialFolder.getName() + ".csv"));

        _writer.write(String.format("%s, %s, %s\n", tutorialFolder.getName(), "Text", "Code"));
    }

    public void startingSubFolder(File subfolder)
    {
        _currentcodelength = BigInteger.valueOf(0);
        _currenttextlength = BigInteger.valueOf(0);
    }

    public void parsedHTMLFileToDocument(Document document)
    {
        Elements codeBlocks = document.select(_codeBlocks[_currentTutorialIndex]);

        for (Element codeblock : codeBlocks)
        {
            _currentcodelength = _currentcodelength.add(BigInteger.valueOf(codeblock.text().length()));
        }
        _currenttextlength = _currenttextlength.add(BigInteger.valueOf(document.text().length()));
    }

    public void endingSubFolder(File subfolder)
    {
        BigInteger plaintextlength = _currenttextlength.subtract(_currentcodelength);
        _writer.write(String.format("%s, %s, %s\n", subfolder.getName(), plaintextlength, _currentcodelength));
    }

    public void endingTutorialFolder(File tutorialFolder)
    {
        _writer.closeFileWriter();
    }

}
