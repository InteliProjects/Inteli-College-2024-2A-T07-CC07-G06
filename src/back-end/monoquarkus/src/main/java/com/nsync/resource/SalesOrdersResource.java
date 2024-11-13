package com.nsync.resource;

import com.nsync.entity.SalesOrders;
import com.nsync.generic.resource.GenericResource;
import jakarta.ws.rs.Path;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

/**
 * RESTful resource for managing {@link SalesOrders} entities.
 * <p>
 * This resource provides CRUD operations for {@link SalesOrders} entities.
 * </p>
 *
 * <p>
 * The resource is accessible at the `/sales-orders` path and is documented with OpenAPI annotations
 * to describe its purpose and operations.
 * </p>
 *
 * <p>
 * The sales orders resource is tagged with a descriptive label for better organization in the OpenAPI documentation.
 * </p>
 *
 * @author mauroDasChagas
 */
@Path("/sales-orders")
@Tag(name = "\uD83E\uDDFE Sales Orders Resource", description = "Operations related to sales orders")
public class SalesOrdersResource extends GenericResource<SalesOrders> {

    /**
     * Constructs a new {@code SalesOrdersResource}, initializing the resource with
     * the {@link SalesOrders} entity class.
     */
    public SalesOrdersResource() {
        super(SalesOrders.class);
    }
}
