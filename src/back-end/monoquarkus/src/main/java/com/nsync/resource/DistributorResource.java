package com.nsync.resource;

import com.nsync.entity.Distributor;
import com.nsync.generic.resource.GenericResource;
import jakarta.ws.rs.Path;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

/**
 * RESTful resource for managing {@link Distributor} entities.
 * <p>
 * This resource provides CRUD operations for distributors, extending the generic
 * functionality provided by {@link GenericResource}.
 * </p>
 *
 * <p>
 * The resource is accessible at the `/distributors` path and is documented with
 * OpenAPI annotations to describe its purpose and operations.
 * </p>
 *
 * <p>
 * The distributor resource is tagged with a descriptive label for better organization
 * in the OpenAPI documentation.
 * </p>
 *
 * @author mauroDasChagas
 */
@Path("/distributors")
@Tag(name = "\uD83C\uDFEC Distributor Resource", description = "Operations related to distributors")
public class DistributorResource extends GenericResource<Distributor> {

    /**
     * Constructs a new {@code DistributorResource}, initializing the resource with
     * the {@link Distributor} entity class.
     */
    public DistributorResource() {
        super(Distributor.class);
    }
}
