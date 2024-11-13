package com.nsync.resource;

import com.nsync.entity.Product;
import com.nsync.exception.custom.BadRequestException;
import com.nsync.exception.custom.EntityNotFoundException;
import com.nsync.generic.resource.GenericResource;
import com.nsync.models.ProductIdsRequest;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.List;

/**
 * RESTful resource for managing {@link Product} entities.
 * <p>
 * This resource provides CRUD operations for {@link Product} entities, including a custom
 * operation to retrieve products by a list of IDs.
 * </p>
 *
 * <p>
 * The resource is accessible at the `/products` path and is documented with OpenAPI annotations
 * to describe its purpose and operations.
 * </p>
 *
 * <p>
 * The product resource is tagged with a descriptive label for better organization in the OpenAPI documentation.
 * </p>
 *
 * @author mauroDasChagas
 */
@Path("/products")
@Tag(name = "\uD83D\uDCF1 Product Resource", description = "Operations related to products")
public class ProductResource extends GenericResource<Product> {

    /**
     * Constructs a new {@code ProductResource}, initializing the resource with
     * the {@link Product} entity class.
     */
    public ProductResource() {
        super(Product.class);
    }

    /**
     * Retrieves a list of {@link Product} entities by a list of IDs.
     * <p>
     * This method accepts a list of product IDs and returns the corresponding products.
     * If the list is empty or invalid, a {@link BadRequestException} is thrown. If no
     * products are found for the provided IDs, an {@link EntityNotFoundException} is thrown.
     * </p>
     *
     * @return a {@link Response} containing the list of products
     * @throws BadRequestException if the list of IDs is empty or null
     * @throws EntityNotFoundException if no products are found for the provided IDs
     */
    @POST
    @Path("/ids")
    @Operation(summary = "Get products by a list of IDs", description = "Returns a list of products that match the provided list of IDs.")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "List of products returned successfully."),
            @APIResponse(responseCode = "400", description = "Invalid list of IDs provided."),
            @APIResponse(responseCode = "404", description = "No products found for the provided IDs.")
    })
    public Response getProductsByIds(ProductIdsRequest request) {
        List<Long> ids = request.getProductsIds();

        if (ids == null || ids.isEmpty()) {
            throw new BadRequestException("The provided list of IDs is empty or invalid.");
        }

        List<Product> products = entityManager.createQuery("FROM Product WHERE id IN :ids", Product.class)
                .setParameter("ids", ids)
                .getResultList();

        if (products.isEmpty()) {
            throw new EntityNotFoundException("No products found for the provided IDs.");
        }

        return Response.ok(products).build();
    }
}
