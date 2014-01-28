package de.inf.mobis.crawl.analyze;

import java.io.File;
import java.io.FileFilter;
import java.util.HashMap;

public class AnalyzePartSplitting extends AbstractAnalyzer
{

    private File CSVFILE = new File(DynamicAnalyzer.ANALYSISPATH + "partsplit.csv");

    private String[] partNames = { ".* Pt [0-9]+", ".* Part [0-9]+", ".* Page [0-9]+" };


    @Override
    public void startingTutorialFolder(File tutorialFolder, int index)
    {

        HashMap<String, Integer> map = new HashMap<String, Integer>();

        for (File folder : tutorialFolder.listFiles(new Filter()))
        {
            String name = folder.getName();

            name = name.replaceAll("Pt [0-9]+", "");
            name = name.replaceAll("Part [0-9]+", "");
            name = name.replaceAll("Page [0-9]+", "");

            Integer i = map.get(name);
            if (i == null)
            {
                i = new Integer(0);
                map.put(name, i);
            }

            i = i + 1;
            
            map.put(name,i);
        }

        
        for(String key : map.keySet())
        {
            System.out.println(key +" : " + map.get(key));
        }
        
    }

    @Override
    public void endingTutorialFolder(File tutorialFolder)
    {

    }

    class Filter implements FileFilter
    {
        @Override
        public boolean accept(File pathname)
        {
            for (String s : partNames)
            {
                if (pathname.getName().matches(s))
                {
                    return true;
                }
            }
            return false;
        }

    }

}
