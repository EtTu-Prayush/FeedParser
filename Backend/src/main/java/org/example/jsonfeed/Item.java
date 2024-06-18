package org.example.jsonfeed;


/* Import Java stuff */
import java.net.URL;
import java.time.Instant;
import java.util.List;


/* Import JSON stuff */
import org.json.JSONObject;
import org.json.JSONString;

public interface Item extends JSONString {

    public String getID();
    public Item setID(String id);
    public URL getUrl();
    public Item setUrl(URL url);
    public URL getExternalUrl();
    public Item setExternalUrl(URL externalUrl);
    public String getTitle();
    public Item setTitle(String title);
    public String getContentText();
    public Item setContentText(String contentText);
    public String getContentHtml();
    public Item setContentHtml(String contentHtml);
    public String getSummary();
    public Item setSummary(String summary);
    public URL getImage();
    public Item setImage(URL image);
    public URL getBannerImage();
    public Item setBannerImage(URL bannerImage);
    public Instant getDatePublished();
    public Item setDatePublished(Instant datePublished);
    public Instant getDateModified();
    public Item setDateModified(Instant dateModified);
    public Author getAuthor();
    public Item setAuthor(Author author);
    public List<Author> getAuthorList();
    public Item setAuthorList(List<Author> authorList);
    public List<String> getTagList();
    public Item setTagList(List<String> tagList);
    public String getLanguage();
    public Item setLanguage(String language);
    public List<Attachment> getAttachmentList();
    public Item setAttachmentList(List<Attachment> attachmentList);
    public JSONObject getExtensionsJSONObject();
    public Item setExtensionsJSONObject(JSONObject extensionsJsonObject);
    public boolean isValid();
    public String toJSONString();

}
