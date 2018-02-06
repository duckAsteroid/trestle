package com.asteroid.duck.trestle.res;

import com.asteroid.duck.trestle.impl.Path;

public interface ResourceLocator {
    Resource locate(Path relativePath);
}
