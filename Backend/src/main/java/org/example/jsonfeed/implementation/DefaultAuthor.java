/**
 * DefaultAuthor.java
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
package org.example.jsonfeed.implementation;


/* Import Java stuff */
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/* Import JSON stuff */
import org.json.JSONArray;
import org.json.JSONObject;


/* Import JSONFeed stuff */
import org.example.jsonfeed.Author;

public class DefaultAuthor implements Author {

    private String name = null;

    private URL url = null;

    private URL avatar = null;

    private JSONObject extensionsJsonObject = new JSONObject();

    protected static Author fromString(final String jsonString) throws MalformedURLException {

        /* Parse the JSON string to a JSON object */
        final JSONObject jsonObject = new JSONObject(jsonString);

        /* Parse the JSON string */
        final Author author = new DefaultAuthor(jsonObject);

        /* Return the author */
        return (author);

    }

    protected static List<Author> fromJsonArray(final JSONArray jsonArray) throws MalformedURLException {

        /* Create the author list */
        final List<Author> authorList = new ArrayList<Author>();

        /* Process the JSON array */
        for ( final Object object : jsonArray ) {
            authorList.add(new DefaultAuthor((JSONObject)object));
        }

        /* Return the author list */
        return (authorList);

    }

    protected DefaultAuthor(final JSONObject jsonObject) throws MalformedURLException {

        /* Get the name */
        this.setName(jsonObject.optString("name", null));

        /* Get the URL */
        if ( jsonObject.has("url") == true ) {
            this.setUrl(new URL(jsonObject.getString("url")));
        }

        /* Get the avatar (URL) */
        if ( jsonObject.has("avatar") == true ) {
            this.setAvatar(new URL(jsonObject.getString("avatar")));
        }


        /* Get the extensions */
        for ( final Map.Entry<String, Object> entry : jsonObject.toMap().entrySet() ) {
            if ( entry.getKey().startsWith("_") == true ) {
                this.extensionsJsonObject.put(entry.getKey(), entry.getValue());
            }
        }

    }


    public DefaultAuthor(final String name, final URL url, final URL avatar) {

        this.setName(name);
        this.setUrl(url);
        this.setAvatar(avatar);

    }


    public DefaultAuthor() {

    }


    @Override
    public String getName() {

        return (this.name);

    }

    @Override
    public Author setName(String name) {

        this.name = name;
        return (this);

    }


    @Override
    public URL getUrl() {

        return (this.url);

    }

    @Override
    public Author setUrl(URL url) {

        this.url = url;
        return (this);

    }


    @Override
    public URL getAvatar() {

        return (this.avatar);

    }

    @Override
    public Author setAvatar(URL avatar) {

        this.avatar = avatar;
        return (this);

    }

    @Override
    public JSONObject getExtensionsJSONObject() {

        return (this.extensionsJsonObject);

    }

    @Override
    public Author setExtensionsJSONObject(JSONObject extensionsJsonObject) {

        this.extensionsJsonObject = extensionsJsonObject;
        return (this);

    }

    @Override
    public boolean isValid() {

        /* Check the author fields */
        if ( this.getName() != null ) {
            return (true);
        }

        if ( this.getUrl() != null ) {
            return (true);
        }

        if ( this.getAvatar() != null ) {
            return (true);
        }

        return (false);

    }

    @Override
    public String toJSONString() {

        final JSONObject jsonObject = new JSONObject();
        if ( this.getName() != null ) {
            jsonObject.put("name", this.getName());
        }
        if ( this.getUrl() != null ) {
            jsonObject.put("url", this.getUrl().toString());
        }
        if ( this.getAvatar() != null ) {
            jsonObject.put("avatar", this.getAvatar().toString());
        }

        if ( this.getExtensionsJSONObject() != null ) {
            for ( final Map.Entry<String, Object> entry : this.getExtensionsJSONObject().toMap().entrySet() ) {
                if ( entry.getKey().startsWith("_") == true ) {
                    jsonObject.put(entry.getKey(), entry.getValue());
                }
            }
        }

        final String jsonString = jsonObject.toString();
        return (jsonString);

    }


}
