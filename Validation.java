package com.example.WebCrawler.ConcurrentWebCrawler;

import java.io.IOException;
import java.net.URL;

public class Validation {
    public static boolean isUnsupportedMimeType(String mimeType) {
        // Add logic to determine if the MIME type is unsupported
        // For example, check if it's a PDF, image, binary file, etc.
        return mimeType != null && (mimeType.startsWith("application/pdf") || mimeType.startsWith("image/"));
    }

    public static boolean isValid(String url, String domain) {
        try {
            URL parsedUrl = new URL(url);
            String urlDomain = parsedUrl.getHost();
            // no visiting of sections of that url
            if (urlDomain != null && urlDomain.equals(domain)) {
                return true;
            }
            return false;
        } catch (IOException e) {
            return false;
        }
    }
}
