package com.example.WebCrawler.ConcurrentWebCrawler;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class Utils {


//    domain ex: monzo.com
    public static  String getDomain(String url) {
        try {
            URL parsedUrl = new URL(url);
            String domain = parsedUrl.getHost();
            return domain;
        } catch (IOException e) {
            return null;
        }
    }

    public static String getMimeType(String url) throws IOException {
        URLConnection connection = new URL(url).openConnection();
        return connection.getContentType();
    }

    /*
        In normalized url
        for url - https://monzo.com/
        protocol - https
        host - monzo.com
        path - /
     */
    /*
        In normalized url
        for url - https://monzo.com#hello
        protocol - https
        host - monzo.com
        path -
     */

    public static String getNormalizedUrl(URL url) throws MalformedURLException {
        String normalizedUrl = url.getProtocol() + "://" + url.getHost() + url.getPath();
        if(url.getPath().equals(null))
        {
            normalizedUrl+="/";
        }
        return normalizedUrl;
    }
}
