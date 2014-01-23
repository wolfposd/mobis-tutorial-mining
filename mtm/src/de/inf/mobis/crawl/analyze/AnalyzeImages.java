package de.inf.mobis.crawl.analyze;

import java.awt.BorderLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;

public class AnalyzeImages extends AbstractAnalyzer
{

    private final EasyFileWriter _writer = new EasyFileWriter();

    private File CSVFILE = new File(DynamicAnalyzer.ANALYSISPATH + "image_stanza_count.csv");

    private ClickWindow _window;

    private ArrayList<ArrayList<File>> list = new ArrayList<ArrayList<File>>();

    private int currentfolder;

    public AnalyzeImages()
    {
        if (CSVFILE.exists())
        {
            CSVFILE.delete();
        }
        _writer.initFileWriter(CSVFILE);
        _writer.writeln("Tutorial, normal, highlighted");
        _writer.closeFileWriter();

    }

    @Override
    public void startingTutorialFolder(File tutorialFolder, int index)
    {

        list.add(new ArrayList<File>());
        currentfolder = index;
    }

    @Override
    public void startingSubFolder(File subfolder)
    {

        File imageFolder = new File(subfolder, "img");
        if (imageFolder.exists())
        {

            if (imageFolder.isDirectory())
            {
                for (File image : imageFolder.listFiles())
                {

                    try
                    {
                        list.get(currentfolder).add(image);
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

        // _writer.write(String.format("%s, %s, %s", tutorialFolder.getName(),
        // _window.imagesNoMark,
        // _window.imagesWithMark));
        // _writer.closeFileWriter();
        // _window.reset();
    }

    public void startImages()
    {

        _writer.initFileWriter(CSVFILE);
        _writer.writeln("Tutorial, normal, highlighted, skipped");
        _writer.closeFileWriter();

        _window = new ClickWindow();

        _window.setVisible(true);
        _window.nextImage();
    }

    public class ClickWindow extends JDialog
    {

        private static final long serialVersionUID = 1L;

        public JLabel label = new JLabel();

        public int imagesNoMark = 0;

        public int imagesWithMark = 0;

        public int skipped = 0;

        public int currentfileIndex = 0;

        public int curIndex = -1;

        public ClickWindow()
        {
            this.setLocationRelativeTo(null);

            this.setLayout(new BorderLayout());
            label.setHorizontalAlignment(JLabel.CENTER);
            this.add(label, BorderLayout.CENTER);
            this.setSize(500, 500);

            KeyAdapter adapter = new KeyAdapter()
            {
                public void keyTyped(KeyEvent e)
                {
                    if (e.getKeyChar() == 'w')
                    {
                        imagesWithMark++;
                        nextImage();
                    }
                    else if (e.getKeyChar() == 'o')
                    {
                        imagesNoMark++;
                        nextImage();
                    }
                    else if (e.getKeyChar() == ' ')
                    {
                        skipped++;
                        nextImage();
                    }
                    else if (e.getKeyChar() == '5')
                    {
                        curIndex = list.get(currentfileIndex).size() - 1;
                        nextImage();
                    }
                    else
                    {
                        System.out.println("WRONG KEY >" + e.getKeyChar() + "<");
                        System.out.println("w für mit Highlight");
                        System.out.println("o  für ohne Highlight");
                        System.out.println("Leertaste zum Überspringen");
                        System.out.println("5 für nächstes Tutorial");
                    }
                }
            };
            label.addKeyListener(adapter);
            this.addKeyListener(adapter);
        }

        public void nextImage()
        {
            if (curIndex == list.get(currentfileIndex).size() - 1)
            {

                _writer.initFileWriter(CSVFILE, true);

                String text = DynamicAnalyzer.FOLDERS[currentfileIndex] + ", " + imagesNoMark + ", " + imagesWithMark
                        + ", " + skipped;

                _writer.writeln(text);
                _writer.closeFileWriter();
                System.out.println(text);

                curIndex = 0;
                if (currentfileIndex < list.size() - 1)
                {
                    currentfileIndex++;
                }
                else
                {
                    System.exit(0);
                }

                imagesNoMark = 0;
                imagesWithMark = 0;
                skipped = 0;

            }
            else
            {
                curIndex++;
            }

            setTitle(DynamicAnalyzer.FOLDERS[currentfileIndex] + " " + (curIndex + 1) + " / "
                    + list.get(currentfileIndex).size());
            setImage(new ImageIcon(list.get(currentfileIndex).get(curIndex).getAbsolutePath()));
        }

        public void setImage(ImageIcon image)
        {
            label.setIcon(image);
            label.repaint();
        }

    }

}
