package de.inf.mobis.crawl.analyze;

import java.io.File;

import org.jsoup.nodes.Document;

/**
 * Class construct for analysis
 * 
 * @author w.posdorfer
 */
public class AbstractAnalyzer
{
    /**
     * @param tutorialFolder
     *            Folder of tutorials
     * @param index
     *            index of folder in list {@link DynamicAnalyzer#FOLDERS}
     */
    public void startingTutorialFolder(File tutorialFolder, int index)
    {
    }

    /**
     * Entering a folder containing html files
     * 
     * @param subfolder
     */
    public void startingSubFolder(File subfolder)
    {
    }

    /**
     * Starting with a file
     */
    public void startingHTMLFile(File htmlFile)
    {
    }

    /**
     * Parsed Document or <code>null</code>, check before using!
     */
    public void parsedHTMLFileToDocument(Document document)
    {
    }

    /**
     * finished with file
     */
    public void endingHTMLFile(File htmlFile)
    {
    }

    /**
     * finished with folder
     */
    public void endingSubFolder(File subfolder)
    {
    }

    /**
     * finished with tutorial
     */
    public void endingTutorialFolder(File tutorialFolder)
    {
    }
}