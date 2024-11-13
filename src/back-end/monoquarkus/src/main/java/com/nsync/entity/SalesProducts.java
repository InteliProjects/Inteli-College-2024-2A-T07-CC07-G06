package com.nsync.entity;

import com.nsync.generic.entity.GenericEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * Represents the association between a sales order and the products within that order.
 * <p>
 * This class is mapped to the "sales_products" table in the database and
 * extends the generic {@link GenericEntity}, inheriting its primary key attribute.
 * </p>
 *
 * <p>
 * The {@code id} attribute, inherited from {@link GenericEntity}, is overridden
 * to use the column name "sales_products_id" in the database table.
 * </p>
 *
 * <p>
 * This entity links a {@link SalesOrders} to a {@link Product}, with attributes to track
 * the quantity of the product and its unit price within the order.
 * </p>
 *
 * @author mauroDasChagas
 */
@Entity
@Table(name = "sales_orders_products")
public class SalesProducts extends GenericEntity {

    /**
     * The sales order associated with the product.
     * This relationship is managed with lazy loading.
     * This field is required and cannot be null.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sale_order_id", referencedColumnName = "id", nullable = false)
    public SalesOrders order;

    /**
     * The product associated with the sales order.
     * This relationship is managed with lazy loading.
     * This field is required and cannot be null.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", referencedColumnName = "id", nullable = false)
    public Product product;

    /**
     *
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "distributor_id", referencedColumnName = "id", nullable = false)
    public Distributor distributor;

    /**
     * The quantity of the product within the sales order.
     * This field is required and cannot be null.
     */
    @Column(nullable = false)
    public Integer quantity;

    /**
     * The unit price of the product within the sales order.
     * This field is required and cannot be null.
     */
    @Column(name = "unit_price", nullable = false)
    public Double unitPrice;
}
