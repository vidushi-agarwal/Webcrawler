package com.example.WebCrawler.ConcurrentWebCrawler;

import com.example.WebCrawler.ConcurrentWebCrawler.ConcurrentWebCrawler;

public class ConcurrentWebCrawlerRunner {

    public static void main(String args[]) {
        String startUrl = "https://monzo.com/security/#mainContent";
        int MAX_DEPTH = 2;
        ConcurrentWebCrawler webCrawler = new ConcurrentWebCrawler(startUrl, 2);
         webCrawler.crawlStart();
    }
}
