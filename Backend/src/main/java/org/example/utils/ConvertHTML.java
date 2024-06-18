package org.example.utils;

/**
 * Dependencies: Jsoup to parse HTML
 */

import org.jsoup.Jsoup;

/**
 * Convert HTML to plain text
 */
public class ConvertHTML {

    /**
     * Check if the text contains HTML tags
     * @param text
     * @return true if the text contains HTML tags
     */
    public static boolean isHtml(String text) {
        if (text == null || text.isEmpty()) {
            return false;
        }
        return text.indexOf('<') != -1 && text.indexOf('>') != -1;
    }

    /**
     * Convert HTML to plain text
     * @param text
     * @return plain text
     */
    public static String convert(String text) {
        if (isHtml(text)) {
            return Jsoup.parse(text).text();
        } else {
            return text;
        }
    }
}

