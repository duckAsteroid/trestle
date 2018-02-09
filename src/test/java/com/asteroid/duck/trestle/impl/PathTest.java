package com.asteroid.duck.trestle.impl;

import org.junit.Test;

import static org.junit.Assert.*;

public class PathTest {
    @Test
    public void parse() throws Exception {
        Path temp = Path.parse("this/is/all/good");
        assertNotNull(temp);
        assertTrue(temp.isRelative());
        assertArrayEquals(new String[]{"this", "is", "all", "good"}, temp.asSegmentList().toArray());

        temp = Path.parse("this//is///all/////good");
        assertNotNull(temp);
        assertTrue(temp.isRelative());
        assertArrayEquals(new String[]{"this", "is", "all", "good"}, temp.asSegmentList().toArray());

        temp = Path.parse("/i/am/root");
        assertNotNull(temp);
        assertFalse(temp.isRelative());
        assertArrayEquals(new String[]{"i", "am", "root"}, temp.asSegmentList().toArray());

        temp = Path.parse("//i/am//root");
        assertNotNull(temp);
        assertFalse(temp.isRelative());
        assertArrayEquals(new String[]{"i", "am", "root"}, temp.asSegmentList().toArray());
    }

    @Test
    public void testToString() throws Exception {
        Path temp = Path.parse("this/is/all/good");
        assertEquals("this/is/all/good", temp.toString());

        temp = Path.parse("/i/am/root");
        assertEquals("/i/am/root", temp.toString());
    }


    @Test
    public void newChild() throws Exception {
        Path temp = Path.parse("this/is/all/good");
        Path child = temp.newChild("ok");
        assertNotNull(child);
        assertArrayEquals(new String[]{"this", "is", "all", "good", "ok"}, child.asSegmentList().toArray());

        try {
            child = temp.newChild("no/slashes");
            fail("No slashes allowed in child segment names");
        }
        catch(IllegalArgumentException expected) {}
    }

    @Test
    public void append() throws Exception {
        Path temp = Path.parse("this/is/all/good");
        Path appended = temp.append("ok/slashes");
        assertArrayEquals(new String[]{"this", "is", "all", "good", "ok", "slashes"}, appended.asSegmentList().toArray());

        appended = temp.append("ok/slashes/");
        assertArrayEquals(new String[]{"this", "is", "all", "good", "ok", "slashes"}, appended.asSegmentList().toArray());

        appended = temp.append("ok///slashes/");
        assertArrayEquals(new String[]{"this", "is", "all", "good", "ok", "slashes"}, appended.asSegmentList().toArray());

        try {
            appended = temp.append("/ok/slashes");
            fail("Not relative child in append");
        }
        catch(IllegalArgumentException expected) {}
    }

}