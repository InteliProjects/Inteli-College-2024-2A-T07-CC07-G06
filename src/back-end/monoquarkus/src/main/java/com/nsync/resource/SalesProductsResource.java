package com.nsync.resource;

import com.nsync.entity.SalesProducts;
import com.nsync.generic.resource.GenericResource;
import jakarta.ws.rs.Path;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

/**
 * RESTful resource for managing {@link SalesProducts} entities.
 * <p>
 * This resource provides CRUD operations for {@link SalesProducts} entities.
 * </p>
 *
 * <p>
 * The resource is accessible at the `/sales-products` path and is documented with OpenAPI annotations
 * to describe its purpose and operations.
 * </p>
 *
 * <p>
 * The sales products resource is tagged with a descriptive label for better organization in the OpenAPI documentation.
 * </p>
 *
 * @author mauroDasChagas
 */
@Path("/sales-products")
@Tag(name = "\uD83D\uDCC8 Sales Products Resource", description = "Operations related to sales products")
public class SalesProductsResource extends GenericResource<SalesProducts> {

    /**
     * Constructs a new {@code SalesProductsResource}, initializing the resource with
     * the {@link SalesProducts} entity class.
     */
    public SalesProductsResource() {
        super(SalesProducts.class);
    }
}
