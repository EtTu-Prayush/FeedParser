/* Package location */
package org.example.jsonfeed;


/* Import Java stuff */
import java.net.URL;


/* Import JSON stuff */
import org.json.JSONObject;
import org.json.JSONString;



public interface Attachment extends JSONString {


    public URL getUrl();
    public Attachment setUrl(URL url);
    public String getMimeType();
    public Attachment setMimeType(String mimeType);
    public String getTitle();
    public Attachment setTitle(String title);
    public Integer getSizeInBytes();
    public Attachment setSizeInBytes(Integer sizeInBytes);
    public Integer getDurationInSeconds();
    public Attachment setDurationInSeconds(Integer durationInSeconds);
    public JSONObject getExtensionsJSONObject();
    public Attachment setExtensionsJSONObject(JSONObject extensionsJsonObject);
    public boolean isValid();
    public String toJSONString();


}
