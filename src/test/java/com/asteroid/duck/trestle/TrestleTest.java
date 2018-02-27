package com.asteroid.duck.trestle;

import com.asteroid.duck.trestle.impl.Path;
import com.asteroid.duck.trestle.res.ClassPathResources;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.junit.Assert.*;

public class TrestleTest {
    private Trestle subject;
    private Client restClient;
    private WebTarget petStoreEndpoint;

    @Before
    public void initialise() throws URISyntaxException, IOException {
        // our test trestle
        subject = new TrestleBuilder()
                .withResources(new ClassPathResources(TrestleTest.class, Path.parse("/test-path")))
                .build();

        // setup the client
        restClient = ClientBuilder.newClient();
        petStoreEndpoint = restClient.target(subject.getPathURI() +"/petstore");
    }

    @Test
    public void testGetPet() {
        WebTarget petEndpoint = petStoreEndpoint.path("pet/dave.json");
        Response response = petEndpoint.request().get();
        assertNotNull(response);
        assertEquals(200, response.getStatus());
        String entity = response.readEntity(String.class);
        assertTrue(entity.length() > 0);
        JSONObject jsonObject = new JSONObject(entity);
        assertNotNull(jsonObject);
        assertTrue(jsonObject.has("id"));
        assertEquals(2, jsonObject.get("id"));
    }


    @Test
    public void testGetOrder() {
        WebTarget petEndpoint = petStoreEndpoint.path("store/order/1.json");
        Response response = petEndpoint.request().get();
        assertNotNull(response);
        assertEquals(200, response.getStatus());
        String entity = response.readEntity(String.class);
        assertTrue(entity.length() > 0);
        JSONObject jsonObject = new JSONObject(entity);
        assertNotNull(jsonObject);
        assertTrue(jsonObject.has("id"));
        assertEquals(1, jsonObject.get("id"));
    }

    @After
    public void dispose() throws IOException {
        subject.close();
    }
}