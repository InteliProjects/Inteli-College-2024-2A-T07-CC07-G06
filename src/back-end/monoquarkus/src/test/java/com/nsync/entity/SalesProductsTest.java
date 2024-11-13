package com.nsync.entity;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Unit test for the {@link SalesProducts} entity class.
 * This class contains several test cases to validate the functionality and constraints of the SalesProducts entity.
 */
@QuarkusTest
public class SalesProductsTest {

    @Inject
    EntityManager entityManager;

    /**
     * Tests the valid scenario of the {@link SalesProducts} entity.
     * Verifies that a valid SalesProducts instance passes validation and can be persisted in the database.
     */
    @Test
    @Transactional
    public void testSalesProduct() {
        Distributor distributor = new Distributor();
        distributor.name = "Distributor Name";
        distributor.cep = "12345678";
        entityManager.persist(distributor);
        entityManager.flush();

        SalesOrders order = new SalesOrders();
        order.customerCep = "12345678";
        order.saleDate = LocalDateTime.now();
        order.total = 1000.00;
        order.lastDeliveryDate = LocalDate.now().plusDays(1);
        order.firstDeliveryDate = LocalDate.now();

        entityManager.persist(order);
        entityManager.flush();

        Product product = new Product();
        product.sku = "SKU123";
        product.name = "Product Name";
        product.price = 500.00;
        product.description = "This is a product description.";
        product.linkImage = "http://example.com/product-image.jpg";
        entityManager.persist(product);
        entityManager.flush();

        SalesProducts salesProduct = new SalesProducts();
        salesProduct.order = order;
        salesProduct.product = product;
        salesProduct.quantity = 2;
        salesProduct.unitPrice = 500.00;

        // Verifies that the values were correctly assigned
        assertEquals(order, salesProduct.order);
        assertEquals(product, salesProduct.product);
        assertEquals(2, salesProduct.quantity);
        assertEquals(500.00, salesProduct.unitPrice);

        // Persists the SalesProducts entity to ensure no constraint violations occur
        entityManager.persist(salesProduct);
        entityManager.flush();
    }

    /**
     * Tests an invalid scenario for the {@link SalesProducts} entity when the 'order' is null.
     * Verifies that a constraint violation exception is thrown for the 'order' field.
     */
    @Test
    @Transactional
    public void testSalesProductWithNullOrder() {
        Product product = new Product();
        product.sku = "SKU123";
        product.name = "Product Name";
        product.price = 500.00;
        product.description = "This is a product description.";
        product.linkImage = "http://example.com/product-image.jpg";
        entityManager.persist(product);
        entityManager.flush();

        SalesProducts salesProduct = new SalesProducts();
        salesProduct.product = product;
        salesProduct.quantity = 2;
        salesProduct.unitPrice = 500.00;

        // Verifies constraint violation for the 'order' field
        assertThrows(ConstraintViolationException.class, () -> {
            entityManager.persist(salesProduct);
            entityManager.flush();
        });
    }

    /**
     * Tests an invalid scenario for the {@link SalesProducts} entity when the 'product' is null.
     * Verifies that a constraint violation exception is thrown for the 'product' field.
     */
    @Test
    @Transactional
    public void testSalesProductWithNullProduct() {
        Distributor distributor = new Distributor();
        distributor.name = "Distributor Name";
        distributor.cep = "12345678";
        entityManager.persist(distributor);
        entityManager.flush();

        SalesOrders order = new SalesOrders();
        order.customerCep = "12345678";
        order.saleDate = LocalDateTime.now();
        order.total = 1000.00;
        order.lastDeliveryDate = LocalDate.now().plusDays(1);
        order.firstDeliveryDate = LocalDate.now();

        entityManager.persist(order);
        entityManager.flush();

        SalesProducts salesProduct = new SalesProducts();
        salesProduct.order = order;
        salesProduct.quantity = 2;
        salesProduct.unitPrice = 500.00;

        // Verifies constraint violation for the 'product' field
        assertThrows(ConstraintViolationException.class, () -> {
            entityManager.persist(salesProduct);
            entityManager.flush();
        });
    }

    /**
     * Tests an invalid scenario for the {@link SalesProducts} entity when 'quantity' is zero.
     * Verifies that a constraint violation exception is thrown for the 'quantity' field.
     */
    @Test
    @Transactional
    public void testSalesProductWithInvalidQuantity() {
        Distributor distributor = new Distributor();
        distributor.name = "Distributor Name";
        distributor.cep = "12345678";
        entityManager.persist(distributor);
        entityManager.flush();

        SalesOrders order = new SalesOrders();
        order.customerCep = "12345678";
        order.saleDate = LocalDateTime.now();
        order.total = 1000.00;
        order.lastDeliveryDate = LocalDate.now().plusDays(1);
        order.firstDeliveryDate = LocalDate.now();

        entityManager.persist(order);
        entityManager.flush();

        Product product = new Product();
        product.sku = "SKU123";
        product.name = "Product Name";
        product.price = 500.00;
        product.description = "This is a product description.";
        product.linkImage = "http://example.com/product-image.jpg";
        entityManager.persist(product);
        entityManager.flush();

        SalesProducts salesProduct = new SalesProducts();
        salesProduct.order = order;
        salesProduct.product = product;
        salesProduct.quantity = 0; 
        salesProduct.unitPrice = 500.00;

        // Verifies constraint violation for the 'quantity' field
        assertThrows(ConstraintViolationException.class, () -> {
            entityManager.persist(salesProduct);
            entityManager.flush();
        });
    }

