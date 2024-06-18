package org.example;

import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import org.example.Classifiers.ClassifyJSON;
import org.example.Parsers.FeedData;
import org.example.Parsers.ParseRSS;
import org.example.Parsers.ParseStructuredJSON;
import org.example.Parsers.ParseTree;

import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

public class Main {
    private static final ParseTree parseTree = new ParseTree();
    private static final Map<String, Map<String, String>> metadataMap = new HashMap<>();

////     Main method for testing purposes, to be commented out in production
//     public static void main(String[] args) {
//         String feedUrl = "https://abcnews.go.com/abcnews/usheadlines";
//         Map<String, Object> result = parseFeed(feedUrl);
//         if (result.containsKey("unstructured")) {
//             System.out.println("The feed is unstructured. Please provide metadata.");
//         } else {
//             List<FeedData> feedDataList = (List<FeedData>) result.get("feedDataList");
//             feedDataList.forEach(System.out::println);
//
//             FeedHandler feedHandler = new FeedHandler();
//             feedHandler.addFeeds(feedDataList);
//         }
//     }

    /**
     * Method to parse the feed from the given URL.
     * The feed can be in the form of RSS or JSON.
     * The feed data is classified as structured or unstructured JSON using the ClassifyJSON class.
     *
     * @param feedUrl - URL of the feed
     * @return Map with keys "feedDataList" or "unstructured"
     */
    public static Map<String, Object> parseFeed(String feedUrl) {
        Map<String, Object> result = new HashMap<>();
        String content = fetchContentFromURL(feedUrl);
        List<FeedData> feedDataList = new ArrayList<>();

        if (isJSON(content)) {
            ClassifyJSON classifier = new ClassifyJSON();
            boolean isStructured = classifier.isStandardJSON(content);
            if (isStructured) {
                ParseStructuredJSON jsonParser = new ParseStructuredJSON();
                List<FeedData> jsonDataList = jsonParser.parseFeedFromString(content);
                feedDataList.addAll(jsonDataList);
                result.put("feedDataList", feedDataList);
            } else {
                System.out.println("#Main: The JSON feed is unstructured.");
                result.put("unstructured", true);
                result.put("content", content);
            }
        } else {
            String feedType = validateFeed(feedUrl);
            if (feedType != null) {
                System.out.println("Feed type is: " + feedType);
                ParseRSS rssParser = new ParseRSS();
                List<FeedData> rssFeedData = rssParser.parseFeed(feedUrl);
                feedDataList.addAll(rssFeedData);
                result.put("feedDataList", feedDataList);
            } else {
                System.out.println("The feed is not properly structured. Please provide metadata.");
                result.put("unstructured", true);
                result.put("content", content);
                return result;
            }
        }
        return result;
    }


    /**
     * Method to check if the content is JSON.
     *
     * @param content - Content to be checked
     * @return true if the content is JSON, false otherwise
     */
    private static boolean isJSON(String content) {
        return content.trim().startsWith("{") || content.trim().startsWith("[");
    }

    /**
     * Method to fetch content from the given URL.
     *
     * @param urlString - URL of the content
     * @return Content fetched from the URL
     */
    public static String fetchContentFromURL(String urlString) {
        StringBuilder content = new StringBuilder();
        try {
            URL url = new URL(urlString);
            try (BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()))) {
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
            }
        } catch (Exception e) {
            System.err.println("Failed to retrieve content from URL: " + urlString);
            e.printStackTrace();
        }
        return content.toString();
    }

    /**
     * Method to validate the feed type.
     *
     * @param feedUrl - URL of the feed
     * @return Feed type if valid, null otherwise
     */
    public static String validateFeed(String feedUrl) {
        try {
            SyndFeedInput input = new SyndFeedInput();
            SyndFeed feed = input.build(new XmlReader(new URL(feedUrl)));
            if (feed.getFeedType().equalsIgnoreCase("rss_1.0") ||
                    feed.getFeedType().equalsIgnoreCase("rss_2.0") ||
                    feed.getFeedType().equalsIgnoreCase("atom_1.0") ||
                    feed.getFeedType().equalsIgnoreCase("rdf")) {
                return feed.getFeedType();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

