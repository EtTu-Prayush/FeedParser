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
import org.example.jsonfeed.Attachment;

public class DefaultAttachment implements Attachment {

    private URL url = null;
    private String mimeType = null;
    private String title = null;
    private Integer sizeInBytes = null;
    private Integer durationInSeconds = null;
    private JSONObject extensionsJsonObject = new JSONObject();

    protected static Attachment fromString(final String jsonString) throws MalformedURLException {

        /* Parse the JSON string to a JSON object */
        final JSONObject jsonObject = new JSONObject(jsonString);

        /* Parse the JSON string */
        final Attachment attachment = new DefaultAttachment(jsonObject);

        /* Return the attachment */
        return (attachment);

    }

    protected static List<Attachment> fromJsonArray(final JSONArray jsonArray) throws MalformedURLException {

        /* Create the attachment list */
        final List<Attachment> attachmentList = new ArrayList<Attachment>();

        /* Process the JSON array */
        for ( final Object object : jsonArray ) {
            attachmentList.add(new DefaultAttachment((JSONObject)object));
        }

        /* Return the attachment list */
        return (attachmentList);

    }

    protected DefaultAttachment(final JSONObject jsonObject) throws MalformedURLException {

        /* Get the URL */
        if ( jsonObject.has("url") == true ) {
            this.setUrl(new URL(jsonObject.getString("url")));
        }

        /* Get the mime type */
        this.setMimeType(jsonObject.optString("mime_type", null));

        /* Get the title */
        this.setTitle(jsonObject.optString("title", null));

        /* Get the size in bytes */
        if ( jsonObject.has("size_in_bytes") == true ) {
            this.setSizeInBytes(jsonObject.getInt("size_in_bytes"));
        }

        /* Get the duration in seconds */
        if ( jsonObject.has("duration_in_seconds") == true ) {
            this.setDurationInSeconds(jsonObject.optInt("duration_in_seconds"));
        }


        /* Get the extensions */
        for ( final Map.Entry<String, Object> entry : jsonObject.toMap().entrySet() ) {
            if ( entry.getKey().startsWith("_") == true ) {
                this.extensionsJsonObject.put(entry.getKey(), entry.getValue());
            }
        }

    }

    public DefaultAttachment(final URL url, final String mimeType) {

        this.setUrl(url);
        this.setMimeType(mimeType);

    }

    @Override
    public URL getUrl() {

        return (this.url);

    }

    @Override
    public Attachment setUrl(URL url) {

        this.url = url;
        return (this);

    }


    @Override
    public String getMimeType() {

        return (this.mimeType);

    }


    @Override
    public Attachment setMimeType(String mimeType) {

        this.mimeType = mimeType;
        return (this);

    }


    @Override
    public String getTitle() {

        return (this.title);

    }

    @Override
    public Attachment setTitle(String title) {

        this.title = title;
        return (this);

    }

    @Override
    public Integer getSizeInBytes() {

        return (this.sizeInBytes);

    }

    @Override
    public Attachment setSizeInBytes(Integer sizeInBytes) {

        this.sizeInBytes = sizeInBytes;
        return (this);

    }


    @Override
    public Integer getDurationInSeconds() {

        return (this.durationInSeconds);

    }

    @Override
    public Attachment setDurationInSeconds(Integer durationInSeconds) {

        this.durationInSeconds = durationInSeconds;
        return (this);

    }

    @Override
    public JSONObject getExtensionsJSONObject() {

        return (this.extensionsJsonObject);

    }

    @Override
    public Attachment setExtensionsJSONObject(JSONObject extensionsJsonObject) {

        this.extensionsJsonObject = extensionsJsonObject;
        return (this);

    }

    @Override
    public boolean isValid() {

        /* Check the attachment fields */
        if ( this.getUrl() == null ) {
            return (false);
        }

        if ( this.getMimeType() == null ) {
            return (false);
        }

        return (true);

    }

    @Override
    public String toJSONString() {

        final JSONObject jsonObject = new JSONObject();
        jsonObject.put("url", this.getUrl().toString());
        jsonObject.put("type", this.getMimeType());

        if ( this.getTitle() != null ) {
            jsonObject.put("type", this.getTitle());
        }

        if ( this.getSizeInBytes() != null ) {
            jsonObject.put("size_in_bytes", this.getSizeInBytes());
        }

        if ( this.getDurationInSeconds() != null ) {
            jsonObject.put("duration_in_seconds", this.getDurationInSeconds());
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
