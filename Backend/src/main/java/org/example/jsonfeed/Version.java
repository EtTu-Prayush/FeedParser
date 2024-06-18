package org.example.jsonfeed;


/* Import Java stuff */
import java.util.HashMap;
import java.util.Map;


/* Import JSON stuff */
import org.json.JSONObject;
import org.json.JSONString;

public enum Version implements JSONString {


    /**
     * Version constants
     */
    VERSION_1_0(10, "https://jsonfeed.org/version/1.0"),
    VERSION_1_1(11, "https://jsonfeed.org/version/1.1");


    /**
     * Hash map for the version name to version enum
     */
    private static final Map<String, Version> nameToEnum = new HashMap<String, Version>();

    /* Set up the hash map */
    static {

        /* Add the version enums */
        for ( final Version version : Version.values() ) {
            Version.nameToEnum.put(version.getVersionName().toLowerCase(), version);
        }

        /* Add a truncated version 1 string */
        Version.nameToEnum.put("https://jsonfeed.org/version/1", Version.VERSION_1_0);
    }



    /**
     * Version ID
     */
    private final int versionID;


    /**
     * Version name
     */
    private final String versionName;




    /**
     * Method to get the version from a string
     *
     * @param       versionString      the version string
     *
     * @return      the version, null otherwise
     */
    public static Version fromString (final String versionString) {

        /* Return the version */
        return (Version.nameToEnum.get(versionString.toLowerCase()));

    }



    /**
     * Constructor
     *
     * @param       versionID       the version ID
     * @param       versionName     the version name
     */
    private Version (final int versionID, final String versionName) {

        /* Set the enum fields */
        this.versionID = versionID;
        this.versionName = versionName;

    }



    /**
     * Method to get the version ID
     *
     * @return      the version ID
     */
    public int getVersionID () {

        /* Return the version ID */
        return (this.versionID);

    }



    /**
     * Method to get the version name
     *
     * @return      the version name
     */
    public String getVersionName () {

        /* Return the version name */
        return (this.versionName);

    }



    /**
     * Override toString() for this object
     *
     * @return      the string representation for this object
     */
    @Override
    public String toString () {

        /* Return the string representation */
        return (this.getVersionName());

    }


    /**
     * Implements toJSONString() for this object
     *
     * @return      the JSON string representation for this object
     */
    @Override
    public String toJSONString() {

        /* Get the version as an escaped JSON string */
        final String jsonString = JSONObject.quote(this.toString());

        /* Return the JSON string */
        return (jsonString);

    }


}
