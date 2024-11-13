package com.nsync.microservice.entity;

import com.nsync.microservice.generic.entity.GenericEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Represents a sales order in the system.
 * <p>
 * This class is mapped to the "sales_orders" table in the database and
 * extends the generic {@link GenericEntity}, inheriting its primary key attribute.
 * </p>
 *
 * <p>
 * The {@code id} attribute, inherited from {@link GenericEntity}, is overridden
 * to use the column name "sales_orders_id" in the database table.
 * </p>
 *
 * <p>
 * This entity links a {@link Distributor} to a sales order, with attributes to track
 * the sale date, total amount, status, and delivery date of the order.
 * </p>
 *
 * @author mauroDasChagas
 */
@Entity
@Table(name = "sales_orders")
public class SalesOrders extends GenericEntity {

    /**
     * The cep of the customer
     */
    @Column(name = "customer_cep", nullable = false)
    @NotNull(message = "Customer cep is required")
    public String customerCep;

    /**
     * The date and time when the sale was made.
     * This field is required and cannot be null.
     */
    @Column(name = "sale_date", nullable = false)
    @NotNull(message = "Sale date is required")
    public LocalDateTime saleDate;

    /**
     * The total amount of the sales order.
     * This field is required and cannot be null.
     */
    @Column(nullable = false)
    @NotNull(message = "Total amount is required")
    @Min(value = 1, message = "Total amount must be greater than 0")
    public Double total;

    /**
     * The expected initial delivery date for the sales order.
     * This field is required and cannot be null.
     */
    @Column(name = "first_delivery_date", nullable = false)
    @NotNull(message = "First delivery date is required")
    public LocalDate firstDeliveryDate;

    /**
     * The expected last delivery date for the sales order.
     * This field is required and cannot be null.
     */
    @Column(name = "last_delivery_date", nullable = false)
    @NotNull(message = "Last delivery date is required")
    public LocalDate lastDeliveryDate;
}
