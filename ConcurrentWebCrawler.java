package com.example.WebCrawler.ConcurrentWebCrawler;

import org.jsoup.Jsoup;
import org.jsoup.UnsupportedMimeTypeException;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;


import static com.example.WebCrawler.ConcurrentWebCrawler.Utils.*;
import static com.example.WebCrawler.ConcurrentWebCrawler.Validation.isUnsupportedMimeType;
import static com.example.WebCrawler.ConcurrentWebCrawler.Validation.isValid;

public class ConcurrentWebCrawler {

    private static  String startUrl;
    private static int MAX_DEPTH;
    private static ConcurrentHashMap<String, Boolean> visitedUrls;
    private static AtomicInteger taskCounter = new AtomicInteger(1);

    private static final Object lock = new Object();

    public ConcurrentWebCrawler(String startUrl, int maxDepth) {
        this.startUrl = startUrl;
        this.MAX_DEPTH = maxDepth;
        this.visitedUrls = new ConcurrentHashMap<>();
    }

    public void crawlStart(){
        String domain = getDomain(startUrl);
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        crawl(startUrl, domain, 0, executorService);
        try {
            while (taskCounter.get() > 0) {
                TimeUnit.SECONDS.sleep(1); // Sleep for a while
            }
        } catch (InterruptedException e) {
            System.err.println("Error waiting for tasks to complete: " + e.getMessage());
        }
        // Shutdown the executor service
        executorService.shutdown();
    }
    public static void crawl(String url, String domain, int depth, ExecutorService executor) {
        if (depth > MAX_DEPTH || visitedUrls.containsKey(url) ) {
            return;
        }

        visitedUrls.put(url, true);
        System.out.println("Visiting: " + url);
        System.out.println("Thread"+" "+executor);
//        System.out.println("HashMap of Visited urls "+ visitedUrls);

            executor.execute(() -> {
                try {

                    Document document = Jsoup.connect(url)
                            .get();
                    Elements links = document.select("a[href]");

                    for (Element link : links) {
                        String nextUrl = link.absUrl("href");
                        if (isValid(nextUrl, domain) && !isUnsupportedMimeType(getMimeType(nextUrl))) { // Check MIME type before attempting to fetch the page content
                            if(isNormalizedUrlValid(nextUrl)) {
                                taskCounter.incrementAndGet();
                                crawl(nextUrl, domain, depth + 1, executor);

                            }
                        }
                    }
                } catch (UnsupportedMimeTypeException e) {
                    System.err.println("Unsupported MIME type for URL: " + url);
                    // Handle gracefully, e.g., skip processing of this URL
                } catch (IOException e) {
                    System.err.println("Error visiting URL: " + url);
                    e.printStackTrace();
                } finally {
                    taskCounter.decrementAndGet();
                }
            });

    }

    /*
        nexturl -  https://monzo.com/security/#mainContent
        normalized url - https://monzo.com/security/

     */
   public static boolean isNormalizedUrlValid(String url){
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