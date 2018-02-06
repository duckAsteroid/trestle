package com.asteroid.duck.trestle.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class Path {
    private final static Path EMPTY = new Path(null, "");
    public final static Path ROOT_PATH = new Path(null, "/");

    private final Path parent;
    private final String segment;

    private transient String toStringCache = null;

    public Path(Path parent, String segment) {
        if (segment == null) {
            throw new IllegalArgumentException("Segment cannot be null");
        }
        this.parent = parent;
        this.segment = segment;
    }

    public static Path root() {
        return ROOT_PATH;
    }

    public static Path parse(String segments) {
        String[] split = segments.split("/");
        Path result = segments.startsWith("/") ? ROOT_PATH : EMPTY;
        for (String seg : split) {
            if (seg != null && seg.length() > 0) {
                result = result.newChild(seg);
            }
        }
        return result;
    }

    public String getSegement() {
        return segment;
    }

    public Path getParent() {
        return parent;
    }

    public Path newChild(String segment) {
        return new Path(this, segment);
    }

    public Path append(String relative) {
        Path tmp = Path.parse(relative);
        return append(tmp);
    }

    public Path append(Path relative) {
        Path result = this;
        for (String seg : relative.asList()) {
            result = result.newChild(seg);
        }
        return result;
    }

    public List<String> asList() {
        if (parent != null) {
            List<String> parentList = parent.asList();
            ArrayList<String> copy = new ArrayList<>(parentList.size() + 1);
            copy.addAll(parentList);
            copy.add(segment);
            return Collections.unmodifiableList(copy);
        }
        else if (segment.length() > 0) {
            return Collections.singletonList(segment);
        }
        else {
            return Collections.emptyList();
        }
    }

    @Override
    public synchronized String toString() {
        if (toStringCache == null) {
            StringBuilder sb = new StringBuilder();
            Iterator<String> iter = asList().iterator();
            while(iter.hasNext()) {
                final String seg = iter.next();
                sb.append(seg);
                if (iter.hasNext()) {
                    sb.append('/');
                }
            }
            toStringCache = sb.toString();
        }
        return toStringCache;
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof Path) {
            Path other = (Path)obj;
            return this.toString().equals(other.toString());
        }
        return false;
    }
}
