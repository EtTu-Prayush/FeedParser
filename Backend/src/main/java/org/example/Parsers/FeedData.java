package org.example.Parsers;
import java.net.URL;
import java.util.List;
import com.apptasticsoftware.rssreader.*;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.utils.ConvertHTML;
import org.example.utils.ConvertTime;

/**
 * FeedData class
 * This class is used to store the data from the RSS feed.
 * It contains the following fields:
 * - title: the title of the feed
 * - description: the description of the feed
 * - link: the link of the feed
 * - publishDate: the publish date of the feed
 * - guid: the guid of the feed
 * - image: the image of the feed
 * - author: the author of the feed
 * - comments: the comments of the feed
 * - enclosures: the enclosures of the feed
 */

public class FeedData implements Comparable<FeedData>{
    private String title;
    private String description;
    private URL link;
    private String publishDate;
    private String guid;
    private URL image;
    private String author;
    private String comments;
    private List<Enclosure> enclosures;
    private long epochTime;
    private String pubDateParsed;


    // Fields for image details
    private String imgURL;
    private String imgType;
    private Long imgLength;
    private Long imgWidth;

    // Default constructor
    public FeedData() {}

    /**
     * Constructor
     * @param title: the title of the feed
     * @param description: the description of the feed
     * @param link: the link of the feed
     * @param publishDate: the publish date of the feed
     * @param guid: the guid of the feed
     * @param image: the image of the feed
     * @param author: the author of the feed
     * @param comments: the comments of the feed
     * @param enclosures: the enclosures of the feed
     * @param imgURL: the URL of the image
     * @param imgType: the type of the image
     * @param imgLength: the length of the image
     * @param imgWidth: the width of the image
     */
    @JsonCreator
    public FeedData(@JsonProperty("title") String title,
                    @JsonProperty("description") String description,
                    @JsonProperty("link") URL link,
                    @JsonProperty("publishDate") String publishDate,
                    @JsonProperty("guid") String guid,
                    @JsonProperty("image") URL image,
                    @JsonProperty("author") String author,
                    @JsonProperty("comments") String comments,
                    @JsonProperty("enclosures") List<Enclosure> enclosures,
                    @JsonProperty("imgURL") String imgURL,
                    @JsonProperty("imgType") String imgType,
                    @JsonProperty("imgLength") Long imgLength,
                    @JsonProperty("imgWidth") Long imgWidth) {
        this.title = title;
        this.description = convertDescription(description);
        this.link = link;
        this.publishDate = publishDate;
        this.guid = guid;
        this.image = image;
        this.author = author;
        this.comments = comments;
        this.enclosures = enclosures;
        this.imgURL = imgURL;
        this.imgType = imgType;
        this.imgLength = imgLength;
        this.imgWidth = imgWidth;
        this.epochTime = convertPublishDateToEpoch(publishDate);
//        this.pubDateParsed = timeParsed(publishDate);
    }

    /**
     * Parse the time
     * @param timeStr: the time string
     * @return the parsed time
     */
    private String timeParsed(String timeStr) {
        ConvertTime converter = new ConvertTime();
        return converter.timeParsed(timeStr);
    }

    /**
     * Convert the publish date to epoch time
     * @param publishDate: the publish date
     * @return the epoch time
     */
    private long convertPublishDateToEpoch(String publishDate) {
        ConvertTime converter = new ConvertTime();
        return converter.convert(publishDate);
    }

    /**
     * Convert the description to HTML
     * @param description: the description
     * @return the converted description
     */
    private String convertDescription(String description) {
        return ConvertHTML.convert(description);
    }



    // Getters
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public URL getLink() { return link; }
    public String getPublishDate() { return publishDate; }
    public String getGuid() { return guid; }
    public URL getImage() { return image; }
    public String getAuthor() { return author; }
    public String getComments() { return comments; }
    public List<Enclosure> getEnclosures() { return enclosures; }
    public String getImgURL() { return imgURL; }
    public String getImgType() { return imgType; }
    public Long getImgLength() { return imgLength; }
    public Long getImgWidth() { return imgWidth; }

    // Setters
    public void setImage(URL imgURL) { this.image = imgURL; }
    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLink(URL link) {
        this.link = link;
    }

    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate;
    }

    public void setImageURL(String imgURL) {
        this.imgURL = imgURL;
    }


    @Override
    public int compareTo(FeedData other) {
        return Long.compare(this.epochTime, other.epochTime);
    }

    /**
     * Override the toString method
     * @return the string representation of the FeedData object
     */
    @Override
    public String toString() {
        return "FeedData{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", link=" + link +
                ", publishDate='" + publishDate + '\'' +
                ", guid='" + guid + '\'' +
                ", image=" + image +
                ", author='" + author + '\'' +
                ", comments='" + comments + '\'' +
                ", enclosures=" + enclosures +
                ", imgURL='" + imgURL + '\'' +
                ", imgType='" + imgType + '\'' +
                ", imgLength=" + imgLength +
                ", imgWidth=" + imgWidth +
                ", epochTime=" + epochTime +
                ", pubDateParsed='" + pubDateParsed + '\'' +
                '}';
    }
}
// Path: src/main/java/org/example/ParseRSS.java