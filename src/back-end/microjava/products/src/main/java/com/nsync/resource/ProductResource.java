package com.nsync.resource;

import com.nsync.cache.ProductIdsCacheKeyGenerator;
import com.nsync.entity.Product;
import com.nsync.exception.custom.BadRequestException;
import com.nsync.exception.custom.EntityNotFoundException;
import com.nsync.models.ProductIdsRequest;
import io.quarkus.cache.CacheResult;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.ws.rs.*;

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
public class ProductResource {

    @Inject
    private EntityManager entityManager;

    /**
     * Retrieves all {@link Product} entities with pagination.
     * 
     * @param page the page number (default is 0)
     * @param size the number of items per page (default is 10)
     * @return a list of products
     */
    @GET
    @Operation(summary = "Get all products", description = "Returns a paginated list of all products.")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "List of products returned successfully."),
            @APIResponse(responseCode = "500", description = "Internal server error.")
    })
    @CacheResult(cacheName = "products-cache")
    public List<Product> getAll(@QueryParam("page") @DefaultValue("0") int page,
                                @QueryParam("size") @DefaultValue("10") int size) {
        return entityManager.createQuery("FROM Product", Product.class)
                .setFirstResult(page * size)
                .setMaxResults(size)
                .getResultList();
    }

    /**
     * Retrieves a specific {@link Product} entity by its ID.
     *
     * @param id the ID of the product to retrieve
     * @return the found product
     * @throws EntityNotFoundException if the product with the specified ID does not exist
     */
    @GET
    @Path("{id}")
    @Operation(summary = "Get a product by ID", description = "Returns the product corresponding to the provided ID.")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Product found."),
            @APIResponse(responseCode = "404", description = "Product not found.")
    })
    @CacheResult(cacheName = "product-cache")
    public Product get(@PathParam("id") Long id) {
        Product product = entityManager.find(Product.class, id);
        if (product == null) {
            throw new EntityNotFoundException("Product with id " + id + " does not exist.");
        }
        return product;
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
    @CacheResult(cacheName = "products-by-ids-cache", keyGenerator = ProductIdsCacheKeyGenerator.class)
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
