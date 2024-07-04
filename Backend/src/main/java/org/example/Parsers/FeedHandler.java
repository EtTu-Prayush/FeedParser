package org.example.Parsers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;


/**
 * FeedHandler class
 * This class is used to handle the feeds.
 * It contains the following fields:
 * - feedQueue: the priority queue of feeds
 * - guids: the set of guids
 * - FILE_PATH: the file path of the feed
 * - objectMapper: the object mapper
 */

public class FeedHandler {
    private PriorityQueue<FeedData> feedQueue = new PriorityQueue<>();
    private Set<String> guids = new HashSet<>();
    private Set<String> titles = new HashSet<>();
    private static final String FILE_PATH = "src/main/java/org/example/utils/Feed.json";
    private ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Constructor
     * This constructor loads the feeds from the file.
     */
    public FeedHandler() {
        loadFeedsFromFile();
    }

    /**
     * Method to load feeds from file
     */
    private void loadFeedsFromFile() {
        try {
            File file = new File(FILE_PATH);
            if (file.exists()) {
                List<FeedData> feeds = objectMapper.readValue(file, new TypeReference<List<FeedData>>() {
                });
                for (FeedData feed : feeds) {
                    feedQueue.add(feed);
                    guids.add(feed.getGuid());
                    titles.add(feed.getTitle());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    /**
//     * Method to get the feed queue
//     * @return the feed queue
//     */
//    public void addFeed(FeedData feed) {
//        if (!guids.contains(feed.getGuid())) {
//            if(!titles.contains(feed.getTitle()))
//            {
//                feedQueue.add(feed);
//                guids.add(feed.getGuid());
//                saveFeedsToFile();
//            }
//        }
//    }

    /**
     * Method to get the feed queue
     * @return the feed queue
     */
    public void addFeed(FeedData feed) {
        if(!titles.contains(feed.getTitle()))
            {
                feedQueue.add(feed);
                titles.add(feed.getTitle());
                saveFeedsToFile();
            }
    }

//    public void addmetadataFeed(FeedData feed) {
//        if (!titles.contains(feed.getTitle())) {
//            feedQueue.add(feed);
//            titles.add(feed.getTitle());
//            saveFeedsToFile();
//        }
//    }

    /**
     * Method to add feeds
     * @param feeds: the list of feeds
     */
    public void addFeeds(List<FeedData> feeds) {
        for (FeedData feed : feeds) {
            addFeed(feed);
        }
    }

    /**
     * Method to save feeds to file
     * @return nothing, mapping the feeds to the file
     */
    private void saveFeedsToFile() {
        try {
            List<FeedData> feeds = List.copyOf(feedQueue);
            objectMapper.writeValue(new File(FILE_PATH), feeds);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to get the feed queue from the file "Feed.json"
     * @return the feed queue
     */
    public List<FeedData> getFeedsfromFile() {
        try {
            File file = new File(FILE_PATH);
            if (file.exists()) {
                List<FeedData> feeds = objectMapper.readValue(file, new TypeReference<List<FeedData>>() {
                });
                return feeds;
            }
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to retrieve feeds from file");
            return null;
        }
    }

    /**
     * Method to clear the file
     * @return nothing, just updating the file
     */
    public void clearFile() {
        try {
            File file = new File(FILE_PATH);
            if (file.exists()) {
                feedQueue.clear();
                file.delete();
                file.createNewFile();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to clear the feed queue
     * @return nothing
     */
    public void clearFeeds() {
        feedQueue.clear();
        guids.clear();
        titles.clear();
        clearFile();
        saveFeedsToFile();

    }

}
