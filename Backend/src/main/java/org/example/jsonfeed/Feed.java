/* Package location */
package org.example.jsonfeed;


/* Import Java stuff */
import java.net.URL;
import java.util.List;


/* Import JSON stuff */
import org.json.JSONObject;
import org.json.JSONString;

public interface Feed extends JSONString {

    public Version getVersion();
    public String getTitle();
    public Feed setTitle(String title);
    public URL getHomePageUrl();
    public Feed setHomePageUrl(URL homePageUrl);
    public URL getFeedUrl();
    public Feed setFeedUrl(URL feedUrl);
    public String getDescription();
    public Feed setDescription(String description);
    public String getUserComment();
    public Feed setUserComment(String userComment);
    public URL getNextUrl();
    public Feed setNextUrl(URL nextUrl);
    public URL getIcon();
    public Feed setIcon(URL icon);
    public URL getFavicon();
    public Feed setFavicon(URL favicon);
    public Author getAuthor();
    public Feed setAuthor(Author author);
    public List<Author> getAuthorList();
    public Feed setAuthorList(List<Author> authorList);
    public String getLanguage();
    public Feed setLanguage(String language);
    public Boolean getExpired();
    public Feed setExpired(Boolean expired);
    public List<Item> getItemList();
    public Feed setItemList(List<Item> itemList);
    public JSONObject getExtensionsJSONObject();
    public Feed setExtensionsJSONObject(JSONObject extensionsJsonObject);
    public boolean isValid();
    public String toJSONString();


}
