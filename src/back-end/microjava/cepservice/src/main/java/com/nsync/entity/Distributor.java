package com.nsync.entity;

import com.nsync.generic.entity.GenericEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * Represents a distributor entity in the system.
 * <p>
 * This class is mapped to the "distributors" table in the database and
 * extends the generic {@link GenericEntity}, inheriting its primary key attribute.
 * </p>
 *
 * <p>
 * The {@code id} attribute, inherited from {@link GenericEntity}, is overridden
 * to use the column name "distributors_id" in the database table.
 * </p>
 *
 * <p>
 * The class also defines additional attributes {@code name} and {@code cep}, both of which
 * are required fields (i.e., cannot be null).
 * </p>
 *
 * @author mauroDasChagas
 */
@Entity
@Table(name = "distributors")
public class Distributor extends GenericEntity {

    /**
     * The name of the distributor.
     * This field is required and cannot be null.
     */
    @Column(nullable = false)
    public String name;

    /**
     * The postal code (CEP) of the distributor.
     * This field is required and cannot be null.
     */
    @Column(nullable = false)
    public String cep;
}
