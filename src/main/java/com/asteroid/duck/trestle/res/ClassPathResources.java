package com.asteroid.duck.trestle.res;

import com.asteroid.duck.trestle.impl.Path;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;

public class ClassPathResources implements ResourceLocator {
    private final Class<?> clazz;
    private final Path rootPath;

    public ClassPathResources(Class<?> clazz, Path rootPath) {
        this.clazz = clazz;
        this.rootPath = rootPath;
    }

    @Override
    public Resource locate(Path relativePath) {
        Path path = rootPath.append(relativePath);
        URL url = clazz.getResource(path.toString());
        if (url != null) {
            return new ClassPathResource(url);
        }
        return null;
    }

    private class ClassPathResource implements Resource {
        private final URL url;

        private ClassPathResource(URL url) {
            this.url = url;
        }

        @Override
        public String getMimeType(Map<String, String> mimeTypes) {
            final String file = url.getFile();
            final int extSplit = file.lastIndexOf('.');
            if (extSplit > 0) {
                final String ext = file.substring(extSplit + 1 );
                if (mimeTypes != null && mimeTypes.containsKey(ext)) {
                    return mimeTypes.get(ext);
                }
            }
            return "application/json";
        }

        @Override
        public InputStream getData() throws IOException {
            return url.openStream();
        }
    }
}
