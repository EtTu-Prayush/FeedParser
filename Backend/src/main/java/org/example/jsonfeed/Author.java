/**
 * Author.java
 *
 * @author Francois Schiettecatte
 * @version 1.0
 *
 * Change History:
 *    - Nov 3, 2020 - File was created
 *
 * TBD:
 *    -
 *
 *
 */


/* Package location */
package org.example.jsonfeed;


/* Import Java stuff */
import java.net.URL;


/* Import JSON stuff */
import org.json.JSONObject;
import org.json.JSONString;

public interface Author extends JSONString {

    public String getName();
    public Author setName(String name);
    public URL getUrl();
    public Author setUrl(URL url);
    public URL getAvatar();
    public Author setAvatar(URL avatar);
    public JSONObject getExtensionsJSONObject();
    public Author setExtensionsJSONObject(JSONObject extensionsJsonObject);
    public boolean isValid();
    public String toJSONString();


}
