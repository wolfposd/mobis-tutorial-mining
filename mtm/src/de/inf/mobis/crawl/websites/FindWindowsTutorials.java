package de.inf.mobis.crawl.websites;

import java.io.IOException;
import java.util.ArrayList;

public class FindWindowsTutorials
{

    public static void parseLinksFromWebsite() throws IOException
    {

        String link1 = "http://msdn.microsoft.com/en-us/library/windowsphone/develop/ff967556%28v=vs.105%29.aspx";

        String content = "div[class=topic]";

        String select = "li > p > span > a[href]";

        String path = "./download/win-official/";

        Downloader d = new Downloader(link1, path, content, select, content);

        d.parseLinksFromWebsite();
    }

    static class Downloader extends EasyDownload
    {

        public Downloader(String website, String path, String content, String cssquery, String contentDownload)
        {
            super(website, path, content, cssquery, contentDownload, new ArrayList<String>());
        }

        @Override
        String folderReplace(String s)
        {
            return s;
        }

    }

}
