package de.inf.mobis.crawl.analyze;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Wraps a FileWriter
 * 
 * @author w.posdorfer
 * 
 */
public class EasyFileWriter
{

    private FileWriter _writer;

    /**
     * initialize<br>
     * does not append
     * 
     * @param file
     */
    public void initFileWriter(File file)
    {
        initFileWriter(file, false);
    }

    /**
     * initialize
     * 
     * @param file
     *            destination
     * @param append
     *            should append text?
     */
    public void initFileWriter(File file, boolean append)
    {
        if (_writer != null)
        {
            try
            {
                _writer.close();
            }
            catch (IOException e)
            {
            }
        }
        try
        {
            _writer = new FileWriter(file, append);
        }
        catch (IOException e)
        {
        }

    }

    /**
     * Closes the writer
     */
    public void closeFileWriter()
    {
        try
        {
            _writer.close();
        }
        catch (IOException e)
        {
        }
        _writer = null;
    }

    /**
     * Writes a String to the file
     * 
     * @param s
     *            String to write
     */
    public void write(String s)
    {
        try
        {
            if (_writer != null)
            {
                _writer.write(s);
            }
        }
        catch (IOException e)
        {
        }
    }

    /**
     * Writes a string to the file and ends the line
     * 
     * @param s
     *            String to write
     */
    public void writeln(String s)
    {
        write(s + '\n');
    }

}
