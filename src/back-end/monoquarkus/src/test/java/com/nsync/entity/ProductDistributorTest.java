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
 * Unit test for the {@link ProductDistributor} entity class.
 * This class contains several test cases to validate the functionality and constraints of the ProductDistributor entity.
 */
@QuarkusTest
public class ProductDistributorTest {

    @Inject
    EntityManager entityManager;

    /**
     * Tests the valid scenario of the {@link ProductDistributor} entity.
     * Verifies that a valid ProductDistributor instance passes validation and can be persisted in the database.
     */
    @Test
    @Transactional
    public void testProductDistributor() {
        // Creates a Distributor instance necessary for the association
        Distributor distributor = new Distributor();
        distributor.name = "Distributor Name";
        distributor.cep = "12345678";
        entityManager.persist(distributor);
        entityManager.flush();

        // Creates a Product instance necessary for the association
        Product product = new Product();
        product.sku = "SKU123";
        product.name = "Product Name";
        product.price = 500.00;
        product.description = "This is a product description.";
        product.linkImage = "http://example.com/product-image.jpg";
        entityManager.persist(product);
        entityManager.flush();

        // Creates a ProductDistributor instance with all required fields filled
        ProductDistributor productDistributor = new ProductDistributor();
        productDistributor.distributor = distributor;
        productDistributor.product = product;
        productDistributor.quantityAvailable = 10;
        productDistributor.quantityReserved = 2;

        // Verifies that the values were correctly assigned
        assertEquals(distributor, productDistributor.distributor);
        assertEquals(product, productDistributor.product);
        assertEquals(10, productDistributor.quantityAvailable);
        assertEquals(2, productDistributor.quantityReserved);

        // Persists the ProductDistributor entity to ensure no constraint violations occur
        entityManager.persist(productDistributor);
        entityManager.flush();
    }

    /**
     * Tests an invalid scenario for the {@link ProductDistributor} entity when the 'distributor' is null.
     * Verifies that a constraint violation exception is thrown for the 'distributor' field.
     */
    @Test
    @Transactional
    public void testProductDistributorWithNullDistributor() {
        Product product = new Product();
        product.sku = "SKU123";
        product.name = "Product Name";
        product.price = 500.00;
        product.description = "This is a product description.";
        product.linkImage = "http://example.com/product-image.jpg";
        entityManager.persist(product);
        entityManager.flush();

        ProductDistributor productDistributor = new ProductDistributor();
        productDistributor.product = product;
        productDistributor.quantityAvailable = 10;
        productDistributor.quantityReserved = 2;

        // Verifies constraint violation for the 'distributor' field
        assertThrows(ConstraintViolationException.class, () -> {
            entityManager.persist(productDistributor);
            entityManager.flush();
        });
    }

    /**
     * Tests an invalid scenario for the {@link ProductDistributor} entity when the 'product' is null.
     * Verifies that a constraint violation exception is thrown for the 'product' field.
     */
    @Test
    @Transactional
    public void testProductDistributorWithNullProduct() {
        Distributor distributor = new Distributor();
        distributor.name = "Distributor Name";
        distributor.cep = "12345678";
        entityManager.persist(distributor);
        entityManager.flush();

        ProductDistributor productDistributor = new ProductDistributor();
        productDistributor.distributor = distributor;
        productDistributor.quantityAvailable = 10;
        productDistributor.quantityReserved = 2;

        // Verifies constraint violation for the 'product' field
        assertThrows(ConstraintViolationException.class, () -> {
            entityManager.persist(productDistributor);
            entityManager.flush();
        });
    }

