


/* Package location */
package org.example.jsonfeed.implementation;


/* Import Java stuff */
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.Reader;
import java.lang.StringBuilder;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;



/* Import JSON stuff */
import org.example.jsonfeed.Feed;
import org.example.jsonfeed.Item;
import org.example.jsonfeed.Version;
import org.json.JSONObject;


/* Import JSONFeed stuff */
import org.example.jsonfeed.Author;


public class DefaultFeed implements Feed {


    /**
     * Version, defaults to 1.0
     */
    private Version version = Version.VERSION_1_0;


    /**
     * Title
     */
    private String title = null;


    /**
     * Home page URL
     */
    private URL homePageUrl = null;


    /**
     * Feed URL
     */
    private URL feedUrl = null;


    /**
     * Description
     */
    private String description = null;


    /**
     * User comment
     */
    private String userComment = null;


    /**
     * Next URL
     */
    private URL nextUrl = null;


    /**
     * Icon (URL)
     */
    private URL icon = null;


    /**
     * Favicon (URL)
     */
    private URL favicon = null;


    /**
     * Author
     */
    private Author author = null;


    /**
     * Author list (JSON Feed 1.1 only)
     */
    private List<Author> authorList = new ArrayList<Author>();


    /**
     * Language (JSON Feed 1.1 only)
     */
    private String language = null;


    /**
     * Expired
     */
    private Boolean expired = null;





    /**
     * Item list
     */
    private List<Item> itemList = new ArrayList<Item>();


    /**
     * Extensions JSON object
     */
    private JSONObject extensionsJsonObject = new JSONObject();




    /**
     * Parse a JSON feed string and return the feed
     *
     * @param   feedString  the JSON feed string
     *
     * @return  the feed object
     *
     * @exception   MalformedURLException
     *              If the home page URL is invalid
     *
     * @exception   MalformedURLException
     *              If the feed URL is invalid
     *
     * @exception   MalformedURLException
     *              If the next URL is invalid
     *
     * @exception   MalformedURLException
     *              If the icon (URL) is invalid
     *
     * @exception   MalformedURLException
     *              If the favicon (URL) is invalid
     *
     * @exception   IllegalArgumentException
     *              If the version is missing or invalid
     */
    public static Feed fromString(final String feedString) throws MalformedURLException {

        /* Parse the JSON string to a JSON object */
        final JSONObject jsonObject = new JSONObject(feedString);

        /* Create a new feed from the JSON object */
        final Feed feed = new DefaultFeed(jsonObject);

        /* Return the feed */
        return (feed);

    }


    /**
     * Constructor
     *
     * @param   jsonObject  the feed as a JSON object
     *
     * @exception   MalformedURLException
     *              If the home page URL is invalid
     *
     * @exception   MalformedURLException
     *              If the feed URL is invalid
     *
     * @exception   MalformedURLException
     *              If the next URL is invalid
     *
     * @exception   MalformedURLException
     *              If the icon (URL) is invalid
     *
     * @exception   MalformedURLException
     *              If the favicon (URL) is invalid
     *
     * @exception   IllegalArgumentException
     *              If the version is missing or invalid
     */
    protected DefaultFeed(final JSONObject jsonObject) throws MalformedURLException {

        /* Get the version, required */
        if ( jsonObject.has("version") == true ) {
            this.version = Version.fromString(jsonObject.getString("version"));
            if ( this.version == null ) {
                throw new IllegalArgumentException("Invalid version value");
            }
        }
        else {
            throw new IllegalArgumentException("Missing version");
        }

        /* Get the title */
        this.setTitle(jsonObject.optString("title", null));

        /* Get the home page URL */
        if ( jsonObject.has("home_page_url") == true ) {
            this.setHomePageUrl(new URL(jsonObject.getString("home_page_url")));
        }

        /* Get the feed URL */
        if ( jsonObject.has("feed_url") == true ) {
            this.setFeedUrl(new URL(jsonObject.getString("feed_url")));
        }

        /* Get the description */
        this.setDescription(jsonObject.optString("description", null));

        /* Get the user comment */
        this.setUserComment(jsonObject.optString("user_comment", null));

        /* Get the next URL */
        if ( jsonObject.has("next_url") == true ) {
            this.setNextUrl(new URL(jsonObject.getString("next_url")));
        }

        /* Get the icon (URL) */
        if ( jsonObject.has("icon") == true ) {
            this.setIcon(new URL(jsonObject.getString("icon")));
        }

        /* Get the favicon (URL) */
        if ( jsonObject.has("favicon") == true ) {
            this.setFavicon(new URL(jsonObject.getString("favicon")));
        }

        /* Get the language */
        if ( jsonObject.has("language") == true ) {
            this.setLanguage(jsonObject.getString("language"));
        }

        /* Get the expired */
        if ( jsonObject.has("expired") == true ) {
            this.setExpired(jsonObject.getBoolean("expired"));
        }

        /* Get the author */
        if ( jsonObject.has("author") == true ) {
            this.setAuthor(new DefaultAuthor(jsonObject.getJSONObject("author")));
        }

        /* Get the authors */
        if ( jsonObject.has("authors") == true ) {
            this.setAuthorList(DefaultAuthor.fromJsonArray(jsonObject.getJSONArray("authors")));
        }


        /* Get the items */
        if ( jsonObject.has("items") == true ) {
            this.setItemList(DefaultItem.fromJsonArray(jsonObject.getJSONArray("items")));
        }


        /* Get the extensions */
        for ( final Map.Entry<String, Object> entry : jsonObject.toMap().entrySet() ) {
            if ( entry.getKey().startsWith("_") == true ) {
                this.extensionsJsonObject.put(entry.getKey(), entry.getValue());
            }
        }


        /* Normalize the feed */
        this.normalize();

    }


