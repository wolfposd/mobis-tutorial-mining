package de.inf.mobis.crawl.websites;

import java.io.File;
import java.io.IOException;
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

    protected String _path = "";
    protected String _content = "";
    protected String _contentDownload = "";
    protected String _website = "";
    protected String _cssQuery = "";
    private List<String> _ignoreImages;

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

        Document doc = Jsoup.connect(_website).get();

        Element element = doc.select(_content).get(0);

        Elements select = element.select(_cssQuery);

        for (int i = 0; i < select.size(); i++)
        {
            System.out.println(String.format("Downloading %3d / %3d", i + 1, select.size()));
            downloadLink(select.get(i).attr("abs:href"));
        }
    }

    void downloadLink(String url)
    {
        try
        {
            Document doc = Jsoup.connect(url).get();

            String folder = Util.normalizeFolderName(doc.title());
            folder = folderReplace(folder);

            Element e = doc.select(_contentDownload).get(0);

            new File(_path + folder).mkdirs();
            Util.saveDocumentToFile(e, _path + folder + "/index.html");
            Util.saveImageFromDocumentToFolder(e, _path + folder, _ignoreImages);
        }
        catch (HttpStatusException e)
        {
            System.err.println("ERROR with " + url + " code:" + e.getStatusCode());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

    }

}
