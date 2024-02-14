package com.example.WebCrawler.ConcurrentWebCrawler.src;

public class ConcurrentWebCrawlerRunner {

    public static void main(String args[]) {
        String startUrl = "https://monzo.com/";
        int MAX_DEPTH = 2;
        int threadCount = 10;
        ConcurrentWebCrawler webCrawler = new ConcurrentWebCrawler(startUrl, MAX_DEPTH, threadCount);
         webCrawler.crawlStart();
    }
}
