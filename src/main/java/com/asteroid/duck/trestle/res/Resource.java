package com.asteroid.duck.trestle.res;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * Provides access to a resource
 */
public interface Resource {
    /**
     * What is the mime type for the data returned from this resource
     * @param mimeTypes the normal mime type to file extension mapping from the server
     * @return a mime type (e.g. text/html)
     */
    String getMimeType(Map<String, String> mimeTypes);

    /**
     * Open a new input stream to read the data
     * @return
     */
    InputStream getData() throws IOException;
}
