package org.example.Parsers;

import com.apptasticsoftware.rssreader.*;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Class to parse the RSS feed and convert it into a list of FeedData objects.
 */
public class ParseRSS {
    private static final Logger logger = LogManager.getLogger(ParseRSS.class);

    /**
     * Method to parse the RSS feed from the given URL and convert it into a list of FeedData objects.
     *
     * @param url - URL of the RSS feed
     * @return List of FeedData objects
     */
    public List<FeedData> parseFeed(String url) {
        RssReader rssReader = new RssReader();
        List<Item> items;
        try {
            items = rssReader.read(url).toList();
        } catch (Exception e) {
            logger.error("Error fetching RSS feed: " + url, e);
            return List.of(); // Return an empty list in case of failure
        }

        return items.stream().map(this::convertToFeedData).collect(Collectors.toList());
    }

    /**
     * Method to convert an Item object from the RSS feed to a FeedData object.
     *
     * @param item - Item object from the RSS feed
     * @return FeedData object
     */
    private FeedData convertToFeedData(Item item) {
        URL linkUrl = convertStringToURL(item.getLink().orElse(null));
        List<Enclosure> enclosures = item.getEnclosures();

        // Extracting image details from the first enclosure, if available
        String imgURL = enclosures.isEmpty() ? null : enclosures.get(0).getUrl();
        String imgType = enclosures.isEmpty() ? null : enclosures.get(0).getType();
        Long imgLength = enclosures.isEmpty() ? null : enclosures.get(0).getLength().orElse(null);

        return new FeedData(
                item.getTitle().orElse("No title"),
                item.getDescription().orElse("No description"),
                linkUrl,
                item.getPubDate().orElse(null),
                item.getGuid().orElse(null),
                null, // image URL from an enclosure if needed
                item.getAuthor().orElse(null),
                item.getComments().orElse(null),
                enclosures,
                imgURL,
                imgType,
                imgLength,
                0L
        );
    }


    /**
     * Method to convert a string to a URL.
     *
     * @param urlString - String to be converted to URL
     * @return URL object
     */
    private URL convertStringToURL(String urlString) {
        if (urlString == null) return null;
        try {
            return new URL(urlString);
        } catch (Exception e) {
            logger.error("Invalid URL: " + urlString, e);
            return null;
        }
    }
}
