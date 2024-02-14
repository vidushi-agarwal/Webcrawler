package com.example.WebCrawler.ConcurrentWebCrawler.src;

import org.jsoup.Jsoup;
import org.jsoup.UnsupportedMimeTypeException;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static com.example.WebCrawler.ConcurrentWebCrawler.src.Utils.getDomain;
import static com.example.WebCrawler.ConcurrentWebCrawler.src.Utils.getMimeType;
import static com.example.WebCrawler.ConcurrentWebCrawler.src.Validation.*;

public class ConcurrentWebCrawler {

    private static String startUrl;
    private static int MAX_DEPTH;
    private static int threadCount;
    private static ConcurrentHashMap<String, Boolean> visitedUrls;
    private static AtomicInteger taskCounter;
    private CountDownLatch latch;
    private ExecutorService executorService;
    private static final Logger logger = LoggerFactory.getLogger(ConcurrentWebCrawler.class);

    public ConcurrentWebCrawler(String startUrl, int maxDepth, int threadCount) {
        this.startUrl = startUrl;
        this.MAX_DEPTH = maxDepth;
        this.threadCount = threadCount;
        this.visitedUrls = new ConcurrentHashMap<>();
        this.taskCounter = new AtomicInteger(0); // Start from 0
        this.latch = new CountDownLatch(1); // Initialize with 1
        this.executorService = Executors.newFixedThreadPool(this.threadCount);
    }

    public void setLatch(CountDownLatch latch) {
        this.latch = latch;
    }
    public static ConcurrentHashMap<String, Boolean> getVisitedUrls() {
        return visitedUrls;
    }

    public static void setVisitedUrls(ConcurrentHashMap<String, Boolean> visitedUrls) {
        ConcurrentWebCrawler.visitedUrls = visitedUrls;
    }

    public void setExecutorService(ExecutorService executorService) {
        this.executorService = executorService;
    }

    public static void setTaskCounter(AtomicInteger taskCounter) {
        ConcurrentWebCrawler.taskCounter = taskCounter;
    }
    public void crawlStart() {
        String domain = getDomain(startUrl);
        // check if startUrl is valid
        try {
            if(isValid(startUrl, domain) && !isUnsupportedMimeType(getMimeType(startUrl))) {
                crawl(startUrl, domain, 0);
            }
            else
                return;
            latch.await(); // Wait for all tasks to complete
            executorService.shutdown(); // Shutdown the executor service after all tasks complete
        } catch (UnknownHostException e) {
            logger.error("Unknown Host Exception: " + startUrl);
        } catch (InterruptedException e) {
            logger.error("Error waiting for tasks to complete: " + e.getMessage());
        } catch (IOException e){
            logger.error("IO Error: " + e.getMessage());
        }
    }

    private void crawl(String url, String domain, int depth) {
//        logger.info("Thread entry: " + Thread.currentThread().getName() + " - Visiting URL: " + url);

        if (depth > MAX_DEPTH || visitedUrls.containsKey(url) ) {
//            logger.info("Thread exit: " + Thread.currentThread().getName() + " - Exiting URL: " + url);
            return;
        }

        visitedUrls.put(url, true);
        System.out.println("Visiting URL: " + url);
        taskCounter.incrementAndGet(); // Increment task counter
        executorService.execute(() -> {
            try {
                Document document = Jsoup.connect(url).get();
                Elements links = document.select("a[href]");
                for (Element link : links) {
                    String nextUrl = link.absUrl("href");
                    logger.info("Sub links found on " + url + " are : - " + nextUrl);
                    if (isValid(nextUrl, domain) && !isUnsupportedMimeType(getMimeType(nextUrl))) {
                        if (isNormalizedUrlValid(nextUrl, visitedUrls)) {
                            crawl(nextUrl, domain, depth + 1);
                        }
                    }
                }
            } catch (UnknownHostException e) {
                System.err.println("Unknown Host Exception: " + url);
            } catch (UnsupportedMimeTypeException e) {
                System.err.println("Unsupported MIME type for URL: " + url);
            } catch (IOException e) {
                System.err.println("Error visiting URL: " + url);
                e.printStackTrace();
            } finally {
                taskCounter.decrementAndGet(); // Decrement task counter
//                logger.info("Thread exit: " + Thread.currentThread().getName() + " - Exiting URL: " + url);
                if (taskCounter.get() == 0) {
                    latch.countDown(); // Signal that all tasks are completed
                }
            }
        });
    }
}
