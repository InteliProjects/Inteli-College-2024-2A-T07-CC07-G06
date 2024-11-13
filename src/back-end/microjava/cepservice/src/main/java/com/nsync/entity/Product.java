package com.nsync.entity;

import com.nsync.generic.entity.GenericEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * Represents a product entity in the system.
 * <p>
 * This class is mapped to the "products" table in the database and
 * extends the generic {@link GenericEntity}, inheriting its primary key attribute.
 * </p>
 *
 * <p>
 * The {@code id} attribute, inherited from {@link GenericEntity}, is overridden
 * to use the column name "products_id" in the database table.
 * </p>
 *
 * <p>
 * The class defines several attributes: {@code sku}, {@code name}, {@code price},
 * {@code description}, and {@code linkImage}, all of which are required fields.
 * </p>
 *
 * @author mauroDasChagas
 */
@Entity
@Table(name = "products")
public class Product extends GenericEntity {

    /**
     * The Stock Keeping Unit (SKU) of the product.
     * This field is required and cannot be null.
     */
    @Column(nullable = false)
    public String sku;

    /**
     * The name of the product.
     * This field is required and cannot be null.
     */
    @Column(nullable = false)
    public String name;

    /**
     * The price of the product.
     * This field is required and cannot be null.
     */
    @Column(nullable = false)
    public Double price;

    /**
     * A brief description of the product.
     * This field is required and cannot be null.
     */
    @Column(nullable = false)
    public String description;

    /**
     * The URL link to the product's image.
     * This field is required and cannot be null.
     */
    @Column(name = "link_image", nullable = false)
    public String linkImage;
}
