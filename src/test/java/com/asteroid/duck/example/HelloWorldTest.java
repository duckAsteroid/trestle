package com.asteroid.duck.example;

import org.hamcrest.CoreMatchers;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * A simple test of the simple hello world class
 */
public class HelloWorldTest {
    @Test
    public void getMessage() throws Exception {
        assertThat(HelloWorld.getMessage(), CoreMatchers.startsWith("Hello"));
    }

}