    /**
     * Get the version
     *
     * @return  the version, null if not specified
     */
    @Override
    public Version getVersion() {

        return (this.version);

    }



    /**
     * Get the title
     *
     * @return  the title, null if not specified
     */
    @Override
    public String getTitle() {

        return (this.title);

    }



    /**
     * Set the title
     *
     * @param   title  the title
     *
     * @return  the feed
     */
    @Override
    public Feed setTitle(String title) {

        this.title = title;
        return (this);

    }



    /**
     * Get the home page URL
     *
     * @return  the home page URL, null if not specified
     */
    @Override
    public URL getHomePageUrl() {

        return (this.homePageUrl);

    }



    /**
     * Set the home page URL
     *
     * @param   homePageUrl     the home page URL
     *
     * @return  the feed
     */
    @Override
    public Feed setHomePageUrl(URL homePageUrl) {

        this.homePageUrl = homePageUrl;
        return (this);

    }



    /**
     * Get the feed URL
     *
     * @return  the feed URL, null if not specified
     */
    @Override
    public URL getFeedUrl() {

        return (this.feedUrl);

    }



    /**
     * Set the feed URL
     *
     * @param   feedUrl     the feed URL
     *
     * @return  the feed
     */
    @Override
    public Feed setFeedUrl(URL feedUrl) {

        this.feedUrl = feedUrl;
        return (this);

    }



    /**
     * Get the description
     *
     * @return  the description, null if not specified
     */
    @Override
    public String getDescription() {

        return (this.description);

    }



    /**
     * Set the description
     *
     * @param   description  the description
     *
     * @return  the feed
     */
    @Override
    public Feed setDescription(String description) {

        this.description = description;
        return (this);

    }



    /**
     * Get the user comment
     *
     * @return  the user comment, null if not specified
     */
    @Override
    public String getUserComment() {

        return (this.userComment);

    }



    /**
     * Set the user comment
     *
     * @param   userComment  the user comment
     *
     * @return  the feed
     */
    @Override
    public Feed setUserComment(String userComment) {

        this.userComment = userComment;
        return (this);

    }



    /**
     * Get the next URL
     *
     * @return  the next URL, null if not specified
     */
    @Override
    public URL getNextUrl() {

        return (this.nextUrl);

    }



    /**
     * Set the next URL
     *
     * @param   nextUrl     the next URL
     *
     * @return  the feed
     */
    @Override
    public Feed setNextUrl(URL nextUrl) {

       this.nextUrl = nextUrl;
       return (this);

    }



    /**
     * Get the icon (URL)
     *
     * @return  the icon URL, null if not specified
     */
    @Override
    public URL getIcon() {

        return (this.icon);

    }



    /**
     * Set the icon (URL)
     *
     * @param   icon     the icon URL
     *
     * @return  the feed
     */
    @Override
    public Feed setIcon(URL icon) {

        this.icon = icon;
        return (this);

    }



    /**
     * Get the favicon (URL)
     *
     * @return  the favicon URL, null if not specified
     */
    @Override
    public URL getFavicon() {

        return (this.favicon);

    }



    /**
     * Set the favicon (URL)
     *
     * @param   favicon     the favicon URL
     *
     * @return  the feed
     */
    @Override
    public Feed setFavicon(URL favicon) {

        this.favicon = favicon;
        return (this);

    }



