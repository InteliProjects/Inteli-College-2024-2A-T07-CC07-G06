package com.nsync.entity;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Unit test for the {@link Product} entity class.
 * This class contains several test cases to validate the functionality and constraints of the Product entity.
 */
@QuarkusTest
public class ProductTest {

    @Inject
    EntityManager entityManager;

    /**
     * Tests the valid scenario of the {@link Product} entity.
     * Verifies that a valid Product instance passes validation and can be persisted in the database.
     */
    @Test
    @Transactional
    public void testProduct() {
        // Creates a Product instance with all required fields filled
        Product product = new Product();
        product.sku = "SKU123";
        product.name = "Product Name";
        product.price = 99.99;
        product.description = "This is a product description.";
        product.linkImage = "http://example.com/product-image.jpg";

        // Verifies that the values were correctly assigned
        assertEquals("SKU123", product.sku);
        assertEquals("Product Name", product.name);
        assertEquals(99.99, product.price);
        assertEquals("This is a product description.", product.description);
        assertEquals("http://example.com/product-image.jpg", product.linkImage);

        // Persists the product to ensure no constraint violations occur
        entityManager.persist(product);
        entityManager.flush();
    }

    /**
     * Tests an invalid scenario for the {@link Product} entity when the 'sku' is null.
     * Verifies that a constraint violation exception is thrown for the 'sku' field.
     */
    @Test
    @Transactional
    public void testProductWithNullSku() {
        Product product = new Product();
        product.name = "Product Name";
        product.price = 99.99;
        product.description = "This is a product description.";
        product.linkImage = "http://example.com/product-image.jpg";

        // Verifies constraint violation for the 'sku' field
        assertThrows(ConstraintViolationException.class, () -> {
            entityManager.persist(product);
            entityManager.flush();
        });
    }

    /**
     * Tests an invalid scenario for the {@link Product} entity when the 'name' is null.
     * Verifies that a constraint violation exception is thrown for the 'name' field.
     */
    @Test
    @Transactional
    public void testProductWithNullName() {
        Product product = new Product();
        product.sku = "SKU123";
        product.price = 99.99;
        product.description = "This is a product description.";
        product.linkImage = "http://example.com/product-image.jpg";

        // Verifies constraint violation for the 'name' field
        assertThrows(ConstraintViolationException.class, () -> {
            entityManager.persist(product);
            entityManager.flush();
        });
    }

    /**
     * Tests an invalid scenario for the {@link Product} entity when the 'price' is null.
     * Verifies that a constraint violation exception is thrown for the 'price' field.
     */
    @Test
    @Transactional
    public void testProductWithNullPrice() {
        Product product = new Product();
        product.sku = "SKU123";
        product.name = "Product Name";
        product.description = "This is a product description.";
        product.linkImage = "http://example.com/product-image.jpg";

        // Verifies constraint violation for the 'price' field
        assertThrows(ConstraintViolationException.class, () -> {
            entityManager.persist(product);
            entityManager.flush();
        });
    }

    /**
     * Tests an invalid scenario for the {@link Product} entity when the 'description' is null.
     * Verifies that a constraint violation exception is thrown for the 'description' field.
     */
    @Test
    @Transactional
    public void testProductWithNullDescription() {
        Product product = new Product();
        product.sku = "SKU123";
        product.name = "Product Name";
        product.price = 99.99;
        product.linkImage = "http://example.com/product-image.jpg";

        // Verifies constraint violation for the 'description' field
        assertThrows(ConstraintViolationException.class, () -> {
            entityManager.persist(product);
            entityManager.flush();
        });
    }

    /**
     * Tests an invalid scenario for the {@link Product} entity when the 'linkImage' is null.
     * Verifies that a constraint violation exception is thrown for the 'linkImage' field.
     */
    @Test
    @Transactional
    public void testProductWithNullLinkImage() {
        Product product = new Product();
        product.sku = "SKU123";
        product.name = "Product Name";
        product.price = 99.99;
        product.description = "This is a product description.";

        // Verifies constraint violation for the 'linkImage' field
        assertThrows(ConstraintViolationException.class, () -> {
            entityManager.persist(product);
            entityManager.flush();
        });
    }

    /**
     * Tests an invalid scenario for the {@link Product} entity when 'price' is negative.
     * Verifies that a constraint violation exception is thrown for the 'price' field.
     */
    @Test
    @Transactional
    public void testProductWithNegativePrice() {
        Product product = new Product();
        product.sku = "SKU123";
        product.name = "Product Name";
        product.price = -10.0;
        product.description = "This is a product description.";
        product.linkImage = "http://example.com/product-image.jpg";

        // Verifies constraint violation for the negative 'price' field
        assertThrows(ConstraintViolationException.class, () -> {
            entityManager.persist(product);
            entityManager.flush();
        });
    }

    /**
     * Tests an invalid scenario for the {@link Product} entity when 'price' is zero.
     * Verifies that a constraint violation exception is thrown for the 'price' field.
     */
    @Test
    @Transactional
    public void testProductWithZeroPrice() {
        Product product = new Product();
        product.sku = "SKU123";
        product.name = "Product Name";
        product.price = 0.0;
        product.description = "This is a product description.";
        product.linkImage = "http://example.com/product-image.jpg";

        // Verifies constraint violation for the 'price' field equal to zero
        assertThrows(ConstraintViolationException.class, () -> {
            entityManager.persist(product);
            entityManager.flush();
        });
    }
}
