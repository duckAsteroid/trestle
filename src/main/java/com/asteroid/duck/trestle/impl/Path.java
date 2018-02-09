package com.asteroid.duck.trestle.impl;

import java.util.*;
import java.util.stream.Collectors;

public class Path {
    public final static Path ROOT_PATH = new Path(null, null) {
        @Override
        public boolean isRoot() {
            return true;
        }

        @Override
        public synchronized String toString() {
            return "";
        }
    };

    private final Path parent;
    private final String segment;

    private transient String toStringCache = null;

    private Path(Path parent, String segment) {
        this.parent = parent;
        this.segment = segment;
    }

    public static Path root() {
        return ROOT_PATH;
    }

    public static Path parse(String segments) {
        String[] split = segments.split("/");
        Path result = segments.startsWith("/") ? ROOT_PATH : null;
        for (String seg : split) {
            if (seg != null && seg.length() > 0) {
                if (result == null) {
                    result = new Path(null, seg);
                }
                else {
                    result = result.newChild(seg);
                }
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

    public boolean isRoot() {
        return false;
    }

    public boolean isRelative() {
        Path root = asList().get(0);
        return !root.isRoot();
    }

    public Path newChild(String segment) {
        if (segment.contains("/")) {
            throw new IllegalArgumentException("Segment name cannot contain '/'");
        }
        return new Path(this, segment);
    }

    public Path append(String relative) {
        Path tmp = Path.parse(relative);
        return append(tmp);
    }

    public Path append(Path relative) {
        if (!relative.isRelative()) {
            throw new IllegalArgumentException("Relative path required to append");
        }
        Path result = this;
        for (String seg : relative.asSegmentList()) {
            result = result.newChild(seg);
        }
        return result;
    }

    public List<Path> asList() {
        if (parent != null) {
            List<Path> parentList = parent.asList();
            ArrayList<Path> copy = new ArrayList<>(parentList.size() + 1);
            copy.addAll(parentList);
            copy.add(this);
            return Collections.unmodifiableList(copy);
        }
        else if (segment == null || segment.length() > 0) {
            return Collections.singletonList(this);
        }
        else {
            return Collections.emptyList();
        }
    }

    public List<String> asSegmentList() {
        return asList().stream()
                .map(path -> path.segment)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    public synchronized String toString() {
        if (toStringCache == null) {
            StringBuilder sb = new StringBuilder();
            if (parent != null) {
                String parentString = parent.toString();
                sb.append(parentString);
                sb.append('/');
            }
            sb.append(segment);
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
