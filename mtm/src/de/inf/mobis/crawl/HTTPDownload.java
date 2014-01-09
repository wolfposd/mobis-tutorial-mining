package de.inf.mobis.crawl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HTTPDownload
{

    public static String downloadFrom(String stringurl)
    {
        try
        {
            URL url = new URL(stringurl);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;

            StringBuffer buffer = new StringBuffer();
            while ((line = rd.readLine()) != null)
            {
                buffer.append(line);
                buffer.append("\n");
            }
            rd.close();

            return buffer.toString();
        }
        catch (MalformedURLException e)
        {
        }
        catch (IOException e)
        {
        }
        return "";
    }

}
