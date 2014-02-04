package de.inf.mobis.crawl.javacv;

import java.awt.Color;
import java.io.File;

import de.inf.mobis.crawl.analyze.AbstractAnalyzer;
import de.inf.mobis.crawl.analyze.DynamicAnalyzer;
import de.inf.mobis.crawl.analyze.EasyFileWriter;

public class AnalyzeImagesRectangles extends AbstractAnalyzer
{

    private int _totalImageCount = 0;
    private int _highlightCount = 0;
    private int _diagramCount;
    private int _sumTotalImages;
    private int _sumHighlightImages;
    private int _sumdiagramCount;

    private final EasyFileWriter _writer = new EasyFileWriter();

    private final File CSVFILE = new File(DynamicAnalyzer.ANALYSISPATH + "imagetype.csv");

    private final RectangleRecognition _recogRed;
    private final RectangleRecognition _recogBlack;

    public AnalyzeImagesRectangles()
    {
        _recogRed = new RectangleRecognition(100, 100, Color.RED, Color.RED);
        _recogBlack = new RectangleRecognition(100, 350, Color.BLACK, Color.WHITE);

        if (CSVFILE.exists())
        {
            CSVFILE.delete();
        }
        _writer.initFileWriter(CSVFILE);
        _writer.writeln("Tutorial, totalImages, imagesHighlight, diagrams");
        _writer.closeFileWriter();
    }

    @Override
    public void startingTutorialFolder(File tutorialFolder, int index)
    {
        _sumTotalImages = 0;
        _sumHighlightImages = 0;
        _sumdiagramCount = 0;

        _writer.initFileWriter(CSVFILE, true);
        _writer.writeln(tutorialFolder.getName() + ", , , ");
    }

    @Override
    public void startingSubFolder(File subfolder)
    {

        _totalImageCount = 0;
        _highlightCount = 0;
        _diagramCount = 0;

        File imageFolder = new File(subfolder, "img");
        if (imageFolder.exists())
        {
            if (imageFolder.isDirectory())
            {
                File[] listFiles = imageFolder.listFiles(new NotFolderFilter());
                _totalImageCount = listFiles.length;

                for (File image : listFiles)
                {
                    try
                    {
                        int count = _recogRed.scanImage(image.getAbsolutePath());
                        if (count > 0)
                        {
                            _highlightCount++;
                        }

                        // int blackBoxes =
                        // _recogBlack.scanImage(image.getAbsolutePath());
                        // if (blackBoxes > 4 && blackBoxes < 15)
                        // {
                        // _diagramCount++;
                        // }

                    }
                    catch (Throwable e)
                    {
                        System.err.println("produced error: " + image.getAbsolutePath());
                        e.printStackTrace();
                    }
                }
            }

        }
    }

    @Override
    public void endingSubFolder(File subfolder)
    {
        _sumdiagramCount += _diagramCount;
        _sumHighlightImages += _highlightCount;
        _sumTotalImages += _totalImageCount;

        _writer.writeln(String.format("%s, %s, %s, %s", subfolder.getName(), _totalImageCount, _highlightCount,
                _diagramCount));
    }

    @Override
    public void endingTutorialFolder(File tutorialFolder)
    {
        _writer.writeln(String.format("Summe, %s, %s, %s", _sumTotalImages, _sumHighlightImages, _sumdiagramCount));
        _writer.writeln(" , , ,");
        _writer.writeln(" , , ,");
        _writer.writeln(" , , ,");
        _writer.closeFileWriter();
    }

    public final class NotFolderFilter extends DynamicAnalyzer.NotFolderFilter
    {
        @Override
        public boolean accept(File pathname)
        {
            return super.accept(pathname) && !pathname.getName().toLowerCase().endsWith(".gif")
                    && pathname.length() > 6600;
        }
    }
}