    /**
     * Get the author
     *
     * @return  the author, null if not specified
     */
    @Override
    public Author getAuthor() {

        return (this.author);

    }



    /**
     * Set the author
     *
     * @param   author     the author
     *
     * @return  the feed
     */
    @Override
    public Feed setAuthor(Author author) {

        this.author = author;
        return (this);

    }



    /**
     * Get the author list (JSON Feed 1.1 only)
     *
     * @return  the author list, empty list if there are no authors
     */
    @Override
    public List<Author> getAuthorList() {

        return (this.authorList);

    }



    /**
     * Set the author list (JSON Feed 1.1 only)
     *
     * @param   authorList  the author list
     *
     * @return  the feed
     */
    @Override
    public Feed setAuthorList(List<Author> authorList) {

        this.authorList = authorList;
        return (this);

    }



    /**
     * Get the language (JSON Feed 1.1 only)
     *
     * @return  the language, null if not specified
     */
    @Override
    public String getLanguage() {

        return (this.language);

    }



    /**
     * Set the language (JSON Feed 1.1 only)
     *
     * @param   language  the language
     *
     * @return  the feed
     */
    @Override
    public Feed setLanguage(String language) {

        this.language = language;
        return (this);

    }



    /**
     * Get the expired
     *
     * @return  the expired, null if not specified
     */
    @Override
    public Boolean getExpired() {

        return (this.expired);

    }



    /**
     * Set the expired
     *
     * @param   expired     the expired
     *
     * @return  the feed
     */
    @Override
    public Feed setExpired(Boolean expired) {

        this.expired = expired;
        return (this);

    }





    /**
     * Get the item list
     *
     * @return  the item list, empty list if there are no items
     */
    @Override
    public List<Item> getItemList() {

        return (this.itemList);

    }







    /**
     * Get feed extensions as a JSON object
     *
     * @return  the extensions JSON object
     */
    @Override
    public JSONObject getExtensionsJSONObject() {

        return (this.extensionsJsonObject);

    }



    /**
     * Set the feed extensions JSON object
     *
     * @param   extensionsJsonObject  the extensions JSON object
     *
     * @return  the feed
     */
    @Override
    public Feed setExtensionsJSONObject(JSONObject extensionsJsonObject) {

        this.extensionsJsonObject = extensionsJsonObject;
        return (this);

    }



    /**
     * Check the validity of the feed object
     *
     * @return  true if the feed object is valid
     */
    @Override
    public boolean isValid() {

        /* Check the feed fields */
        if ( this.getVersion() == null ) {
            return (false);
        }

        if ( this.getTitle() == null ) {
            return (false);
        }

        if ( this.getItemList() == null ) {
            return (false);
        }

        if ( this.getItemList().isEmpty() == true ) {
            return (false);
        }

        return (true);

    }



    /**
     * Upgrade this feed to the passed version
     *
     * @param   toVersion       to version
     *
     * @return  true if the feed was upgraded
     */

    /**
     * Upgrade this feed to the passed version
     *
     * @param   toVersion       to version
     *
     * @return  true if the feed was upgraded
     */
    public boolean upgrade(final Version toVersion) {

        /* Upgrade flag */
        boolean feedUpgraded = false;

        /* Upgrade to version 1.1 */
        if ( toVersion == Version.VERSION_1_1 ) {

            /* Upgrade the feed author */
            if ( this.getAuthor() != null ) {
                this.authorList.add(this.getAuthor());
                this.setAuthor(null);
                feedUpgraded = true;
            }

            /* Upgrade every item */
            for ( final Item item : this.getItemList() ) {
                feedUpgraded |= ((DefaultItem)item).upgrade(toVersion);
            }

            /* Update the version number */
            if ( this.getVersion() != toVersion ) {
                this.version = toVersion;
                feedUpgraded = true;
            }
        }

        /* Feed was not upgraded */
        return (feedUpgraded);

    }

    /**
     * Set the item list
     *
     * @param   itemList  the item list
     *
     * @return  the feed
     */
    @Override
    public Feed setItemList(List<Item> itemList) {

        /* Upgrade every item */
        if ( itemList != null ) {
            for ( final Item item : itemList ) {
                ((DefaultItem)item).upgrade(this.getVersion());
            }
        }

        this.itemList = itemList;
        return (this);

    }




