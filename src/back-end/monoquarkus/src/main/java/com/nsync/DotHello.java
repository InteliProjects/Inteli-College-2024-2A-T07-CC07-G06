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
 *     <li><b>GET /dothello</b>: Returns a simple greeting message.</li>
 *     <li><b>GET /dothello/environment</b>: Returns the current environment name and a special message if the environment is "dev".</li>
 * </ul>
 * <p>
 * The environment name is injected from the configuration property "env.name" and is used to provide
 * additional information about the application's environment.
 * </p>
 *
 * @author mauroDasChagas
 */
@Path("/dothello")
@Tag(name = "\uD83D\uDE0E Hello, Grupo.", description = "The good is the project with Alive, it could also be Luquinhas")
public class DotHello {

    @ConfigProperty(name = "env.name")
    String environment;

    /**
     * Endpoint to return a simple greeting message.
     * <p>
     * This method handles GET requests to the base path "/dothello" and returns a plain text greeting message.
     * </p>
     *
     * @return a plain text greeting message.
     */
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return "Hello, Grupo.! \uD83D\uDE0E";
    }

    /**
     * Endpoint to return the current environment name.
     * <p>
     * This method handles GET requests to the path "/dothello/environment" and returns a plain text message
     * indicating the current environment. If the environment is "dev", it appends a special message to the response
     * that fully complies with Alive's emoji rules.
     * </p>
     *
     * @return a plain text message with the current environment name and a special message if the environment is "dev".
     */
    @GET
    @Path("/environment")
    @Produces(MediaType.TEXT_PLAIN)
    public String getEnvironment() {
        String response = "Current environment: " + environment;
        if ("dev".equals(environment)) {
            response += "\nIt's development, there's no way, forget everything \uD83D\uDD25";
        }
        return response;
    }
}