    /**
     * Tests an invalid scenario for the {@link ProductDistributor} entity when 'quantityAvailable' is negative.
     * Verifies that a constraint violation exception is thrown for the 'quantityAvailable' field.
     */
    @Test
    @Transactional
    public void testProductDistributorWithInvalidQuantityAvailable() {
        Distributor distributor = new Distributor();
        distributor.name = "Distributor Name";
        distributor.cep = "12345678";
        entityManager.persist(distributor);
        entityManager.flush();

        Product product = new Product();
        product.sku = "SKU123";
        product.name = "Product Name";
        product.price = 500.00;
        product.description = "This is a product description.";
        product.linkImage = "http://example.com/product-image.jpg";
        entityManager.persist(product);
        entityManager.flush();

        ProductDistributor productDistributor = new ProductDistributor();
        productDistributor.distributor = distributor;
        productDistributor.product = product;
        productDistributor.quantityAvailable = -1; // Invalid quantity
        productDistributor.quantityReserved = 2;

        // Verifies constraint violation for the 'quantityAvailable' field
        assertThrows(ConstraintViolationException.class, () -> {
            entityManager.persist(productDistributor);
            entityManager.flush();
        });
    }

    /**
     * Tests an invalid scenario for the {@link ProductDistributor} entity when 'quantityReserved' is negative.
     * Verifies that a constraint violation exception is thrown for the 'quantityReserved' field.
     */
    @Test
    @Transactional
    public void testProductDistributorWithInvalidQuantityReserved() {
        Distributor distributor = new Distributor();
        distributor.name = "Distributor Name";
        distributor.cep = "12345678";
        entityManager.persist(distributor);
        entityManager.flush();

        Product product = new Product();
        product.sku = "SKU123";
        product.name = "Product Name";
        product.price = 500.00;
        product.description = "This is a product description.";
        product.linkImage = "http://example.com/product-image.jpg";
        entityManager.persist(product);
        entityManager.flush();

        ProductDistributor productDistributor = new ProductDistributor();
        productDistributor.distributor = distributor;
        productDistributor.product = product;
        productDistributor.quantityAvailable = 10;
        productDistributor.quantityReserved = -1; // Invalid quantity

        // Verifies constraint violation for the 'quantityReserved' field
        assertThrows(ConstraintViolationException.class, () -> {
            entityManager.persist(productDistributor);
            entityManager.flush();
        });
    }

    /**
     * Tests an invalid scenario for the {@link ProductDistributor} entity when 'quantityAvailable' is null.
     * Verifies that a constraint violation exception is thrown for the 'quantityAvailable' field.
     */
    @Test
    @Transactional
    public void testProductDistributorWithNullQuantityAvailable() {
        Distributor distributor = new Distributor();
        distributor.name = "Distributor Name";
        distributor.cep = "12345678";
        entityManager.persist(distributor);
        entityManager.flush();

        Product product = new Product();
        product.sku = "SKU123";
        product.name = "Product Name";
        product.price = 500.00;
        product.description = "This is a product description.";
        product.linkImage = "http://example.com/product-image.jpg";
        entityManager.persist(product);
        entityManager.flush();

        ProductDistributor productDistributor = new ProductDistributor();
        productDistributor.distributor = distributor;
        productDistributor.product = product;
        productDistributor.quantityAvailable = null; // Null quantity
        productDistributor.quantityReserved = 2;

        // Verifies constraint violation for the 'quantityAvailable' field
        assertThrows(ConstraintViolationException.class, () -> {
            entityManager.persist(productDistributor);
            entityManager.flush();
        });
    }

    /**
     * Tests an invalid scenario for the {@link ProductDistributor} entity when 'quantityReserved' is null.
     * Verifies that a constraint violation exception is thrown for the 'quantityReserved' field.
     */
    @Test
    @Transactional
    public void testProductDistributorWithNullQuantityReserved() {
        Distributor distributor = new Distributor();
        distributor.name = "Distributor Name";
        distributor.cep = "12345678";
        entityManager.persist(distributor);
        entityManager.flush();

        Product product = new Product();
        product.sku = "SKU123";
        product.name = "Product Name";
        product.price = 500.00;
        product.description = "This is a product description.";
        product.linkImage = "http://example.com/product-image.jpg";
        entityManager.persist(product);
        entityManager.flush();

        ProductDistributor productDistributor = new ProductDistributor();
        productDistributor.distributor = distributor;
        productDistributor.product = product;
        productDistributor.quantityAvailable = 10;
        productDistributor.quantityReserved = null; // Null quantity

        // Verifies constraint violation for the 'quantityReserved' field
        assertThrows(ConstraintViolationException.class, () -> {
            entityManager.persist(productDistributor);
            entityManager.flush();
        });
    }
}
