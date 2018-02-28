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

import static javax.ws.rs.core.Response.Status.Family.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;
import static org.valid4j.matchers.http.HttpResponseMatchers.*;
import static com.jayway.jsonpath.matchers.JsonPathMatchers.*;

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
        assertThat(response, hasStatusCode(ofFamily(SUCCESSFUL)));
        String json = response.readEntity(String.class);

        assertThat(json, isJson(
                withJsonPath("$.id", equalTo(2))
        ));
    }


    @Test
    public void testGetOrder() {
        WebTarget petEndpoint = petStoreEndpoint.path("store/order/1.json");
        Response response = petEndpoint.request().get();
        assertThat(response, hasStatusCode(ofFamily(SUCCESSFUL)));
        String json = response.readEntity(String.class);

        assertThat(json, isJson(
                withJsonPath("$.id", equalTo(1))
        ));
    }

    @After
    public void dispose() throws IOException {
        subject.close();
    }
}