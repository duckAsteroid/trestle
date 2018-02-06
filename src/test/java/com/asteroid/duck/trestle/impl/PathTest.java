package com.asteroid.duck.trestle.impl;

import org.junit.Test;

import static org.junit.Assert.*;

public class PathTest {
    @Test
    public void parse() throws Exception {
        Path temp = Path.parse("this/is/all/good");
        assertNotNull(temp);
        assertArrayEquals(new String[]{"this", "is", "all", "good"}, temp.asList().toArray());
    }


    @Test
    public void newChild() throws Exception {
        Path temp = Path.parse("this/is/all/good");
        Path child = temp.newChild("ok");
        assertNotNull(child);
        assertArrayEquals(new String[]{"this", "is", "all", "good", "ok"}, child.asList().toArray());

        child = temp.newChild("no/slashes");
        assertArrayEquals(new String[]{"this", "is", "all", "good", "no/slashes"}, child.asList().toArray());
    }

    @Test
    public void append() throws Exception {
        Path temp = Path.parse("this/is/all/good");
        Path appended = temp.append("ok/slashes");
        assertArrayEquals(new String[]{"this", "is", "all", "good", "ok", "slashes"}, appended.asList().toArray());

        appended = temp.append("/ok/slashes");
        assertArrayEquals(new String[]{"this", "is", "all", "good", "ok", "slashes"}, appended.asList().toArray());

        appended = temp.append("ok/slashes/");
        assertArrayEquals(new String[]{"this", "is", "all", "good", "ok", "slashes"}, appended.asList().toArray());

        appended = temp.append("//ok///slashes/");
        assertArrayEquals(new String[]{"this", "is", "all", "good", "ok", "slashes"}, appended.asList().toArray());
    }

}