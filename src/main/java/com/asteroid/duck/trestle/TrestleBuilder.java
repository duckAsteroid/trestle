package com.asteroid.duck.trestle;

import com.asteroid.duck.trestle.res.ResourceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

public class TrestleBuilder {
    /** SLF4J Logger */
    private static final Logger LOG = LoggerFactory.getLogger(TrestleBuilder.class);

    int port = 9080;
    ResourceLocator locator;
    boolean readonly = true;

    public TrestleBuilder onPort(int port) {
        this.port = port;
        return this;
    }

    public TrestleBuilder withResources(ResourceLocator resourceLocator) {
        locator = resourceLocator;
        return this;
    }

    public TrestleBuilder readOnly(boolean readonly) {
        this.readonly = readonly;
        return this;
    }

    public Trestle build() throws IOException, URISyntaxException {
        return new Trestle(port, locator, readonly);
    }
}
