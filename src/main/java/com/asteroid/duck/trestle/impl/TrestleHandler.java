package com.asteroid.duck.trestle.impl;

import com.asteroid.duck.trestle.res.Resource;
import com.asteroid.duck.trestle.res.ResourceLocator;
import com.asteroid.duck.trestle.res.WriteableResourceLocator;
import fi.iki.elonen.NanoHTTPD;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

public class TrestleHandler extends NanoHTTPD {
    /**
     * Provides access to the resources (e.g. JSON files) that this server will
     * serve up, and that provide instructions on how to handle requests.
     */
    private final ResourceLocator resourceLocator;
    /**
     * An (optional) resource locator for content written (PUT) by the clients
     */
    private final WriteableResourceLocator writeLocator;

    public TrestleHandler(String hostName, int port, ResourceLocator resourceLocator, WriteableResourceLocator writeLocator) {
        super(hostName, port);
        this.resourceLocator = resourceLocator;
        this.writeLocator = writeLocator;
    }


    @Override
    public Response serve(IHTTPSession session) {
        Path path = Path.parse(session.getUri());
        NanoHTTPD.Method method = session.getMethod();
        switch (method) {
            case GET:
                return get(path);
            default:
                // Respond with error
                return noSuchResource(path);
        }
    }

    /**
     * Handles HTTP method GET
     * @param path the path to get
     * @return the response
     */
    private Response get(Path path) {
        Resource resource = null;
        // was this a written resource
        if (writeLocator != null) {
            resource = writeLocator.locate(path);
        }
        // did we find a written resource?
        if (resource == null) {
            resource = resourceLocator.locate(path);
        }
        // return resource content to client
        if (resource != null) {
            String mimeType = resource.getMimeType(MIME_TYPES);
            try {
                Response response = newChunkedResponse(Response.Status.ACCEPTED, mimeType, resource.getData());
                return response;
            }
            catch (IOException e) {
                return serverError(path, e);
            }
        }
        // roll into default 404..
        return noSuchResource(path);
    }

    private Response noSuchResource(Path path) {
        Response error = newFixedLengthResponse("No such resource as " + path.toString());
        error.setStatus(Response.Status.NOT_FOUND);
        return error;
    }

    private Response serverError(Path path, IOException e) {
        StringWriter sw = new StringWriter();
        PrintWriter writer = new PrintWriter(sw);
        writer.print("Error processing path=");
        writer.println(path.toString());
        e.printStackTrace(writer);
        writer.flush();
        sw.flush();
        Response error = newFixedLengthResponse(sw.toString());
        error.setStatus(Response.Status.INTERNAL_ERROR);
        return error;
    }


}
