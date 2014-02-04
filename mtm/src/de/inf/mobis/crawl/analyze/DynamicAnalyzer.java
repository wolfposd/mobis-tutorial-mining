package de.inf.mobis.crawl.analyze;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class DynamicAnalyzer
{

    public static final String DOWNLOADPATH = "./download/";

    public static final String[] FOLDERS = { "android-community", "android-official", "apple-community",
            "apple-official", "win-official", "windows-community" };

    public static final String ANALYSISPATH = "./analysis/";

    private IAnalyze _analyzer;

    public static void analyze(IAnalyze analyzer)
    {
        new DynamicAnalyzer(analyzer);
    }

    public DynamicAnalyzer(IAnalyze analyzer)
    {
        _analyzer = analyzer;

        for (int i = 0; i < FOLDERS.length; i++)
        {
            System.out.println("Starting " + FOLDERS[i]);
            File folder = new File(DOWNLOADPATH + FOLDERS[i] + "/");

            analyzeForCode(folder, i);
        }

    }

    private void analyzeForCode(File folder, int index)
    {
    	
    	_analyzer.startingTutorialFolder(folder, index);

    	for (File subfolder : folder.listFiles(new FolderFilter()))
    	{

    		_analyzer.startingSubFolder(subfolder);

    		for (File htmlFile : subfolder.listFiles(new NotFolderFilter()))
    		{
    			_analyzer.startingHTMLFile(htmlFile);
    			try
    			{
    				Document d = Jsoup.parse(htmlFile, "UTF-8");
    				_analyzer.parsedHTMLFileToDocument(d);
    			}
    			catch (IOException e)
    			{
    				_analyzer.parsedHTMLFileToDocument(null);
    			}
    			_analyzer.endingHTMLFile(htmlFile);
    		}
    		_analyzer.endingSubFolder(subfolder);
    	}

    	_analyzer.endingTutorialFolder(folder);

    }

    public static class FolderFilter implements FileFilter
    {
        public boolean accept(File pathname)
        {
            //return pathname.isDirectory() && !pathname.getName().contains("Reference");
            return pathname.isDirectory() && !pathname.getName().matches(".*Reference.*")
            		&& !pathname.getName().matches(".*API.*Differences.*") 
            		&& !pathname.getName().matches(".*Additions.*")
            		&& !pathname.getName().matches(".*Release.*Note.*")
            		&& !pathname.getName().matches(".*iOS.*Release.*Note.*");
        }
    }

    public static class NotFolderFilter implements FileFilter
    {
        public boolean accept(File pathname)
        {
        	return !pathname.isDirectory() && !pathname.getName().equals(".DS_Store");
        }
    }

}
