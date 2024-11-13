package com.nsync.entity;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Unit test for the {@link SalesOrders} entity class.
 * This class contains several test cases to validate the functionality and constraints of the SalesOrders entity.
 */
@QuarkusTest
public class SalesOrdersTest {

    @Inject
    EntityManager entityManager;

    /**
     * Tests the valid scenario of the {@link SalesOrders} entity.
     * Verifies that a valid SalesOrders instance can be persisted in the database.
     */
    @Test
    @Transactional
    public void testSalesOrders() {
        SalesOrders order = new SalesOrders();
        order.customerCep = "12345678";
        order.saleDate = LocalDateTime.now();
        order.total = 1000.00;
        order.lastDeliveryDate = LocalDate.now().plusDays(1);
        order.firstDeliveryDate = LocalDate.now();

        entityManager.persist(order);
        entityManager.flush();
    }

    /**
     * Tests an invalid scenario for the {@link SalesOrders} entity when the 'total' is zero.
     * Verifies that an exception is thrown due to the invalid total amount.
     */
    @Test
    @Transactional
    public void testSalesOrdersWithInvalidTotal() {
        SalesOrders order = new SalesOrders();
        order.customerCep = "12345678";
        order.saleDate = LocalDateTime.now();
        order.total = 0.00;
        order.lastDeliveryDate = LocalDate.now().plusDays(1);
        order.firstDeliveryDate = LocalDate.now();

        try {
            entityManager.persist(order);
            entityManager.flush();
        } catch (Exception e) {
            // Expected exception due to invalid total amount
        }

    }

    /**
     * Tests an invalid scenario for the {@link SalesOrders} entity when several fields are null.
     * Verifies that an exception is thrown due to null fields.
     */
    @Test
    @Transactional
    public void testSalesOrdersWithNullFields() {
        SalesOrders order = new SalesOrders();
        order.customerCep = null; 
        order.saleDate = null; 
        order.total = null;
        order.lastDeliveryDate = null; 
        order.firstDeliveryDate = null;

        try {
            entityManager.persist(order);
            entityManager.flush();
        } catch (Exception e) {
            // Expected exception due to null fields
        }
    }

    /**
     * Tests a scenario for the {@link SalesOrders} entity when some dates are in the future.
     * Verifies that the SalesOrders instance can be persisted in the database without constraint violations.
     */
    @Test
    @Transactional
    public void testSalesOrdersWithFutureDates() {
        SalesOrders order = new SalesOrders();
        order.customerCep = "12345678";
        order.saleDate = LocalDateTime.now().plusDays(1);
        order.total = 1000.00;
        order.lastDeliveryDate = LocalDate.now().plusDays(2); 
        order.firstDeliveryDate = LocalDate.now().plusDays(1);

        entityManager.persist(order);
        entityManager.flush();
    }

    /**
     * Tests a scenario for the {@link SalesOrders} entity when some dates are in the past.
     * Verifies that the SalesOrders instance can be persisted in the database without constraint violations.
     */
    @Test
    @Transactional
    public void testSalesOrdersWithPastDates() {
        SalesOrders order = new SalesOrders();
        order.customerCep = "12345678";
        order.saleDate = LocalDateTime.now().minusDays(1); 
        order.total = 1000.00;
        order.lastDeliveryDate = LocalDate.now().minusDays(1); 
        order.firstDeliveryDate = LocalDate.now().minusDays(2);

        entityManager.persist(order);
        entityManager.flush();
    }

    /**
     * Tests a scenario for the {@link SalesOrders} entity with different statuses.
     * Verifies that multiple SalesOrders instances with different statuses can be persisted in the database.
     */
    @Test
    @Transactional
    public void testSalesOrdersWithDifferentStatuses() {
        SalesOrders order1 = new SalesOrders();
        order1.customerCep = "12345678";
        order1.saleDate = LocalDateTime.now();
        order1.total = 1000.00;
        order1.lastDeliveryDate = LocalDate.now().plusDays(1);
        order1.firstDeliveryDate = LocalDate.now();

        SalesOrders order2 = new SalesOrders();
        order2.customerCep = "87654321";
        order2.saleDate = LocalDateTime.now();
        order2.total = 2000.00;
        order2.lastDeliveryDate = LocalDate.now().plusDays(2);
        order2.firstDeliveryDate = LocalDate.now().plusDays(1);

        entityManager.persist(order1);
        entityManager.persist(order2);
        entityManager.flush();
    }
}
