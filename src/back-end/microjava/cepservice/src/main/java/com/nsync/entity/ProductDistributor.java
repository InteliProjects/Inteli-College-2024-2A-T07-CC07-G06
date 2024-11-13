package com.nsync.entity;

import com.nsync.generic.entity.GenericEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * Represents the relationship between products and distributors in the system. This is the inventory of products.
 * <p>
 * This class is mapped to the "products_distributor" table in the database and
 * extends the generic {@link GenericEntity}, inheriting its primary key attribute.
 * </p>
 *
 * <p>
 * The {@code id} attribute, inherited from {@link GenericEntity}, is overridden
 * to use the column name "products_distributors_id" in the database table.
 * </p>
 *
 * <p>
 * This entity links a {@link Distributor} to a {@link Product}, with attributes
 * to track the quantity available and the quantity reserved for that product-distributor pair.
 * </p>
 *
 * @author mauroDasChagas
 */
@Entity
@Table(name = "products_distributors")
public class ProductDistributor extends GenericEntity {

    /**
     * The distributor associated with the product.
     * This relationship is managed with lazy loading.
     * This field is required and cannot be null.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "distributor_id", referencedColumnName = "id", nullable = false)
    public Distributor distributor;

    /**
     * The product associated with the distributor.
     * This relationship is managed with lazy loading.
     * This field is required and cannot be null.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", referencedColumnName = "id", nullable = false)
    public Product product;

    /**
     * The quantity of the product available with the distributor.
     * This field is required and cannot be null.
     */
    @Column(name = "quantity_available", nullable = false)
    public Integer quantityAvailable;

    /**
     * The quantity of the product that is reserved with the distributor.
     * This field is required and cannot be null.
     */
    @Column(name = "quantity_reserved", nullable = false)
    public Integer quantityReserved;
}
