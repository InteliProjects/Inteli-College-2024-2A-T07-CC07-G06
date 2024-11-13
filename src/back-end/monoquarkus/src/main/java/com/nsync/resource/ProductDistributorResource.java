package com.nsync.resource;

import com.nsync.entity.ProductDistributor;
import com.nsync.generic.resource.GenericResource;
import jakarta.ws.rs.Path;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

/**
 * RESTful resource for managing the relationship between {@link ProductDistributor} entities.
 * <p>
 * This resource provides CRUD operations for the association between products and distributors,
 * extending the generic functionality provided by {@link GenericResource}.
 * </p>
 *
 * <p>
 * The resource is accessible at the `/products-distributors` path and is documented with
 * OpenAPI annotations to describe its purpose and operations.
 * </p>
 *
 * <p>
 * The product-distributor resource is tagged with a descriptive label for better organization
 * in the OpenAPI documentation.
 * </p>
 *
 * @author mauroDasChagas
 */
@Path("/products-distributors")
@Tag(name = "\uD83D\uDCE6 ProductDistributor Resource", description = "Operations related to the relation between products and distributors")
public class ProductDistributorResource extends GenericResource<ProductDistributor> {

    /**
     * Constructs a new {@code ProductDistributorResource}, initializing the resource with
     * the {@link ProductDistributor} entity class.
     */
    public ProductDistributorResource() {
        super(ProductDistributor.class);
    }
}
