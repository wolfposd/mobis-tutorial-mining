package de.inf.mobis.crawl.websites;

import java.io.IOException;
import java.util.ArrayList;

public class FindKirupaTutorials extends EasyDownload
{

    static final String website = "http://www.kirupa.com/windowsphone/";

    static final String content = "body";

    static final String cssquery = "li > a[class=siteLinkSM_Bold],[href]";

    static final String contentDownload = "td[style*=#D3D2CB;]";

    public FindKirupaTutorials()
    {
        super(website, "./download/windows-community/", content, cssquery, contentDownload, new ArrayList<String>());

        _ignorePages.add("http://www.kirupa.com/windowsphone/index.htm");

        _regex = ".*/(windowsphone|blend_silverlight)/.*";

    }

    @Override
    String folderReplace(String s)
    {
        return s.replace("kirupa.com - ", "");
    }

    public static void downloadTutorial() throws IOException
    {
        new FindKirupaTutorials().parseLinksFromWebsite();
    }

}
