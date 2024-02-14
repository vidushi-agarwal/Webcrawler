package com.example.WebCrawler.ConcurrentWebCrawler.src;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ConcurrentHashMap;

import static com.example.WebCrawler.ConcurrentWebCrawler.src.Utils.getNormalizedUrl;

public class Validation {
    public static boolean isUnsupportedMimeType(String mimeType) {
        // For example, check if it's a PDF, image, binary file, etc.
        return mimeType != null && (mimeType.startsWith("application/pdf") || mimeType.startsWith("image/"));
    }

    public static boolean isValid(String url, String domain) {
        try {
            URL parsedUrl = new URL(url);
            String urlDomain = parsedUrl.getHost();
            if (urlDomain != null && urlDomain.equals(domain)) {
                return true;
            }
            return false;
        } catch (IOException e) {
            return false;
        }
    }

    /*
        nexturl -  https://monzo.com/security/#mainContent
        normalized url - https://monzo.com/security/
     */
    public static boolean isNormalizedUrlValid(String url, ConcurrentHashMap<String, Boolean> visitedUrls){
        URL parsedUrl = null;
        try {
            parsedUrl = new URL(url);
            String normalizedUrl = getNormalizedUrl(parsedUrl);
            if(visitedUrls.containsKey(normalizedUrl))
                return false;
            else{
                if(!normalizedUrl.equals(url))
                    visitedUrls.put(normalizedUrl, true);
                return true;
            }
        } catch (MalformedURLException e) {
            System.out.println(e);
            return  false;
        }
    }
}
