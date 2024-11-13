package com.nsync.microservice.entity;

import com.nsync.microservice.generic.entity.GenericEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

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
    @NotNull(message = "SKU cannot be null")
    public String sku;

    /**
     * The name of the product.
     * This field is required and cannot be null.
     */
    @Column(nullable = false)
    @NotNull(message = "Name cannot be null")
    public String name;

    /**
     * The price of the product.
     * This field is required and cannot be null.
     */
    @Column(nullable = false)
    @NotNull(message = "Price cannot be null")
    @Min(value = 1, message = "Price must be greater than 0")
    public Double price;

    /**
     * A brief description of the product.
     * This field is required and cannot be null.
     */
    @Column(nullable = false)
    @NotNull(message = "Description cannot be null")
    public String description;

    /**
     * The URL link to the product's image.
     * This field is required and cannot be null.
     */
    @Column(name = "link_image", nullable = false)
    @NotNull(message = "Link image cannot be null")
    public String linkImage;
}

