package com.asteroid.duck.trestle.res;

import com.asteroid.duck.trestle.impl.Path;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Map;
import java.util.TreeMap;

/**
 * A temporary set of resources that have been written by clients
 */
public class WriteableResourceLocator implements ResourceLocator {
    /** SLF4J Logger */
    private static final Logger LOG = LoggerFactory.getLogger(WriteableResourceLocator.class);
    /**
     * A resource type for the cached resources
     */
    class WriteableResource implements Resource, Closeable {
        private final String mimeType;
        private final File tempFile;

        public WriteableResource(String mimeType, InputStream readFrom) throws IOException {
            this.mimeType = mimeType;
            tempFile = File.createTempFile("cachedResource", Long.toString(System.currentTimeMillis()));
            FileUtils.copyInputStreamToFile(readFrom, tempFile);
        }

        @Override
        public String getMimeType(Map<String, String> mimeTypes) {
            return mimeType;
        }

        @Override
        public InputStream getData() throws IOException {
            return new FileInputStream(tempFile);
        }

        @Override
        public void close() throws IOException {
            tempFile.delete();
        }
    }

    /**
     * A cache of written resources by path
     */
    private final Map<Path, WriteableResource> data = new TreeMap<>();

    @Override
    public synchronized Resource locate(Path path) {
        return data.get(path);
    }

    /**
     * Store a resource for a given path. Any existing resource is overwritten.
     * @param path the path of the resource
     * @param mimeType the mimetype
     * @param source the source data to store
     * @throws IOException if the source data cannot be stored
     */
    public synchronized void store(Path path, String mimeType, InputStream source) throws IOException {
        WriteableResource resource = new WriteableResource(mimeType, source);
        if (data.containsKey(path)) {
            WriteableResource existing = data.get(path);
            existing.close();
        }
        data.put(path, resource);
    }

    public synchronized void delete(Path path) {
        if (data.containsKey(path)) {
            try {
                data.get(path).close();
            } catch (IOException e) {
                LOG.error("Unable to delete temp file", e);
            }
            data.remove(path);
        }
    }

    /**
     * Clear out the cache of data
     */
    public synchronized void clear() {
        for(WriteableResource resource : data.values()) {
            try {
                resource.close();
            }
            catch (IOException e) {
               LOG.error("Unable to close existing resource", e);
            }
        }
        data.clear();
    }
}
