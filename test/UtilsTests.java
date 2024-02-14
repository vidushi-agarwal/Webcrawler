package com.example.WebCrawler.ConcurrentWebCrawler.test;

import com.example.WebCrawler.ConcurrentWebCrawler.src.Utils;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URL;

import static com.example.WebCrawler.ConcurrentWebCrawler.src.Utils.getDomain;
import static com.example.WebCrawler.ConcurrentWebCrawler.src.Utils.getNormalizedUrl;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UtilsTests {

    //verify domain
    /**
     * Method under test:
     * {@link Utils#getDomain(String)}
     */
    @Test
    public void testCrawl_Domain() {
        String url = "https://monzo.com";
        String domain = getDomain(url);
        assertTrue(domain.equals("monzo.com"));
    }

    //verify normalized Url
    /**
     * Method under test:
     * {@link Utils#getNormalizedUrl(URL)}
     */
    @Test
    public void testCrawl_NormalizedUrl1() throws IOException {
        URL url = new URL("https://monzo.com#hello");
        String normalizedUrl = getNormalizedUrl(url);
        assertTrue(normalizedUrl.equals("https://monzo.com/"));
    }

    /**
     * Method under test:
     * {@link Utils#getNormalizedUrl(URL)}
     */
    @Test
    public void testCrawl_NormalizedUrl2() throws IOException {
        URL url = new URL("https://monzo.com");
        String normalizedUrl = getNormalizedUrl(url);
        assertTrue(normalizedUrl.equals("https://monzo.com/"));
    }
}
