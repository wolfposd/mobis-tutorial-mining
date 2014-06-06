package de.inf.mobis.crawl.websites;

import java.io.File;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.HashSet;
import java.util.List;

import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import de.inf.mobis.crawl.util.Util;

/**
 * Download for simple structured blogs
 * 
 * @author w.posdorfer
 */
public abstract class EasyDownload
{

    private static final int TIMEOUT = 30000;
    protected String _path = "";
    protected String _content = "";
    protected String _contentDownload = "";
    protected String _website = "";
    protected String _cssQuery = "";
    private List<String> _ignoreImages;

    protected HashSet<String> _ignorePages = new HashSet<String>();
    protected String _regex = ".*";

    /**
     * Constructor
     * 
     * @param website
     *            Website-URL
     * @param path
     *            Path to save files
     * @param content
     *            main content to look for links
     * @param cssquery
     *            query to select links
     * @param contentDownload
     *            main content of tutorial
     * @param ignoreImages
     *            images to ignore
     */
    public EasyDownload(String website, String path, String content, String cssquery, String contentDownload,
            List<String> ignoreImages)
    {
        _website = website;
        _path = path;
        _content = content;
        _cssQuery = cssquery;
        _contentDownload = contentDownload;
        _ignoreImages = ignoreImages;
    }

    abstract String folderReplace(String s);

    public void parseLinksFromWebsite() throws IOException
    {

        Document doc = Jsoup.connect(_website).timeout(TIMEOUT).get();

        Element element = doc.select(_content).get(0);

        Elements select = element.select(_cssQuery);

        for (int i = 0; i < select.size(); i++)
        {
            Element e = select.get(i);
            String attr = e.attr("abs:href");

            if (!_ignorePages.contains(attr) && attr.matches(_regex))
            {
                System.out.println(String.format("Downloading %3d / %3d", i + 1, select.size()));
                downloadLink(attr);
            }
            else
            {
                System.out.println(String.format("Skipping %3d / %3d", i + 1, select.size()));
            }
        }
    }

    void downloadLink(String url)
    {
        try
        {
            Document doc = Jsoup.connect(url).timeout(TIMEOUT).get();

            String folder = Util.normalizeFolderName(doc.title());
            folder = folderReplace(folder);

            System.out.println(url);

            Elements preSelect = doc.select(_contentDownload);

            if (preSelect.size() > 0)
            {
                Element e = preSelect.get(0);

                new File(_path + folder).mkdirs();
                Util.saveDocumentToFile(e, _path + folder + "/index.html");
                Util.saveImageFromDocumentToFolder(e, _path + folder, _ignoreImages);
            }
            else
            {
                System.err.println("No content " + url);
            }
        }
        catch (HttpStatusException e)
        {
            System.err.println("ERROR with " + url + " code:" + e.getStatusCode());
        }
        catch (SocketTimeoutException e)
        {
            System.err.println("TIMEOUT with " + url);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

    }

}