    /**
     * Normalize this feed if needed
     *
     * @return  true if the feed was normalized
     */
    private boolean normalize() {

        /* Detected versions */
        boolean detectedVersion_1_0 = false;
        boolean detectedVersion_1_1 = false;

        /* Check language */
        if ( this.getLanguage() != null ) {
            detectedVersion_1_1 = true;
        }

        /* Check author list */
        if ( (this.getAuthorList() != null) && (this.getAuthorList().size() > 0) ) {
            detectedVersion_1_1 = true;
        }

        /* Check author */
        if ( this.getAuthor() != null ) {
            detectedVersion_1_0 = true;
        }

        /* Check item language / author list / author */
        for ( final Item item : this.getItemList() ) {
            if ( item.getLanguage() != null ) {
                detectedVersion_1_1 = true;
            }
            if ( (item.getAuthorList() != null) && (item.getAuthorList().size() > 0) ) {
                detectedVersion_1_1 = true;
            }
            if ( this.getAuthor() != null ) {
                detectedVersion_1_0 = true;
            }
        }


        /* Both version 1.0 and 1.1 elements were detected, upgrade to 1.1 */
        if ( (detectedVersion_1_0 == true) && (detectedVersion_1_1 == true) ) {
            return (this.upgrade(Version.VERSION_1_1));
        }

        /* Version 1.1 elements were detected and the feed version is 1.0, upgrade to 1.1 */
        else if ( (detectedVersion_1_1 == true) && (this.getVersion() == Version.VERSION_1_0) ) {
            return (this.upgrade(Version.VERSION_1_1));
        }

        /* Version 1.0 elements were detected and the feed version is 1.1, upgrade to 1.1 */
        else if ( (detectedVersion_1_0 == true) && (this.getVersion() == Version.VERSION_1_1) ) {
            return (this.upgrade(Version.VERSION_1_1));
        }


        /* Feed was not normalized */
        return (false);

    }



    /**
     * Return the JSON string representation for this object
     *
     * The feed will be upgaded from version 1.0 to 1.1 if needed
     *
     * @return      the JSON string representation for this object
     */
    @Override
    public String toJSONString() {

        /* Normalize the feed */
        this.normalize();


        /* Create the JSON object */
        final JSONObject jsonObject = new JSONObject();

        /* Add the version */
        jsonObject.put("version", this.getVersion());

        /* Add the title */
        jsonObject.put("title", this.getTitle());

        /* Add the home page URL */
        if ( this.getHomePageUrl() != null ) {
            jsonObject.put("home_page_url", this.getHomePageUrl().toString());
        }

        /* Add the feed URL */
        if ( this.getFeedUrl() != null ) {
            jsonObject.put("feed_url", this.getFeedUrl().toString());
        }

        /* Add the description */
        if ( this.getDescription() != null ) {
            jsonObject.put("description", this.getDescription());
        }

        /* Add the user comment */
        if ( this.getUserComment() != null ) {
            jsonObject.put("user_comment", this.getUserComment());
        }

        /* Add the next URL */
        if ( this.getFeedUrl() != null ) {
            jsonObject.put("next_url", this.getFeedUrl().toString());
        }

        /* Add the icon (URL) */
        if ( this.getIcon() != null ) {
            jsonObject.put("icon", this.getIcon().toString());
        }

        /* Add the favicon (URL) */
        if ( this.getFavicon() != null ) {
            jsonObject.put("favicon", this.getFavicon().toString());
        }

        /* Add the authors */
        if ( (this.getAuthorList() != null) && (this.getAuthorList().size() > 0) ) {
            jsonObject.put("authors", this.getAuthorList());
        }
        /* Add the author */
        else if ( this.getAuthor() != null ) {
            jsonObject.put("author", this.getAuthor());
        }

        /* Add the language */
        if ( this.getLanguage() != null ) {
            jsonObject.put("language", this.getLanguage());
        }

        /* Add the expired */
        if ( this.getExpired() != null ) {
            jsonObject.put("expired", this.getExpired());
        }


        /* Add the items */
        if ( (this.getItemList() != null) && (this.getItemList().size() > 0) ) {
            jsonObject.put("items", this.getItemList());
        }

        /* Add the extensions */
        if ( this.getExtensionsJSONObject() != null ) {
            for ( final Map.Entry<String, Object> entry : this.getExtensionsJSONObject().toMap().entrySet() ) {
                if ( entry.getKey().startsWith("_") == true ) {
                    jsonObject.put(entry.getKey(), entry.getValue());
                }
            }
        }

        /* Get the JSON string */
        final String jsonString = jsonObject.toString();

        /* Return the JSON string */
        return (jsonString);

    }


}
