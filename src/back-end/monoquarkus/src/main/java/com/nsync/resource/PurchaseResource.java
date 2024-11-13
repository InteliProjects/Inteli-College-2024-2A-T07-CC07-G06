package com.nsync.resource;

import com.nsync.dto.ProductRequestDTO;
import com.nsync.dto.PurchaseRequestDTO;
import com.nsync.models.ProductRequest;
import com.nsync.models.PurchaseRequest;
import com.nsync.service.purchase.PurchaseService;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/purchases")
public class PurchaseResource {

    @Inject
    PurchaseService purchaseService;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response purchase(PurchaseRequestDTO dto) {
        boolean success = purchaseService.processPurchase(dtoToEntity(dto));
        if (success) {
            return Response.ok().build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    private PurchaseRequest dtoToEntity(PurchaseRequestDTO dto) {
        PurchaseRequest request = new PurchaseRequest();
        request.setCep(dto.getCep());

        List<ProductRequestDTO> productsList = dto.getProducts();

        if (productsList == null || productsList.isEmpty()) {
            throw new IllegalArgumentException("Product list cannot be null or empty");
        }

        List<ProductRequest> products = productsList.stream()
                .map(productDTO -> {
                    ProductRequest product = new ProductRequest();
                    product.setProductId(productDTO.getProductId());
                    product.setQuantity(productDTO.getQuantity());
                    return product;
                }).toList();

        request.setProducts(products);
        return request;
    }

}
