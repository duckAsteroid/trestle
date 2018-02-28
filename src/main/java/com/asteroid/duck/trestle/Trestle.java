package com.asteroid.duck.trestle;

import com.asteroid.duck.trestle.impl.Path;
import com.asteroid.duck.trestle.impl.TrestleHandler;
import com.asteroid.duck.trestle.res.ResourceLocator;
import com.asteroid.duck.trestle.res.WriteableResourceLocator;

import java.io.Closeable;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

public class Trestle implements Closeable {

    private final URI baseUri;
    /**
     * The handler/server that takes requests on the undertow server
     * and deals with them
     */
    private final TrestleHandler rootHandler;

    /**
     * Create a trestle server
     * @param port the TCP/IP port to listen on (localhost)
     * @param path the root path of the server
     * @param resourceLocator the resources to serve (from the root)
     * @throws URISyntaxException If the resulting URI of the server is not valid
     */
    public Trestle(int port, ResourceLocator resourceLocator, boolean readonly) throws URISyntaxException, IOException {
        // FIXME Implement write cache
        WriteableResourceLocator writeLocator = (readonly) ? null : new WriteableResourceLocator();
        this.rootHandler = new TrestleHandler("localhost", port, resourceLocator, writeLocator);
        rootHandler.start();

        // work out the URI of this server
        StringBuilder uri = new StringBuilder("http://");
        uri.append("localhost");
        if (port != 80) {
            uri.append(":").append(Integer.toString(port));
        }
        uri.append('/');
        this.baseUri = new URI(uri.toString());
    }

    @Override
    public void close() throws IOException {
        rootHandler.stop();
    }

    public URI getPathURI() {
        return baseUri;
    }

    public void reset() {
        // FIXME Actually reset PUT data
    }
}
