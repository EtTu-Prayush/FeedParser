package org.example.Parsers;
import org.example.jsonfeed.Feed;
import org.example.jsonfeed.implementation.DefaultFeed;
import org.example.jsonfeed.Item;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Class to parse the structured JSON feed and convert it into a list of FeedData objects.
 */
public class ParseStructuredJSON {

    /**
     * Method to parse the structured JSON feed from the given content and convert it into a list of FeedData objects.
     *
     * @param content - JSON feed content
     * @return List of FeedData objects
     */
    public List<FeedData> parseFeedFromString(String content) {
        Feed feed = null;
        try {
            feed = DefaultFeed.fromString(content);
            if (!feed.isValid()) {
                System.out.println("Failed to parse JSON Feed or feed is invalid.");
                return List.of(); // Return empty list if feed is invalid
            }
        } catch (Exception e) {
            System.err.println("Failed to parse JSON Feed: " + e.getMessage());
            return List.of(); // Return empty list if there is an exception
        }

        final Feed finalFeed = feed; // Ensure feed is effectively final
        return finalFeed.getItemList().stream()
                .map(item -> convertItemToFeedData(item, finalFeed))
                .collect(Collectors.toList());
    }

    /**
     * Method to convert an Item object from the JSON feed to a FeedData object.
     *
     * @param item - Item object from the JSON feed
     * @param feed - Feed object containing the item
     * @return FeedData object
     */
    private FeedData convertItemToFeedData(Item item, Feed feed) {
        URL imageUrl = null;
        if (item.getImage() != null) {
            try {
                imageUrl = item.getImage();
            } catch (Exception e) {
                System.err.println("Malformed URL for image: " + item.getImage());
            }
        }

        String description = item.getContentText();

        if (description == null) {
            description = item.getContentHtml();
        }


        return new FeedData(
                item.getTitle(),
                description,
                item.getUrl(),
                item.getDatePublished().toString(),
                item.getID(),
                imageUrl,
                (item.getAuthor() != null ? item.getAuthor().getName() : null),
                item.getSummary(),
                null,
                (imageUrl != null ? imageUrl.toString() : null),
                (item.getImage() != null ? "image" : null),
                0L,
                0L

        );
    }
}