    /**
     * Tests an invalid scenario for the {@link SalesProducts} entity when 'unitPrice' is negative.
     * Verifies that a constraint violation exception is thrown for the 'unitPrice' field.
     */
    @Test
    @Transactional
    public void testSalesProductWithInvalidUnitPrice() {
        Distributor distributor = new Distributor();
        distributor.name = "Distributor Name";
        distributor.cep = "12345678";
        entityManager.persist(distributor);
        entityManager.flush();

        SalesOrders order = new SalesOrders();
        order.customerCep = "12345678";
        order.saleDate = LocalDateTime.now();
        order.total = 1000.00;
        order.lastDeliveryDate = LocalDate.now().plusDays(1);
        order.firstDeliveryDate = LocalDate.now();

        entityManager.persist(order);
        entityManager.flush();

        Product product = new Product();
        product.sku = "SKU123";
        product.name = "Product Name";
        product.price = 500.00;
        product.description = "This is a product description.";
        product.linkImage = "http://example.com/product-image.jpg";
        entityManager.persist(product);
        entityManager.flush();

        SalesProducts salesProduct = new SalesProducts();
        salesProduct.order = order;
        salesProduct.product = product;
        salesProduct.quantity = 2;
        salesProduct.unitPrice = -10.00; 

        // Verifies constraint violation for the 'unitPrice' field
        assertThrows(ConstraintViolationException.class, () -> {
            entityManager.persist(salesProduct);
            entityManager.flush();
        });
    }

    /**
     * Tests an invalid scenario for the {@link SalesProducts} entity when 'quantity' is negative.
     * Verifies that a constraint violation exception is thrown for the 'quantity' field.
     */
    @Test
    @Transactional
    public void testSalesProductWithNegativeQuantity() {
        Distributor distributor = new Distributor();
        distributor.name = "Distributor Name";
        distributor.cep = "12345678";
        entityManager.persist(distributor);
        entityManager.flush();

        SalesOrders order = new SalesOrders();
        order.customerCep = "12345678";
        order.saleDate = LocalDateTime.now();
        order.total = 1000.00;
        order.lastDeliveryDate = LocalDate.now().plusDays(1);
        order.firstDeliveryDate = LocalDate.now();

        entityManager.persist(order);
        entityManager.flush();

        Product product = new Product();
        product.sku = "SKU123";
        product.name = "Product Name";
        product.price = 500.00;
        product.description = "This is a product description.";
        product.linkImage = "http://example.com/product-image.jpg";
        entityManager.persist(product);
        entityManager.flush();

        SalesProducts salesProduct = new SalesProducts();
        salesProduct.order = order;
        salesProduct.product = product;
        salesProduct.quantity = -1; 
        salesProduct.unitPrice = 500.00;

        // Verifies constraint violation for the 'quantity' field
        assertThrows(ConstraintViolationException.class, () -> {
            entityManager.persist(salesProduct);
            entityManager.flush();
        });
    }

    /**
     * Tests a scenario for the {@link SalesProducts} entity when 'unitPrice' is zero.
     * Verifies that the SalesProducts instance can be persisted in the database without constraint violations.
     */
    @Test
    @Transactional
    public void testSalesProductWithZeroUnitPrice() {
        Distributor distributor = new Distributor();
        distributor.name = "Distributor Name";
        distributor.cep = "12345678";
        entityManager.persist(distributor);
        entityManager.flush();

        SalesOrders order = new SalesOrders();
        order.customerCep = "12345678";
        order.saleDate = LocalDateTime.now();
        order.total = 1000.00;
        order.lastDeliveryDate = LocalDate.now().plusDays(1);
        order.firstDeliveryDate = LocalDate.now();

        entityManager.persist(order);
        entityManager.flush();

        Product product = new Product();
        product.sku = "SKU123";
        product.name = "Product Name";
        product.price = 500.00;
        product.description = "This is a product description.";
        product.linkImage = "http://example.com/product-image.jpg";
        entityManager.persist(product);
        entityManager.flush();

        SalesProducts salesProduct = new SalesProducts();
        salesProduct.order = order;
        salesProduct.product = product;
        salesProduct.quantity = 2;
        salesProduct.unitPrice = 0.00; 

        // Verifies that the values were correctly assigned
        assertEquals(order, salesProduct.order);
        assertEquals(product, salesProduct.product);
        assertEquals(2, salesProduct.quantity);
        assertEquals(0.00, salesProduct.unitPrice);

        // Persists the SalesProducts entity to ensure no constraint violations occur
        entityManager.persist(salesProduct);
        entityManager.flush();
    }
}
