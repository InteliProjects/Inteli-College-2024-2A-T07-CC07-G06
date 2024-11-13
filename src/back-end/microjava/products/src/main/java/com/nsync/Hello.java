package com.nsync;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

/**
 * Resource class that provides endpoints for basic greetings and environment information.
 * <p>
 * This class defines two endpoints:
 * </p>
 * <ul>
 *     <li><b>GET /hello</b>: Returns a simple greeting message.</li>
 * </ul>
 * <p>
 * The environment name is injected from the configuration property "env.name" and is used to provide
 * additional information about the application's environment.
 * </p>
 *
 * @author mauroDasChagas
 */
@Path("/hello")
public class Hello {

    @ConfigProperty(name = "env.name")
    String environment;

    /**
     * Endpoint to return a simple greeting message.
     * <p>
     * This method handles GET requests to the base path "/hello" and returns a plain text greeting message.
     * </p>
     *
     * @return a plain text greeting message.
     */
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return "Hello, from Java Products API!";
    }
}
