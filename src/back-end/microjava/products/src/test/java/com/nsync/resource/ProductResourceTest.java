package com.nsync.resource;

import com.nsync.entity.Product;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

/**
 * Integration test for the {@link ProductResource} REST API.
 * This class contains several test cases to validate the functionality of the Product resource endpoints.
 */
@QuarkusTest
public class ProductResourceTest {
    
    private Long productId;

    /**
     * Sets up a product before each test method.
     * This product is used for subsequent GET, PUT, and DELETE operations.
     */
    @BeforeEach
    public void setup() {
        Product product = new Product();
        product.sku = "123456";
        product.name = "Test Product";
        product.price = 10.0;
        product.description = "Test Description";
        product.linkImage = "http://test.com/image.jpg";

        Response response = given()
            .contentType(ContentType.JSON)
            .body(product)
            .when().post("/products")
            .then()
            .statusCode(201)
            .extract().response();

        productId = response.jsonPath().getLong("id");
    }

    /**
     * Tests listing all products using the GET endpoint.
     * Verifies that the request returns a 200 OK status.
     */
    @Test
    public void testListProducts() {
        given()
            .when().get("/products")
            .then()
            .statusCode(200);
    }

    /**
     * Tests retrieving a specific product by ID using the GET endpoint.
     * Verifies that the product is retrieved successfully with the correct attributes.
     */
    @Test
    public void testGetProduct() {
        given()
            .when().get("/products/" + productId)
            .then()
            .statusCode(200)
            .body("sku", is("123456"))
            .body("name", is("Test Product"))
            .body("price", is(10.0f))
            .body("description", is("Test Description"))
            .body("linkImage", is("http://test.com/image.jpg"));
    }
}
