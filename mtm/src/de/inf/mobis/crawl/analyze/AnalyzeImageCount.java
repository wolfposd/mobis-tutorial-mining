package de.inf.mobis.crawl.analyze;

import java.io.File;

import javax.swing.ImageIcon;

public class AnalyzeImageCount extends AbstractAnalyzer
{

    private int _folderCount = 0;
    
    private int _tutorialCount = 0;

    private int _imageCount = 0;

    private int _smallImageCount;

    private final EasyFileWriter _writer = new EasyFileWriter();

    private File CSVFILE = new File(DynamicAnalyzer.ANALYSISPATH + "imagecount.csv");

    public AnalyzeImageCount()
    {
        if (CSVFILE.exists())
        {
            CSVFILE.delete();
        }
        _writer.initFileWriter(CSVFILE);
        _writer.writeln("Tutorial, Folders, Images, SmallImages, Tutorials w. Images");
        _writer.closeFileWriter();
    }

    @Override
    public void startingTutorialFolder(File tutorialFolder, int index)
    {
        _imageCount = 0;
        _smallImageCount = 0;
        _folderCount = 0;
        _tutorialCount = 0;
        _writer.initFileWriter(CSVFILE, true);
    }

    @Override
    public void startingSubFolder(File subfolder)
    {
        _folderCount++;
        File imageFolder = new File(subfolder, "img");
        if (imageFolder.exists())
        {
            if (imageFolder.isDirectory())
            {
            	_tutorialCount++;
            	
                for (File image : imageFolder.listFiles())
                {
                    try
                    {
                    	String test = image.getAbsolutePath();
                        ImageIcon icon = new ImageIcon(image.getAbsolutePath());

                        if (icon.getIconWidth() > 10 && icon.getIconHeight() > 10)
                        {
                            _imageCount++;
                        }
                        else
                        {
                            _smallImageCount++;
                        }
                    }
                    catch (Throwable e)
                    {
                        System.err.println("produced error: " + image.getAbsolutePath());
                    }
                }
            }

        }
    }

    @Override
    public void endingTutorialFolder(File tutorialFolder)
    {
        _writer.writeln(String.format("%s, %s, %s, %s, %s", tutorialFolder.getName(), _folderCount, _imageCount,
                _smallImageCount, _tutorialCount));

        _writer.closeFileWriter();
    }

}
