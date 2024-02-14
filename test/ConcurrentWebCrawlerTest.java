package com.example.WebCrawler.ConcurrentWebCrawler.test;

import com.example.WebCrawler.ConcurrentWebCrawler.src.ConcurrentWebCrawler;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

import static com.example.WebCrawler.ConcurrentWebCrawler.src.Utils.getDomain;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ConcurrentWebCrawlerTest {

    private ConcurrentWebCrawler webCrawler;

    /**
     * Method under test:
     * {@link ConcurrentWebCrawler#crawlStart()}
     */
    @Test
    public void testCrawlStart() throws InterruptedException {
        // Mock ConcurrentHashMap, ExecutorService, and CountDownLatch
        ExecutorService executorServiceMock = mock(ExecutorService.class);
        ConcurrentHashMap<String, Boolean> visitedUrlsMock = mock(ConcurrentHashMap.class);
        CountDownLatch latchMock = mock(CountDownLatch.class);

        String url = "https://agvidushi.github.io/";

        webCrawler = new ConcurrentWebCrawler(url, 1,1);

        // Set mock objects
        webCrawler.setVisitedUrls(visitedUrlsMock);
        webCrawler.setTaskCounter(new AtomicInteger(1)); // Set task counter to 1 to simulate an active task
        webCrawler.setLatch(latchMock);
        webCrawler.setExecutorService(executorServiceMock);

        // Call crawlStart() method
        webCrawler.crawlStart();

        // Verify interactions
        verify(latchMock).await(); // Verify that await() method is called on the latch
        verify(executorServiceMock).shutdown(); // Verify that shutdown() method is called on the executor service
    }



    // valid Url
    /**
     * Method under test:
     * {@link ConcurrentWebCrawler#crawlStart()}
     */
    @Test
    public void testCrawl_ValidURL() {
        String url = "https://agvidushi.github.io/";

        webCrawler = new ConcurrentWebCrawler(url, 1,1);
        webCrawler.crawlStart();

        // Verify that the visited URL is added to the ConcurrentHashMap
        assertTrue(webCrawler.getVisitedUrls()
                .containsKey(url));
        // no external links
        assertFalse(webCrawler.getVisitedUrls().containsKey("facebook.com"));
    }

    //verify invalid urls should not be traced
    /**
     * Method under test:
     * {@link ConcurrentWebCrawler#crawlStart()}
     */
    @Test
    public void testCrawl_InvalidURL() {
        String url = "community.monzo.com";

        webCrawler = new ConcurrentWebCrawler(url, 2,1);
        webCrawler.crawlStart();

        // Verify that the visited URL is  not added to the ConcurrentHashMap
        assertFalse(webCrawler.getVisitedUrls()
                .containsKey(url));
        assertTrue(webCrawler.getVisitedUrls().size()==0);
    }

    // verify section tracing
    /**
     * Method under test:
     * {@link ConcurrentWebCrawler#crawlStart()}
     */
    @Test
    public void testCrawl_URLSection1() {
        String url = "https://monzo.com/security/#mainContent";
        String normalizedUrl = "https://monzo.com/security/";

        webCrawler = new ConcurrentWebCrawler(url, 1, 1);
        webCrawler.crawlStart();

        // Verify that the visited URL is added to the ConcurrentHashMap
        assertTrue(webCrawler.getVisitedUrls()
                .containsKey(normalizedUrl));
    }

    // verify section tracing
    /**
     * Method under test:
     * {@link ConcurrentWebCrawler#crawlStart()}
     */
    @Test
    public void testCrawl_URLSection2() throws IOException, InterruptedException {
        String url = "https://monzo.com/security/";
        String normalizedUrl = "https://monzo.com/security/#mainContent";

        webCrawler = new ConcurrentWebCrawler(url, 1, 1);
        webCrawler.crawlStart();

        // Verify that the visited URL is not added to the ConcurrentHashMap
        assertFalse(webCrawler.getVisitedUrls()
                .containsKey(normalizedUrl));
    }


    /**
     * Method under test:
     * {@link ConcurrentWebCrawler#crawlStart()}
     */
    // verify mimetype
    @Test
    public void testCrawl_UnsupportedMimeType() {
        String url = "https://monzo.com/legal/files/premium/monzo-premium-zurich-terms-and-conditions-1.1.pdf"; // Simulating a PDF URL

        webCrawler = new ConcurrentWebCrawler(url, 2, 1);
        webCrawler.crawlStart();

        // Verify that the visited URL is not added to the ConcurrentHashMap
        assertFalse(webCrawler.getVisitedUrls()
                .containsKey(url));
    }
}