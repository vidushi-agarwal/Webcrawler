package com.example.WebCrawler.ConcurrentWebCrawler.test;

import com.example.WebCrawler.ConcurrentWebCrawler.src.Validation;
import org.junit.jupiter.api.Test;

import static com.example.WebCrawler.ConcurrentWebCrawler.src.Validation.isUnsupportedMimeType;
import static com.example.WebCrawler.ConcurrentWebCrawler.src.Validation.isValid;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ValidationTests {

    /**
     * Method under test:
     * {@link Validation#isUnsupportedMimeType(String)}
     */
    @Test
    public void testIsUnsupportedMimeType_PDF() {
        String pdfMimeType = "application/pdf";
        assertTrue(isUnsupportedMimeType(pdfMimeType));
    }

    /**
     * Method under test:
     * {@link Validation#isUnsupportedMimeType(String)}
     */
    @Test
    public void testIsUnsupportedMimeType_Image() {
        String imageMimeType = "image/png";
        assertTrue(isUnsupportedMimeType(imageMimeType));
    }

    /**
     * Method under test:
     * {@link Validation#isUnsupportedMimeType(String)}
     */
    @Test
    public void testIsUnsupportedMimeType_Html() {
        String htmlMimeType = "text/html";
        assertFalse(isUnsupportedMimeType(htmlMimeType));
    }

    /**
     * Method under test:
     * {@link Validation#isValid(String,String)}
     */
    @Test
    public void testIsValid_ValidURL() {
        String url = "https://example.com";
        String domain = "example.com";
        assertTrue(isValid(url, domain));
    }

    /**
     * Method under test:
     * {@link Validation#isValid(String,String)}
     */
    @Test
    public void testIsValid_InvalidURL() {
        String url = "https://example.com";
        String domain = "anotherdomain.com";
        assertFalse(isValid(url, domain));
    }

}
